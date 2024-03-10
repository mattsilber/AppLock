package com.guardanis.applock.settings

data class BiometricsLanguage(
    val enroll: BiometricsEnrollLanguage = BiometricsEnrollLanguage(),
    val unlock: BiometricsUnlockLanguage = BiometricsUnlockLanguage()
)

data class BiometricsEnrollLanguage(
    val title: String = "Unlock to Enable Biometric Auth",
    val subtitle: String? = null,
    val description: String? = null,
    val negativeAction: String = "Cancel",
)

data class BiometricsUnlockLanguage(
    val title: String = "Unlock to Continue",
    val subtitle: String? = null,
    val description: String? = null,
    val negativeAction: String = "Cancel",
)
