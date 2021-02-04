package com.rachidbs.todo.userInfo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rachidbs.todo.network.UserInfo
import com.rachidbs.todo.network.UserInfoRepository
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi
import okhttp3.MultipartBody

@ExperimentalSerializationApi
class UserInfoViewModel : ViewModel() {
    private val repository = UserInfoRepository()
    private val _user = MutableLiveData<UserInfo>()
    val user: LiveData<UserInfo> = _user

    fun loadInfo() {
        viewModelScope.launch {
            _user.value = repository.loadInfo()
        }
    }

    fun updateAvatar(avatar: MultipartBody.Part) {
        viewModelScope.launch {
            val userInfo: UserInfo? = repository.updateAvatar(avatar);
            if (userInfo != null)
                _user.value = userInfo!!;
        }
    }

    fun updateInfo(info: UserInfo) {
        viewModelScope.launch {
            val userInfo: UserInfo? = repository.updateInfo(info);
            if (userInfo != null)
                _user.value = userInfo!!;
        }
    }

}