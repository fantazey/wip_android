package com.example.wipmobile

import android.app.Application
import com.example.wipmobile.di.AppComponent
import com.example.wipmobile.di.DaggerAppComponent

open class WipApplication: Application() {
    val appComponent: AppComponent by lazy {
        DaggerAppComponent.factory().create(applicationContext)
    }
}