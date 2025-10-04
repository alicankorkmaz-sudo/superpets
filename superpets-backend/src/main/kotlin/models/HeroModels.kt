package com.alicankorkmaz.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Hero(
    val id: String,
    val hero: String,
    val identity: String,
    @SerialName("scene_options")
    val sceneOptions: List<String>
)

@Serializable
data class HeroesData(
    val classics: List<Hero>,
    val uniques: List<Hero>
)

@Serializable
data class HeroesResponse(
    val classics: List<Hero>,
    val uniques: List<Hero>
)
