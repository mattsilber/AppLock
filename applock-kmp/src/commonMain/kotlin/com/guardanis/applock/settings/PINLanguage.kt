package com.guardanis.applock.settings

/**
 * A class for overriding the default text used within the SDK, until KMP
 * supports resources as part of the publication
 */
data class PINLanguage(
    val enroll: PINEnrollLanguage = PINEnrollLanguage(),
    val unlock: PINUnlockLanguage = PINUnlockLanguage()
)

data class PINEnrollLanguage(
    val createDescription: String = "Create a PIN to secure this application.",
    val confirmDescription: String = "Re-enter your PIN to confirm.",
    val errorIncorrectLength: String = "Wrong number of digits! Please try again",
    val errorMismatch: String = "The PINs don't match!",
)

data class PINUnlockLanguage(
    val inputDescription: String = "Enter your PIN to unlock.",
    val errorMismatch: String = "The PINs don't match!",
)