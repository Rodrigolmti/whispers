package com.vortex.secret.ui

import androidx.lifecycle.*
import com.vortex.secret.data.repository.IAuthRepository
import com.vortex.secret.util.BaseViewModel
import com.vortex.secret.util.Result

class SplashViewModel(private val repository: IAuthRepository) : BaseViewModel(), LifecycleObserver {

    private val _userSessionMutableLiveData = MutableLiveData<Boolean>()

    val userSessionLiveData: LiveData<Boolean>
        get() = _userSessionMutableLiveData

    init {
        _userSessionMutableLiveData.value = false
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun verifyUserSession() {
        launchData {
            val response = repository.verifyUserSession()
            when(response) {
                is Result.Success -> { _userSessionMutableLiveData.value = response.data }
            }
        }
    }
}