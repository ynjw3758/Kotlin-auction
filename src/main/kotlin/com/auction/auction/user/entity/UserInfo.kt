package com.auction.auction.user.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table


@Entity
@Table(name = "userinfo")
class UserInfo (

    @Id
    @Column(name = "connectid")
    var connectId: String = "",

    @Column(name = "id", nullable = false)
    var loginId: String = "",

    @Column(name = "password", nullable = false)
    var passwordHash: String = ""
    )