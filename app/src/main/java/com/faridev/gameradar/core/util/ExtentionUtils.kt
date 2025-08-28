package com.faridev.gameradar.core.util

import android.content.Context
import java.io.IOException

fun Context.loadJSONFromAsset(fileName: String): String? {
    return try {
        assets.open(fileName).bufferedReader().use { it.readText() }
    } catch (ex: IOException) {
        ex.printStackTrace()
        null
    }
}