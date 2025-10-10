package fun.superpets.mobile.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fun.superpets.mobile.BuildKonfig
import fun.superpets.mobile.data.settings.SettingsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class ProfileUiState(
    val isDarkTheme: Boolean = false,
    val versionName: String = ""
)

class ProfileViewModel(
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    val uiState: StateFlow<ProfileUiState> =
        settingsRepository.getThemeIsDark()
            .map { isDarkTheme ->
                ProfileUiState(
                    isDarkTheme = isDarkTheme,
                    versionName = BuildKonfig.VERSION_NAME
                )
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = ProfileUiState()
            )

    fun setTheme(isDark: Boolean) {
        viewModelScope.launch {
            settingsRepository.setThemeIsDark(isDark)
        }
    }

    fun logout() {
        viewModelScope.launch {
            settingsRepository.setOnboardingCompleted(false)
        }
    }
} 