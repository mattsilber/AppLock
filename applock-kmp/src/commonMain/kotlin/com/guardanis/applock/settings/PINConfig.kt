package com.guardanis.applock.settings

data class PINConfig(
    val theme: PINTheme = PINTheme(),
    val language: PINLanguage = PINLanguage()
)