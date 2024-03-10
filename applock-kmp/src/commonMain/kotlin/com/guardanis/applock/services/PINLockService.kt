package com.guardanis.applock.services

class PINLockService {

    enum class ErrorCode {
        NOT_ENROLLED,
        MISMATCH
    }

    fun authenticate(
        input: String,
        success: () -> Unit,
        fail: (ErrorCode) -> Unit
    ) {

        if (!isEnrolled()) {
            fail(ErrorCode.NOT_ENROLLED)

            return
        }

        if (encryptForStorage(input) != getEnrolledPin()) {
            fail(ErrorCode.MISMATCH)

            return
        }

        success()
    }
}

expect fun PINLockService.isEnrolled(): Boolean

expect fun PINLockService.enroll(unencryptedPin: String)

expect fun PINLockService.invalidateEnrollment()

expect fun PINLockService.getEnrolledPin(): String

expect fun PINLockService.encryptForStorage(pin: String): String