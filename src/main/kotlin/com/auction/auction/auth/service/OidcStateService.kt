package com.auction.auction.auth.service

import com.auction.auction.auth.oidc.OidcStatePayload
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.stereotype.Service
import java.security.SecureRandom
import java.time.Duration
import java.util.Base64



@Service
class OidcStateService(
    private final val RedisService: RedisServices
) {

    private val random = SecureRandom()

    // TTL은 3~5분 추천
    private val ttl: Duration = Duration.ofMinutes(5)

    // Redis Key Prefix
    private val keyPrefix = "oidc:state:"

    /* ===================== Public API ===================== */

    fun generateState(): String = secureRandomUrlSafe(32)

    fun generateNonce(): String = secureRandomUrlSafe(32)

    fun store(state: String, payload: OidcStatePayload) {
        val key = keyPrefix + state
        val objectMapper = jacksonObjectMapper()
        val json = objectMapper.writeValueAsString(payload)
        RedisService.set(key, json, ttl)
    }

    /**
     * 1회용 소비 (조회 + 삭제)
     */
    fun consume(state: String): OidcStatePayload? {
        val key = keyPrefix + state
        val raw = RedisService.get(key) ?: return null
        RedisService.delete(key)
        return decode(raw)
    }

    fun delete(state: String) {
        RedisService.delete(keyPrefix + state)
    }

    /* ===================== Internal ===================== */

    private fun secureRandomUrlSafe(byteLen: Int): String {
        val bytes = ByteArray(byteLen)
        random.nextBytes(bytes)
        return Base64.getUrlEncoder()
            .withoutPadding()
            .encodeToString(bytes)
    }

    /**
     * nonce|returnUrl|createdAt
     */
    private fun encode(p: OidcStatePayload): String {
        val safeReturn = p.returnUrl ?: ""
        return "${p.nonce}|$safeReturn|${p.createdAtEpochMs}"
    }

    private fun decode(raw: String): OidcStatePayload {
        val parts = raw.split("|", limit = 3)
        return OidcStatePayload(
            nonce = parts.getOrElse(0) { "" },
            returnUrl = parts.getOrElse(1) { "" }.ifBlank { null },
            createdAtEpochMs = parts.getOrElse(2) { "0" }.toLongOrNull() ?: 0L
        )
    }


}