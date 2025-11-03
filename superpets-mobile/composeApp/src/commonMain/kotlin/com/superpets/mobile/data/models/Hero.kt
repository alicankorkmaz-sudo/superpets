package com.superpets.mobile.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class Hero(
    @SerialName("id") val id: String,
    @SerialName("hero") val hero: String, // This is the "name" field from backend
    @SerialName("identity") val identity: String,
    @SerialName("scene_options") val sceneOptions: List<String>,
    @Transient val category: String = "" // "classics" or "uniques" - set by HeroesResponse
) {
    // Computed property for display name (matches what UI expects)
    val name: String get() = hero
    val scenes: List<String> get() = sceneOptions
}

@Serializable
data class HeroesResponse(
    @SerialName("classics") val classics: List<Hero>,
    @SerialName("uniques") val uniques: List<Hero>
) {
    // Computed property to combine all heroes for the UI
    val heroes: List<Hero> get() =
        classics.map { it.copy(category = "classics") } +
        uniques.map { it.copy(category = "uniques") }
}
