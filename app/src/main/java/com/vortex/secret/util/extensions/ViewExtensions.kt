package com.vortex.secret.util.extensions

import android.view.View
import com.google.android.material.snackbar.Snackbar

fun View.showSnackBar(message: String) = Snackbar.make(this, message, Snackbar.LENGTH_LONG).show()

fun View.gone() = run { this.visibility = View.GONE }
fun View.visible() = run { this.visibility = View.VISIBLE }
