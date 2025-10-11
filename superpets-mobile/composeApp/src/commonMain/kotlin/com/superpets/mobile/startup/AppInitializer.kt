package com.superpets.mobile.startup

import com.superpets.mobile.di.initKoin
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import org.koin.core.KoinApplication

fun initialize(config: KoinApplication.() -> Unit = {}) {
    Napier.base(DebugAntilog())
    Napier.i("Napier Initialized")
    initKoin(config)
} 