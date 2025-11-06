package com.superpets.mobile.di

import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.superpets.mobile.data.AppDatabase
import com.superpets.mobile.data.settings.SettingsRepository
import com.superpets.mobile.data.auth.AuthManager
import com.superpets.mobile.data.auth.AuthTokenProvider
import com.superpets.mobile.data.network.HttpClientFactory
import com.superpets.mobile.data.network.SuperpetsApiService
import com.superpets.mobile.data.repository.SuperpetsRepository
import com.superpets.mobile.core.dispatchers.DefaultDispatcherProvider
import com.superpets.mobile.core.dispatchers.DispatcherProvider
import com.superpets.mobile.core.image.ImageCompressor
import com.superpets.mobile.screens.auth.AuthViewModel
import com.superpets.mobile.screens.editor.EditorViewModel
import com.superpets.mobile.screens.landing.LandingViewModel
import com.superpets.mobile.screens.splash.SplashViewModel
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.Settings
import com.russhwolf.settings.coroutines.toFlowSettings
import io.ktor.client.HttpClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

/**
 * Database module
 * Currently empty database - reserved for future local caching (Phase 6)
 */
val databaseModule = module {
    single<AppDatabase> {
        get<RoomDatabase.Builder<AppDatabase>>()
            .setDriver(BundledSQLiteDriver())
            .setQueryCoroutineContext(Dispatchers.IO)
            .build()
    }
}

/**
 * Core data module
 * Settings, dispatchers, and other core services
 */
val dataModule = module {
    single<DispatcherProvider> { DefaultDispatcherProvider() }
    single { Settings() }
    single { (get<Settings>() as ObservableSettings).toFlowSettings(get<DispatcherProvider>().io) }
    single { SettingsRepository(get()) }
    single<ImageCompressor> { ImageCompressor() }
}

/**
 * Superpets-specific module
 * Authentication and API services
 */
val superpetsModule = module {
    // Authentication (inject Settings and platform-specific Ktor engine)
    single<AuthManager> { AuthManager(settings = get(), httpClientEngine = get()) }
    single<AuthTokenProvider> { get<AuthManager>() }

    // HTTP Client configured with auth
    single<HttpClient>(qualifier = org.koin.core.qualifier.named("superpets")) {
        HttpClientFactory.create(
            authTokenProvider = get<AuthTokenProvider>(),
            enableLogging = true
        )
    }

    // API Service
    single<SuperpetsApiService> {
        SuperpetsApiService(
            httpClient = get(qualifier = org.koin.core.qualifier.named("superpets"))
        )
    }

    // Repository Layer
    single<SuperpetsRepository> {
        SuperpetsRepository(apiService = get())
    }
}

/**
 * ViewModel module
 * All screen ViewModels
 */
val viewModelModule = module {
    // Splash screen (singleton since it's used at app start)
    single { SplashViewModel(get(), get()) }

    // Landing/Onboarding
    factory { LandingViewModel(get()) }

    // Auth ViewModels
    factory { AuthViewModel(get()) }

    // Feature ViewModels
    factory { com.superpets.mobile.screens.home.HomeViewModel(get(), get()) }
    factory { com.superpets.mobile.screens.history.HistoryViewModel(get()) }
    factory { com.superpets.mobile.screens.profile.ProfileViewModel(get(), get()) }
    factory { EditorViewModel(get(), get(), get(), get()) }
}

/**
 * Initialize Koin dependency injection
 */
fun initKoin(config: KoinApplication.() -> Unit = {}) {
    startKoin {
        config()
        modules(
            databaseModule,
            dataModule,
            superpetsModule,
            viewModelModule,
            platformModule,
        )
    }
}
