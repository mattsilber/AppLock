package com.guardanis.applock

import com.guardanis.applock.services.BiometricLockService
import com.guardanis.applock.services.PINLockService
import com.guardanis.applock.services.enroll
import com.guardanis.applock.services.invalidateEnrollment
import com.guardanis.applock.services.isEnrolled

object AppLock {

    enum class Enrollment {
        NONE,
        BIOMETRICS,
        PIN
    }

    private val pinLockService: PINLockService = PINLockService()
    private val biometricLockService: BiometricLockService = BiometricLockService()

    /**
     * @return [Enrollment.BIOMETRICS] when biometric hardware is supported and the device is enrolled;
     * otherwise [Enrollment.PIN].
     */
    fun deviceEligibleEnrollment(): Enrollment {
        if (biometricLockService.isDeviceBiometricLockingEnabled()) {
            return Enrollment.BIOMETRICS
        }

        return Enrollment.PIN
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
                notifyUnlockSuccessful(Enrollment.PIN)
                success()
            },
            fail = {
                notifyUnlockFailed(Enrollment.PIN)
                fail(it)
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
                notifyUnlockSuccessful(Enrollment.BIOMETRICS)
                success()
            },
            fail = {
                notifyUnlockFailed(Enrollment.BIOMETRICS)
                fail(it)
            }
        )
    }

    /**
     * This will attempt to authenticate with the device's biometric service and,
     * upon authorization success, will locally enroll the user. Assuming no other
     * service has been enrolled, future calls to [enrollment] should return [Enrollment.BIOMETRICS].
     */
    fun enrollBiometricAuthentication(
        success: () -> Unit,
        fail: (BiometricLockService.ErrorCode) -> Unit
    ) {

        biometricLockService.authenticate(
            localEnrollmentCheckRequired = false,
            success = {
                biometricLockService.enroll()

                success()
            },
            fail = fail
        )
    }

    private fun notifyUnlockSuccessful(enrollment: Enrollment) {
        // TODO
    }

    private fun notifyUnlockFailed(enrollment: Enrollment) {
        // TODO
    }

    /**
     * Invalidate all enrollments so that [enrollment] returns [Enrollment.NONE]
     */
    fun invalidateEnrollments() {
        pinLockService.invalidateEnrollment()
        biometricLockService.invalidateEnrollment()
    }
}