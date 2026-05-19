package com.arrocean.dev.module.system.infrastructure.persistence.postgresql.log

import com.arrocean.dev.module.system.infrastructure.persistence.entity.log.LoginLogEntity
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.repository.CrudRepository

@JdbcRepository(dialect = Dialect.POSTGRES)
interface LoginLogEntityRepository : CrudRepository<LoginLogEntity, Long>

