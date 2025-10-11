package com.superpets.mobile.data.models

import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import kotlin.test.Test
import kotlin.test.assertEquals

class ApiModelsTest {

    private val json = Json {
        prettyPrint = true
        isLenient = true
        ignoreUnknownKeys = true
    }

    @Test
    fun testEditImageRequestSerialization() {
        val request = EditImageRequest(
            inputImages = listOf("https://example.com/image1.jpg"),
            heroId = "superman",
            numImages = 3
        )

        val jsonString = json.encodeToString(request)
        val decoded = json.decodeFromString<EditImageRequest>(jsonString)

        assertEquals(request, decoded)
    }

    @Test
    fun testEditImageResponseDeserialization() {
        val jsonString = """
            {
                "outputs": [
                    "https://example.com/output1.jpg",
                    "https://example.com/output2.jpg"
                ],
                "prompt": "Transform the pet into Superman..."
            }
        """.trimIndent()

        val response = json.decodeFromString<EditImageResponse>(jsonString)

        assertEquals(2, response.outputs.size)
        assertEquals("https://example.com/output1.jpg", response.outputs[0])
        assertEquals("Transform the pet into Superman...", response.prompt)
    }

    @Test
    fun testApiErrorDeserialization() {
        val jsonString = """
            {
                "error": "INSUFFICIENT_CREDITS",
                "message": "You don't have enough credits"
            }
        """.trimIndent()

        val error = json.decodeFromString<ApiError>(jsonString)

        assertEquals("INSUFFICIENT_CREDITS", error.error)
        assertEquals("You don't have enough credits", error.message)
    }

    @Test
    fun testCheckoutSessionRequestSerialization() {
        val request = CheckoutSessionRequest(
            priceId = "price_123",
            successUrl = "https://app.com/success",
            cancelUrl = "https://app.com/cancel"
        )

        val jsonString = json.encodeToString(request)
        val decoded = json.decodeFromString<CheckoutSessionRequest>(jsonString)

        assertEquals(request, decoded)
    }

    @Test
    fun testCheckoutSessionResponseDeserialization() {
        val jsonString = """
            {
                "sessionId": "cs_test_123",
                "url": "https://checkout.stripe.com/pay/cs_test_123"
            }
        """.trimIndent()

        val response = json.decodeFromString<CheckoutSessionResponse>(jsonString)

        assertEquals("cs_test_123", response.sessionId)
        assertEquals("https://checkout.stripe.com/pay/cs_test_123", response.url)
    }
}
