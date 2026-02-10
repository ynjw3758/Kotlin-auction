package com.auction.auction.auth.oidc

import com.auction.auction.auth.service.AuthServices
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.util.UriComponentsBuilder


private val log = LoggerFactory.getLogger(KeycloakAuthUrlBuilder::class.java)
@Service
class KeycloakAuthUrlBuilder(
    @Value("\${keycloak.base-url}") private val baseUrl: String,
    @Value("\${keycloak.realm}") private val realm: String,
    @Value("\${keycloak.client-id}") private val clientId: String,
    @Value("\${keycloak.redirect-uri}") private val redirectUri: String,
    @Value("\${keycloak.scope:openid}") private val scope: String
) {
    fun buildNormal(state: String, nonce: String): String {
        return build(state, nonce, idpHint = null)
    }

    fun buildKakao(state: String , nonce: String) :String{
        return build(state, nonce, idpHint = "kakao")
    }

    private fun build(state: String, nonce: String, idpHint: String?): String {
        val builder = UriComponentsBuilder
            .fromHttpUrl("$baseUrl/realms/$realm/protocol/openid-connect/auth")
            .queryParam("client_id", clientId)
            .queryParam("redirect_uri", redirectUri)
            .queryParam("response_type", "code")
            .queryParam("scope", scope)
            .queryParam("state", state)
            .queryParam("nonce", nonce)

        if (!idpHint.isNullOrBlank()) {
            log.info("test")
            builder.queryParam("kc_idp_hint", idpHint)
        }
        return try {
            val url = builder.encode().build().toUriString()
            url
        } catch (e: Exception) {
            throw e
        }
    }
}