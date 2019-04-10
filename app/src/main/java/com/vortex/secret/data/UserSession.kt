package com.vortex.secret.data

object UserSession {

    var userId: String? = null

    fun setupUser(userId: String) {
        this.userId = userId
    }

    fun removeUser() {
        userId = null
    }
}