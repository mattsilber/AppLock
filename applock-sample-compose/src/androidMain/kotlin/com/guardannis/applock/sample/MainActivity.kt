package com.guardannis.applock.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.Navigator
import com.guardanis.applock.AppLockAppContext
import com.guardanis.applock.sample.SampleHomeScreen
import com.guardanis.applock.settings.Config
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
                Navigator(
                    SampleHomeScreen(
                        config = Config()
                    ),
                    content = {
                        Scaffold(
                            topBar = {

                            },
                            content = { padding ->
                                Box(
                                    modifier = Modifier.padding(padding),
                                    content = {
                                        CurrentScreen()
                                    }
                                )
                            }
                        )
                    }
                )
            }
        )
    }
}