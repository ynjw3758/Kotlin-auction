package com.auction.auction.auth.dto.response

data class LoginResponse(
    val accessToken: String,
    val loginId: String,
    val exp: Long
)