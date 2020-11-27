package com.mercadopago.android.px.internal.core

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

class PermissionHelper {

    private lateinit var context: Context

    fun initialize(context: Context) {
        this.context = context
    }

    fun isLocationGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        @JvmStatic
        val instance by lazy { PermissionHelper() }
    }
}