package com.auction.auction.common.security


import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
class PasswordConfig {
    @Bean
    fun passwordEncoder(): PasswordEncoder {
        // {bcrypt} 같은 prefix를 자동 처리해줌
        return PasswordEncoderFactories.createDelegatingPasswordEncoder()
    }
}