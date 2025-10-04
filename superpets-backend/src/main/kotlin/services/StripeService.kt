package com.alicankorkmaz.services

import com.stripe.Stripe
import com.stripe.model.checkout.Session
import com.stripe.param.checkout.SessionCreateParams
import io.ktor.server.application.*

class StripeService(private val application: Application) {

    init {
        // Initialize Stripe with your secret key
        Stripe.apiKey = application.environment.config.propertyOrNull("stripe.secretKey")?.getString()
            ?: System.getenv("STRIPE_SECRET_KEY")
            ?: throw IllegalStateException("Stripe secret key not configured")
    }

    // Pricing tiers mapping
    private val pricingTiers = mapOf(
        10 to 499L,   // 10 credits = $4.99
        25 to 1199L,  // 25 credits = $11.99
        50 to 2149L,  // 50 credits = $21.49
        100 to 3999L  // 100 credits = $39.99
    )

    fun createCheckoutSession(
        credits: Int,
        userId: String,
        userEmail: String,
        successUrl: String,
        cancelUrl: String
    ): Session {
        val priceInCents = pricingTiers[credits]
            ?: throw IllegalArgumentException("Invalid credit amount: $credits")

        val params = SessionCreateParams.builder()
            .setMode(SessionCreateParams.Mode.PAYMENT)
            .setSuccessUrl(successUrl)
            .setCancelUrl(cancelUrl)
            .setClientReferenceId(userId)
            .setCustomerEmail(userEmail)
            .addLineItem(
                SessionCreateParams.LineItem.builder()
                    .setPriceData(
                        SessionCreateParams.LineItem.PriceData.builder()
                            .setCurrency("usd")
                            .setUnitAmount(priceInCents)
                            .setProductData(
                                SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                    .setName("Superpets Credits")
                                    .setDescription("$credits credits for AI-powered pet transformations")
                                    .build()
                            )
                            .build()
                    )
                    .setQuantity(1L)
                    .build()
            )
            .putMetadata("credits", credits.toString())
            .putMetadata("userId", userId)
            .build()

        return Session.create(params)
    }

    fun verifyWebhookSignature(payload: String, signature: String): Boolean {
        val webhookSecret = application.environment.config.propertyOrNull("stripe.webhookSecret")?.getString()
            ?: System.getenv("STRIPE_WEBHOOK_SECRET")
            ?: return false

        return try {
            com.stripe.net.Webhook.constructEvent(payload, signature, webhookSecret)
            true
        } catch (e: Exception) {
            application.log.error("Webhook signature verification failed", e)
            false
        }
    }
}
