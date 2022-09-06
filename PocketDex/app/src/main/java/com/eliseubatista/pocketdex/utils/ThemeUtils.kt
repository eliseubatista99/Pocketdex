package com.eliseubatista.pocketdex.utils

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate

fun isInDarkMode(): Boolean {

    return when (Configuration.UI_MODE_NIGHT_MASK) {
        Configuration.UI_MODE_NIGHT_NO -> false // Night mode is not active, we're using the light theme
        Configuration.UI_MODE_NIGHT_YES -> true // Night mode is active, we're using dark theme
        else -> true
    }
}

fun changeApplicationNightMode(dark: Boolean) {
    when (dark) {
        false -> {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        else -> {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }
}