package com.auction.auction.auth.dto.keyloak
import com.fasterxml.jackson.annotation.JsonProperty

data class KeycloakUserInfo(
    @JsonProperty("sub") val sub: String? = null,
    @JsonProperty("preferred_username") val username: String? = null,
    @JsonProperty("email") val email: String? = null,
    @JsonProperty("given_name") val firstName: String? = null,
    @JsonProperty("family_name") val lastName: String? = null,
)
