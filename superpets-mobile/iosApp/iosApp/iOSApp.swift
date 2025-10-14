import SwiftUI
import ComposeApp

@main
struct iOSApp: App {
    init() {
        IOSAppInitializerKt.initializeApp()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
