package com.guardanis.applock.services

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.toCValues
import platform.CoreCrypto.CC_LONG
import platform.CoreCrypto.CC_SHA1
import platform.CoreCrypto.CC_SHA1_DIGEST_LENGTH
import platform.Foundation.NSString
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.NSUserDefaults
import platform.Foundation.dataUsingEncoding
import platform.Foundation.setValue

private const val pinPrefKey: String = "pin__saved_locked_password"

actual fun PINLockService.isEnrolled(): Boolean {
    return getEnrolledPin()
        .isNotEmpty()
}

actual fun PINLockService.enroll(unencryptedPin: String) {
    NSUserDefaults.standardUserDefaults.setValue(
        value = encryptForStorage(pin = unencryptedPin),
        forKey = pinPrefKey
    )
}

actual fun PINLockService.invalidateEnrollment() {
    NSUserDefaults.standardUserDefaults.removeObjectForKey(pinPrefKey)
}

actual fun PINLockService.getEnrolledPin(): String {
    return NSUserDefaults.standardUserDefaults.stringForKey(pinPrefKey) ?: ""
}

@OptIn(ExperimentalForeignApi::class, ExperimentalStdlibApi::class)
actual fun PINLockService.encryptForStorage(pin: String): String {
    try {
        val data = (pin as NSString)
            .dataUsingEncoding(NSUTF8StringEncoding)!!

        val digest: UByteArray = 0
            .until(CC_SHA1_DIGEST_LENGTH)
            .map({ 0.toUByte() })
            .toUByteArray()

        memScoped({
            CC_SHA1(
                data = data.bytes,
                len = data.length.toUInt(),
                md = digest.toCValues().ptr
            )

            return digest.joinToString(
                separator = "",
                transform = UByte::toHexString
            )
        })
    }
    catch (e: Exception) {
        // I say I'll deal with this later, but we all know I really won't... Sorry...
        e.printStackTrace()
    }

    return ""
}