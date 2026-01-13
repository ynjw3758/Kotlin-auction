package com.auction.auction.auth.dto.response


data class LoginResponse<T>(
    val message: String,
    var body: T?,
    var code: String,
    var errorCode: String,
    )
