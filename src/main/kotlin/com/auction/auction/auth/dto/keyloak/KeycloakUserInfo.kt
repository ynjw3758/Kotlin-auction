package com.auction.auction.auth.dto.keyloak
import com.fasterxml.jackson.annotation.JsonProperty

data class KeycloakUserInfo(
    // Keycloak 고유 식별자 (OIDC sub)
    @JsonProperty("sub")
    val sub: String? = null,

    // 로그인 ID (Keycloak username)
    @JsonProperty("preferred_username")
    val username: String? = null,

    // 이메일
    @JsonProperty("email")
    val email: String? = null,

    // 실명 (User Profile에서 만든 custom attribute: name)
    @JsonProperty("name")
    val name: String? = null,

    // 닉네임 (custom attribute)
    @JsonProperty("nickname")
    val nickname: String? = null,

    // 전화번호 (custom attribute)
    @JsonProperty("phone")
    val phone: String? = null,
)
