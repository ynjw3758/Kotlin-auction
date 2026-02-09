package com.auction.auction.user.service

import com.auction.auction.auth.dto.keyloak.KeycloakUserInfo
import com.auction.auction.user.entity.UserStatus
import com.auction.auction.user.entity.Users
import com.auction.auction.user.repo.UserInfoRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class UserCommandService(
    private val userInfoRepository: UserInfoRepository,
) {

    @Transactional
    fun upsertFromKeycloak(userInfo: KeycloakUserInfo): Users {
        val sub = userInfo.sub ?: error("userinfo.sub is null")
        val loginId = userInfo.username ?: error("userinfo.preferred_username is null")
        val nickname = userInfo.nickname ?: loginId

        val existing = userInfoRepository.findByKeycloakSub(sub)

        return if (existing == null) {
            userInfoRepository.save(
                Users(
                    id = UUID.randomUUID(),
                    keycloakSub = sub,
                    loginId = loginId,
                    nickname = nickname,
                    name = userInfo.name,
                    phone = userInfo.phone,
                    status = UserStatus.ACTIVE
                )
            )
        } else {
            existing.nickname = nickname
            existing.name = userInfo.name
            existing.phone = userInfo.phone

            if (existing.status == UserStatus.DELETED) {
                existing.status = UserStatus.ACTIVE
                existing.deletedAt = null
            }

            existing
        }
    }
}