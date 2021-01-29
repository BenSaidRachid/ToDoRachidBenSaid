package com.rachidbs.todo.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit

object Api {
    private const val BASE_URL = "https://android-tasks-api.herokuapp.com/api/"
    private const val TOKEN =
        "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjo0NzYsImV4cCI6MTY0MzQ0MDM5Mn0.iDiAqDLSm5omXOeBOYRb5bNayzWbbq4WU1avpx3GJ1U"

    private val jsonSerializer = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    @ExperimentalSerializationApi
    private val converterFactory =
        jsonSerializer.asConverterFactory("application/json".toMediaType())

    private val okHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor { chain ->
                val newRequest = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer $TOKEN")
                    .build()
                chain.proceed(newRequest)
            }
            .build()
    }

    @ExperimentalSerializationApi
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(converterFactory)
        .build()

    @ExperimentalSerializationApi
    val userService: UserService by lazy {
        retrofit.create(UserService::class.java)
    }

    @ExperimentalSerializationApi
    val tasksWebService: TasksWebService by lazy {
        retrofit.create(TasksWebService::class.java)
    }
}