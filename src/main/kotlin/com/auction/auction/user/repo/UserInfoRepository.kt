package com.auction.auction.user.repo


import com.auction.auction.user.entity.Users
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID


interface UserInfoRepository : JpaRepository<Users, UUID> {

    fun findByKeycloakSub(keycloakSub: String): Users?
}