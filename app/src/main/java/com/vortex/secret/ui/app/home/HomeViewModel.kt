package com.vortex.secret.ui.app.home

import androidx.lifecycle.*
import com.vortex.secret.data.model.PostModel
import com.vortex.secret.data.repository.IPostRepository
import com.vortex.secret.util.BaseViewModel
import com.vortex.secret.util.Result

class HomeViewModel(private val repository: IPostRepository) : BaseViewModel(), LifecycleObserver {

    private val _updatePostMutableLiveDate = repository.postsMutableLiveData
    private val _errorMutableLiveData: MutableLiveData<Throwable> = MutableLiveData()
    private val _loadingMutableLiveData: MutableLiveData<Boolean> = MutableLiveData()

    val updatePostLiveDate: LiveData<MutableList<PostModel>>
        get() = _updatePostMutableLiveDate
    val errorLiveData: LiveData<Throwable>
        get() = _errorMutableLiveData
    val loadingLiveData: LiveData<Boolean>
        get() = _loadingMutableLiveData

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun updatePosts() {
        launchData {
            _loadingMutableLiveData.value = true
            val response = repository.updatePosts()
            when (response) {
                is Result.Error -> { _errorMutableLiveData.value = response.error }
            }
            _loadingMutableLiveData.value = false
        }
    }

    fun updatePostLike(postModel: PostModel) {
        launchData {
            val response = repository.updatePostLike(postModel)
            when (response) {
                is Result.Error -> { _errorMutableLiveData.value = response.error }
            }
        }
    }

    fun removePost(postModel: PostModel) {
        launchData {
            val response = repository.deletePost(postModel)
            when (response) {
                is Result.Error -> { _errorMutableLiveData.value = response.error }
            }
        }
    }
}
