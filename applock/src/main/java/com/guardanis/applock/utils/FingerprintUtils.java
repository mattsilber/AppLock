package com.guardanis.applock.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.support.v4.content.ContextCompat;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v4.os.CancellationSignal;

import com.guardanis.applock.AppLock;
import com.guardanis.applock.R;

import java.security.KeyStore;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;

public class FingerprintUtils {

    public interface AuthenticationDelegate {
        public void onResolutionRequired(int errorCode);
        public void onAuthenticating(CancellationSignal cancellationSignal);
        public void onAuthenticationSuccess();
        public void onAuthenticationFailed(String message);
    }

    private static final String PREF_ENROLLMENT_ALLOWED = "pin__fingerprint_enrollment_allowed";
    private static final String KEYSTORE_NAME  = "AndroidKeyStore";

    public static void authenticate(Context context, boolean localEnrollmentRequired, AuthenticationDelegate delegate) {
        if (!isHardwarePresent(context)) {
            delegate.onResolutionRequired(AppLock.ERROR_CODE_FINGERPRINTS_MISSING_HARDWARE);
            return;
        }

        FingerprintManagerCompat manager = FingerprintManagerCompat.from(context);

        if (localEnrollmentRequired && !isLocallyEnrolled(context)) {
            delegate.onResolutionRequired(AppLock.ERROR_CODE_FINGERPRINTS_NOT_LOCALLY_ENROLLED);
            return;
        }

        if (!manager.hasEnrolledFingerprints()) {
            delegate.onResolutionRequired(AppLock.ERROR_CODE_FINGERPRINTS_EMPTY);
            return;
        }

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
            delegate.onResolutionRequired(AppLock.ERROR_CODE_FINGERPRINTS_PERMISSION_REQUIRED);
            return;
        }

        attemptAuthentication(context, manager, delegate);
    }

    private static void attemptAuthentication(final Context context, FingerprintManagerCompat manager, final AuthenticationDelegate delegate) {
        final CancellationSignal cancellationSignal = new CancellationSignal();

        FingerprintManagerCompat.AuthenticationCallback callback = new FingerprintManagerCompat.AuthenticationCallback() {
            public void onAuthenticationError(int errMsgId, CharSequence errString) {
                super.onAuthenticationError(errMsgId, errString);

                delegate.onAuthenticationFailed(context.getString(R.string.pin__fingerprint_error_unknown));
            }

            public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
                super.onAuthenticationHelp(helpMsgId, helpString);
            }

            public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);

                setLocalEnrollmentEnabled(context);

                delegate.onAuthenticationSuccess();
            }

            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();

                delegate.onAuthenticationFailed(context.getString(R.string.pin__fingerprint_error_unrecognized));
            }
        };

        delegate.onAuthenticating(cancellationSignal);

        try {
            Cipher cipher = generateAuthCipher(context, false, 0);
            FingerprintManagerCompat.CryptoObject cryptoObject = new FingerprintManagerCompat.CryptoObject(cipher);

            manager.authenticate(cryptoObject, 0, cancellationSignal, callback, null);
        }
        catch (Exception e) {
            e.printStackTrace();

            delegate.onResolutionRequired(AppLock.ERROR_CODE_FINGERPRINTS_MISSING_HARDWARE);
        }
    }

    public static boolean isHardwarePresent(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return false;

        return FingerprintManagerCompat.from(context)
                .isHardwareDetected();
    }

    public static boolean isLocallyEnrolled(Context context) {
        return AppLock.getInstance(context)
                .getPreferences()
                .getBoolean(PREF_ENROLLMENT_ALLOWED, false);
    }

    public static void setLocalEnrollmentEnabled(Context context) {
        AppLock.getInstance(context)
                .getPreferences()
                .edit()
                .putBoolean(PREF_ENROLLMENT_ALLOWED, true)
                .commit();
    }

    public static void removeAuthentications(Context context) {
        AppLock.getInstance(context)
                .getPreferences()
                .edit()
                .putBoolean(PREF_ENROLLMENT_ALLOWED, false)
                .commit();
    }

    private static Cipher generateAuthCipher(Context context, boolean forceRegenerate, int attempts) throws Exception {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return null;

        String alias = context.getString(R.string.pin__fingerprint_alias);

        KeyStore keyStore = KeyStore.getInstance(KEYSTORE_NAME);
        keyStore.load(null);

        if (forceRegenerate || !keyStore.containsAlias(alias)) {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, KEYSTORE_NAME);
            keyGenerator.init(new KeyGenParameterSpec.Builder(alias, KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build());

            keyGenerator.generateKey();
        }

        String cipherFormat = String.format(
                "%s/%s/%s",
                KeyProperties.KEY_ALGORITHM_AES,
                KeyProperties.BLOCK_MODE_CBC,
                KeyProperties.ENCRYPTION_PADDING_PKCS7);

        try {
            Cipher cipher = Cipher.getInstance(cipherFormat);
            cipher.init(Cipher.ENCRYPT_MODE, keyStore.getKey(alias, null));

            return cipher;
        }
        catch (KeyPermanentlyInvalidatedException e) {
            e.printStackTrace();

            if (1 < attempts)
                return generateAuthCipher(context, true, attempts + 1);
        }

        return null;
    }
}