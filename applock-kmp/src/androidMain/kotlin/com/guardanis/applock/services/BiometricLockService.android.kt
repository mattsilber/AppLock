package com.guardanis.applock.services

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private const val prefEnrolledKey: String = "pin__fingerprint_enrollment_allowed"

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class BiometricAuthenticator: BiometricLockService.Authenticator {

    @Composable
    override fun authenticate(
        success: () -> Unit,
        fail: (BiometricLockService.ErrorCode) -> Unit
    ) {

        // TODO
        fail(BiometricLockService.ErrorCode.UNKNOWN)
    }

    @Composable
    override fun isHardwareEligible(): Boolean {
        // TODO
        return false
    }

    @Composable
    override fun isDeviceBiometricLockingEnabled(): Boolean {
        // TODO
        return false
    }
}

@Composable
actual fun BiometricLockService.isEnrolled(): Boolean {
    return appLockPreferences(LocalContext.current)
        .getBoolean(prefEnrolledKey, false)
}

@Composable
actual fun BiometricLockService.enroll() {
    appLockPreferences(LocalContext.current)
        .edit()
        .putBoolean(prefEnrolledKey, true)
        .apply()
}

@Composable
actual fun BiometricLockService.invalidateEnrollment() {
    appLockPreferences(LocalContext.current)
        .edit()
        .remove(prefEnrolledKey)
        .apply()
}