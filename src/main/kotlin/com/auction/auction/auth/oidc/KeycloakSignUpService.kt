package com.auction.auction.auth.oidc

import com.auction.auction.auth.controller.AuthController
import com.auction.auction.auth.dto.request.SignUpRequest
import org.keycloak.admin.client.Keycloak
import org.keycloak.representations.idm.CredentialRepresentation
import org.keycloak.representations.idm.UserRepresentation
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

private val log = LoggerFactory.getLogger(KeycloakSignUpService::class.java)
@Service
class KeycloakSignUpService(
    private val keycloakAdmin: Keycloak,
    @Value("\${keycloak.realm}") private val targetRealm: String
) {
    fun createUser(req: SignUpRequest) {

        val user = UserRepresentation().apply {
            username = req.id
            email = req.email
            isEnabled = true

            // firstName, lastName 안 씀

            attributes = mapOf(
                "name" to listOf(req.name),
                "nickname" to listOf(req.nickname),
                "address" to listOf(req.address)
            )
        }
         log.info("User :" + user.username)
        val usersResource = keycloakAdmin.realm(targetRealm).users()
        val response = usersResource.create(user)
      log.info("respoinse"+response.status)
        log.info("respoinse"+response.location)
        log.info("respoinse"+response.date)
        if (response.status !in 200..299) {
            val error = response.readEntity(String::class.java)
            throw IllegalStateException(
                "Keycloak user create failed: ${response.status} / $error"
            )
        }

        val location = response.location.toString()
        val userId = location.substringAfterLast("/")

        val credential = CredentialRepresentation().apply {
            type = CredentialRepresentation.PASSWORD
            value = req.password
            isTemporary = false
        }

        usersResource.get(userId).resetPassword(credential)
    }

}