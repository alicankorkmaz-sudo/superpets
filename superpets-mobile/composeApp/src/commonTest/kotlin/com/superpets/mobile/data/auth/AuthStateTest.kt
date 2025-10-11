package com.superpets.mobile.data.auth

import kotlin.test.*

class AuthStateTest {

    @Test
    fun testLoadingState() {
        val state = AuthState.Loading

        assertTrue(state is AuthState.Loading)
        assertFalse(state is AuthState.Authenticated)
        assertFalse(state is AuthState.Unauthenticated)
    }

    @Test
    fun testUnauthenticatedState() {
        val state = AuthState.Unauthenticated

        assertTrue(state is AuthState.Unauthenticated)
        assertFalse(state is AuthState.Authenticated)
        assertFalse(state is AuthState.Loading)
    }

    @Test
    fun testAuthenticatedState() {
        val email = "test@example.com"
        val state = AuthState.Authenticated(email)

        assertTrue(state is AuthState.Authenticated)
        assertFalse(state is AuthState.Unauthenticated)
        assertFalse(state is AuthState.Loading)
        assertEquals(email, state.email)
    }

    @Test
    fun testAuthenticatedStateEquality() {
        val state1 = AuthState.Authenticated("test@example.com")
        val state2 = AuthState.Authenticated("test@example.com")
        val state3 = AuthState.Authenticated("different@example.com")

        assertEquals(state1, state2)
        assertNotEquals(state1, state3)
    }

    @Test
    fun testStateTransitions() {
        var state: AuthState = AuthState.Loading

        // Transition to authenticated
        state = AuthState.Authenticated("user@example.com")
        assertTrue(state is AuthState.Authenticated)
        assertEquals("user@example.com", (state as AuthState.Authenticated).email)

        // Transition to unauthenticated
        state = AuthState.Unauthenticated
        assertTrue(state is AuthState.Unauthenticated)

        // Transition back to loading
        state = AuthState.Loading
        assertTrue(state is AuthState.Loading)
    }

    @Test
    fun testWhenExpression() {
        val states = listOf(
            AuthState.Loading,
            AuthState.Unauthenticated,
            AuthState.Authenticated("test@example.com")
        )

        states.forEach { state ->
            val result = when (state) {
                is AuthState.Loading -> "loading"
                is AuthState.Unauthenticated -> "unauthenticated"
                is AuthState.Authenticated -> "authenticated:${state.email}"
            }

            when (state) {
                is AuthState.Loading -> assertEquals("loading", result)
                is AuthState.Unauthenticated -> assertEquals("unauthenticated", result)
                is AuthState.Authenticated -> {
                    assertTrue(result.startsWith("authenticated:"))
                    assertEquals("authenticated:test@example.com", result)
                }
            }
        }
    }
}
