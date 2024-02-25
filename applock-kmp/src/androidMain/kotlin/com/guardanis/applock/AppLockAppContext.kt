package com.guardanis.applock

import android.content.Context
import android.content.SharedPreferences
import java.lang.ref.WeakReference

object AppLockAppContext {

    var context: WeakReference<Context?> = WeakReference<Context?>(null)

    fun preferences(): SharedPreferences? {
        return context.get()?.getSharedPreferences("pin__preferences", Context.MODE_PRIVATE)
    }
}