package com.guardanis.applock.services

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
            alreadyEnrolled: Boolean,
            success: () -> Unit,
            fail: (ErrorCode) -> Unit
        )

        fun isDeviceBiometricLockingEnabled(): Boolean
    }

    fun authenticate(
        localEnrollmentCheckRequired: Boolean = true,
        success: () -> Unit,
        fail: (ErrorCode) -> Unit
    ) {

        val currentlyEnrolled = isEnrolled()

        if (localEnrollmentCheckRequired && !currentlyEnrolled) {
            fail(ErrorCode.NOT_ENROLLED)

            return
        }

        if (!isDeviceBiometricLockingEnabled()) {
            fail(ErrorCode.DEVICE_NOT_ENROLLED)

            return
        }

        authenticator.authenticate(
            alreadyEnrolled = currentlyEnrolled,
            success = success,
            fail = fail
        )
    }

    fun isDeviceBiometricLockingEnabled(): Boolean {
        return authenticator.isDeviceBiometricLockingEnabled()
    }
}

expect class BiometricAuthenticator(): BiometricLockService.Authenticator

expect fun BiometricLockService.isEnrolled(): Boolean

expect fun BiometricLockService.enroll()

expect fun BiometricLockService.invalidateEnrollment()