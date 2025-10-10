package fun.superpets.mobile.di

import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import fun.superpets.mobile.data.AppDatabase
import fun.superpets.mobile.data.KtorMuseumApi
import fun.superpets.mobile.data.MuseumApi
import fun.superpets.mobile.data.MuseumRepository
import fun.superpets.mobile.data.settings.SettingsRepository
import fun.superpets.mobile.core.dispatchers.DefaultDispatcherProvider
import fun.superpets.mobile.core.dispatchers.DispatcherProvider
import fun.superpets.mobile.screens.splash.SplashViewModel
import fun.superpets.mobile.screens.detail.DetailViewModel
import fun.superpets.mobile.screens.list.ListViewModel
import fun.superpets.mobile.screens.onboarding.OnboardingViewModel
import fun.superpets.mobile.screens.profile.ProfileViewModel
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
            viewModelModule,
            platformModule,
        )
    }
}