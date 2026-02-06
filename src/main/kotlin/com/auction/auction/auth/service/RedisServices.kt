package com.auction.auction.auth.service

import org.slf4j.LoggerFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import java.time.Duration

private val log = LoggerFactory.getLogger(AuthServices::class.java)

@Service
class RedisServices(
    private val redisTemplate: RedisTemplate<String, String>) {
    private val ops = redisTemplate.opsForValue()


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

    /* ---------------- OIDC State / Nonce ---------------- */


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