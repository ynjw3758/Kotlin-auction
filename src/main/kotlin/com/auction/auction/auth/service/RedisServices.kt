package com.auction.auction.auth.service

import com.auction.auction.auth.dto.keyloak.OidcStateData
import com.auction.auction.auth.exception.AuthErrorCode
import com.auction.auction.common.exception.ApiException
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import java.time.Duration

private val log = LoggerFactory.getLogger(AuthServices::class.java)

@Service
class RedisServices(
         private val redisTemplate: RedisTemplate<String, String>) {

    private val ops = redisTemplate.opsForValue()
    private val om = jacksonObjectMapper()

    private val OIDC_STATE_PREFIX = "oidc:state:"
    private val MAX_STATE_AGE_MS = Duration.ofMinutes(5).toMillis()

    /* ===================== 공통 ===================== */

    fun set(key: String, value: String, ttl: Duration) {
        ops.set(key, value, ttl)
        log.debug("Redis SET key={}, ttl={}s", key, ttl.seconds)
    }

    fun get(key: String): String? =
        ops.get(key)

    fun delete(key: String) {
        redisTemplate.delete(key)
        log.debug("Redis DEL key={}", key)
    }

    /* ================= OIDC State ================= */

    /**
     * 🔐 OIDC Callback에서 state 검증
     */
    fun validateOidcState(state: String): OidcStateData {
        val key = "oidc:state:$state"

        val json = ops.get(key)
            ?: throw ApiException(AuthErrorCode.OIDC_STATE_INVALID)

        val data = try {
            om.readValue(json, OidcStateData::class.java)
        } catch (e: Exception) {
            delete(key)
            throw ApiException(AuthErrorCode.OIDC_STATE_CORRUPTED)
        }

        val age = System.currentTimeMillis() - data.createdAtEpochMs
        if (age < 0 || age > MAX_STATE_AGE_MS) {
            delete(key)
            throw ApiException(AuthErrorCode.OIDC_STATE_INVALID)
        }

        return data
    }
    /**
     * 🔐 state는 1회용 → 성공 시 반드시 소비
     */
    fun consumeOidcState(state: String) {
        val key = "$OIDC_STATE_PREFIX$state"
        delete(key)
    }


    /*
    fun SaveRefreshToken(connectId: String, refreshToken: String,) {
        val REFRESH_EXPIRE_SECONDS = 14L * 24 * 60 * 60
        //val key = "refresh:$connectId"
        ops.set(
            connectId,                      // ✅ key = connectId
            refreshToken,                   // ✅ value = refreshToken
            Duration.ofSeconds(REFRESH_EXPIRE_SECONDS)
        )

    }
     */
}