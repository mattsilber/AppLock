package com.guardanis.applock

import com.guardanis.applock.services.BiometricLockService
import com.guardanis.applock.services.PINLockService
import com.guardanis.applock.services.enroll
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
    fun deviceEligibleEnrollments(): EnumSet<Enrollment> {
        if (biometricLockService.isHardwareEligible() && biometricLockService.isDeviceBiometricLockingEnabled()) {
            return EnumSet.of(
                Enrollment.PIN,
                Enrollment.BIOMETRICS
            )
        }

        return EnumSet.of(Enrollment.PIN)
    }

    /**
     * @return the active [Enrollment] type, or [Enrollment.NONE] if
     * no authentication services have been enrolled. Only one service
     * may be enrolled at a time.
     */
    fun enrollment(): Enrollment {
        if (pinLockService.isEnrolled()) {
            return Enrollment.PIN
        }

        if (biometricLockService.isEnrolled()) {
            return Enrollment.BIOMETRICS
        }

        return Enrollment.NONE
    }

    /**
     * This should only be called after a successful PIN confirmation,
     * enabling future PIN auth calls. Assuming no other service has been
     * enrolled, future calls to [enrollment] should return [Enrollment.PIN].
     */
    fun enrollPinAuthentication(pin: String) {
        pinLockService.enroll(
            unencryptedPin = pin
        )
    }

    /**
     * This function assumes an immediately-preceding call to [enrollment] has returned
     * [Enrollment.PIN]. [success] is triggered when the supplied [pin] matches the stored pin.
     * [fail] will be triggered for varying [PINLockService.ErrorCode] instances.
     */
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

    /**
     * This function assumes an immediately-preceding call to [enrollment] has returned
     * [Enrollment.BIOMETRICS]. [success] is triggered when the user authenticates with their
     * device biometrics or lock screen. [fail] will be triggered for varying
     * [BiometricLockService.ErrorCode] instances.
     */
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

    /**
     * This should only be called after a successful biometric authentication call,
     * enabling future biometric auth calls. Assuming no other service has been
     * enrolled, future calls to [enrollment] should return [Enrollment.BIOMETRICS].
     */
    fun enrollBiometricAuthentication() {
        biometricLockService.enroll()
    }
}