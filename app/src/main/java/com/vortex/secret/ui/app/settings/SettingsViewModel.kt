package com.vortex.secret.ui.app.settings

import androidx.lifecycle.*
import com.vortex.secret.data.repository.IAuthRepository
import com.vortex.secret.data.repository.IUserRepository
import com.vortex.secret.ui.base.BaseViewModel
import com.vortex.secret.util.Result

class SettingsViewModel(
    private val authRepository: IAuthRepository,
    private val userRepository: IUserRepository
) : BaseViewModel(), LifecycleObserver {

    private val _responseAnonymousModeMutableLiveData: MutableLiveData<Boolean> = MutableLiveData()
    private val _responseLogoutMutableLiveData: MutableLiveData<Boolean> = MutableLiveData()
    private val _errorMutableLiveData: MutableLiveData<Throwable> = MutableLiveData()
    private val _loadingMutableLiveData: MutableLiveData<Boolean> = MutableLiveData()

    val responseAnonymousModeLiveData: LiveData<Boolean>
        get() = _responseAnonymousModeMutableLiveData
    val responseLogoutLiveData: LiveData<Boolean>
        get() = _responseLogoutMutableLiveData
    val errorLiveData: LiveData<Throwable>
        get() = _errorMutableLiveData
    val loadingLiveData: LiveData<Boolean>
        get() = _loadingMutableLiveData

    fun logoutUser() {
        launchData {
            _loadingMutableLiveData.value = true
            val response = authRepository.logoutUser()
            when (response) {
                is Result.Success -> {
                    _responseLogoutMutableLiveData.value = response.data
                }
                is Result.Error -> {
                    _errorMutableLiveData.value = response.error
                }
            }
            _loadingMutableLiveData.value = false
        }
    }

    fun updateUserAnonymousMode(anonymous: Boolean) {
        launchData {
            val response = userRepository.updateUserAnonymousMode(anonymous)
            when (response) {
                is Result.Error -> {
                    _errorMutableLiveData.value = response.error
                }
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun getUserAnonymousMode() {
        launchData {
            val response = userRepository.getUserAnonymousMode()
            when (response) {
                is Result.Success -> {
                    _responseAnonymousModeMutableLiveData.value = response.data
                }
                is Result.Error -> {
                    _errorMutableLiveData.value = response.error
                }
            }
        }
    }
}
