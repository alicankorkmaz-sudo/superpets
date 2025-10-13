package com.superpets.mobile.data.network

import com.superpets.mobile.data.auth.AuthTokenProvider
import kotlin.test.*

class HttpClientFactoryTest {

    private class FakeAuthTokenProvider(private val token: String?) : AuthTokenProvider {
        override suspend fun getToken(): String? = token
        override suspend fun isAuthenticated(): Boolean = token != null
    }

    @Test
    fun testCreateClientWithoutAuth() {
        val client = HttpClientFactory.create(
            authTokenProvider = null,
            enableLogging = false
        )

        assertNotNull(client)
        client.close()
    }

    @Test
    fun testCreateClientWithAuth() {
        val authProvider = FakeAuthTokenProvider("test-token-123")
        val client = HttpClientFactory.create(
            authTokenProvider = authProvider,
            enableLogging = false
        )

        assertNotNull(client)
        client.close()
    }

    @Test
    fun testCreateClientWithLogging() {
        val client = HttpClientFactory.create(
            authTokenProvider = null,
            enableLogging = true
        )

        assertNotNull(client)
        // Client should be created successfully with logging enabled
        client.close()
    }

    @Test
    fun testCreateClientWithoutLogging() {
        val client = HttpClientFactory.create(
            authTokenProvider = null,
            enableLogging = false
        )

        assertNotNull(client)
        // Client should be created successfully without logging
        client.close()
    }

    @Test
    fun testBaseUrlConfiguration() {
        assertEquals(
            "https://api.superpets.fun",
            HttpClientFactory.BASE_URL
        )
    }

    @Test
    fun testClientCanBeReused() {
        val authProvider = FakeAuthTokenProvider("test-token")
        val client1 = HttpClientFactory.create(authProvider, false)
        val client2 = HttpClientFactory.create(authProvider, false)

        assertNotNull(client1)
        assertNotNull(client2)

        // Each call creates a new instance
        assertNotSame(client1, client2)

        client1.close()
        client2.close()
    }
}
