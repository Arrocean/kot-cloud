package com.arrocean.dev.module.member.infrastructure.persistence.entity.user

import com.arrocean.dev.framework.common.core.MDTenantBaseEntity
import com.arrocean.dev.framework.postgresql.convert.PostgreSqlInetColumn
import io.micronaut.data.annotation.GeneratedValue
import io.micronaut.data.annotation.Id
import io.micronaut.data.annotation.MappedEntity
import java.net.InetAddress
import java.time.Instant

@MappedEntity("member_users")
data class MemberUserEntity(
    @field:Id
    @field:GeneratedValue(GeneratedValue.Type.AUTO)
    var id: Long? = null,
    var username: String,
    var mobile: String? = null,
    var passwordHash: String,
    var nickname: String,
    var email: String? = null,
    var avatarUrl: String? = null,
    var status: Short = 1,
    @field:PostgreSqlInetColumn
    var loginIp: InetAddress? = null,
    var loginTime: Instant? = null,
) : MDTenantBaseEntity()
