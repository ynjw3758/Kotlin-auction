package com.auction.auction.auth.oidc

data class OidcStatePayload(
    val nonce: String,
    val returnUrl: String? = null,
    val codeVerifier: String? = null,
    val createdAtEpochMs: Long = System.currentTimeMillis()
)
