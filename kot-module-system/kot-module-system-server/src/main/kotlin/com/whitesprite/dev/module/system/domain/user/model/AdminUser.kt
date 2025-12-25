package com.whitesprite.dev.module.system.domain.user.model

import com.whitesprite.dev.module.system.infrastructure.persistence.entity.user.AdminUserEntity
import java.time.LocalDateTime

data class AdminUser(
    val id: Long,
    val username: String,
    val password: String,
    val nickname: String,
    val remark: String,
    val creator: String,
    val createTime: LocalDateTime,
    val updater: String,
    val updateTime: LocalDateTime,
    val deleted: Boolean
) {
    fun toEntity(): AdminUserEntity {
        return AdminUserEntity(
            id = id,
            username = username,
            password = password,
            nickname = nickname,
            remark = remark,
            creator = creator,
            createTime = createTime,
            updater = updater,
            updateTime = updateTime,
            deleted = deleted
        )
    }
}