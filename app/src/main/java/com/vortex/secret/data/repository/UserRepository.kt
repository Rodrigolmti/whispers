package com.vortex.secret.data.repository

import com.vortex.secret.data.UserSession
import com.vortex.secret.data.local.*
import com.vortex.secret.data.model.Session
import com.vortex.secret.data.remote.AnalyticsManager
import com.vortex.secret.data.remote.IFirestoreManager
import com.vortex.secret.util.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

interface IUserRepository {

    suspend fun updateUserAnonymousMode(anonymous: Boolean): Result<Boolean>

    suspend fun getUserAnonymousMode(): Result<Boolean>

    fun updateUserSession()

    fun clearUserSession()
}

class UserRepository(
    private val localPreferences: ILocalPreferences,
    private val firestoreManager: IFirestoreManager,
    private val analyticsManager: AnalyticsManager
) : IUserRepository {

    override suspend fun updateUserAnonymousMode(anonymous: Boolean): Result<Boolean> {
        return withContext(Dispatchers.IO) {
            suspendCoroutine<Result<Boolean>> { continuation ->
                try {

                    localPreferences.putBoolean(ANONYMOUS_MODE, anonymous)
                    UserSession.session?.userAnonymousMode = anonymous
                    continuation.resume(Result.Success(true))

                } catch (error: Exception) {
                    continuation.resumeWithException(error)
                    analyticsManager.sendError(error)
                }
            }
        }
    }

    override suspend fun getUserAnonymousMode(): Result<Boolean> {
        return withContext(Dispatchers.IO) {
            suspendCoroutine<Result<Boolean>> { continuation ->
                try {

                    continuation.resume(Result.Success(localPreferences.getBoolean(ANONYMOUS_MODE)))

                } catch (error: Exception) {
                    continuation.resumeWithException(error)
                    analyticsManager.sendError(error)
                }
            }
        }
    }

    override fun updateUserSession() {
        try {

            val nickname = if (localPreferences.getString(USER_NAME) == NO_VALUE) {
                firestoreManager.getCurrentUser()?.displayName
            } else {
                localPreferences.getString(USER_NAME)
            }

            UserSession.setupUserSession(
                Session(
                    nickname,
                    localPreferences.getString(USER_ID),
                    localPreferences.getBoolean(ANONYMOUS_MODE)
                )
            )

        } catch (error: Exception) {
            analyticsManager.sendError(error)
        }
    }

    override fun clearUserSession() {
        try {

            UserSession.removeUser()
            localPreferences.clearKey(USER_ID)
            localPreferences.clearKey(ANONYMOUS_MODE)

        } catch (error: Exception) {
            analyticsManager.sendError(error)
        }
    }
}