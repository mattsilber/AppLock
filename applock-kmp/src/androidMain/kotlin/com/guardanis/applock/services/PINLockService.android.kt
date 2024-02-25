package com.guardanis.applock.services

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import java.security.MessageDigest

private const val pinPrefKey: String = "pin__saved_locked_password"

@Composable
actual fun PINLockService.isEnrolled(): Boolean {
    return getEnrolledPin()
        .isNotEmpty()
}

@Composable
actual fun PINLockService.enroll(encryptedPin: String) {
    appLockPreferences(LocalContext.current)
        .edit()
        .putString(pinPrefKey, encryptedPin)
        .apply()
}

@Composable
actual fun PINLockService.invalidateEnrollment() {
    appLockPreferences(LocalContext.current)
        .edit()
        .remove(pinPrefKey)
        .apply()
}

@Composable
actual fun PINLockService.getEnrolledPin(): String {
    return appLockPreferences(LocalContext.current)
        .getString(pinPrefKey, null) ?: ""
}

@Composable
actual fun PINLockService.encryptForStorage(pin: String): String {
    try {
        val md = MessageDigest.getInstance("SHA-1")
        md.update(pin.toByteArray(charset("UTF-8")), 0, pin.length)

        return convertToHex(md.digest())
    }
    catch (e: Exception) {
        e.printStackTrace()
    }

    return ""
}

/**
 * I have no fucking idea where this originally came from and I'm not stepping through to figure out why,
 * or even if, it actually works right now... The important thing is it's deterministic and we're
 * not saving the user's selected PIN
 */
private fun convertToHex(data: ByteArray): String {
    val buf = StringBuilder()

    for (b in data) {
        var half = b.toInt() ushr 4 and 0x0F
        var twoHalves = 0

        do {
            buf.append(if (half <= 9) ('0'.code + half).toChar() else ('a'.code + (half - 10)).toChar())
            half = b.toInt() and 0x0F
        }
        while (twoHalves++ < 1)
    }

    return buf.toString()
}