package com.vortex.secret.ui.auth.sign_in

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.AuthResult
import com.vortex.secret.data.repository.IAuthRepository
import com.vortex.secret.util.BaseViewModel
import com.vortex.secret.util.Result

class SignInViewModel(private val repository: IAuthRepository) : BaseViewModel() {

    private val _loadingMutableLiveData = MutableLiveData<Boolean>()
    private val _successSignInMutableLiveDate = MutableLiveData<AuthResult>()
    private val _errorSignInMutableLiveDate = MutableLiveData<String>()

    val loadingLiveData: LiveData<Boolean>
        get() = _loadingMutableLiveData
    val successSignInMutableLiveDate: LiveData<AuthResult>
        get() = _successSignInMutableLiveDate
    val errorSignInMutableLiveDate : LiveData<String>
        get() = _errorSignInMutableLiveDate

    fun signInUser(email: String, password: String) {
        launchData {
            _loadingMutableLiveData.value = true
            val response = repository.signInUserWithEmail(email, password)
            when (response) {
                is Result.Success -> {
                    _successSignInMutableLiveDate.value = response.data
                }
                is Result.Error -> {
                    _errorSignInMutableLiveDate.value = response.error.message
                }
            }
            _loadingMutableLiveData.value = false
        }
    }
}
