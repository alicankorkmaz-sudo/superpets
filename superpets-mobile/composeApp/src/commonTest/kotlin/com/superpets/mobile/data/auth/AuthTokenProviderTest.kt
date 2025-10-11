package com.superpets.mobile.data.auth

import kotlinx.coroutines.test.runTest
import kotlin.test.*

class AuthTokenProviderTest {

    private class TestAuthTokenProvider(
        private val token: String?,
        private val authenticated: Boolean
    ) : AuthTokenProvider {
        override suspend fun getToken(): String? = token
        override suspend fun isAuthenticated(): Boolean = authenticated
    }

    @Test
    fun testAuthenticatedProvider() = runTest {
        val provider = TestAuthTokenProvider(
            token = "test-jwt-token-123",
            authenticated = true
        )

        assertTrue(provider.isAuthenticated())
        assertEquals("test-jwt-token-123", provider.getToken())
    }

    @Test
    fun testUnauthenticatedProvider() = runTest {
        val provider = TestAuthTokenProvider(
            token = null,
            authenticated = false
        )

        assertFalse(provider.isAuthenticated())
        assertNull(provider.getToken())
    }

    @Test
    fun testProviderWithExpiredToken() = runTest {
        // Simulate expired token scenario - token exists but not authenticated
        val provider = TestAuthTokenProvider(
            token = "expired-token",
            authenticated = false
        )

        assertFalse(provider.isAuthenticated())
        assertNotNull(provider.getToken()) // Token exists but invalid
    }
}
