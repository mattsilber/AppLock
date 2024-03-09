package com.guardanis.applock.services

import androidx.test.core.app.ApplicationProvider
import com.guardanis.applock.AppLockAppContext
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.`when` as MOCK_WHEN
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.robolectric.RobolectricTestRunner
import java.lang.ref.WeakReference

@RunWith(RobolectricTestRunner::class)
class BiometricLockServiceTests {

    private val authenticator = mock<BiometricLockService.Authenticator>()
    private val success = mock<() -> Unit>()
    private val fail = mock<(BiometricLockService.ErrorCode) -> Unit>()

    private val service = BiometricLockService(
        authenticator = authenticator
    )

    @Before
    fun setupAppLockAppContext() {
        AppLockAppContext.context = WeakReference(ApplicationProvider.getApplicationContext())
    }

    @Before
    fun clearBiometricServiceEnrollments() {
        service.invalidateEnrollment()
    }

    @Before
    fun setupMockAuthenticator() {
        MOCK_WHEN(authenticator.isHardwareEligible())
            .thenReturn(false)

        MOCK_WHEN(authenticator.isDeviceBiometricLockingEnabled())
            .thenReturn(false)
    }

    @Test
    fun testAuthenticateWhenNotEnrolledFailsWithNotEnrolled() {
        service.authenticate(success, fail)

        verify(fail, times(1))
            .invoke(eq(BiometricLockService.ErrorCode.NOT_ENROLLED))
    }

    @Test
    fun testAuthenticateWhenNotHardwareEligibleFailsWithDeviceNotEligible() {
        service.enroll()
        service.authenticate(success, fail)

        verify(fail, times(1))
            .invoke(eq(BiometricLockService.ErrorCode.DEVICE_NOT_ELIGIBLE))
    }

    @Test
    fun testAuthenticateWhenNotEnrolledInDeviceBiometricsFailsWithDeviceNotEnrolled() {
        MOCK_WHEN(authenticator.isHardwareEligible())
            .thenReturn(true)

        service.enroll()
        service.authenticate(success, fail)

        verify(fail, times(1))
            .invoke(eq(BiometricLockService.ErrorCode.DEVICE_NOT_ENROLLED))
    }

    @Test
    fun testAuthenticateWhenEnrolledAndEligibleAttemptsToAuthenticate() {
        MOCK_WHEN(authenticator.isHardwareEligible())
            .thenReturn(true)

        MOCK_WHEN(authenticator.isDeviceBiometricLockingEnabled())
            .thenReturn(true)

        service.enroll()
        service.authenticate(success, fail)

        verify(authenticator, times(1))
            .authenticate(eq(success), eq(fail))
    }
}