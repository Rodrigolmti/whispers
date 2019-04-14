package com.vortex.secret.data.model

data class Session(
    var nickname: String?,
    var userUuid: String,
    var userAnonymousMode: Boolean
)