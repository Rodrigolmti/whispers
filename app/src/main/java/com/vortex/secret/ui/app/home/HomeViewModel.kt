package com.vortex.secret.ui.app.home

import androidx.lifecycle.*
import com.vortex.secret.data.model.PostModel
import com.vortex.secret.data.repository.IPostRepository
import com.vortex.secret.util.BaseViewModel

class HomeViewModel(private val repository: IPostRepository) : BaseViewModel(), LifecycleObserver {

    private val _updatePostMutableLiveDate = repository.postsMutableLiveData
    private val _errorMutableLiveData = repository.responseErrorMutableLiveData
    private val _loadingMutableLiveData = MutableLiveData<Boolean>()

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
            repository.updatePosts()
            _loadingMutableLiveData.value = false
        }
    }

    fun updatePostLike(postModel: PostModel) {
        launchData { repository.updatePostLike(postModel) }
    }

    fun removePost(postModel: PostModel) {
        launchData {
            repository.deletePost(postModel)
        }
    }
}
