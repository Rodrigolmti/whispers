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
    ): Result<Boolean>

    suspend fun signInUserWithEmail(
        email: String,
        password: String
    ): Result<Boolean>

    suspend fun verifyUserSession(): Result<Boolean>
}

class AuthRepository(
    private val firestoreManager: IFirestoreManager,
    private val localPreferences: ILocalPreferences
) : IAuthRepository {

    override suspend fun signUpUserWithEmail(email: String, password: String): Result<Boolean> {
        return withContext(IO) {
            suspendCoroutine<Result<Boolean>> { continuation ->
                try {

                    firestoreManager.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                        handleAuthResult(task, continuation)
                    }.addOnFailureListener { error ->
                        continuation.resume(Result.Error(error))
                    }

                } catch (error: Exception) {
                    continuation.resumeWithException(error)
                }
            }
        }
    }

    override suspend fun signInUserWithEmail(email: String, password: String): Result<Boolean> {
        return withContext(IO) {
            suspendCoroutine<Result<Boolean>> { continuation ->
                try {

                    firestoreManager.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                        handleAuthResult(task, continuation)
                    }.addOnFailureListener { error ->
                        continuation.resume(Result.Error(error))
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

                    val userUuid = localPreferences.getString(USER_ID)
                    userUuid.takeIf { it != NO_VALUE }?.let {
                        UserSession.setupUser(it)
                        continuation.resume(Result.Success(true))
                    } ?: run {
                        continuation.resume(Result.Success(false))
                    }

                } catch (error: Exception) {
                    continuation.resumeWithException(error)
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
                    UserSession.setupUser(uuid)
                    localPreferences.putString(USER_ID, uuid)
                    continuation.resume(Result.Success(true))
                } ?: run {
                    continuation.resume(Result.Success(false))
                }
            }
        } else {
            task.exception?.let { continuation.resume(Result.Error(it)) }
        }
    }
}
