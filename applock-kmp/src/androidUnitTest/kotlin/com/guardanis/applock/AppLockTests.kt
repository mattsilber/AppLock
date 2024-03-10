package com.guardanis.applock

import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.robolectric.RobolectricTestRunner
import java.lang.ref.WeakReference

@RunWith(RobolectricTestRunner::class)
class AppLockTests {

    @Before
    fun setupAppLockAppContext() {
        AppLockAppContext.context = WeakReference(ApplicationProvider.getApplicationContext())
    }

    @Before
    fun clearEnrollments() {
        AppLock.invalidateEnrollments()
    }

    @Test
    fun testEnrollmentReturnsNonWhenNotEnrolled() {
        assertEquals(AppLock.Enrollment.NONE, AppLock.enrollment())
    }

    @Test
    fun testEnrollmentReturnsPINWhenEnrolledInPINLocking() {
        AppLock.enrollPinAuthentication("123")

        assertEquals(AppLock.Enrollment.PIN, AppLock.enrollment())
    }

    @Test
    fun testCanAuthenticateWithPINEnrollmentService() {
        AppLock.enrollPinAuthentication("123")

        val success = mock<() -> Unit>()

        AppLock.pinAuthenticate("123", success, { })

        verify(success, times(1))
            .invoke()
    }
}