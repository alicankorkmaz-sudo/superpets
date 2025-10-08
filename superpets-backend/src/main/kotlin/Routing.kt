package com.alicankorkmaz

import com.alicankorkmaz.models.*
import com.alicankorkmaz.services.SupabaseAuthService
import com.alicankorkmaz.services.SupabaseService
import com.alicankorkmaz.services.NanoBananaService
import com.alicankorkmaz.services.HeroService
import com.alicankorkmaz.services.StripeService
import com.alicankorkmaz.services.RateLimitingService
import com.alicankorkmaz.services.buildPrompt
import com.alicankorkmaz.utils.FileValidation
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.http.content.*
import io.ktor.utils.io.*
import io.ktor.utils.io.jvm.javaio.*
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

fun Application.configureRouting() {
    val nanoBananaService = NanoBananaService(this)
    val supabaseService = SupabaseService(this)
    val supabaseAuthService = SupabaseAuthService(this)
    val heroService = HeroService(this)
    val stripeService = StripeService(this)
    val rateLimitingService = RateLimitingService()

    routing {
        // Status page
        get("/") {
            // IP-based rate limiting: 60 requests per minute
            val ipAddress = call.request.local.remoteAddress
            val rateLimitResult = rateLimitingService.checkIpRateLimit(ipAddress, maxRequests = 60, windowSeconds = 60)

            if (!rateLimitResult.allowed) {
                call.response.headers.append("X-RateLimit-Limit", "60")
                call.response.headers.append("X-RateLimit-Remaining", "0")
                call.response.headers.append("X-RateLimit-Reset", rateLimitResult.resetTime.toString())
                return@get call.respond(
                    HttpStatusCode.TooManyRequests,
                    ErrorResponse("Rate limit exceeded. Try again in ${rateLimitResult.resetTime - java.time.Instant.now().epochSecond} seconds.")
                )
            }

            call.response.headers.append("X-RateLimit-Limit", "60")
            call.response.headers.append("X-RateLimit-Remaining", rateLimitResult.remainingRequests.toString())
            call.response.headers.append("X-RateLimit-Reset", rateLimitResult.resetTime.toString())

            call.respond(HttpStatusCode.OK, StatusResponse(
                status = "ok",
                service = "superpets-backend",
                version = "0.0.1",
                timestamp = System.currentTimeMillis()
            ))
        }

        // Get all heroes (public endpoint)
        get("/heroes") {
            try {
                // IP-based rate limiting: 60 requests per minute
                val ipAddress = call.request.local.remoteAddress
                val rateLimitResult = rateLimitingService.checkIpRateLimit(ipAddress, maxRequests = 60, windowSeconds = 60)

                if (!rateLimitResult.allowed) {
                    call.response.headers.append("X-RateLimit-Limit", "60")
                    call.response.headers.append("X-RateLimit-Remaining", "0")
                    call.response.headers.append("X-RateLimit-Reset", rateLimitResult.resetTime.toString())
                    return@get call.respond(
                        HttpStatusCode.TooManyRequests,
                        ErrorResponse("Rate limit exceeded. Try again in ${rateLimitResult.resetTime - java.time.Instant.now().epochSecond} seconds.")
                    )
                }

                call.response.headers.append("X-RateLimit-Limit", "60")
                call.response.headers.append("X-RateLimit-Remaining", rateLimitResult.remainingRequests.toString())
                call.response.headers.append("X-RateLimit-Reset", rateLimitResult.resetTime.toString())

                val heroes = heroService.getAllHeroes()
                call.respond(HttpStatusCode.OK, HeroesResponse(
                    classics = heroes.classics,
                    uniques = heroes.uniques
                ))
            } catch (e: Exception) {
                application.log.error("Error getting heroes", e)
                call.respond(HttpStatusCode.InternalServerError, ErrorResponse(e.message ?: "Unknown error"))
            }
        }

        authenticate("supabase-auth") {
            // Get user profile
            get("/user/profile") {
                try {
                    val userId = call.principal<UserIdPrincipal>()?.name
                        ?: return@get call.respond(HttpStatusCode.Unauthorized, com.alicankorkmaz.models.ErrorResponse("Unauthorized"))

                    // Per-user rate limiting: 30 requests per minute
                    val rateLimitResult = rateLimitingService.checkUserRateLimit(userId, maxRequests = 30, windowSeconds = 60)
                    if (!rateLimitResult.allowed) {
                        call.response.headers.append("X-RateLimit-Limit", "30")
                        call.response.headers.append("X-RateLimit-Remaining", "0")
                        call.response.headers.append("X-RateLimit-Reset", rateLimitResult.resetTime.toString())
                        return@get call.respond(
                            HttpStatusCode.TooManyRequests,
                            com.alicankorkmaz.models.ErrorResponse("Rate limit exceeded. Try again in ${rateLimitResult.resetTime - java.time.Instant.now().epochSecond} seconds.")
                        )
                    }
                    call.response.headers.append("X-RateLimit-Limit", "30")
                    call.response.headers.append("X-RateLimit-Remaining", rateLimitResult.remainingRequests.toString())
                    call.response.headers.append("X-RateLimit-Reset", rateLimitResult.resetTime.toString())

                    // Get email from token
                    val authHeader = call.request.headers["Authorization"]
                    val token = authHeader?.removePrefix("Bearer ")
                    val email = token?.let { supabaseAuthService.getUserEmail(it) } ?: "unknown@example.com"

                    // Auto-create user with 5 free credits on first access
                    val user = supabaseService.getUserOrCreate(userId, email, initialCredits = 5)

                    call.respond(HttpStatusCode.OK, com.alicankorkmaz.models.UserProfile(
                        uid = user.uid,
                        email = user.email,
                        credits = user.credits,
                        createdAt = user.createdAt,
                        isAdmin = user.isAdmin
                    ))
                } catch (e: Exception) {
                    application.log.error("Error getting user profile", e)
                    call.respond(HttpStatusCode.InternalServerError, com.alicankorkmaz.models.ErrorResponse(e.message ?: "Unknown error"))
                }
            }

            // Get credit balance
            get("/user/credits") {
                try {
                    val userId = call.principal<UserIdPrincipal>()?.name
                        ?: return@get call.respond(HttpStatusCode.Unauthorized, com.alicankorkmaz.models.ErrorResponse("Unauthorized"))

                    // Per-user rate limiting: 30 requests per minute
                    val rateLimitResult = rateLimitingService.checkUserRateLimit(userId, maxRequests = 30, windowSeconds = 60)
                    if (!rateLimitResult.allowed) {
                        call.response.headers.append("X-RateLimit-Limit", "30")
                        call.response.headers.append("X-RateLimit-Remaining", "0")
                        call.response.headers.append("X-RateLimit-Reset", rateLimitResult.resetTime.toString())
                        return@get call.respond(
                            HttpStatusCode.TooManyRequests,
                            com.alicankorkmaz.models.ErrorResponse("Rate limit exceeded. Try again in ${rateLimitResult.resetTime - java.time.Instant.now().epochSecond} seconds.")
                        )
                    }
                    call.response.headers.append("X-RateLimit-Limit", "30")
                    call.response.headers.append("X-RateLimit-Remaining", rateLimitResult.remainingRequests.toString())
                    call.response.headers.append("X-RateLimit-Reset", rateLimitResult.resetTime.toString())

                    // Get email from token
                    val authHeader = call.request.headers["Authorization"]
                    val token = authHeader?.removePrefix("Bearer ")
                    val email = token?.let { supabaseAuthService.getUserEmail(it) } ?: "unknown@example.com"

                    // Auto-create user with 5 free credits on first access
                    val user = supabaseService.getUserOrCreate(userId, email, initialCredits = 5)

                    call.respond(HttpStatusCode.OK, com.alicankorkmaz.models.CreditBalanceResponse(user.credits))
                } catch (e: Exception) {
                    application.log.error("Error getting credits", e)
                    call.respond(HttpStatusCode.InternalServerError, com.alicankorkmaz.models.ErrorResponse(e.message ?: "Unknown error"))
                }
            }

            // Get transaction history
            get("/user/transactions") {
                try {
                    val userId = call.principal<UserIdPrincipal>()?.name
                        ?: return@get call.respond(HttpStatusCode.Unauthorized, com.alicankorkmaz.models.ErrorResponse("Unauthorized"))

                    // Per-user rate limiting: 30 requests per minute
                    val rateLimitResult = rateLimitingService.checkUserRateLimit(userId, maxRequests = 30, windowSeconds = 60)
                    if (!rateLimitResult.allowed) {
                        call.response.headers.append("X-RateLimit-Limit", "30")
                        call.response.headers.append("X-RateLimit-Remaining", "0")
                        call.response.headers.append("X-RateLimit-Reset", rateLimitResult.resetTime.toString())
                        return@get call.respond(
                            HttpStatusCode.TooManyRequests,
                            com.alicankorkmaz.models.ErrorResponse("Rate limit exceeded. Try again in ${rateLimitResult.resetTime - java.time.Instant.now().epochSecond} seconds.")
                        )
                    }
                    call.response.headers.append("X-RateLimit-Limit", "30")
                    call.response.headers.append("X-RateLimit-Remaining", rateLimitResult.remainingRequests.toString())
                    call.response.headers.append("X-RateLimit-Reset", rateLimitResult.resetTime.toString())

                    val transactions = supabaseService.getTransactionHistory(userId)
                    call.respond(HttpStatusCode.OK, com.alicankorkmaz.models.TransactionHistoryResponse(transactions))
                } catch (e: Exception) {
                    application.log.error("Error getting transactions", e)
                    call.respond(HttpStatusCode.InternalServerError, com.alicankorkmaz.models.ErrorResponse(e.message ?: "Unknown error"))
                }
            }

            // Get edit history
            get("/user/edits") {
                try {
                    val userId = call.principal<UserIdPrincipal>()?.name
                        ?: return@get call.respond(HttpStatusCode.Unauthorized, EditHistoryResponse(emptyList()))

                    // Per-user rate limiting: 30 requests per minute
                    val rateLimitResult = rateLimitingService.checkUserRateLimit(userId, maxRequests = 30, windowSeconds = 60)
                    if (!rateLimitResult.allowed) {
                        call.response.headers.append("X-RateLimit-Limit", "30")
                        call.response.headers.append("X-RateLimit-Remaining", "0")
                        call.response.headers.append("X-RateLimit-Reset", rateLimitResult.resetTime.toString())
                        return@get call.respond(
                            HttpStatusCode.TooManyRequests,
                            ErrorResponse("Rate limit exceeded. Try again in ${rateLimitResult.resetTime - java.time.Instant.now().epochSecond} seconds.")
                        )
                    }
                    call.response.headers.append("X-RateLimit-Limit", "30")
                    call.response.headers.append("X-RateLimit-Remaining", rateLimitResult.remainingRequests.toString())
                    call.response.headers.append("X-RateLimit-Reset", rateLimitResult.resetTime.toString())

                    val edits = supabaseService.getEditHistory(userId)
                    call.respond(HttpStatusCode.OK, EditHistoryResponse(edits))
                } catch (e: Exception) {
                    application.log.error("Error getting edit history", e)
                    call.respond(HttpStatusCode.InternalServerError, ErrorResponse(e.message ?: "Unknown error"))
                }
            }

            // Add credits (admin only or payment webhook)
            post("/user/credits/add") {
                try {
                    val userId = call.principal<UserIdPrincipal>()?.name
                        ?: return@post call.respond(HttpStatusCode.Unauthorized, ErrorResponse("Unauthorized"))

                    // Per-user rate limiting: 10 requests per minute (admin/webhook operations)
                    val rateLimitResult = rateLimitingService.checkUserRateLimit(userId, maxRequests = 10, windowSeconds = 60)
                    if (!rateLimitResult.allowed) {
                        call.response.headers.append("X-RateLimit-Limit", "10")
                        call.response.headers.append("X-RateLimit-Remaining", "0")
                        call.response.headers.append("X-RateLimit-Reset", rateLimitResult.resetTime.toString())
                        return@post call.respond(
                            HttpStatusCode.TooManyRequests,
                            ErrorResponse("Rate limit exceeded. Try again in ${rateLimitResult.resetTime - java.time.Instant.now().epochSecond} seconds.")
                        )
                    }
                    call.response.headers.append("X-RateLimit-Limit", "10")
                    call.response.headers.append("X-RateLimit-Remaining", rateLimitResult.remainingRequests.toString())
                    call.response.headers.append("X-RateLimit-Reset", rateLimitResult.resetTime.toString())

                    val body = call.receive<AddCreditsRequest>()

                    val success = supabaseService.addCredits(
                        userId = userId,
                        amount = body.amount,
                        type = TransactionType.PURCHASE,
                        description = body.description
                    )

                    if (success) {
                        val user = supabaseService.getUser(userId)
                        call.respond(HttpStatusCode.OK, AddCreditsResponse(
                            success = true,
                            credits = user?.credits
                        ))
                    } else {
                        call.respond(HttpStatusCode.InternalServerError, ErrorResponse("Failed to add credits"))
                    }
                } catch (e: Exception) {
                    application.log.error("Error adding credits", e)
                    call.respond(HttpStatusCode.InternalServerError, ErrorResponse(e.message ?: "Unknown error"))
                }
            }

            post("/nano-banana/edit") {
                try {
                    // Get authenticated user ID
                    val userId = call.principal<UserIdPrincipal>()?.name
                        ?: return@post call.respondText(
                            "Unauthorized",
                            status = HttpStatusCode.Unauthorized
                        )

                    // Per-user rate limiting: 5 requests per minute (expensive operation)
                    val rateLimitResult = rateLimitingService.checkUserRateLimit(userId, maxRequests = 5, windowSeconds = 60)
                    if (!rateLimitResult.allowed) {
                        call.response.headers.append("X-RateLimit-Limit", "5")
                        call.response.headers.append("X-RateLimit-Remaining", "0")
                        call.response.headers.append("X-RateLimit-Reset", rateLimitResult.resetTime.toString())
                        return@post call.respondText(
                            "Rate limit exceeded. Try again in ${rateLimitResult.resetTime - java.time.Instant.now().epochSecond} seconds.",
                            status = HttpStatusCode.TooManyRequests
                        )
                    }
                    call.response.headers.append("X-RateLimit-Limit", "5")
                    call.response.headers.append("X-RateLimit-Remaining", rateLimitResult.remainingRequests.toString())
                    call.response.headers.append("X-RateLimit-Reset", rateLimitResult.resetTime.toString())

                    val request = call.receive<NanoBananaEditRequest>()

                    // Validate request
                    if (request.imageUrl.isBlank()) {
                        return@post call.respondText(
                            "Image URL is required",
                            status = HttpStatusCode.BadRequest
                        )
                    }

                    if (request.numImages !in 1..10) {
                        return@post call.respondText(
                            "Number of images must be between 1 and 10",
                            status = HttpStatusCode.BadRequest
                        )
                    }

                    // Get hero
                    val hero = heroService.getHeroById(request.heroId)
                        ?: return@post call.respondText(
                            "Invalid heroId: ${request.heroId}",
                            status = HttpStatusCode.BadRequest
                        )

                    // Get or create user (first time users get 5 free credits)
                    val authHeader = call.request.headers["Authorization"]
                    val token = authHeader?.removePrefix("Bearer ")
                    val email = token?.let { supabaseAuthService.getUserEmail(it) } ?: "unknown"

                    val user = supabaseService.getUserOrCreate(userId, email, initialCredits = 5)

                    // Check if user has enough credits (cost: 1 credit per image generated)
                    val creditCost = request.numImages.toLong()
                    if (user.credits < creditCost) {
                        return@post call.respondText(
                            "Insufficient credits. You have ${user.credits} credits but need $creditCost.",
                            status = HttpStatusCode.PaymentRequired
                        )
                    }

                    // Deduct credits before making the API call(s)
                    val deducted = supabaseService.deductCredits(
                        userId = userId,
                        amount = creditCost,
                        description = "Image edit: hero ${request.heroId} (${request.numImages} images)"
                    )

                    if (!deducted) {
                        return@post call.respondText(
                            "Failed to deduct credits. Please try again.",
                            status = HttpStatusCode.InternalServerError
                        )
                    }

                    // Generate all unique prompts upfront to ensure variety
                    val prompts = (1..request.numImages).map { hero.buildPrompt() }

                    // Make parallel API calls, each with its unique prompt
                    val results = coroutineScope {
                        prompts.mapIndexed { index, prompt ->
                            async {
                                val editRequest = NanoBananaEditRequest(
                                    heroId = request.heroId,
                                    imageUrl = request.imageUrl,
                                    numImages = 1,  // Each call generates 1 image
                                    outputFormat = request.outputFormat,
                                    syncMode = request.syncMode
                                )
                                nanoBananaService.editImage(prompt, editRequest)
                            }
                        }.map { it.await() }
                    }

                    // Aggregate results
                    val response = NanoBananaEditResponse(
                        images = results.flatMap { it.images },
                        description = "Edited with hero: ${hero.hero}"
                    )

                    // Save edit history
                    supabaseService.saveEditHistory(
                        userId = userId,
                        prompt = prompts.joinToString(" | "),
                        inputImages = listOf(request.imageUrl),
                        outputImages = response.images.map { it.url },
                        creditsCost = creditCost
                    )

                    call.respond(HttpStatusCode.OK, response)

                } catch (e: Exception) {
                    application.log.error("Error processing request", e)
                    call.respondText(
                        "Error processing request: ${e.message}",
                        status = HttpStatusCode.InternalServerError
                    )
                }
            }

            post("/nano-banana/upload-and-edit") {
                try {
                    // Get authenticated user ID
                    val userId = call.principal<UserIdPrincipal>()?.name
                        ?: return@post call.respondText(
                            "Unauthorized",
                            status = HttpStatusCode.Unauthorized
                        )

                    // Per-user rate limiting: 5 requests per minute (expensive operation)
                    val rateLimitResult = rateLimitingService.checkUserRateLimit(userId, maxRequests = 5, windowSeconds = 60)
                    if (!rateLimitResult.allowed) {
                        call.response.headers.append("X-RateLimit-Limit", "5")
                        call.response.headers.append("X-RateLimit-Remaining", "0")
                        call.response.headers.append("X-RateLimit-Reset", rateLimitResult.resetTime.toString())
                        return@post call.respondText(
                            "Rate limit exceeded. Try again in ${rateLimitResult.resetTime - java.time.Instant.now().epochSecond} seconds.",
                            status = HttpStatusCode.TooManyRequests
                        )
                    }
                    call.response.headers.append("X-RateLimit-Limit", "5")
                    call.response.headers.append("X-RateLimit-Remaining", rateLimitResult.remainingRequests.toString())
                    call.response.headers.append("X-RateLimit-Reset", rateLimitResult.resetTime.toString())

                    // Parse multipart form data
                    val multipart = call.receiveMultipart()
                    var imageFile: Pair<ByteArray, Pair<String, String>>? = null // bytes, (fileName, contentType)
                    var heroId: String? = null
                    var numImages: Int = 1
                    var outputFormat: String = "jpeg"

                    multipart.forEachPart { part ->
                        when (part) {
                            is PartData.FileItem -> {
                                if (part.name == "file" && imageFile == null) {
                                    val fileName = part.originalFileName ?: "image.jpg"
                                    val contentType = part.contentType?.toString() ?: "image/jpeg"
                                    val bytes = part.provider().toInputStream().readBytes()
                                    imageFile = bytes to (fileName to contentType)
                                }
                            }
                            is PartData.FormItem -> {
                                when (part.name) {
                                    "hero_id" -> heroId = part.value
                                    "num_images" -> numImages = part.value.toIntOrNull() ?: 1
                                    "output_format" -> outputFormat = part.value
                                }
                            }
                            else -> {}
                        }
                        part.dispose()
                    }

                    // Validate inputs
                    if (imageFile == null) {
                        return@post call.respondText(
                            "Image file is required",
                            status = HttpStatusCode.BadRequest
                        )
                    }

                    // Validate file size, content type, and extension
                    val (bytes, fileInfo) = imageFile!!
                    val (fileName, contentType) = fileInfo
                    val fileValidation = FileValidation.validateFile(
                        fileSize = bytes.size.toLong(),
                        contentType = contentType,
                        filename = fileName
                    )

                    if (!fileValidation.isValid) {
                        return@post call.respondText(
                            fileValidation.errorMessage ?: "Invalid file",
                            status = HttpStatusCode.BadRequest
                        )
                    }

                    if (heroId.isNullOrBlank()) {
                        return@post call.respondText(
                            "heroId is required",
                            status = HttpStatusCode.BadRequest
                        )
                    }

                    // Get hero
                    val hero = heroService.getHeroById(heroId)
                        ?: return@post call.respondText(
                            "Invalid heroId: $heroId",
                            status = HttpStatusCode.BadRequest
                        )

                    if (numImages !in 1..10) {
                        return@post call.respondText(
                            "Number of images must be between 1 and 10",
                            status = HttpStatusCode.BadRequest
                        )
                    }

                    // Get or create user (first time users get 5 free credits)
                    val authHeader = call.request.headers["Authorization"]
                    val token = authHeader?.removePrefix("Bearer ")
                    val email = token?.let { supabaseAuthService.getUserEmail(it) } ?: "unknown"

                    val user = supabaseService.getUserOrCreate(userId, email, initialCredits = 5)

                    // Check if user has enough credits (cost: 1 credit per image generated)
                    val creditCost = numImages.toLong()
                    if (user.credits < creditCost) {
                        return@post call.respondText(
                            "Insufficient credits. You have ${user.credits} credits but need $creditCost.",
                            status = HttpStatusCode.PaymentRequired
                        )
                    }

                    // Deduct credits before making the API call(s)
                    val deducted = supabaseService.deductCredits(
                        userId = userId,
                        amount = creditCost,
                        description = "Image edit (upload): hero $heroId ($numImages images)"
                    )

                    if (!deducted) {
                        return@post call.respondText(
                            "Failed to deduct credits. Please try again.",
                            status = HttpStatusCode.InternalServerError
                        )
                    }

                    // Upload image to fal.ai storage (already validated above)
                    val imageUrl = nanoBananaService.uploadFile(bytes, fileName, contentType)

                    // Generate all unique prompts upfront to ensure variety
                    val prompts = (1..numImages).map { hero.buildPrompt() }

                    // Make parallel API calls, each with its unique prompt
                    val results = coroutineScope {
                        prompts.mapIndexed { index, prompt ->
                            async {
                                val editRequest = NanoBananaEditRequest(
                                    heroId = heroId,
                                    imageUrl = imageUrl,
                                    numImages = 1,  // Each call generates 1 image
                                    outputFormat = outputFormat
                                )
                                nanoBananaService.editImage(prompt, editRequest)
                            }
                        }.map { it.await() }
                    }

                    // Aggregate results
                    val response = NanoBananaEditResponse(
                        images = results.flatMap { it.images },
                        description = "Edited with hero: ${hero.hero}"
                    )

                    // Save edit history
                    supabaseService.saveEditHistory(
                        userId = userId,
                        prompt = prompts.joinToString(" | "),
                        inputImages = listOf(imageUrl),
                        outputImages = response.images.map { it.url },
                        creditsCost = creditCost
                    )

                    call.respond(HttpStatusCode.OK, response)

                } catch (e: Exception) {
                    application.log.error("Error processing upload and edit request", e)
                    call.respondText(
                        "Error processing request: ${e.message}",
                        status = HttpStatusCode.InternalServerError
                    )
                }
            }

            // Admin routes
            get("/admin/stats") {
                try {
                    val userId = call.principal<UserIdPrincipal>()?.name
                        ?: return@get call.respond(HttpStatusCode.Unauthorized, ErrorResponse("Unauthorized"))

                    // Check if user is admin
                    val user = supabaseService.getUser(userId)
                    if (user == null || !user.isAdmin) {
                        return@get call.respond(HttpStatusCode.Forbidden, ErrorResponse("Admin access required"))
                    }

                    val stats = supabaseService.getAdminStats()
                    call.respond(HttpStatusCode.OK, stats)
                } catch (e: Exception) {
                    application.log.error("Error getting admin stats", e)
                    call.respond(HttpStatusCode.InternalServerError, ErrorResponse(e.message ?: "Unknown error"))
                }
            }

            get("/admin/users") {
                try {
                    val userId = call.principal<UserIdPrincipal>()?.name
                        ?: return@get call.respond(HttpStatusCode.Unauthorized, ErrorResponse("Unauthorized"))

                    // Check if user is admin
                    val user = supabaseService.getUser(userId)
                    if (user == null || !user.isAdmin) {
                        return@get call.respond(HttpStatusCode.Forbidden, ErrorResponse("Admin access required"))
                    }

                    val limit = call.request.queryParameters["limit"]?.toIntOrNull() ?: 100
                    val offset = call.request.queryParameters["offset"]?.toIntOrNull() ?: 0

                    val users = supabaseService.getAllUsersWithDetails(limit, offset)
                    val total = supabaseService.getTotalUsers()

                    call.respond(HttpStatusCode.OK, AdminUsersResponse(users, total))
                } catch (e: Exception) {
                    application.log.error("Error getting admin users", e)
                    call.respond(HttpStatusCode.InternalServerError, ErrorResponse(e.message ?: "Unknown error"))
                }
            }

            post("/admin/users/update") {
                try {
                    val adminUserId = call.principal<UserIdPrincipal>()?.name
                        ?: return@post call.respond(HttpStatusCode.Unauthorized, ErrorResponse("Unauthorized"))

                    // Check if requesting user is admin
                    val adminUser = supabaseService.getUser(adminUserId)
                    if (adminUser == null || !adminUser.isAdmin) {
                        return@post call.respond(HttpStatusCode.Forbidden, ErrorResponse("Admin access required"))
                    }

                    val request = call.receive<AdminUpdateUserRequest>()

                    // Update admin status if provided
                    if (request.isAdmin != null) {
                        val success = supabaseService.updateUserAdmin(request.userId, request.isAdmin)
                        if (!success) {
                            return@post call.respond(HttpStatusCode.InternalServerError, ErrorResponse("Failed to update admin status"))
                        }
                    }

                    // Update credits if provided
                    if (request.credits != null) {
                        val success = supabaseService.updateUserCredits(request.userId, request.credits)
                        if (!success) {
                            return@post call.respond(HttpStatusCode.InternalServerError, ErrorResponse("Failed to update credits"))
                        }
                    }

                    val updatedUser = supabaseService.getUser(request.userId)
                        ?: return@post call.respond(HttpStatusCode.NotFound, ErrorResponse("User not found"))

                    call.respond(HttpStatusCode.OK, AdminUpdateUserResponse(
                        success = true,
                        user = updatedUser
                    ))
                } catch (e: Exception) {
                    application.log.error("Error updating user", e)
                    call.respond(HttpStatusCode.InternalServerError, ErrorResponse(e.message ?: "Unknown error"))
                }
            }

            get("/admin/transactions") {
                try {
                    val userId = call.principal<UserIdPrincipal>()?.name
                        ?: return@get call.respond(HttpStatusCode.Unauthorized, ErrorResponse("Unauthorized"))

                    // Check if user is admin
                    val user = supabaseService.getUser(userId)
                    if (user == null || !user.isAdmin) {
                        return@get call.respond(HttpStatusCode.Forbidden, ErrorResponse("Admin access required"))
                    }

                    val limit = call.request.queryParameters["limit"]?.toIntOrNull() ?: 100
                    val offset = call.request.queryParameters["offset"]?.toIntOrNull() ?: 0

                    val transactions = supabaseService.getAllTransactions(limit, offset)
                    call.respond(HttpStatusCode.OK, TransactionHistoryResponse(transactions))
                } catch (e: Exception) {
                    application.log.error("Error getting admin transactions", e)
                    call.respond(HttpStatusCode.InternalServerError, ErrorResponse(e.message ?: "Unknown error"))
                }
            }

            get("/admin/edits") {
                try {
                    val userId = call.principal<UserIdPrincipal>()?.name
                        ?: return@get call.respond(HttpStatusCode.Unauthorized, ErrorResponse("Unauthorized"))

                    // Check if user is admin
                    val user = supabaseService.getUser(userId)
                    if (user == null || !user.isAdmin) {
                        return@get call.respond(HttpStatusCode.Forbidden, ErrorResponse("Admin access required"))
                    }

                    val limit = call.request.queryParameters["limit"]?.toIntOrNull() ?: 100
                    val offset = call.request.queryParameters["offset"]?.toIntOrNull() ?: 0

                    val edits = supabaseService.getAllEdits(limit, offset)
                    call.respond(HttpStatusCode.OK, EditHistoryResponse(edits))
                } catch (e: Exception) {
                    application.log.error("Error getting admin edits", e)
                    call.respond(HttpStatusCode.InternalServerError, ErrorResponse(e.message ?: "Unknown error"))
                }
            }

            // Stripe checkout session creation
            post("/stripe/create-checkout-session") {
                try {
                    val userId = call.principal<UserIdPrincipal>()?.name
                        ?: return@post call.respond(HttpStatusCode.Unauthorized, ErrorResponse("Unauthorized"))

                    // Per-user rate limiting: 10 requests per minute
                    val rateLimitResult = rateLimitingService.checkUserRateLimit(userId, maxRequests = 10, windowSeconds = 60)
                    if (!rateLimitResult.allowed) {
                        call.response.headers.append("X-RateLimit-Limit", "10")
                        call.response.headers.append("X-RateLimit-Remaining", "0")
                        call.response.headers.append("X-RateLimit-Reset", rateLimitResult.resetTime.toString())
                        return@post call.respond(
                            HttpStatusCode.TooManyRequests,
                            ErrorResponse("Rate limit exceeded. Try again in ${rateLimitResult.resetTime - java.time.Instant.now().epochSecond} seconds.")
                        )
                    }
                    call.response.headers.append("X-RateLimit-Limit", "10")
                    call.response.headers.append("X-RateLimit-Remaining", rateLimitResult.remainingRequests.toString())
                    call.response.headers.append("X-RateLimit-Reset", rateLimitResult.resetTime.toString())

                    val authHeader = call.request.headers["Authorization"]
                    val token = authHeader?.removePrefix("Bearer ")
                    val email = token?.let { supabaseAuthService.getUserEmail(it) } ?: "unknown@example.com"

                    val request = call.receive<CreateCheckoutSessionRequest>()

                    // Validate credit amount
                    if (request.credits !in listOf(10, 25, 50, 100)) {
                        return@post call.respond(
                            HttpStatusCode.BadRequest,
                            ErrorResponse("Invalid credit amount. Must be 10, 25, 50, or 100")
                        )
                    }

                    // Create Stripe checkout session
                    val session = stripeService.createCheckoutSession(
                        credits = request.credits,
                        userId = userId,
                        userEmail = email,
                        successUrl = "http://localhost:5173?payment=success",
                        cancelUrl = "http://localhost:5173?payment=cancelled"
                    )

                    call.respond(
                        HttpStatusCode.OK,
                        CreateCheckoutSessionResponse(
                            sessionId = session.id,
                            url = session.url
                        )
                    )
                } catch (e: Exception) {
                    application.log.error("Error creating checkout session", e)
                    call.respond(HttpStatusCode.InternalServerError, ErrorResponse(e.message ?: "Unknown error"))
                }
            }
        }

        // Stripe webhook (not authenticated)
        post("/stripe/webhook") {
            try {
                // IP-based rate limiting: 100 requests per minute (Stripe webhooks)
                val ipAddress = call.request.local.remoteAddress
                val rateLimitResult = rateLimitingService.checkIpRateLimit(ipAddress, maxRequests = 100, windowSeconds = 60)

                if (!rateLimitResult.allowed) {
                    call.response.headers.append("X-RateLimit-Limit", "100")
                    call.response.headers.append("X-RateLimit-Remaining", "0")
                    call.response.headers.append("X-RateLimit-Reset", rateLimitResult.resetTime.toString())
                    return@post call.respond(
                        HttpStatusCode.TooManyRequests,
                        ErrorResponse("Rate limit exceeded. Try again in ${rateLimitResult.resetTime - java.time.Instant.now().epochSecond} seconds.")
                    )
                }

                call.response.headers.append("X-RateLimit-Limit", "100")
                call.response.headers.append("X-RateLimit-Remaining", rateLimitResult.remainingRequests.toString())
                call.response.headers.append("X-RateLimit-Reset", rateLimitResult.resetTime.toString())

                val payload = call.receiveText()
                val signature = call.request.headers["Stripe-Signature"]
                    ?: return@post call.respond(HttpStatusCode.BadRequest, ErrorResponse("Missing signature"))

                // Verify webhook signature
                if (!stripeService.verifyWebhookSignature(payload, signature)) {
                    return@post call.respond(HttpStatusCode.BadRequest, ErrorResponse("Invalid signature"))
                }

                // Parse the event
                val event = com.stripe.model.Event.GSON.fromJson(payload, com.stripe.model.Event::class.java)

                when (event.type) {
                    "checkout.session.completed" -> {
                        // Parse the session from the event data
                        val sessionJson = event.data.`object`
                        val session = com.stripe.model.checkout.Session.GSON.fromJson(
                            sessionJson.toJson(),
                            com.stripe.model.checkout.Session::class.java
                        )

                        val userId = session.clientReferenceId
                        val credits = session.metadata["credits"]?.toLongOrNull() ?: 0L

                        if (userId != null && credits > 0) {
                            // Add credits to user account
                            val success = supabaseService.addCredits(
                                userId = userId,
                                amount = credits,
                                type = TransactionType.PURCHASE,
                                description = "Purchased $credits credits via Stripe"
                            )

                            if (success) {
                                application.log.info("Successfully added $credits credits to user $userId")
                            } else {
                                application.log.error("Failed to add credits to user $userId")
                            }
                        }
                    }

                    "payment_intent.payment_failed" -> {
                        val paymentIntentJson = event.data.`object`
                        val paymentIntent = com.stripe.model.PaymentIntent.GSON.fromJson(
                            paymentIntentJson.toJson(),
                            com.stripe.model.PaymentIntent::class.java
                        )
                        application.log.warn("Payment failed for payment intent: ${paymentIntent.id}")
                        // Could notify user via email here
                    }

                    "charge.refunded" -> {
                        val chargeJson = event.data.`object`
                        val charge = com.stripe.model.Charge.GSON.fromJson(
                            chargeJson.toJson(),
                            com.stripe.model.Charge::class.java
                        )

                        // Extract metadata from the charge to find the user and credits
                        val metadata = charge.metadata
                        val userId = metadata["userId"]
                        val credits = metadata["credits"]?.toLongOrNull()

                        if (userId != null && credits != null && credits > 0) {
                            // Deduct refunded credits
                            val success = supabaseService.deductCredits(
                                userId = userId,
                                amount = credits,
                                description = "Refund: ${credits} credits removed (Charge: ${charge.id})"
                            )

                            if (success) {
                                application.log.info("Successfully deducted $credits credits from user $userId due to refund")
                            } else {
                                application.log.error("Failed to deduct credits for refund from user $userId")
                            }
                        } else {
                            application.log.warn("Refund received but missing metadata: chargeId=${charge.id}")
                        }
                    }

                    "charge.dispute.created" -> {
                        val disputeJson = event.data.`object`
                        val dispute = com.stripe.model.Dispute.GSON.fromJson(
                            disputeJson.toJson(),
                            com.stripe.model.Dispute::class.java
                        )
                        application.log.warn("Dispute created for charge: ${dispute.charge}. Amount: ${dispute.amount}")
                        // Could flag the user account or send admin notification
                    }

                    "charge.dispute.closed" -> {
                        val disputeJson = event.data.`object`
                        val dispute = com.stripe.model.Dispute.GSON.fromJson(
                            disputeJson.toJson(),
                            com.stripe.model.Dispute::class.java
                        )
                        application.log.info("Dispute closed for charge: ${dispute.charge}. Status: ${dispute.status}")
                    }

                    else -> {
                        application.log.info("Unhandled webhook event type: ${event.type}")
                    }
                }

                call.respond(HttpStatusCode.OK, mapOf("received" to true))
            } catch (e: Exception) {
                application.log.error("Error processing webhook", e)
                call.respond(HttpStatusCode.InternalServerError, ErrorResponse(e.message ?: "Unknown error"))
            }
        }
    }
}
