package com.auction.auction.auth.dto.request

data class SignUpRequest(
        val id: String,
        val password: String,
        val name: String,
        val email: String,
        val address: String,
        val nickname: String,
)
