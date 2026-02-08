package com.auction.auction.auth.oidc

import com.auction.auction.auth.exception.AuthErrorCode
import com.auction.auction.common.exception.ApiException
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.stereotype.Component
import java.util.Base64

@Component
class KeycloakIdTokenVerifier {


    private val ObjectMapper = jacksonObjectMapper()

    fun verifyNonce(idToken: String, expectedNonce:String) {
        val nonceInToken = extractNonce(idToken)

        if (nonceInToken.isNullOrBlank() || nonceInToken != expectedNonce) {
            throw ApiException(AuthErrorCode.OIDC_NONCE_MISMATCH)
        }
    }

    fun extractNonce(IdToken:String) : String?{
        val parts = IdToken.split(".")
        if (parts.size < 2) {
            throw IllegalArgumentException("Invalid JWT format")
        }

        // JWT는 Base64URL 인코딩
        val payloadJson = String(Base64.getUrlDecoder().decode(parts[1]))

        val payload: Map<String, Any?> = ObjectMapper.readValue(payloadJson)
        return payload["nonce"] as? String
    }
}