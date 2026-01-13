package com.auction.auction.auth.dto.response

data class LoginBody(
    val access_token: String,
    val exp:Long
)