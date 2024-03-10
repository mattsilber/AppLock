package com.guardannis.applock.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.guardanis.applock.AppLockAppContext
import com.guardanis.applock.sample.SampleApp
import java.lang.ref.WeakReference

class MainActivity: ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // This should generally be set in the Application, but I'm lazy.
        // If it's not included, the SharedPreferences will be inaccessible
        // on Android, preventing enrollment persistence.
        AppLockAppContext.context = WeakReference(applicationContext)

        setContent(
            content = {
                SampleApp()
            }
        )
    }
}