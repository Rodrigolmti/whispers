package com.vortex.secret.data.repository

import androidx.lifecycle.MutableLiveData
import com.vortex.secret.data.UserSession
import com.vortex.secret.data.mappers.mapFirebaseDocumentToPostModel
import com.vortex.secret.data.model.PostLike
import com.vortex.secret.data.model.PostModel
import com.vortex.secret.data.remote.IFirestoreManager
import com.vortex.secret.util.exceptions.EmptyDataError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface IPostRepository {

    val postsMutableLiveData: MutableLiveData<MutableList<PostModel>>
    val responseErrorMutableLiveData: MutableLiveData<Throwable>

    suspend fun updatePosts()

    suspend fun addPost(postModel: PostModel)

    suspend fun updatePostLike(postModel: PostModel)

    suspend fun deletePost(postModel: PostModel)
}

class PostRepository(
    private val firestoreManager: IFirestoreManager
) : IPostRepository {

    override val postsMutableLiveData = MutableLiveData<MutableList<PostModel>>()
    override val responseErrorMutableLiveData = MutableLiveData<Throwable>()

    init {
        postsMutableLiveData.value = mutableListOf()
    }

    override suspend fun updatePosts() {
        withContext(Dispatchers.IO) {
            try {
                firestoreManager.getPostsByDate().addOnSuccessListener { result ->
                    val posts = mapFirebaseDocumentToPostModel(result)
                    posts.takeIf { it.isEmpty() }?.let {
                        responseErrorMutableLiveData.value = EmptyDataError()
                    } ?: run {
                        postsMutableLiveData.value = posts
                    }
                }.addOnFailureListener { error ->
                    responseErrorMutableLiveData.value = error
                }
            } catch (error: Exception) {
                responseErrorMutableLiveData.value = error
            }
        }
    }

    override suspend fun addPost(postModel: PostModel) {
        withContext(Dispatchers.IO) {
            try {
                postModel.authorId = UserSession.userId
                firestoreManager.addPost(postModel)
                    .addOnSuccessListener {
                        postModel.id = it.id
                        postsMutableLiveData.value?.add(0, postModel)
                        postsMutableLiveData.postValue(postsMutableLiveData.value)
                    }
                    .addOnFailureListener { error ->
                        responseErrorMutableLiveData.value = error
                    }
            } catch (error: Exception) {
                responseErrorMutableLiveData.value = error
            }
        }
    }

    override suspend fun updatePostLike(postModel: PostModel) {
        withContext(Dispatchers.IO) {
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
                    }.addOnFailureListener { error ->
                        responseErrorMutableLiveData.value = error
                    }
                }
            } catch (error: Exception) {
                responseErrorMutableLiveData.value = error
            }
        }
    }

    override suspend fun deletePost(postModel: PostModel) {
        withContext(Dispatchers.IO) {
            try {
                postModel.id?.let {
                    firestoreManager.deletePost(postModel).addOnSuccessListener {
                        postsMutableLiveData.value?.remove(postModel)
                        postsMutableLiveData.postValue(postsMutableLiveData.value)
                    }.addOnFailureListener { error ->
                        responseErrorMutableLiveData.value = error
                    }
                }
            } catch (error: Exception) {
                responseErrorMutableLiveData.value = error
            }
        }
    }
}