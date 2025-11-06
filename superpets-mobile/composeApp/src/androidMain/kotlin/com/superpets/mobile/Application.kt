package com.superpets.mobile

import android.app.Application
import com.superpets.mobile.core.ContextProvider
import com.superpets.mobile.startup.initialize
import org.koin.android.ext.koin.androidContext

class Application : Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize ContextProvider for platform-specific utilities
        ContextProvider.init(this)
        initialize{
            androidContext(this@Application)
        }
    }

    companion object {
        private lateinit var instance: Application

        fun getInstance(): Application {
            return instance
        }
    }
}
