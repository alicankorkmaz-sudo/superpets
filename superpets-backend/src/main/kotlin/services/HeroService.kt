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
        Create a high-end, professional superhero portrait of the SAME pet from the input photo.

        IDENTITY PRESERVATION (natural, not rigid):
        - The pet must be immediately recognizable as the same individual.
        - Preserve facial structure, eyes, nose, mouth, ear shape, fur color/pattern, and overall proportions.
        - Do NOT exaggerate features or turn the pet into a cartoon or toy.

        NATURAL INTEGRATION (NO CUT-PASTE):
        - The superhero suit must look custom-made for THIS pet’s anatomy.
        - Seamlessly blend fur and costume at the neck, shoulders, and limbs.
        - Fur should naturally overlap costume edges (no hard cut lines).
        - Body proportions must match the pet’s head size and posture.

        COSTUME RULES:
        - Add a ${hero}-inspired suit as realistic fabric clothing.
        - Open face only: eyes, nose, mouth, muzzle, and ears fully visible.
        - No helmet, no full mask, no human face replacement.
        - Costume should follow the pet’s natural pose and muscle structure.

        CINEMATIC REALISM:
        - Consistent lighting across face, body, and environment.
        - Matching shadows between pet and background.
        - Correct perspective and scale (no floating or pasted look).
        - Photorealistic fur detail with visible depth and softness.

        SCENE:
        - Place the pet ${scene}.
        - Use a professional, cinematic composition (not a sticker or collage).
        - Shallow depth of field, subject clearly grounded in the scene.

        STRICTLY AVOID:
        - Cut-out or pasted appearance
        - Toy-like or costume-doll proportions
        - Mismatched lighting or sharp edge transitions
        - Cartoon, chibi, or low-effort Photoshop look
    """.trimIndent()
}