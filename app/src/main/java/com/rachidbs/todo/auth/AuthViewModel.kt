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
    private val _response = MutableLiveData<LoginResponse>()
    val response: LiveData<LoginResponse> = _response

    fun login(form: LoginForm) {
        viewModelScope.launch {
            val loginResponse: LoginResponse? = repository.login(form)
            if (loginResponse != null)
                _response.value = loginResponse
        }
    }
}
