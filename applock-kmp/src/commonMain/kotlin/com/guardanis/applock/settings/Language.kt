package com.guardanis.applock.settings

/**
 * A class for overriding the default text used within the SDK, until KMP
 * supports resources as part of the publication
 */
data class Language(
    val pinCreation: PINCreationLanguage = PINCreationLanguage()
)

data class PINCreationLanguage(
    val createDescription: String = "Create a PIN to secure this application.",
    val confirmDescription: String = "Create a PIN to secure this application.",
    val errorIncorrectLength: String = "Wrong number of digits! Please try again",
    val errorMismatch: String = "The PINs don't match!",
)