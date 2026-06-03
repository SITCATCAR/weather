package com.example.funnyweather

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

class FunnyWeatherApplication: Application() {
    companion object{
        const val TOKEN = "6AAGr6Kr3yHPdxgU"
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context=applicationContext
    }
}