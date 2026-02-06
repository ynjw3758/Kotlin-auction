package com.auction.auction.auth.dto.response

data class LoginBody(
    val accessToken: String,
    val refreshToken: String,
    val loginId: String,
    val exp: Long
)
