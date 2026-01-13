package com.auction.auction.user.repo

import com.auction.auction.user.entity.UserInfo
import org.springframework.data.jpa.repository.JpaRepository


interface UserInfoRepository : JpaRepository<UserInfo, String> {

    fun findByLoginId(loginId: String): UserInfo?
}