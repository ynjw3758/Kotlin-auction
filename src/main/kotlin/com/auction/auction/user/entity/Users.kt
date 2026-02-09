package com.auction.auction.user.entity

import jakarta.persistence.*
import java.time.OffsetDateTime
import java.util.UUID
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes

@Entity
@Table(
    name = "users",
    uniqueConstraints = [
        UniqueConstraint(name = "uk_users_keycloak_sub", columnNames = ["keycloak_sub"])
    ],
    indexes = [
        Index(name = "ix_users_nickname_normalized", columnList = "nickname_normalized"),
        Index(name = "ix_users_nickname_chosung", columnList = "nickname_chosung"),
        Index(name = "ix_users_nickname_jamo", columnList = "nickname_jamo"),
        Index(name = "ix_users_status", columnList = "status")
    ]
)
class Users(
    @Id
    @Column(name = "id", columnDefinition = "uuid")
    val id: UUID = UUID.randomUUID(),

    @Column(name = "keycloak_sub", nullable = false, length = 64)
    val keycloakSub: String,

    @Column(name = "login_id", nullable = false, length = 50)
    val loginId: String,

    @Column(name = "nickname", nullable = false, length = 30)
    var nickname: String,

    @Column(name = "nickname_normalized", length = 60)
    var nicknameNormalized: String? = null,

    @Column(name = "nickname_chosung", length = 60)
    var nicknameChosung: String? = null,

    @Column(name = "nickname_jamo", length = 120)
    var nicknameJamo: String? = null,

    @Column(name = "name", length = 50)
    var name: String? = null,

    @Column(name = "phone", length = 20)
    var phone: String? = null,

    // ✅ Postgres enum(user_status)로 바인딩
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, columnDefinition = "user_status")
    var status: UserStatus = UserStatus.ACTIVE,

    @Column(name = "created_at", nullable = false)
    var createdAt: OffsetDateTime = OffsetDateTime.now(),

    @Column(name = "updated_at", nullable = false)
    var updatedAt: OffsetDateTime = OffsetDateTime.now(),

    @Column(name = "deleted_at")
    var deletedAt: OffsetDateTime? = null
) {
    @PrePersist
    fun prePersist() {
        val now = OffsetDateTime.now()
        createdAt = now
        updatedAt = now
    }

    @PreUpdate
    fun preUpdate() {
        updatedAt = OffsetDateTime.now()
    }

    fun softDelete() {
        status = UserStatus.DELETED
        deletedAt = OffsetDateTime.now()
    }
}

enum class UserStatus {
    ACTIVE, SUSPENDED, DELETED
}
