package com.vortex.secret.ui.app.comment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vortex.secret.data.model.PostCommentModel
import com.vortex.secret.data.model.PostModel
import com.vortex.secret.data.repository.IPostRepository
import com.vortex.secret.ui.base.BaseViewModel
import com.vortex.secret.util.Result

class PostCommentViewModel(private val repository: IPostRepository) : BaseViewModel() {

    private val _responsePostModelMutableLiveData: MutableLiveData<PostModel> = MutableLiveData()
    private val _errorMutableLiveData: MutableLiveData<Throwable> = MutableLiveData()
    private val _loadingMutableLiveData: MutableLiveData<Boolean> = MutableLiveData()

    val responsePostModelLiveData: LiveData<PostModel>
        get() = _responsePostModelMutableLiveData
    val errorLiveData: LiveData<Throwable>
        get() = _errorMutableLiveData
    val loadingLiveData: LiveData<Boolean>
        get() = _loadingMutableLiveData

    fun getPostById(postId: String) {
        launchData {
            _loadingMutableLiveData.value = true
            val response = repository.getPostById(postId)
            when(response) {
                is Result.Success -> { _responsePostModelMutableLiveData.value = response.data }
                is Result.Error -> { _errorMutableLiveData.value = response.error }
            }
            _loadingMutableLiveData.value = false
        }
    }

    fun updatePostComment(postModel: PostModel, postCommentModel: PostCommentModel) {
        launchData {
            _loadingMutableLiveData.value = true
            val response = repository.updatePostComment(postModel, postCommentModel)
            when (response) {
                is Result.Success -> { _responsePostModelMutableLiveData.value = response.data }
                is Result.Error -> { _errorMutableLiveData.value = response.error }
            }
            _loadingMutableLiveData.value = false
        }
    }
}
