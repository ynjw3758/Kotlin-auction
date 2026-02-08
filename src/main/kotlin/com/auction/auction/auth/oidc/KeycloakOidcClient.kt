package com.auction.auction.auth.oidc

import com.auction.auction.auth.dto.keyloak.KeycloakUserInfo
import com.auction.auction.auth.dto.keyloak.TokenResponse
import com.auction.auction.auth.exception.AuthErrorCode
import com.auction.auction.common.exception.ApiException
import org.springframework.web.client.RestClient
import org.springframework.stereotype.Component
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.util.LinkedMultiValueMap


@Component
class KeycloakOidcClient(
    private val restClient: RestClient,
    @Value("\${keycloak.base-url}") private val baseUrl: String,
    @Value("\${keycloak.realm}") private val realm: String,
    @Value("\${keycloak.client-id}") private val clientId: String,
    @Value("\${keycloak.client-secret:}") private val clientSecret: String?,
    @Value("\${keycloak.redirect-uri}") private val redirectUri: String,
) {

    fun exchangeCodeForToken(code: String, codeVerifier: String?): TokenResponse {
        val form = LinkedMultiValueMap<String, String>().apply {
            add("grant_type", "authorization_code")
            add("client_id", clientId)
            add("redirect_uri", redirectUri)
            add("code", code)

            if (!codeVerifier.isNullOrBlank()) {
                // PKCE (Public client)
                add("code_verifier", codeVerifier)
            } else {
                // Confidential client
                add("client_secret", clientSecret)
            }
        }

        return restClient.post()
            .uri("$baseUrl/realms/$realm/protocol/openid-connect/token")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(form)
            .retrieve()
            .body(TokenResponse::class.java)
            ?: throw ApiException(AuthErrorCode.OIDC_TOKEN_EXCHANGE_FAILED)
    }

    fun fetchUserInfo(accessToken: String): KeycloakUserInfo =
        restClient.get()
            .uri("$baseUrl/realms/$realm/protocol/openid-connect/userinfo")
            .header("Authorization", "Bearer $accessToken")
            .retrieve()
            .body(KeycloakUserInfo::class.java)!!
}

