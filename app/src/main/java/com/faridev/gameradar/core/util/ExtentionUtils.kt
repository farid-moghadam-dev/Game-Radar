package com.faridev.gameradar.core.util

import android.content.Context
import android.util.Patterns
import java.io.IOException

fun Context.loadJSONFromAsset(fileName: String): String? {
    return try {
        assets.open(fileName).bufferedReader().use { it.readText() }
    } catch (ex: IOException) {
        ex.printStackTrace()
        null
    }
}

fun String.checkUrlValidation(
    onValidUrl: (validUrl : String) -> Unit,
    onUrlValidationError: (() -> Unit)? = null
) {
    if (Patterns.WEB_URL.matcher(this).matches()) {
        val url = if (this.startsWith("http://") || this.startsWith("https://")) {
            this
        } else {
            "https://$this"
        }
        onValidUrl.invoke(url)
    } else {
        onUrlValidationError?.invoke()
    }
}