package com.superpets.mobile.data.network

import com.superpets.mobile.data.models.*
import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import kotlin.test.*

class SuperpetsApiServiceTest {

    private fun createMockClient(
        responseContent: String,
        statusCode: HttpStatusCode = HttpStatusCode.OK
    ): HttpClient {
        return HttpClient(MockEngine) {
            engine {
                addHandler { request ->
                    respond(
                        content = responseContent,
                        status = statusCode,
                        headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                    )
                }
            }
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                })
            }
            // Set default content type for requests (needed for POST with body)
            defaultRequest {
                contentType(ContentType.Application.Json)
            }
        }
    }

    @Test
    fun testGetHeroesSuccess() = runTest {
        val responseJson = """
            {
                "heroes": [
                    {
                        "id": "superman",
                        "name": "Superman",
                        "category": "classics",
                        "identity": "blue suit with red cape",
                        "scenes": ["flying over Metropolis"]
                    }
                ]
            }
        """.trimIndent()

        val client = createMockClient(responseJson)
        val apiService = SuperpetsApiService(client)

        val result = apiService.getHeroes()

        assertTrue(result.isSuccess)
        assertEquals(1, result.getOrNull()?.heroes?.size)
        assertEquals("superman", result.getOrNull()?.heroes?.first()?.id)
    }

    @Test
    fun testGetUserProfileSuccess() = runTest {
        val responseJson = """
            {
                "uid": "user123",
                "email": "test@example.com",
                "credits": 10,
                "isAdmin": false,
                "createdAt": "2025-10-10T00:00:00Z"
            }
        """.trimIndent()

        val client = createMockClient(responseJson)
        val apiService = SuperpetsApiService(client)

        val result = apiService.getUserProfile()

        assertTrue(result.isSuccess)
        assertEquals("user123", result.getOrNull()?.uid)
        assertEquals(10, result.getOrNull()?.credits)
    }

    @Test
    fun testGetUserCreditsSuccess() = runTest {
        val responseJson = """
            {
                "credits": 25
            }
        """.trimIndent()

        val client = createMockClient(responseJson)
        val apiService = SuperpetsApiService(client)

        val result = apiService.getUserCredits()

        assertTrue(result.isSuccess)
        assertEquals(25, result.getOrNull()?.credits)
    }

    @Test
    fun testEditImageSuccess() = runTest {
        val responseJson = """
            {
                "outputs": [
                    "https://example.com/output1.jpg",
                    "https://example.com/output2.jpg"
                ],
                "prompt": "Transform the pet into Superman..."
            }
        """.trimIndent()

        val client = createMockClient(responseJson)
        val apiService = SuperpetsApiService(client)

        val result = apiService.editImage(
            inputImages = listOf("https://example.com/input.jpg"),
            heroId = "superman",
            numImages = 2
        )

        assertTrue(result.isSuccess)
        assertEquals(2, result.getOrNull()?.outputs?.size)
        assertNotNull(result.getOrNull()?.prompt)
    }

    @Test
    fun testApiErrorUnauthorized() = runTest {
        val client = createMockClient(
            responseContent = """{"error": "Unauthorized"}""",
            statusCode = HttpStatusCode.Unauthorized
        )
        val apiService = SuperpetsApiService(client)

        val result = apiService.getUserProfile()

        // Verify the request failed
        assertTrue(result.isFailure)
        assertNotNull(result.exceptionOrNull())
    }

    @Test
    fun testApiErrorInsufficientCredits() = runTest {
        val client = createMockClient(
            responseContent = """{"error": "Insufficient credits"}""",
            statusCode = HttpStatusCode.PaymentRequired
        )
        val apiService = SuperpetsApiService(client)

        val result = apiService.editImage(
            inputImages = listOf("https://example.com/input.jpg"),
            heroId = "superman"
        )

        // Verify the request failed
        assertTrue(result.isFailure)
        assertNotNull(result.exceptionOrNull())
    }

    @Test
    fun testApiErrorRateLimited() = runTest {
        val client = createMockClient(
            responseContent = """{"error": "Rate limit exceeded"}""",
            statusCode = HttpStatusCode.TooManyRequests
        )
        val apiService = SuperpetsApiService(client)

        val result = apiService.getHeroes()

        // Verify the request failed
        assertTrue(result.isFailure)
        assertNotNull(result.exceptionOrNull())
    }

    @Test
    fun testCreateCheckoutSessionSuccess() = runTest {
        val responseJson = """
            {
                "sessionId": "cs_test_123",
                "url": "https://checkout.stripe.com/pay/cs_test_123"
            }
        """.trimIndent()

        val client = createMockClient(responseJson)
        val apiService = SuperpetsApiService(client)

        val result = apiService.createCheckoutSession(
            priceId = "price_123",
            successUrl = "https://app.com/success",
            cancelUrl = "https://app.com/cancel"
        )

        assertTrue(result.isSuccess)
        assertEquals("cs_test_123", result.getOrNull()?.sessionId)
        assertNotNull(result.getOrNull()?.url)
    }
}
