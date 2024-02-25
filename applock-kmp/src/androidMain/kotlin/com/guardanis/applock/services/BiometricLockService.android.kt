package com.guardanis.applock.services

import com.guardanis.applock.AppLockAppContext

private const val prefEnrolledKey: String = "pin__fingerprint_enrollment_allowed"

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class BiometricAuthenticator: BiometricLockService.Authenticator {

    override fun authenticate(
        success: () -> Unit,
        fail: (BiometricLockService.ErrorCode) -> Unit
    ) {

        // TODO
        fail(BiometricLockService.ErrorCode.UNKNOWN)
    }

    override fun isHardwareEligible(): Boolean {
        // TODO
        return false
    }

    override fun isDeviceBiometricLockingEnabled(): Boolean {
        // TODO
        return false
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