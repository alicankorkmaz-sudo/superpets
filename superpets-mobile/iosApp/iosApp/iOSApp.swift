import SwiftUI
import ComposeApp

@main
struct iOSApp: App {
    init() {
        AppInitializerKt.initialize()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
