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
                .onOpenURL { url in
                    handleDeepLink(url: url)
                }
        }
    }

    private func handleDeepLink(url: URL) {
        // Call Kotlin function to handle the deeplink
        // This mimics the Android handleDeeplinks behavior
        IosDeeplinkHelperKt.handleIOSDeepLink(url: url.absoluteString)
    }
}
