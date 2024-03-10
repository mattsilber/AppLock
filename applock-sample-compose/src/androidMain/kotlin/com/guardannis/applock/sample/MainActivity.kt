package com.guardannis.applock.sample

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.fragment.app.FragmentActivity
import com.guardanis.applock.AppLockAppContext
import com.guardanis.applock.sample.SampleApp
import java.lang.ref.WeakReference

class MainActivity: FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // This should generally be set in the Application, but I'm lazy.
        // If it's not included, the [SharedPreferences] will be inaccessible
        // on Android, preventing enrollment persistence.
        AppLockAppContext.context = WeakReference(applicationContext)

        setContent(
            content = {
                SampleApp()
            }
        )
    }

    override fun onResume() {
        super.onResume()

        // Required for our [BiometricLockService] to create a [BiometricPrompt]
        // in the correct Activity
        AppLockAppContext.activity = WeakReference(this)
    }
}