package com.vortex.secret.ui.auth.sign_in

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vortex.secret.data.repository.IAuthRepository
import com.vortex.secret.util.BaseViewModel
import com.vortex.secret.util.Result

class SignInViewModel(private val repository: IAuthRepository) : BaseViewModel() {

    private val _loadingMutableLiveData: MutableLiveData<Boolean> = MutableLiveData()
    private val _authResponseMutableLiveData: MutableLiveData<Boolean> = MutableLiveData()
    private val _errorSignInMutableLiveDate: MutableLiveData<Throwable> = MutableLiveData()

    val loadingLiveData: LiveData<Boolean>
        get() = _loadingMutableLiveData
    val authResponseLiveData: LiveData<Boolean>
        get() = _authResponseMutableLiveData
    val errorSignInMutableLiveDate: LiveData<Throwable>
        get() = _errorSignInMutableLiveDate

    fun signInUser(email: String, password: String) {
        launchData {
            _loadingMutableLiveData.value = true
            val response = repository.signInUserWithEmail(email, password)
            when(response) {
                is Result.Success -> { _authResponseMutableLiveData.value = response.data }
                is Result.Error -> { _errorSignInMutableLiveDate.value = response.error }
            }
            _loadingMutableLiveData.value = false
        }
    }
}
