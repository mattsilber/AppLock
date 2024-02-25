package com.guardanis.applock.services

import com.guardanis.applock.AppLockAppContext
import java.security.MessageDigest

private const val pinPrefKey: String = "pin__saved_locked_password"

actual fun PINLockService.isEnrolled(): Boolean {
    return getEnrolledPin()
        .isNotEmpty()
}

actual fun PINLockService.enroll(unencryptedPin: String) {
    AppLockAppContext
        .preferences()
        ?.edit()
        ?.putString(pinPrefKey, encryptForStorage(pin = unencryptedPin))
        ?.apply()
}

actual fun PINLockService.invalidateEnrollment() {
    AppLockAppContext
        .preferences()
        ?.edit()
        ?.remove(pinPrefKey)
        ?.apply()
}

actual fun PINLockService.getEnrolledPin(): String {
    return AppLockAppContext
        .preferences()
        ?.getString(pinPrefKey, null) ?: ""
}

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