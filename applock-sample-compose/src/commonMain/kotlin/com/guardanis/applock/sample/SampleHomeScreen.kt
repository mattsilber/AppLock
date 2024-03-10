package com.guardanis.applock.sample

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import com.guardanis.applock.AppLock
import com.guardanis.applock.sample.theme.AppTheme
import com.guardanis.applock.settings.PINConfig

class SampleHomeScreen(
    val config: PINConfig
): Screen {

    private val statusInformation = mutableStateOf("Click an action to get started")

    @Composable
    override fun Content() = AppTheme({
        val navigator = LocalNavigator.current!!

        val statusInformation = remember({ statusInformation })

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
            content = {
                Text(
                    text = statusInformation.value,
                    modifier = Modifier.padding(all = 24.dp),
                    style = MaterialTheme.typography.headlineMedium
                )

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        deviceEligibleEnrollOrUnlockButtonClicked(
                            navigator,
                            statusInformation
                        )
                    },
                    content = {
                        Text("Device-Eligible Enrollment / Unlock")
                    }
                )

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        pinEnrollOrUnlockButtonClicked(
                            navigator,
                            statusInformation
                        )
                    },
                    content = {
                        Text("Check PIN Enrollment / Unlock")
                    }
                )

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        statusInformation.value = "Enrollments invalidated"

                        AppLock.invalidateEnrollments()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    ),
                    content = {
                        Text(
                            "Invalidate Enrollments",
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                )
            }
        )
    })

    private fun deviceEligibleEnrollOrUnlockButtonClicked(
        navigator: Navigator,
        statusInformation: MutableState<String>
    ) {

        statusInformation.value = ""

        when (AppLock.enrollment()) {
            AppLock.Enrollment.NONE -> {
                when (AppLock.deviceEligibleEnrollment()) {
                    AppLock.Enrollment.BIOMETRICS -> {
                        AppLock.enrollBiometricAuthentication(
                            success = {
                                statusInformation.value = "Biometrics Enrollment Success"
                            },
                            fail = {
                                statusInformation.value = "Biometrics Enrollment Failed: $it"
                            }
                        )
                    }
                    else -> {
                        navigator.push(
                            PINEnrollmentScreen(
                                config,
                                completionStatusUpdateMessage = statusInformation
                            )
                        )
                    }
                }
            }
            AppLock.Enrollment.BIOMETRICS -> {
                AppLock.biometricAuthenticate(
                    success = {
                        statusInformation.value = "Biometrics Unlock Success"
                    },
                    fail = {
                        statusInformation.value = "Biometrics Unlock Failed: $it"
                    }
                )
            }
            AppLock.Enrollment.PIN -> {
                navigator.push(
                    PINUnlockScreen(
                        config,
                        completionStatusUpdateMessage = statusInformation
                    )
                )
            }
        }
    }

    private fun pinEnrollOrUnlockButtonClicked(
        navigator: Navigator,
        statusInformation: MutableState<String>
    ) {

        statusInformation.value = ""

        when (AppLock.enrollment()) {
            AppLock.Enrollment.NONE -> {
                navigator.push(
                    PINEnrollmentScreen(
                        config,
                        completionStatusUpdateMessage = statusInformation
                    )
                )
            }
            AppLock.Enrollment.BIOMETRICS -> {
                statusInformation.value = "Can't enroll in PIN while enrolled in biometrics"
            }
            AppLock.Enrollment.PIN -> {
                navigator.push(
                    PINUnlockScreen(
                        config,
                        completionStatusUpdateMessage = statusInformation
                    )
                )
            }
        }
    }
}