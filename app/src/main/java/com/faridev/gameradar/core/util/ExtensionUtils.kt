package com.faridev.gameradar.core.util

import android.content.Context
import android.util.Patterns
import java.io.IOException

fun Context.loadJSONFromAsset(fileName: String): String? = try {
    assets.open(fileName).bufferedReader().use { it.readText() }
} catch (ex: IOException) {
    ex.printStackTrace()
    null
}

fun String.checkUrlValidation(
    onValidUrl: (validUrl: String) -> Unit,
    onUrlValidationError: (() -> Unit)? = null,
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

private val HtmlTagRegex = Regex("<[^>]*>")
private val HtmlEntities = mapOf(
    "&amp;" to "&",
    "&lt;" to "<",
    "&gt;" to ">",
    "&quot;" to "\"",
    "&#39;" to "'",
    "&nbsp;" to " ",
)

fun String.stripHtml(): String {
    var result = HtmlTagRegex.replace(this, "")
    HtmlEntities.forEach { (entity, char) -> result = result.replace(entity, char) }
    return result.trim()
}
