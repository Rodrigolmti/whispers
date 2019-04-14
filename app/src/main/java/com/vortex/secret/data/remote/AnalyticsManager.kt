package com.vortex.secret.data.remote

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics

enum class AnalyticsEvents(event: String) {
    ADD_POST("add_post"),
    REMOVE_POST("remove_post"),
    ADD_COMMENT("add_comment"),
    LIKE_POST("like_post"),
    LOGOUT("logout")
}

class AnalyticsManager(context: Context) {

    private var firebaseAnalytics: FirebaseAnalytics = FirebaseAnalytics.getInstance(context)

    fun sendEvent(event: String) {
        firebaseAnalytics.logEvent(event, Bundle())
    }
}