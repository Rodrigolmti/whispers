package com.vortex.secret.ui.auth.sign_up

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.AuthResult
import com.vortex.secret.data.repository.IAuthRepository
import com.vortex.secret.util.Result

import com.vortex.secret.util.BaseViewModel

class SignUpViewModel(private val repository: IAuthRepository) : BaseViewModel() {

    private val _loadingMutableLiveData = MutableLiveData<Boolean>()
    private val _successSignUpnMutableLiveDate = MutableLiveData<AuthResult>()
    private val _errorSignUpMutableLiveDate = MutableLiveData<String>()

    val loadingLiveData: LiveData<Boolean>
        get() = _loadingMutableLiveData
    val successSignUpMutableLiveDate: LiveData<AuthResult>
        get() = _successSignUpnMutableLiveDate
    val errorSignUpMutableLiveDate : LiveData<String>
        get() = _errorSignUpMutableLiveDate

    fun signUpUser(email: String, password: String) {
        launchData {

            _loadingMutableLiveData.value = true
            val response = repository.signUpUserWithEmail(email, password)
            when (response) {
                is Result.Success -> {
                    _successSignUpnMutableLiveDate.value = response.data
                }
                is Result.Error -> {
                    _errorSignUpMutableLiveDate.value = response.error.message
                }
            }
            _loadingMutableLiveData.value = false
        }
    }
}
