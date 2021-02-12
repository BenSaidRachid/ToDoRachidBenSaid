package com.rachidbs.todo.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rachidbs.todo.network.UserInfoRepository
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi

@ExperimentalSerializationApi
class AuthViewModel : ViewModel() {
    private val repository = UserInfoRepository()
    private val _response = MutableLiveData<AuthResponse>()
    val response: LiveData<AuthResponse> = _response

    fun login(form: LoginForm) {
        viewModelScope.launch {
            val authResponse: AuthResponse? = repository.login(form)
            if (authResponse != null)
                _response.value = authResponse!!
        }
    }


    fun signUp(form: SignUpForm) {
        viewModelScope.launch {
            val authResponse: AuthResponse? = repository.signUp(form)
            if (authResponse != null)
                _response.value = authResponse!!
        }
    }
}
