package com.guardanis.applock.services

import androidx.compose.runtime.Composable

class BiometricLockService {

    enum class ErrorCode {
        NOT_ENROLLED,
        DEVICE_NOT_ENROLLED,
        MISMATCH,
        UNKNOWN
    }

    @Composable
    fun authenticate(
        success: () -> Unit,
        fail: (ErrorCode) -> Unit
    ) {

        fail(ErrorCode.UNKNOWN)
    }
}
