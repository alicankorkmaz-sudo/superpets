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
                        handleDeepLink(url: url)
                    }
                }
        }
    }

    private func handleDeepLink(url: URL) {
        // Use Supabase's built-in handleDeeplinks method
        // This handles both implicit and PKCE flow
        let supabaseClient = DIHelperKt.getSupabaseClient()
        let authManager = DIHelperKt.getAuthManager()

        // Call handleDeeplinks with NSURL
        // The onSessionSuccess callback is executed when session is imported
        supabaseClient.handleDeeplinks(url: url as NSURL) { session in
            let email = session.user?.email ?? ""
            print("Session imported successfully from deep link: \(email)")
            authManager.onDeepLinkSuccess(email: email)
        }
    }
}
