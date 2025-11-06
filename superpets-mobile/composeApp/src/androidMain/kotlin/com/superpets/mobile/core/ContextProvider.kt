package com.superpets.mobile.core

import android.content.Context

/**
 * Singleton to provide Android Context globally for platform-specific implementations.
 * Must be initialized in Application.onCreate().
 */
object ContextProvider {
    private var context: Context? = null

    fun init(context: Context) {
        this.context = context.applicationContext
    }

    fun get(): Context {
        return context ?: throw IllegalStateException("ContextProvider not initialized. Call init() in Application.onCreate()")
    }
}
