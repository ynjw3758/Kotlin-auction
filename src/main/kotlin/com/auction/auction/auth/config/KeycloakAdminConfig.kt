package com.auction.auction.auth.config

import org.keycloak.admin.client.Keycloak
import org.keycloak.admin.client.KeycloakBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class KeycloakAdminConfig(
    @Value("\${keycloak.base-url}") private val baseUrl: String,
    @Value("\${keycloak.admin-realm}") private val adminRealm: String,
    @Value("\${keycloak.admin-client-id}") private val adminClientId: String,
    @Value("\${keycloak.admin-client-secret}") private val adminClientSecret: String,
) {


    @Bean
    fun keycloakAdmin(): Keycloak {
        return KeycloakBuilder.builder()
            .serverUrl(baseUrl)
            .realm(adminRealm)
            .grantType("client_credentials")
            .clientId(adminClientId)
            .clientSecret(adminClientSecret)
            .build()
    }


}