package com.auction.auction.common.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.StringRedisSerializer


@Configuration
class RedisConfig {

    @Bean
    fun redisTemplate(
        connectionFactory: RedisConnectionFactory
    ): RedisTemplate<String, String> {

        return RedisTemplate<String, String>().apply {
            this.connectionFactory = connectionFactory

            // Key: String
            keySerializer = StringRedisSerializer()

            // Value: String (JWT 토큰)
            valueSerializer = StringRedisSerializer()

            // Hash도 String으로 (나중에 써도 됨)
            hashKeySerializer = StringRedisSerializer()
            hashValueSerializer = StringRedisSerializer()

            afterPropertiesSet()
        }
    }
}