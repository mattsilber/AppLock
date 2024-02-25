package com.guardanis.applock

import androidx.compose.runtime.Composable
import com.guardanis.applock.services.BiometricLockService
import com.guardanis.applock.services.PINLockService
import com.guardanis.applock.services.isEnrolled
import java.util.EnumSet

object AppLock {

    enum class Enrollment {
        NONE,
        BIOMETRICS,
        PIN
    }

    private val pinLockService: PINLockService = PINLockService()
    private val biometricLockService: BiometricLockService = BiometricLockService()

    /**
     * @return a set of the eligible enrollment types for this device.
     * [Enrollment.PIN] is always an option
     */
    @Composable
    fun deviceEligibleEnrollments(): EnumSet<Enrollment> {
        // TODO: Check for Biometric service eligibility

        return EnumSet.of(Enrollment.PIN)
    }

    /**
     * @return the active [Enrollment] type, or [Enrollment.NONE] if
     * no authentication services have been enrolled. Only one service
     * may be enrolled at a time.
     */
    @Composable
    fun enrollment(): Enrollment {
        if (pinLockService.isEnrolled()) {
            return Enrollment.PIN
        }

        // TODO: Check BiometricLockService that doesn't exist

        return Enrollment.NONE
    }

    @Composable
    fun pinAuthenticate(
        pin: String,
        success: () -> Unit,
        fail: (PINLockService.ErrorCode) -> Unit
    ) {

        pinLockService.authenticate(
            input = pin,
            success = {
                // TODO
            },
            fail = {
                // TODO
            }
        )
    }

    @Composable
    fun biometricAuthenticate(
        success: () -> Unit,
        fail: (BiometricLockService.ErrorCode) -> Unit
    ) {

        biometricLockService.authenticate(
            success = {
                // TODO
            },
            fail = {
                // TODO
            }
        )
    }
}