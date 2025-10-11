package com.superpets.mobile.data.models

import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import kotlin.test.Test
import kotlin.test.assertEquals

class HeroTest {

    private val json = Json {
        prettyPrint = true
        isLenient = true
        ignoreUnknownKeys = true
    }

    @Test
    fun testHeroSerialization() {
        val hero = Hero(
            id = "superman",
            name = "Superman",
            category = "classics",
            identity = "a blue suit with a red cape and the iconic 'S' symbol on the chest",
            scenes = listOf("flying over Metropolis", "standing heroically on a rooftop")
        )

        val jsonString = json.encodeToString(hero)
        val decodedHero = json.decodeFromString<Hero>(jsonString)

        assertEquals(hero, decodedHero)
    }

    @Test
    fun testHeroesResponseDeserialization() {
        val jsonString = """
            {
                "heroes": [
                    {
                        "id": "batman",
                        "name": "Batman",
                        "category": "classics",
                        "identity": "a dark suit with bat ears and cape",
                        "scenes": ["patrolling Gotham", "on the Batmobile"]
                    },
                    {
                        "id": "wonderwoman",
                        "name": "Wonder Woman",
                        "category": "classics",
                        "identity": "an Amazonian warrior outfit with golden lasso",
                        "scenes": ["defending Paradise Island"]
                    }
                ]
            }
        """.trimIndent()

        val response = json.decodeFromString<HeroesResponse>(jsonString)

        assertEquals(2, response.heroes.size)
        assertEquals("batman", response.heroes[0].id)
        assertEquals("Wonder Woman", response.heroes[1].name)
    }

    @Test
    fun testHeroWithEmptyScenes() {
        val jsonString = """
            {
                "id": "custom",
                "name": "Custom Hero",
                "category": "uniques",
                "identity": "custom identity",
                "scenes": []
            }
        """.trimIndent()

        val hero = json.decodeFromString<Hero>(jsonString)

        assertEquals(emptyList(), hero.scenes)
    }
}
