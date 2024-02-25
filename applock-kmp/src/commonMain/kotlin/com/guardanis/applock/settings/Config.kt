package com.guardanis.applock.settings

data class Config(
    val theme: Theme = Theme(),
    val biometricServiceAllowed: Boolean = true,
)