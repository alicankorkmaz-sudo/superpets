package com.alicankorkmaz.services

import com.alicankorkmaz.models.Hero
import com.alicankorkmaz.models.HeroesData
import io.ktor.server.application.*
import kotlinx.serialization.json.Json
import java.io.File

class HeroService(private val app: Application) {
    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    private val heroesData: HeroesData by lazy {
        loadHeroesFromFile()
    }

    private fun loadHeroesFromFile(): HeroesData {
        val file = File("heroes.json")
        if (!file.exists()) {
            app.log.error("heroes.json not found in project root")
            throw IllegalStateException("heroes.json not found")
        }

        val jsonString = file.readText()
        return json.decodeFromString<HeroesData>(jsonString)
    }

    fun getAllHeroes(): HeroesData {
        return heroesData
    }

    fun getHeroById(heroId: String): com.alicankorkmaz.models.Hero? {
        val allHeroes = heroesData.classics + heroesData.uniques
        return allHeroes.find { it.id == heroId }
    }
}

fun Hero.buildPrompt(): String {
    val scene = sceneOptions.random()
    return "Transform the pet into ${hero}. Keep the pet’s identity like face, fur, and body proportions etc exactly the same. Add ${identity}. Place them ${scene}. Avoid distortions or altering the pet’s natural features."
}