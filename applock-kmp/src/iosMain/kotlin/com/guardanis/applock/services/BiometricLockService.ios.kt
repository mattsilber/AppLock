package com.guardanis.applock.services

import com.guardanis.applock.settings.BiometricsLanguage
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSUserDefaults
import platform.LocalAuthentication.LAContext
import platform.LocalAuthentication.LAPolicyDeviceOwnerAuthenticationWithBiometrics

private const val prefEnrolledKey: String = "pin__biometrics_enrolled"

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
@OptIn(ExperimentalForeignApi::class)
actual class BiometricAuthenticator: BiometricLockService.Authenticator {

    private val context: LAContext = LAContext()

    override fun authenticate(
        alreadyEnrolled: Boolean,
        success: () -> Unit,
        fail: (BiometricLockService.ErrorCode) -> Unit
    ) {

        context
            .evaluatePolicy(
                LAPolicyDeviceOwnerAuthenticationWithBiometrics,
                if (alreadyEnrolled) language.unlock.reason else language.enroll.reason,
                { authorized, error ->
                    if (!authorized) {
                        // TODO: Map the errors back
                        fail(BiometricLockService.ErrorCode.UNKNOWN)
                    }

                    success()
                }
            )
    }

    override fun isDeviceBiometricLockingEnabled(): Boolean {
        return context.canEvaluatePolicy(LAPolicyDeviceOwnerAuthenticationWithBiometrics, null)
    }

    companion object {

        var language: BiometricsLanguage = BiometricsLanguage()
    }
}

actual fun BiometricLockService.isEnrolled(): Boolean {
    return NSUserDefaults.standardUserDefaults.boolForKey(prefEnrolledKey)
}

actual fun BiometricLockService.enroll() {
    NSUserDefaults.standardUserDefaults.setBool(
        value = true,
        forKey = prefEnrolledKey
    )
}

actual fun BiometricLockService.invalidateEnrollment() {
    NSUserDefaults.standardUserDefaults.removeObjectForKey(prefEnrolledKey)
}