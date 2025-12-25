package com.whitesprite.dev.module.system.infrastructure.persistence.mapper.user

import com.whitesprite.dev.module.system.api.user.dto.AdminUserDto
import com.whitesprite.dev.module.system.domain.user.model.AdminUser
import com.whitesprite.dev.module.system.infrastructure.persistence.entity.user.AdminUserEntity

/**
 * TODO WhiteSprite：用途 => Entity <-> Domain / Entity <-> 投影模型
 */
object AdminUserMapper {
    fun toDTO(entity: AdminUserEntity): AdminUserDto = AdminUserDto(
        id = entity.id,
        username = entity.username,
        password = entity.password,
        nickname = entity.nickname,
        remark = entity.remark,
        creator = entity.creator,
        createTime = entity.createTime,
        updater = entity.updater,
        updateTime = entity.updateTime,
        deleted = entity.deleted
    )

    fun toDomain(entity: AdminUserEntity): AdminUser = AdminUser(
        id = entity.id,
        username = entity.username,
        password = entity.password,
        nickname = entity.nickname,
        remark = entity.remark,
        creator = entity.creator,
        createTime = entity.createTime,
        updater = entity.updater,
        updateTime = entity.updateTime,
        deleted = entity.deleted
    )

    fun toEntity(domain: AdminUser): AdminUserEntity = AdminUserEntity(
        id = domain.id,
        username = domain.username,
        password = domain.password,
        nickname = domain.nickname,
        remark = domain.remark,
        creator = domain.creator,
        createTime = domain.createTime,
        updater = domain.updater,
        updateTime = domain.updateTime,
        deleted = domain.deleted
    )
}