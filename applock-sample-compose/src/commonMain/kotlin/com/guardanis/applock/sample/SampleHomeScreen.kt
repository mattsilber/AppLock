package com.guardanis.applock.sample

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

        var statusInformation by remember({ mutableStateOf("") })

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            content = {
                Text(
                    text = statusInformation,
                    color = Color.Black,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(all = 24.dp)
                )

                Button(
                    modifier = buttonModifier,
                    onClick = {
                        when (AppLock.enrollment()) {
                            AppLock.Enrollment.NONE -> {
                                when (AppLock.deviceEligibleEnrollment()) {
                                    AppLock.Enrollment.BIOMETRICS -> {
                                        AppLock.enrollBiometricAuthentication(
                                            success = {
                                                statusInformation = "Biometrics Enrollment Success"
                                            },
                                            fail = {
                                                statusInformation = "Biometrics Enrollment Failed: $it"
                                            }
                                        )
                                    }
                                    else -> {
                                        navigator.push(
                                            PINEnrollmentScreen(
                                                config,
                                                {
                                                    statusInformation = "PIN Enrollment Success"

                                                    navigator.pop()
                                                }
                                            )
                                        )
                                    }
                                }
                            }
                            AppLock.Enrollment.BIOMETRICS -> {
                                AppLock.biometricAuthenticate(
                                    success = {
                                        statusInformation = "Biometrics Unlock Success"
                                    },
                                    fail = {
                                        statusInformation = "Biometrics Unlock Failed: $it"
                                    }
                                )
                            }
                            AppLock.Enrollment.PIN -> {
                                navigator.push(
                                    PINUnlockScreen(
                                        config,
                                        {
                                            statusInformation = "PIN Unlock Success"

                                            navigator.pop()
                                        }
                                    )
                                )
                            }
                        }
                    },
                    content = {
                        Text("Check Device-Eligible Enrollment")
                    }
                )

                Button(
                    modifier = buttonModifier,
                    onClick = {
                        statusInformation = "Enrollments invalidated"

                        AppLock.invalidateEnrollments()
                    },
                    content = {
                        Text("Invalidate Enrollments")
                    }
                )
            }
        )
    })
}