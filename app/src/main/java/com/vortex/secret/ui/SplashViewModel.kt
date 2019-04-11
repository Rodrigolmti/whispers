package com.vortex.secret.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vortex.secret.data.repository.IAuthRepository
import com.vortex.secret.util.Result
import com.vortex.secret.util.BaseViewModel

class SplashViewModel(private val repository: IAuthRepository) : BaseViewModel() {

    private val _userSessionMutableLiveData = MutableLiveData<Boolean>()

    val userSessionLiveData: LiveData<Boolean>
        get() = _userSessionMutableLiveData

    init {
        _userSessionMutableLiveData.value = false
    }

    fun onStart() {
        launchData {
            val response = repository.verifyUserSession()
            when(response) {
                is Result.Success -> { _userSessionMutableLiveData.value = response.data }
                is Result.Error -> { }
            }
        }
    }
}