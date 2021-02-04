package com.rachidbs.todo.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserInfo(
    @SerialName("email")
    val email: String,
    @SerialName("firstname")
    val firstName: String,
    @SerialName("lastname")
    val lastName: String,
    @SerialName("avatar")
    val avatar: String = "https://play-lh.googleusercontent.com/uX9l3XFeyyAz7on2xTyZVgMsxsYbdHUGeCV_SC7bMHjUZqkjNQxFdp8MlX_X6izOa9kR=s360"
)