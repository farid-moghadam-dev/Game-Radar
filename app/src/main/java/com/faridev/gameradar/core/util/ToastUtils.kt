package com.faridev.gameradar.core.util

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes

fun Context.showShortToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.showShortToast(@StringRes messageResId: Int) {
    Toast.makeText(this, getString(messageResId), Toast.LENGTH_SHORT).show()
}

fun Context.showLongToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun Context.showLongToast(@StringRes messageResId: Int) {
    Toast.makeText(this, getString(messageResId), Toast.LENGTH_LONG).show()
}