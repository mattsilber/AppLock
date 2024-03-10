package com.guardanis.applock.services

import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.guardanis.applock.AppLockAppContext

private const val prefEnrolledKey: String = "pin__fingerprint_enrollment_allowed"

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class BiometricAuthenticator: BiometricLockService.Authenticator {

    private var biometricPrompt: BiometricPrompt? = null

    override fun authenticate(
        alreadyEnrolled: Boolean,
        success: () -> Unit,
        fail: (BiometricLockService.ErrorCode) -> Unit
    ) {

        val activity = AppLockAppContext.activity.get()

        if (activity == null) {
            fail(BiometricLockService.ErrorCode.UNKNOWN)

            return
        }

        val promptCallback = object: BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)

                // TODO: Map error codes
                fail(BiometricLockService.ErrorCode.UNKNOWN)
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()

                fail(BiometricLockService.ErrorCode.UNKNOWN)
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)

                // TODO: Store crypto object for implementation access
                success()
            }
        }

        biometricPrompt = BiometricPrompt(
            activity,
            ContextCompat.getMainExecutor(activity),
            promptCallback
        )

        val language = AppLockAppContext.biometricsLanguage

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(
                if (alreadyEnrolled) language.unlock.title else language.enroll.title
            )
            .setSubtitle(
                if (alreadyEnrolled) language.unlock.subtitle else language.enroll.subtitle
            )
            .setDescription(
                if (alreadyEnrolled) language.unlock.description else language.enroll.description
            )
            .setConfirmationRequired(false)
            .setNegativeButtonText(
                if (alreadyEnrolled) language.unlock.negativeAction else language.enroll.negativeAction
            )
            .build()

        // TODO: Get crypto object
        biometricPrompt?.authenticate(promptInfo)
    }

    override fun isDeviceBiometricLockingEnabled(): Boolean {
        val context = AppLockAppContext.context.get() ?: return false

        // TODO: Make [Authenticators] configurable
        val authenticationResponse = BiometricManager
            .from(context)
            .canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK)

        return authenticationResponse == BiometricManager.BIOMETRIC_SUCCESS
    }
}

actual fun BiometricLockService.isEnrolled(): Boolean {
    return AppLockAppContext
        .preferences()
        ?.getBoolean(prefEnrolledKey, false) ?: false
}

actual fun BiometricLockService.enroll() {
    AppLockAppContext
        .preferences()
        ?.edit()
        ?.putBoolean(prefEnrolledKey, true)
        ?.apply()
}

actual fun BiometricLockService.invalidateEnrollment() {
    AppLockAppContext
        .preferences()
        ?.edit()
        ?.remove(prefEnrolledKey)
        ?.apply()
}