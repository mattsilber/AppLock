package com.guardanis.applock.services

import androidx.compose.runtime.Composable

class BiometricLockService(
    val authenticator: Authenticator = BiometricAuthenticator()
) {

    enum class ErrorCode {
        NOT_ENROLLED,
        DEVICE_NOT_ENROLLED,
        DEVICE_NOT_ELIGIBLE,
        MISMATCH,
        UNKNOWN
    }

    interface Authenticator {

        fun authenticate(
            success: () -> Unit,
            fail: (ErrorCode) -> Unit
        )

        fun isHardwareEligible(): Boolean

        fun isDeviceBiometricLockingEnabled(): Boolean
    }

    fun authenticate(
        success: () -> Unit,
        fail: (ErrorCode) -> Unit
    ) {

        if (!isEnrolled()) {
            fail(ErrorCode.NOT_ENROLLED)

            return
        }

        if (!isHardwareEligible()) {
            fail(ErrorCode.DEVICE_NOT_ELIGIBLE)

            return
        }

        if (!isDeviceBiometricLockingEnabled()) {
            fail(ErrorCode.DEVICE_NOT_ENROLLED)

            return
        }

        authenticator.authenticate(
            success = success,
            fail = fail
        )
    }

    fun isHardwareEligible(): Boolean {
        return authenticator.isHardwareEligible()
    }

    fun isDeviceBiometricLockingEnabled(): Boolean {
        return authenticator.isDeviceBiometricLockingEnabled()
    }
}

expect class BiometricAuthenticator(): BiometricLockService.Authenticator

expect fun BiometricLockService.isEnrolled(): Boolean

expect fun BiometricLockService.enroll()

expect fun BiometricLockService.invalidateEnrollment()