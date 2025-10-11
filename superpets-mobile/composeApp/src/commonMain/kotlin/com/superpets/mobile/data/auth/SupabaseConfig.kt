package com.superpets.mobile.data.auth

/**
 * Supabase configuration constants
 *
 * These credentials are shared with the web frontend and backend.
 * The anon key is safe to use in client apps as it's public.
 */
object SupabaseConfig {
    /**
     * Supabase project URL
     * Project ref: zrivjktyzllaevduydai
     */
    const val SUPABASE_URL = "https://zrivjktyzllaevduydai.supabase.co"

    /**
     * Supabase anonymous/public key (safe to use in client apps)
     * This is the "anon" key from Supabase project settings
     */
    const val SUPABASE_ANON_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InpyaXZqa3R5emxsYWV2ZHV5ZGFpIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTk1NTI0NTEsImV4cCI6MjA3NTEyODQ1MX0.dXFAjDoomItP-QwlGUFIgM3Pr8HRdx43SO3uJdaZK_Q"
}
