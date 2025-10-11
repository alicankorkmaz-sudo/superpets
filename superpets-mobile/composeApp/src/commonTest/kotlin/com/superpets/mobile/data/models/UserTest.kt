package com.superpets.mobile.data.models

import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import kotlin.test.Test
import kotlin.test.assertEquals

class UserTest {

    private val json = Json {
        prettyPrint = true
        isLenient = true
        ignoreUnknownKeys = true
    }

    @Test
    fun testUserSerialization() {
        val user = User(
            uid = "user123",
            email = "test@example.com",
            credits = 10,
            isAdmin = false,
            createdAt = "2025-10-10T00:00:00Z"
        )

        val jsonString = json.encodeToString(user)
        val decodedUser = json.decodeFromString<User>(jsonString)

        assertEquals(user, decodedUser)
    }

    @Test
    fun testUserDeserialization() {
        val jsonString = """
            {
                "uid": "user456",
                "email": "admin@example.com",
                "credits": 100,
                "isAdmin": true,
                "createdAt": "2025-10-10T12:00:00Z"
            }
        """.trimIndent()

        val user = json.decodeFromString<User>(jsonString)

        assertEquals("user456", user.uid)
        assertEquals("admin@example.com", user.email)
        assertEquals(100, user.credits)
        assertEquals(true, user.isAdmin)
        assertEquals("2025-10-10T12:00:00Z", user.createdAt)
    }

    @Test
    fun testUserDefaultValues() {
        val jsonString = """
            {
                "uid": "user789",
                "email": "user@example.com",
                "credits": 5,
                "createdAt": "2025-10-10T08:00:00Z"
            }
        """.trimIndent()

        val user = json.decodeFromString<User>(jsonString)

        assertEquals(false, user.isAdmin) // Default value
    }
}
