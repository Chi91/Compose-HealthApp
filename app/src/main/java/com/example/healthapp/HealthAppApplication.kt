package com.example.healthapp

import android.app.Application
import android.util.Log
import com.example.healthapp.data.AppContainer
import com.example.healthapp.data.DataContainer

class HealthAppApplication: Application() {

    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = DataContainer(this)
        Log.d("HealthAppApplication", "AppContainer initialized")
    }
}