package fun.superpets.mobile

import android.app.Application
import fun.superpets.mobile.startup.initialize
import org.koin.android.ext.koin.androidContext

class Application : Application() {
    override fun onCreate() {
        super.onCreate()
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
