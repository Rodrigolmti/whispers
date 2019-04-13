package com.vortex.secret.data.local

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.vortex.secret.util.extensions.decrypt
import com.vortex.secret.util.extensions.encrypt

const val FILENAME = "whispers_secure_prefs"
const val NO_VALUE = "no_value"
const val USER_ID = "user_id"

interface ILocalPreferences {

    fun putString(key: String, value: String)

    fun getString(key: String) : String

    fun clearKey(key: String)
}

class LocalPreferences(context: Context) : ILocalPreferences {

    private val preferences = context.getSharedPreferences(FILENAME, MODE_PRIVATE)

    override fun getString(key: String): String {
        var string = preferences.getString(key.encrypt(), NO_VALUE)
        if (string != NO_VALUE) { string = string.decrypt() }
        return string
    }

    override fun putString(key: String, value: String) {
        val editor = preferences.edit()
        editor.putString(key.encrypt(), value.encrypt())
        editor.apply()
    }

    override fun clearKey(key: String) {
        val editor = preferences.edit()
        editor.remove(key.encrypt())
        editor.apply()
    }
}