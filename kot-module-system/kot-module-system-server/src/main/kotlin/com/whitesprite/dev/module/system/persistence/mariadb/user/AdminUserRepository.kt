package com.whitesprite.dev.module.system.persistence.mariadb.user

import com.whitesprite.dev.module.system.persistence.entity.user.AdminUserEntity
import io.micronaut.data.annotation.Repository
import io.micronaut.data.repository.CrudRepository

@Repository
interface AdminUserRepository : CrudRepository<AdminUserEntity, Long> {

    fun existsByUsername(name: String): Boolean

    fun findByUsername(name: String): List<AdminUserEntity>

    fun findByNicknameIlike(name: String) : List<AdminUserEntity>
}