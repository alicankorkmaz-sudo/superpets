package com.superpets.mobile.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.serialization.Serializable

/**
 * Root-level navigation routes for Superpets app
 */
@Serializable
sealed interface RootRoute {
    @Serializable
    data object Splash : RootRoute

    @Serializable
    data object Landing : RootRoute

    @Serializable
    data object Auth : RootRoute

    @Serializable
    data object Main : RootRoute
}

/**
 * Authentication flow routes
 */
@Serializable
sealed interface AuthRoute {
    @Serializable
    data object Login : AuthRoute

    @Serializable
    data object Signup : AuthRoute
}

/**
 * Main bottom navigation routes
 */
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
    data object Create : MainRoute {
        override val title = "Create"
        override val icon = Icons.Default.Add
    }

    @Serializable
    data object History : MainRoute {
        override val title = "History"
        override val icon = Icons.Default.List
    }

    @Serializable
    data object Profile : MainRoute {
        override val title = "Profile"
        override val icon = Icons.Default.Person
    }
}

/**
 * Feature routes (accessed from main screens)
 */
@Serializable
sealed interface FeatureRoute {
    @Serializable
    data object HeroSelection : FeatureRoute

    @Serializable
    data class ImageEditor(val heroId: String) : FeatureRoute

    @Serializable
    data object GenerationProgress : FeatureRoute

    @Serializable
    data object ResultGallery : FeatureRoute

    @Serializable
    data object Pricing : FeatureRoute

    @Serializable
    data class EditDetail(val editId: String) : FeatureRoute
}
