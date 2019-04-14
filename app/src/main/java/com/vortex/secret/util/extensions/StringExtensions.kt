package com.vortex.secret.util.extensions

import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Base64

fun paintText(
        message: String,
        color: Int,
        startIndex: Int,
        endIndex: Int
) : SpannableString {
    val word = SpannableString(message)
    word.setSpan(ForegroundColorSpan(color), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    return word
}

fun String.encrypt(): String {
    return Base64.encodeToString(this.toByteArray(), Base64.DEFAULT)
}

fun String.decrypt(): String {
    return String(Base64.decode(this, Base64.DEFAULT))
}
