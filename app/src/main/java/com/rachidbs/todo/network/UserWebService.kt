package com.rachidbs.todo.network

import com.rachidbs.todo.auth.AuthResponse
import com.rachidbs.todo.auth.LoginForm
import com.rachidbs.todo.auth.SignUpForm
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface UserWebService {
    @GET("users/info")
    suspend fun getInfo(): Response<UserInfo>

    @Multipart
    @PATCH("users/update_avatar")
    suspend fun updateAvatar(@Part avatar: MultipartBody.Part): Response<UserInfo>

    @PATCH("users")
    suspend fun update(@Body user: UserInfo): Response<UserInfo>

    @POST("users/login")
    suspend fun login(@Body user: LoginForm): Response<AuthResponse>

    @POST("users/sign_up")
    suspend fun signUp(@Body user: SignUpForm): Response<AuthResponse>
}