package com.guardanis.applock.settings

data class BiometricsLanguage(
    val enroll: BiometricsEnrollLanguage = BiometricsEnrollLanguage(),
    val unlock: BiometricsUnlockLanguage = BiometricsUnlockLanguage()
)

data class BiometricsEnrollLanguage(
    val reason: String = "Unlock to Enable Biometric Auth",
)

data class BiometricsUnlockLanguage(
    val reason: String = "Unlock to Continue",
)
