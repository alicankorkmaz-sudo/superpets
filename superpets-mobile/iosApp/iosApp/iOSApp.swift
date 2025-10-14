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
                    // Handle deep link for email confirmation
                    if url.scheme == "superpets" && url.host == "auth" {
                        print("Deep link received: \(url.absoluteString)")
                        // Pass the URL to the AuthManager via Kotlin/Native
                        handleDeepLink(url: url.absoluteString)
                    }
                }
        }
    }

    private func handleDeepLink(url: String) {
        // Get AuthManager from Koin and handle the deep link
        // This will be handled on the Kotlin side through the AuthManager
        Task {
            do {
                let authManager = DIHelperKt.getAuthManager()
                try await authManager.handleDeepLink(url: url)
            } catch {
                print("Error handling deep link: \(error)")
            }
        }
    }
}
