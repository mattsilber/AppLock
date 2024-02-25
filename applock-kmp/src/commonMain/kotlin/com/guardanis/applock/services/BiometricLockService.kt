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

        @Composable
        fun authenticate(
            success: () -> Unit,
            fail: (ErrorCode) -> Unit
        )

        @Composable
        fun isHardwareEligible(): Boolean

        @Composable
        fun isDeviceBiometricLockingEnabled(): Boolean
    }

    @Composable
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

    @Composable
    fun isHardwareEligible(): Boolean {
        return authenticator.isHardwareEligible()
    }

    @Composable
    fun isDeviceBiometricLockingEnabled(): Boolean {
        return authenticator.isDeviceBiometricLockingEnabled()
    }
}

expect class BiometricAuthenticator(): BiometricLockService.Authenticator

@Composable
expect fun BiometricLockService.isEnrolled(): Boolean

@Composable
expect fun BiometricLockService.enroll()

@Composable
expect fun BiometricLockService.invalidateEnrollment()