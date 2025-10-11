package com.superpets.mobile.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.serialization.Serializable

@Serializable
sealed interface RootRoute {
    @Serializable
    data object Splash : RootRoute
    
    @Serializable
    data object Onboarding : RootRoute

    @Serializable
    data object MainRoute : RootRoute
}

@Serializable
sealed interface MainRoute {
    val title: String
    val icon: ImageVector

    @Serializable
    data object Home : MainRoute {
        override val title = "Home"
        override val icon = Icons.Default.Home
    }

    @Serializable
    data object Profile : MainRoute {
        override val title = "Profile"
        override val icon = Icons.Default.Person
    }
}

@Serializable
sealed interface HomeRoute {

    @Serializable
    data object Master : HomeRoute

    @Serializable
    data class Slave(val objectId: String) : HomeRoute
}