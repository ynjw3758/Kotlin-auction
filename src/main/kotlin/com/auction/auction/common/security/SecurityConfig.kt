package com.auction.auction.common.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain


@Configuration
class SecurityConfig {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .cors {  }
            .authorizeHttpRequests { auth ->
                auth.requestMatchers("/api/auth/login").permitAll()
                auth.anyRequest().permitAll()   // 개발 중이니 일단 전체 허용
            }
            .httpBasic { it.disable() }
            .formLogin { it.disable() }

        return http.build()
    }
}