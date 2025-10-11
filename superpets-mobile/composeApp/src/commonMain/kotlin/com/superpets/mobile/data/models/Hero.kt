package com.superpets.mobile.data.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Hero(
    @SerialName("id") val id: String,
    @SerialName("name") val name: String,
    @SerialName("category") val category: String, // "classics" or "uniques"
    @SerialName("identity") val identity: String,
    @SerialName("scenes") val scenes: List<String>
)

@Serializable
data class HeroesResponse(
    @SerialName("heroes") val heroes: List<Hero>
)
