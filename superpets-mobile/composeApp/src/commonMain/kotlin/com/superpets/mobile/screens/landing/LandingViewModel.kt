package com.superpets.mobile.screens.landing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.superpets.mobile.data.settings.SettingsRepository
import kotlinx.coroutines.launch

/**
 * ViewModel for landing/onboarding screen
 *
 * Handles marking onboarding as completed
 */
class LandingViewModel(
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    /**
     * Mark onboarding as completed
     * This will trigger navigation to Auth or Main based on authentication status
     */
    fun completeOnboarding() {
        viewModelScope.launch {
            settingsRepository.setOnboardingCompleted(true)
        }
    }
}
