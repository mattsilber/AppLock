package com.guardannis.applock.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import cafe.adriel.voyager.navigator.Navigator
import com.guardanis.applock.sample.SampleHomeScreen
import com.guardanis.applock.settings.Config

class MainActivity: ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent(
            content = {
                Navigator(
                    SampleHomeScreen(
                        config = Config()
                    )
                )
            }
        )
    }
}