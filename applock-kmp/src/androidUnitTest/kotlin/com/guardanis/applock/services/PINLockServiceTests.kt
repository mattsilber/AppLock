package com.guardanis.applock.services

import androidx.test.core.app.ApplicationProvider
import com.guardanis.applock.AppLockAppContext
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.lang.ref.WeakReference
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify

@RunWith(RobolectricTestRunner::class)
class PINLockServiceTests {

    private val service = PINLockService()
    private val success = mock<() -> Unit>()
    private val fail = mock<(PINLockService.ErrorCode) -> Unit>()

    @Before
    fun setupAppLockAppContext() {
        AppLockAppContext.context = WeakReference(ApplicationProvider.getApplicationContext())
    }

    @Before
    fun clearPINLockServiceEnrollments() {
        service.invalidateEnrollment()
    }

    @Test
    fun testAuthenticateWhenNotEnrolledFailsWithNotEnrolled() {
        service.authenticate("123", success, fail)

        verify(fail, times(1))
            .invoke(eq(PINLockService.ErrorCode.NOT_ENROLLED))
    }

    @Test
    fun testAuthenticateWhenPINDoesNotMatchFailsWithMismatch() {
        service.enroll(unencryptedPin = "321")
        service.authenticate("123", success, fail)

        verify(fail, times(1))
            .invoke(eq(PINLockService.ErrorCode.MISMATCH))
    }

    @Test
    fun testAuthenticateWithMatchingPINCallsSuccess() {
        service.enroll(unencryptedPin = "123")
        service.authenticate("123", success, fail)

        verify(success, times(1))
            .invoke()
    }
}