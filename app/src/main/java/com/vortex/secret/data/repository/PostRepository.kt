package com.vortex.secret.data.repository

import androidx.lifecycle.MutableLiveData
import com.vortex.secret.data.UserSession
import com.vortex.secret.data.mappers.mapFirebaseDocumentToPostModel
import com.vortex.secret.data.model.PostLike
import com.vortex.secret.data.model.PostModel
import com.vortex.secret.data.remote.IFirestoreManager
import com.vortex.secret.util.Result
import com.vortex.secret.util.exceptions.EmptyDataError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

interface IPostRepository {

    val postsMutableLiveData: MutableLiveData<MutableList<PostModel>>

    suspend fun updatePosts(): Result<Boolean>

    suspend fun addPost(postModel: PostModel): Result<Boolean>

    suspend fun updatePostLike(postModel: PostModel): Result<Boolean>

    suspend fun deletePost(postModel: PostModel): Result<Boolean>
}

class PostRepository(
    private val firestoreManager: IFirestoreManager
) : IPostRepository {

    override val postsMutableLiveData: MutableLiveData<MutableList<PostModel>> = MutableLiveData()

    init {
        postsMutableLiveData.value = mutableListOf()
    }

    override suspend fun updatePosts(): Result<Boolean> {
        return withContext(Dispatchers.IO) {
            suspendCoroutine<Result<Boolean>> { continuation ->
                try {

                    firestoreManager.getPostsByDate().addOnSuccessListener { result ->
                        val posts = mapFirebaseDocumentToPostModel(result)
                        posts.takeIf { it.isEmpty() }?.let {
                            continuation.resume(Result.Error(EmptyDataError()))
                        } ?: run {
                            continuation.resume(Result.Success(true))
                            postsMutableLiveData.value = posts
                        }
                    }.addOnFailureListener { error ->
                        continuation.resume(Result.Error(error))
                    }

                } catch (error: Exception) {
                    continuation.resumeWithException(error)
                }
            }
        }
    }

    override suspend fun addPost(postModel: PostModel): Result<Boolean> {
        return withContext(Dispatchers.IO) {
            suspendCoroutine<Result<Boolean>> { continuation ->
                try {

                    postModel.authorId = UserSession.userId
                    firestoreManager.addPost(postModel).addOnSuccessListener {
                        it.id.takeIf { postId -> postId.isNotEmpty() }?.let { postId ->
                            postModel.id = postId
                            postsMutableLiveData.apply {
                                value?.add(0, postModel)
                                postValue(postsMutableLiveData.value)
                            }
                            continuation.resume(Result.Success(true))
                        } ?: run {
                            continuation.resume(Result.Success(false))
                        }
                    }.addOnFailureListener { error ->
                        continuation.resume(Result.Error(error))
                    }

                } catch (error: Exception) {
                    continuation.resumeWithException(error)
                }
            }
        }
    }

    override suspend fun updatePostLike(postModel: PostModel): Result<Boolean> {
        return withContext(Dispatchers.IO) {
            suspendCoroutine<Result<Boolean>> { continuation ->
                try {

                    val authorId = UserSession.userId
                    var removeIndex = -1

                    postModel.likes.forEachIndexed { index, postLike ->
                        if (postLike.authorId == authorId) {
                            removeIndex = index
                        }
                    }

                    if (removeIndex != -1) {
                        postModel.likes.removeAt(removeIndex)
                    } else {
                        postModel.likes.add(PostLike(authorId))
                    }

                    postModel.id?.let {
                        firestoreManager.updatePostLike(postModel).addOnSuccessListener {
                            postsMutableLiveData.postValue(postsMutableLiveData.value)
                            continuation.resume(Result.Success(true))
                        }.addOnFailureListener { error ->
                            continuation.resume(Result.Error(error))
                        }
                    }

                } catch (error: Exception) {
                    continuation.resumeWithException(error)
                }
            }
        }
    }

    override suspend fun deletePost(postModel: PostModel): Result<Boolean> {
        return withContext(Dispatchers.IO) {
            suspendCoroutine<Result<Boolean>> { continuation ->
                try {

                    postModel.id?.let {
                        firestoreManager.deletePost(postModel).addOnSuccessListener {
                            postsMutableLiveData.value?.remove(postModel)
                            postsMutableLiveData.postValue(postsMutableLiveData.value)
                            continuation.resume(Result.Success(true))
                        }.addOnFailureListener { error ->
                            continuation.resume(Result.Error(error))
                        }
                    } ?: run {
                        continuation.resume(Result.Success(false))
                    }

                } catch (error: Exception) {
                    continuation.resumeWithException(error)
                }
            }
        }
    }
}