package fun.superpets.mobile.data.settings

import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.coroutines.FlowSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@OptIn(ExperimentalSettingsApi::class)
class SettingsRepository(private val settings: FlowSettings) {
    fun getThemeIsDark(): Flow<Boolean> {
        return settings.getBooleanFlow(IS_DARK_THEME_KEY, false)
    }

    suspend fun setThemeIsDark(isDark: Boolean) {
        settings.putBoolean(IS_DARK_THEME_KEY, isDark)
    }

    fun getOnboardingCompleted(): Flow<Boolean> {
        return settings.getBooleanFlow(ONBOARDING_COMPLETED_KEY, false)
    }

    suspend fun setOnboardingCompleted(completed: Boolean) {
        settings.putBoolean(ONBOARDING_COMPLETED_KEY, completed)
    }

    companion object {
        private const val IS_DARK_THEME_KEY = "is_dark_theme"
        private const val ONBOARDING_COMPLETED_KEY = "onboarding_completed"
    }
} 