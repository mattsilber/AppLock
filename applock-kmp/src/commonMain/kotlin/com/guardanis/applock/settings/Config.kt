package com.guardanis.applock.settings

data class Config(
    val pinTheme: PINTheme = PINTheme(),
    val language: Language = Language(),
    val biometricServiceAllowed: Boolean = true,
)