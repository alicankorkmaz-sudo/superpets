package com.superpets.mobile

import com.superpets.mobile.startup.initialize

/**
 * iOS-specific app initializer.
 * This function is exported to Swift/Objective-C and should be called
 * from the iOS app's entry point before any other Compose code.
 */
fun initializeApp() {
    initialize()
}
