package com.eliseubatista.pocketdex.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat

fun checkStoragePermision(context: Context): Boolean {

    return ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.READ_EXTERNAL_STORAGE
    ) == PackageManager.PERMISSION_GRANTED
}

fun checkCameraPermision(context: Context): Boolean {
    val res1 = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.CAMERA
    ) == PackageManager.PERMISSION_GRANTED

    val res2 = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    ) == PackageManager.PERMISSION_GRANTED

    return res1 && res2
}

fun requestStoragePermission(activity: Activity) {
    val permissions = listOf(Manifest.permission.READ_EXTERNAL_STORAGE)

    requestPermissions(activity, permissions.toTypedArray(), 100)
}

fun requestCameraPermission(activity: Activity) {
    val permissions = listOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)

    requestPermissions(activity, permissions.toTypedArray(), 100)
}