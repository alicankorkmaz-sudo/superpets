package com.superpets.mobile

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.superpets.mobile.data.settings.SettingsRepository
import com.superpets.mobile.navigation.RootNavigationGraph
import com.superpets.mobile.screens.splash.SplashViewModel
import org.koin.compose.koinInject

@Composable
fun App() {
    val settingsRepository: SettingsRepository = koinInject()
    val isDarkTheme by settingsRepository.getThemeIsDark().collectAsState(isSystemInDarkTheme())
    val splashViewModel: SplashViewModel = koinInject()

    MaterialTheme(
        colorScheme = if (isDarkTheme) darkColorScheme() else lightColorScheme()
    ) {
        Surface(
            modifier = Modifier.systemBarsPadding()
        ) {
            RootNavigationGraph(navController = rememberNavController(), splashViewModel = splashViewModel)
        }
    }
} 