package com.guardanis.applock.sample

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import com.guardanis.applock.AppLock
import com.guardanis.applock.sample.theme.AppTheme
import com.guardanis.applock.settings.Config

class SampleHomeScreen(
    val config: Config
): Screen {

    @Composable
    override fun Content() = AppTheme({
        val navigator = LocalNavigator.current!!

        val buttonModifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)

        Column(
            modifier = Modifier.fillMaxSize(),
            content = {
                Button(
                    modifier = buttonModifier,
                    onClick = {
                        when (AppLock.enrollment()) {
                            AppLock.Enrollment.NONE -> {
                                when (AppLock.deviceEligibleEnrollment()) {
                                    AppLock.Enrollment.BIOMETRICS -> {
                                        AppLock.enrollBiometricAuthentication(
                                            success = { println("Biometrics Enrollment Success") },
                                            fail = { println("Biometrics Enrollment Failed: $it") }
                                        )
                                    }
                                    else -> {
                                        navigator.push(
                                            PINCreationScreen(
                                                config,
                                                { println("PIN Enrollment Success") }
                                            )
                                        )
                                    }
                                }
                            }
                            AppLock.Enrollment.BIOMETRICS -> {
                                AppLock.biometricAuthenticate(
                                    success = { println("Biometrics Unlock Success") },
                                    fail = { println("Biometrics Unlock Failed: $it") }
                                )
                            }
                            AppLock.Enrollment.PIN -> {
                                navigator.push(
                                    PINUnlockScreen(
                                        config,
                                        { println("PIN Unlock Success") }
                                    )
                                )
                            }
                        }
                    },
                    content = {
                        Text("Check Device-Eligible Enrollment")
                    }
                )
            }
        )
    })
}