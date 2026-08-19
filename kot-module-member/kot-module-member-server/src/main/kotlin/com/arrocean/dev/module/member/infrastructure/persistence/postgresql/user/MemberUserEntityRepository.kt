package com.arrocean.dev.module.member.infrastructure.persistence.postgresql.user

import com.arrocean.dev.module.member.infrastructure.persistence.entity.user.MemberUserEntity
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.repository.CrudRepository
import java.util.Optional

@JdbcRepository(dialect = Dialect.POSTGRES)
interface MemberUserEntityRepository : CrudRepository<MemberUserEntity, Long> {
    fun findByUsernameAndDeletedFalse(username: String): Optional<MemberUserEntity>

    fun findByMobileAndDeletedFalse(mobile: String): Optional<MemberUserEntity>

    fun findByIdAndDeletedFalse(id: Long): Optional<MemberUserEntity>
}
