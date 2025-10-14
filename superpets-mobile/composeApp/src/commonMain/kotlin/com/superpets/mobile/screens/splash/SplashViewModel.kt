package com.superpets.mobile.screens.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.superpets.mobile.data.auth.AuthManager
import com.superpets.mobile.data.auth.AuthState
import com.superpets.mobile.data.settings.SettingsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class SplashViewModel(
    settingsRepository: SettingsRepository,
    authManager: AuthManager
) : ViewModel() {
    val isOnboardingCompleted: StateFlow<Boolean?> = settingsRepository.getOnboardingCompleted()
        .stateIn(viewModelScope, SharingStarted.Companion.WhileSubscribed(5000), null)

    val authState: StateFlow<AuthState> = authManager.authState

    /**
     * Combined state to determine navigation
     * Returns null while loading, NavigationTarget when ready
     *
     * Navigation flow:
     * 1. Wait for data to load
     * 2. Check onboarding status first:
     *    - Not onboarded -> Landing (onboarding)
     *    - Onboarded -> Check auth status:
     *      - Authenticated -> Main
     *      - Not authenticated -> Auth (login/signup)
     */
    val navigationTarget: StateFlow<NavigationTarget?> = combine(
        isOnboardingCompleted,
        authState
    ) { onboardingCompleted, auth ->
        when {
            // Wait for data to load
            onboardingCompleted == null || auth is AuthState.Loading -> null

            // First time user - show onboarding/landing
            !onboardingCompleted -> NavigationTarget.Landing

            // Onboarded users - check authentication
            auth is AuthState.Authenticated -> NavigationTarget.Main
            auth is AuthState.Unauthenticated -> NavigationTarget.Auth

            // Default fallback (should not reach here)
            else -> NavigationTarget.Auth
        }
    }.stateIn(viewModelScope, SharingStarted.Companion.WhileSubscribed(5000), null)
}

enum class NavigationTarget {
    Landing,
    Auth,
    Main
}