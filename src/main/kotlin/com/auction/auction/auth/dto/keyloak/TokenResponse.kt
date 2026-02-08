package com.auction.auction.auth.dto.keyloak
import com.fasterxml.jackson.annotation.JsonProperty


data class TokenResponse(
                    @JsonProperty("access_token") val accessToken: String,
                     @JsonProperty("id_token") val idToken: String? = null,
                     @JsonProperty("refresh_token") val refreshToken: String? = null,
                     @JsonProperty("expires_in") val expiresIn: Long? = null
)
