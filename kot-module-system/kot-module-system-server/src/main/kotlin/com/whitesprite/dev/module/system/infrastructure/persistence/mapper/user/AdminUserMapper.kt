package com.whitesprite.dev.module.system.infrastructure.persistence.mapper.user

import com.whitesprite.dev.module.system.api.user.dto.AdminUserDto
import com.whitesprite.dev.module.system.domain.user.model.AdminUser
import com.whitesprite.dev.module.system.domain.user.model.AdminUserDraft
import com.whitesprite.dev.module.system.infrastructure.persistence.entity.user.AdminUserEntity

/**
 * 系统用户 映射器
 * 用途 => ( Entity <-> Domain )/( Entity <-> 投影模型)
 *
 * @author WhiteSprite
 */
object AdminUserMapper {
    fun toDTO(entity: AdminUserEntity): AdminUserDto {
        val id = requireNotNull(entity.id) {
            "AdminUserEntity.id 为空, 实例=$entity"
        }

        val createTime = requireNotNull(entity.createTime) {
            "AdminUserEntity.createTime 为空, id=$id"
        }

        val updateTime = requireNotNull(entity.updateTime) {
            "AdminUserEntity.updateTime 为空, id=$id"
        }

        return AdminUserDto(
            id = id,
            username = entity.username,
            passwordHash = entity.passwordHash,
            nickname = entity.nickname,
            deptId = entity.deptId,
            email = entity.email,
            mobile = entity.mobile,
            gender = entity.gender,
            status = entity.status,
            avatarUrl = entity.avatarUrl,
            loginIp = entity.loginIp,
            remark = entity.remark,

            creatorId = entity.creatorId,
            createTime = createTime,
            updaterId = entity.updaterId,
            updateTime = updateTime,

            deleted = entity.deleted
        )
    }

    /**
     * AdminUser 实体模型转为领域模型
     * Domain -> DTO
     * @param entity AdminUserEntity 实体模型
     * @return 领域模型
     */
    fun toDomain(entity: AdminUserEntity): AdminUser {
        val id = requireNotNull(entity.id) {
            "AdminUserEntity.id 为空, 实例=$entity"
        }

        val createTime = requireNotNull(entity.createTime) {
            "AdminUserEntity.createTime 为空, id=$id"
        }

        val updateTime = requireNotNull(entity.updateTime) {
            "AdminUserEntity.updateTime 为空, id=$id"
        }
        return AdminUser(
            id = id,
            username = entity.username,
            passwordHash = entity.passwordHash,
            nickname = entity.nickname,
            deptId = entity.deptId,
            email = entity.email,
            mobile = entity.mobile,
            gender = entity.gender,
            status = entity.status,
            avatarUrl = entity.avatarUrl,
            loginIp = entity.loginIp,
            remark = entity.remark,
            creatorId = entity.creatorId,
            createTime = createTime,
            updaterId = entity.updaterId,
            updateTime = updateTime,
            deleted = entity.deleted,
        )
    }

    /**
     * AdminUser 领域模型转为实体模型
     * Domain -> Entity
     *
     * @param domain AdminUser 领域模型
     * @return 实体模型
     */
    fun toEntity(domain: AdminUser): AdminUserEntity {

        val createTime = requireNotNull(domain.createTime) {
            "AdminUser.createTime 为空, id=$domain.id"
        }

        val updateTime = requireNotNull(domain.updateTime) {
            "AdminUser.updateTime 为空, id=$domain.id"
        }
        return AdminUserEntity(
            id = domain.id,
            username = domain.username,
            passwordHash = domain.passwordHash,
            nickname = domain.nickname,
            deptId = domain.deptId,
            email = domain.email,
            mobile = domain.mobile,
            gender = domain.gender,
            status = domain.status,
            avatarUrl = domain.avatarUrl,
            loginIp = domain.loginIp,
            remark = domain.remark
        ).apply {
            // 注意：这些是 TenantBaseEntity 的属性，不是构造器参数
            this.creatorId = domain.creatorId
            this.createTime = createTime
            this.updaterId = domain.updaterId
            this.updateTime = updateTime
            this.deleted = domain.deleted
            this.tenantId = domain.tenantId
        }
    }

    /**
     * AdminUserDraft 领域模型转为实体模型
     * DraftDomain -> Entity
     *
     * @param domain AdminUserDraft 领域模型
     * @return 实体模型
     */
    fun toEntity(domain: AdminUserDraft): AdminUserEntity {
        return AdminUserEntity(
            username = domain.username,
            passwordHash = domain.passwordHash,
            nickname = domain.nickname,
            deptId = domain.deptId,
            email = domain.email,
            mobile = domain.mobile,
            gender = domain.gender,
            status = domain.status,
            avatarUrl = domain.avatarUrl,
            loginIp = domain.loginIp,
            remark = domain.remark
        ).apply {
            // 注意：这些是 TenantBaseEntity 的属性，不是构造器参数
            this.creatorId = domain.creatorId
            this.updaterId = domain.creatorId
            this.tenantId = domain.tenantId
        }
    }
}