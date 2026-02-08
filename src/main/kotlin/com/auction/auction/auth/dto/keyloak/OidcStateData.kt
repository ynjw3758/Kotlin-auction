package com.auction.auction.auth.dto.keyloak

data class OidcStateData(
    val nonce: String,
    val returnUrl: String? = null,
    val codeVerifier: String? = null,
    val createdAtEpochMs: Long
)
