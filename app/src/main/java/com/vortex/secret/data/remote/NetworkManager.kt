package com.vortex.secret.data.remote

import android.content.Context
import android.net.ConnectivityManager

class NetworkManager(private val context: Context) {

    fun isOnline(): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.activeNetworkInfo
        return netInfo?.isConnected ?: false
    }
}