package fun.superpets.mobile.screens.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fun.superpets.mobile.data.settings.SettingsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class SplashViewModel(
    settingsRepository: SettingsRepository,
) : ViewModel() {
    val isOnboardingCompleted: StateFlow<Boolean?> = settingsRepository.getOnboardingCompleted()
        .stateIn(viewModelScope, SharingStarted.Companion.WhileSubscribed(5000), null)
}