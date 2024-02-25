package com.guardanis.applock.services

import android.content.Context
import android.content.SharedPreferences

fun appLockPreferences(context: Context): SharedPreferences {
    return context.getSharedPreferences("pin__preferences", Context.MODE_PRIVATE)
}