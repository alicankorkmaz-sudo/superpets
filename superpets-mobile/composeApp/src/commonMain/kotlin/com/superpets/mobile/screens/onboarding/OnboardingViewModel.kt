package com.superpets.mobile.screens.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.superpets.mobile.data.settings.SettingsRepository
import kotlinx.coroutines.launch

class OnboardingViewModel(
    private val settingsRepository: SettingsRepository
) : ViewModel() {
    fun onOnboardingCompleted() {
        viewModelScope.launch {
            settingsRepository.setOnboardingCompleted(true)
        }
    }
} 