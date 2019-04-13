package com.vortex.secret.util.extensions

import android.app.Activity
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.google.android.material.snackbar.Snackbar

fun View.showSnackBar(message: String) = Snackbar.make(this, message, Snackbar.LENGTH_LONG).show()

fun View.gone() = run { this.visibility = View.GONE }
fun View.visible() = run { this.visibility = View.VISIBLE }

fun View.hideKeyboard() {
    val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(this.windowToken, 0)
}