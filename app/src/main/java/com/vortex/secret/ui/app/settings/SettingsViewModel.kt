package com.vortex.secret.ui.app.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vortex.secret.data.repository.IAuthRepository
import com.vortex.secret.util.BaseViewModel
import com.vortex.secret.util.Result

class SettingsViewModel(private val repository: IAuthRepository) : BaseViewModel() {

    private val _responseLogoutMutableLiveData: MutableLiveData<Boolean> = MutableLiveData()
    private val _errorMutableLiveData: MutableLiveData<Throwable> = MutableLiveData()
    private val _loadingMutableLiveData: MutableLiveData<Boolean> = MutableLiveData()

    val responseLogoutLiveData: LiveData<Boolean>
        get() = _responseLogoutMutableLiveData
    val errorLiveData: LiveData<Throwable>
        get() = _errorMutableLiveData
    val loadingLiveData: LiveData<Boolean>
        get() = _loadingMutableLiveData

    fun logoutUser() {
        launchData {
            _loadingMutableLiveData.value = true
            val response = repository.logoutUser()
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
}
