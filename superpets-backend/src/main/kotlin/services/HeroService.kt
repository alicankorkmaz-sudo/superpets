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
    return """
        Create a superhero-style portrait of the SAME pet from the input photo.

        CRITICAL IDENTITY LOCK (must match the input exactly):
        - Keep the pet’s face shape, eyes, nose, mouth, ear shape/position, fur length/texture, markings, and body proportions.
        - Preserve the exact breed look, color pattern, and fluffiness.
        - Keep the pet as the same species (do not turn into a human/character/plush/toy).

        SUPERHERO COSTUME RULES:
        - Add a ${hero}-inspired suit as CLOTHING OVER the pet (fabric suit, chest emblem, gloves/boots-like paw covers).
        - Do NOT cover or replace the pet’s face. No full mask, no helmet, no human-like face, no big cartoon eyes.
        - Ears must remain visible and uncovered; whiskers and muzzle must remain visible.
        - If a mask is iconic for ${hero}, use a partial/open-face version: around the head/neck only, leaving eyes, nose, and mouth fully visible.

        SCENE + STYLE:
        - Place the pet ${scene}. Keep natural pet anatomy and realistic fur detail.
        - Match lighting and shadows to the environment; keep the pet’s fur readable (no plastic/smooth skin).
        - Camera: close/medium shot, pet centered, face clearly visible, sharp focus on facial features.

        NEGATIVE CONSTRAINTS (avoid):
        - Do not change facial features, fur pattern, ear shape, eye shape, or proportions.
        - No extreme stylization, no chibi/cartoon, no heavy CGI that removes fur detail.
        - No distortion, no extra limbs, no oversized head, no hidden muzzle, no identity loss.
    """.trimIndent()
}