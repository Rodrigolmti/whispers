package com.vortex.secret.data

import com.vortex.secret.data.model.Session

object UserSession {

    var session: Session? = null

    fun setupUserSession(session: Session) {
        this.session = session
    }

    fun removeUser() {
        session = null
    }
}