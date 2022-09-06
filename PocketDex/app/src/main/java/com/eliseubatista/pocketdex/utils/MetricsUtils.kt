package com.eliseubatista.pocketdex.utils

import android.content.Context

fun pxToDp(context: Context, pxValue: Int): Int {
    val res = context.resources

    val density = res.displayMetrics.density

    return (pxValue / density).toInt()
}

fun dpToPx(context: Context, dpValue: Int): Int {
    val res = context.resources

    val density = res.displayMetrics.density

    return (dpValue * density).toInt()
}