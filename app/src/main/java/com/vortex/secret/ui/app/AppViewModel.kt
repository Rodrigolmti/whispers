package com.vortex.secret.ui.app

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vortex.secret.data.model.PostModel
import com.vortex.secret.data.repository.IPostRepository
import com.vortex.secret.ui.base.BaseViewModel
import com.vortex.secret.util.Result

class AppViewModel(private val repository: IPostRepository) : BaseViewModel() {

    private val _likeResponseErrorMutableLiveData: MutableLiveData<Throwable> = MutableLiveData()
    private val _likeResponseMutableLiveData: MutableLiveData<Boolean> = MutableLiveData()

    val likeResponseErrorLiveData: LiveData<Throwable>
        get() = _likeResponseErrorMutableLiveData
    val likeResponseLiveData: LiveData<Boolean>
        get() = _likeResponseMutableLiveData

    fun addNewPost(postModel: PostModel) {
        launchData {
            val response = repository.addPost(postModel)
            when (response) {
                is Result.Success -> {
                    _likeResponseMutableLiveData.value = response.data
                }
                is Result.Error -> {
                    _likeResponseErrorMutableLiveData.value = response.error
                }
            }
        }
    }
}