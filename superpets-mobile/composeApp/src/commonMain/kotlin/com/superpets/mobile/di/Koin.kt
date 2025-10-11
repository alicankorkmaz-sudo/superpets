package com.superpets.mobile.di

import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.superpets.mobile.data.AppDatabase
import com.superpets.mobile.data.KtorMuseumApi
import com.superpets.mobile.data.MuseumApi
import com.superpets.mobile.data.MuseumRepository
import com.superpets.mobile.data.settings.SettingsRepository
import com.superpets.mobile.data.auth.AuthManager
import com.superpets.mobile.data.auth.AuthTokenProvider
import com.superpets.mobile.data.network.HttpClientFactory
import com.superpets.mobile.data.network.SuperpetsApiService
import com.superpets.mobile.core.dispatchers.DefaultDispatcherProvider
import com.superpets.mobile.core.dispatchers.DispatcherProvider
import com.superpets.mobile.screens.splash.SplashViewModel
import com.superpets.mobile.screens.detail.DetailViewModel
import com.superpets.mobile.screens.list.ListViewModel
import com.superpets.mobile.screens.onboarding.OnboardingViewModel
import com.superpets.mobile.screens.profile.ProfileViewModel
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.Settings
import com.russhwolf.settings.coroutines.toFlowSettings
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.serialization.json.Json
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val databaseModule = module {
    single<AppDatabase> {
        get<RoomDatabase.Builder<AppDatabase>>()
            .setDriver(BundledSQLiteDriver())
            .setQueryCoroutineContext(Dispatchers.IO)
            .build()
    }
    single { get<AppDatabase>().museumDao() }
}

val dataModule = module {
    single<DispatcherProvider> { DefaultDispatcherProvider() }
    single { Settings() }
    single { (get<Settings>() as ObservableSettings).toFlowSettings(get<DispatcherProvider>().io) }
    single { SettingsRepository(get()) }
    single {
        val json = Json { ignoreUnknownKeys = true }
        HttpClient {
            install(ContentNegotiation) {
                // TODO Fix API so it serves application/json
                json(json, contentType = ContentType.Any)
            }
        }
    }

    single<MuseumApi> { KtorMuseumApi(get()) }
    single {
        MuseumRepository(get(), get()).apply {
            initialize()
        }
    }
}

val superpetsModule = module {
    // Authentication
    single<AuthManager> { AuthManager() }
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
}

val viewModelModule = module {
    factory { ListViewModel(get(), get()) }
    factoryOf(::DetailViewModel)
    factoryOf(::ProfileViewModel)
    factoryOf(::OnboardingViewModel)
    singleOf(::SplashViewModel)
}

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