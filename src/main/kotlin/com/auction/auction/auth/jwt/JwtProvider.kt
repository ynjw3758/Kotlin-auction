package com.auction.auction.auth.jwt

import com.auction.auction.auth.service.AuthServices
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm

private val log = LoggerFactory.getLogger(AuthServices::class.java)

@Component
class JwtProvider(
           @Value("\${jwt.access.secret}")
           private val accessSecret:String,

           @Value("\${jwt.refresh.secret}")
           private val refreshSecret:String,

           @Value("\${jwt.access.expire-seconds}")
           private val accessExpireSeconds: Long,

           @Value("\${jwt.refresh.expire-seconds}")
           private val refreshExpireSeconds: Long,
    ) {

    fun CreateAcessToken(connectId: UUID, loginId:String): Pair<String, Long>{
        val now = Date()
        val exp = Date(now.time + accessExpireSeconds * 1000)

        val token = Jwts.builder()
            .setSubject(connectId.toString())
            .claim("loginId", loginId)
            .setIssuedAt(now)
            .setExpiration(exp)
            .signWith(SignatureAlgorithm.HS256, accessSecret)
            .compact()

        return token to (exp.time / 1000)
    }

    fun CreateRefreshToken(connectId: UUID): String {
            val now = Date()
            val exp = Date(now.time + refreshExpireSeconds * 1000)

            return Jwts.builder()
                .setSubject(connectId.toString())
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(SignatureAlgorithm.HS256, refreshSecret)
                .compact()
        }

}