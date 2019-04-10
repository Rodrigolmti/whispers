package com.vortex.secret.data.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.vortex.secret.data.UserSession
import com.vortex.secret.data.local.ILocalPreferences
import com.vortex.secret.data.local.NO_VALUE
import com.vortex.secret.data.local.USER_ID
import com.vortex.secret.data.remote.IFirestoreManager
import com.vortex.secret.util.Result
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
    ): Result<AuthResult>

    suspend fun signInUserWithEmail(
        email: String,
        password: String
    ): Result<AuthResult>

    suspend fun verifyUserSession(): Result<Boolean>
}

class AuthRepository(
    private val firestoreManager: IFirestoreManager,
    private val localPreferences: ILocalPreferences
) : IAuthRepository {

    override suspend fun signUpUserWithEmail(email: String, password: String): Result<AuthResult> {
        return withContext(IO) {
            suspendCoroutine<Result<AuthResult>> { continuation ->
                try {
                    firestoreManager.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            handleTaskResult(task, continuation)
                        }
                } catch (error: Exception) {
                    continuation.resumeWithException(error)
                }
            }
        }
    }

    override suspend fun signInUserWithEmail(email: String, password: String): Result<AuthResult> {
        return withContext(IO) {
            suspendCoroutine<Result<AuthResult>> { continuation ->
                try {
                    firestoreManager.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            handleTaskResult(task, continuation)
                        }
                } catch (error: Exception) {
                    continuation.resumeWithException(error)
                }
            }
        }
    }

    override suspend fun verifyUserSession(): Result<Boolean> {
        return withContext(IO) {
            suspendCoroutine<Result<Boolean>> { continuation ->
                try {
                    val response = localPreferences.getString(USER_ID)
                    response.takeIf { it != NO_VALUE }?.let { UserSession.setupUser(it) }
                    continuation.resume(Result.Success(response != NO_VALUE))
                } catch (error: Exception) {
                    continuation.resumeWithException(error)
                }
            }
        }
    }

    private fun handleTaskResult(
        task: Task<AuthResult>,
        continuation: Continuation<Result<AuthResult>>
    ) = if (task.isSuccessful) {
        task.result?.let {
            val userId = it.user.uid
            localPreferences.putString(USER_ID, userId)
            continuation.resume(Result.Success(it))
            UserSession.setupUser(userId)
        }
    } else {
        task.exception?.let { continuation.resume(Result.Error(it)) }
    }
}
