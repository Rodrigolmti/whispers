package com.vortex.secret.data.model

data class Session(
    val nickname: String?,
    val userUuid: String,
    val userAnonymousMode: Boolean
)