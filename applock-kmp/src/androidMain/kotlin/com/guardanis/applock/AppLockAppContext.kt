package com.guardanis.applock

import android.content.Context
import android.content.SharedPreferences
import androidx.fragment.app.FragmentActivity
import java.lang.ref.WeakReference

object AppLockAppContext {

    var context: WeakReference<Context?> = WeakReference<Context?>(null)
    var activity: WeakReference<FragmentActivity?> = WeakReference<FragmentActivity?>(null)

    fun preferences(): SharedPreferences? {
        return context.get()?.getSharedPreferences("pin__preferences", Context.MODE_PRIVATE)
    }
}