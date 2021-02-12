package com.rachidbs.todo.network
import com.rachidbs.todo.auth.LoginForm
import com.rachidbs.todo.auth.LoginResponse
import kotlinx.serialization.ExperimentalSerializationApi
import okhttp3.MultipartBody

@ExperimentalSerializationApi
class UserInfoRepository {
    private val webService = Api.INSTANCE.USER_WEB_SERVICE

    suspend fun loadInfo(): UserInfo? {
        val response = webService.getInfo()
        return if (response.isSuccessful) response.body()!! else null
    }

    suspend fun updateAvatar(avatar: MultipartBody.Part): UserInfo? {
        val response = webService.updateAvatar(avatar)
        return if (response.isSuccessful) response.body()!! else null
    }

    suspend fun updateInfo(info: UserInfo): UserInfo? {
        val response = webService.update(info)
        return if (response.isSuccessful) response.body()!! else null
    }

    suspend fun login(form: LoginForm): LoginResponse? {
        val response = webService.login(form)
        return if (response.isSuccessful) response.body()!! else null
    }
}