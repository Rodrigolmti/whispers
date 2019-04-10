package com.vortex.secret.ui.app

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vortex.secret.data.model.PostModel
import com.vortex.secret.data.repository.IPostRepository
import com.vortex.secret.util.BaseViewModel

class AppViewModel(private val repository: IPostRepository) : BaseViewModel() {

    //TODO: implement this error, who is posting values here?
    private val _likeResponseErrorMutableLiveData = MutableLiveData<String>()

    val likeResponseErrorLiveData: LiveData<String>
        get() = _likeResponseErrorMutableLiveData

    fun addNewPost(postModel: PostModel) {
        launchData {
            repository.addPost(postModel)
        }
    }
}