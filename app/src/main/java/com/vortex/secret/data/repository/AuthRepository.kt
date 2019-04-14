package com.vortex.secret.data.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.vortex.secret.data.local.ILocalPreferences
import com.vortex.secret.data.local.NO_VALUE
import com.vortex.secret.data.local.USER_ID
import com.vortex.secret.data.remote.AnalyticsManager
import com.vortex.secret.data.remote.IFirestoreManager
import com.vortex.secret.data.remote.NetworkManager
import com.vortex.secret.util.Result
import com.vortex.secret.util.exceptions.NetworkError
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

interface IAuthRepository {

    suspend fun signUpUserWithEmail(
        email: String,
        password: String
    ): Result<Boolean>

    suspend fun signInUserWithEmail(
        email: String,
        password: String
    ): Result<Boolean>

    suspend fun verifyUserSession(): Result<Boolean>

    suspend fun logoutUser(): Result<Boolean>
}

class AuthRepository(
    private val firestoreManager: IFirestoreManager,
    private val localPreferences: ILocalPreferences,
    private val analyticsManager: AnalyticsManager,
    private val userRepository: IUserRepository,
    private val networkManager: NetworkManager
) : IAuthRepository {

    override suspend fun signUpUserWithEmail(email: String, password: String): Result<Boolean> {
        return withContext(IO) {
            suspendCoroutine<Result<Boolean>> { continuation ->
                try {

                    if (!networkManager.isOnline()) {
                        continuation.resume(Result.Error(NetworkError()))
                    } else {
                        firestoreManager.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                            handleAuthResult(task, continuation)
                        }.addOnFailureListener { error ->
                            continuation.resume(Result.Error(error))
                            analyticsManager.sendError(error)
                        }
                    }

                } catch (error: Exception) {
                    continuation.resumeWithException(error)
                    analyticsManager.sendError(error)
                }
            }
        }
    }

    override suspend fun signInUserWithEmail(email: String, password: String): Result<Boolean> {
        return withContext(IO) {
            suspendCoroutine<Result<Boolean>> { continuation ->
                try {

                    if (!networkManager.isOnline()) {
                        continuation.resume(Result.Error(NetworkError()))
                    } else {
                        firestoreManager.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                            handleAuthResult(task, continuation)
                        }.addOnFailureListener { error ->
                            continuation.resume(Result.Error(error))
                            analyticsManager.sendError(error)
                        }
                    }

                } catch (error: Exception) {
                    continuation.resumeWithException(error)
                    analyticsManager.sendError(error)
                }
            }
        }
    }

    override suspend fun verifyUserSession(): Result<Boolean> {
        return withContext(IO) {
            suspendCoroutine<Result<Boolean>> { continuation ->
                try {

                    val userUuid = localPreferences.getString(USER_ID)
                    userUuid.takeIf { it != NO_VALUE }?.let { uuid ->
                        userRepository.updateUserSession()
                        analyticsManager.updateUserId(uuid)
                        continuation.resume(Result.Success(true))
                    } ?: run {
                        continuation.resume(Result.Success(false))
                    }

                } catch (error: Exception) {
                    continuation.resumeWithException(error)
                    analyticsManager.sendError(error)
                }
            }
        }
    }

    override suspend fun logoutUser(): Result<Boolean> {
        return withContext(IO) {
            suspendCoroutine<Result<Boolean>> { continuation ->
                try {

                    firestoreManager.signOut()
                    userRepository.clearUserSession()
                    continuation.resume(Result.Success(true))

                } catch (error: Exception) {
                    continuation.resumeWithException(error)
                    analyticsManager.sendError(error)
                }
            }
        }
    }

    private fun handleAuthResult(
        task: Task<AuthResult>,
        continuation: Continuation<Result<Boolean>>
    ) {
        if (task.isSuccessful) {
            task.result?.let {
                it.user.uid.takeIf { uid -> uid.isNotEmpty() }?.let { uuid ->
                    userRepository.updateUserSession()
                    analyticsManager.updateUserId(uuid)
                    localPreferences.putString(USER_ID, uuid)
                    continuation.resume(Result.Success(true))
                } ?: run {
                    continuation.resume(Result.Success(false))
                }
            }
        } else {
            task.exception?.let { error ->
                continuation.resume(Result.Error(error))
                analyticsManager.sendError(error)
            }
        }
    }
}
