package com.rachidbs.todo.network

import android.content.Context
import androidx.preference.PreferenceManager
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.rachidbs.todo.utils.Constants
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit

class Api(private val context: Context) {
    companion object {
        private const val BASE_URL = "https://android-tasks-api.herokuapp.com/api/"
        lateinit var INSTANCE: Api
    }

    private val jsonSerializer = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    fun getToken(): String? {
        return PreferenceManager.getDefaultSharedPreferences(context)
            .getString(Constants.SHARED_PREF_TOKEN_KEY, "")
    }

    @ExperimentalSerializationApi
    private val converterFactory =
        jsonSerializer.asConverterFactory("application/json".toMediaType())

    private val okHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor { chain ->
                val newRequest = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer ${getToken()}")
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
    val USER_WEB_SERVICE: UserWebService by lazy {
        retrofit.create(UserWebService::class.java)
    }

    @ExperimentalSerializationApi
    val tasksWebService: TasksWebService by lazy {
        retrofit.create(TasksWebService::class.java)
    }
}