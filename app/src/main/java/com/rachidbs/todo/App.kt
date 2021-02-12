package com.rachidbs.todo

import android.app.Application
import com.rachidbs.todo.network.Api

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        Api.INSTANCE = Api(this)
    }
}