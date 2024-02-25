package com.guardanis.applock.services

import androidx.compose.runtime.Composable

class PINLockService {

    enum class ErrorCode {
        NOT_ENROLLED,
        MISMATCH
    }

    @Composable
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

@Composable
expect fun PINLockService.isEnrolled(): Boolean

@Composable
expect fun PINLockService.enroll(encryptedPin: String)

@Composable
expect fun PINLockService.invalidateEnrollment()

@Composable
expect fun PINLockService.getEnrolledPin(): String

@Composable
expect fun PINLockService.encryptForStorage(pin: String): String