package com.arrocean.dev.module.system.infrastructure.persistence.repositoryimpl.postgresql.rbac

import com.arrocean.dev.module.system.domain.rbac.model.Menu
import com.arrocean.dev.module.system.domain.rbac.model.MenuDraft
import com.arrocean.dev.module.system.domain.rbac.model.Permission
import com.arrocean.dev.module.system.domain.rbac.model.Role
import com.arrocean.dev.module.system.domain.rbac.model.RoleDraft
import com.arrocean.dev.module.system.domain.rbac.repository.MenuRepository
import com.arrocean.dev.module.system.domain.rbac.repository.PermissionRepository
import com.arrocean.dev.module.system.domain.rbac.repository.RolePermissionRepository
import com.arrocean.dev.module.system.domain.rbac.repository.RoleRepository
import com.arrocean.dev.module.system.domain.rbac.repository.UserRoleRepository
import com.arrocean.dev.module.system.infrastructure.persistence.entity.rbac.RolePermissionEntity
import com.arrocean.dev.module.system.infrastructure.persistence.entity.rbac.UserRoleEntity
import com.arrocean.dev.module.system.infrastructure.persistence.mapper.rbac.RbacPersistenceMapper
import com.arrocean.dev.module.system.infrastructure.persistence.postgresql.rbac.MenuEntityRepository
import com.arrocean.dev.module.system.infrastructure.persistence.postgresql.rbac.PermissionEntityRepository
import com.arrocean.dev.module.system.infrastructure.persistence.postgresql.rbac.RoleEntityRepository
import com.arrocean.dev.module.system.infrastructure.persistence.postgresql.rbac.RolePermissionEntityRepository
import com.arrocean.dev.module.system.infrastructure.persistence.postgresql.rbac.UserRoleEntityRepository
import jakarta.inject.Singleton

/** PostgreSQL 角色领域仓储实现。 */
@Singleton
class RoleRepositoryPgImpl(
    private val entityRepository: RoleEntityRepository,
) : RoleRepository {
    override suspend fun findById(id: Long): Role? = entityRepository.findById(id)
        ?.let(RbacPersistenceMapper::toDomain)

    override suspend fun findByCode(code: String, tenantId: Long): Role? =
        entityRepository.findByCodeAndTenantId(code, tenantId)
            ?.let(RbacPersistenceMapper::toDomain)

    override suspend fun findByIds(ids: Collection<Long>): List<Role> = if (ids.isEmpty()) emptyList() else {
        entityRepository.findByIdInList(ids).map(RbacPersistenceMapper::toDomain)
    }

    override suspend fun findAll(tenantId: Long): List<Role> = entityRepository.findByTenantIdOrderByCodeAsc(tenantId)
        .map(RbacPersistenceMapper::toDomain)

    override suspend fun save(draft: RoleDraft): Role = RbacPersistenceMapper.toDomain(
        entityRepository.save(RbacPersistenceMapper.toEntity(draft)),
    )

    override suspend fun save(role: Role): Role = RbacPersistenceMapper.toDomain(
        entityRepository.save(RbacPersistenceMapper.toEntity(role)),
    )

    override suspend fun deleteById(id: Long) {
        entityRepository.deleteById(id)
    }
}

/** PostgreSQL 权限领域仓储实现。 */
@Singleton
class PermissionRepositoryPgImpl(
    private val entityRepository: PermissionEntityRepository,
) : PermissionRepository {
    override suspend fun findAll(tenantId: Long): List<Permission> =
        entityRepository.findByTenantIdOrderByCodeAsc(tenantId)
            .map(RbacPersistenceMapper::toDomain)

    override suspend fun findByIds(ids: Collection<Long>): List<Permission> = if (ids.isEmpty()) emptyList() else {
        entityRepository.findByIdInList(ids).map(RbacPersistenceMapper::toDomain)
    }

    override suspend fun findEffectiveByUserId(userId: Long, tenantId: Long): List<Permission> =
        entityRepository.findEffectiveByUserId(userId, tenantId).map(RbacPersistenceMapper::toDomain)
}

/** PostgreSQL 菜单领域仓储实现。 */
@Singleton
class MenuRepositoryPgImpl(
    private val entityRepository: MenuEntityRepository,
) : MenuRepository {
    override suspend fun findAll(tenantId: Long): List<Menu> = entityRepository.findAllActiveByTenantId(tenantId)
        .map(RbacPersistenceMapper::toDomain)

    override suspend fun findByCode(code: String, tenantId: Long): Menu? =
        entityRepository.findByCodeAndTenantId(code, tenantId)
            ?.let(RbacPersistenceMapper::toDomain)

    override suspend fun save(draft: MenuDraft): Menu = RbacPersistenceMapper.toDomain(
        entityRepository.save(RbacPersistenceMapper.toEntity(draft)),
    )

    override suspend fun save(menu: Menu): Menu = RbacPersistenceMapper.toDomain(
        entityRepository.save(RbacPersistenceMapper.toEntity(menu)),
    )

    override suspend fun deleteById(id: Long) {
        entityRepository.deleteById(id)
    }
}

/** PostgreSQL 用户角色关联领域仓储实现。 */
@Singleton
class UserRoleRepositoryPgImpl(
    private val entityRepository: UserRoleEntityRepository,
) : UserRoleRepository {
    override suspend fun findRoleIdsByUserId(userId: Long, tenantId: Long): Set<Long> =
        entityRepository.findByUserIdAndTenantId(userId, tenantId).map { it.roleId }.toSet()

    override suspend fun findUserIdsByRoleId(roleId: Long, tenantId: Long): Set<Long> =
        entityRepository.findByRoleIdAndTenantId(roleId, tenantId).map { it.userId }.toSet()

    override suspend fun replaceRoles(userId: Long, roleIds: Set<Long>, tenantId: Long) {
        entityRepository.deleteByUserIdAndTenantId(userId, tenantId)
        if (roleIds.isNotEmpty()) {
            entityRepository.saveAll(roleIds.map { UserRoleEntity(userId = userId, roleId = it, tenantId = tenantId) })
        }
    }

    override suspend fun addRole(userId: Long, roleId: Long, tenantId: Long) {
        if (roleId !in findRoleIdsByUserId(userId, tenantId)) {
            entityRepository.save(UserRoleEntity(userId = userId, roleId = roleId, tenantId = tenantId))
        }
    }
}

/** PostgreSQL 角色权限关联领域仓储实现。 */
@Singleton
class RolePermissionRepositoryPgImpl(
    private val entityRepository: RolePermissionEntityRepository,
) : RolePermissionRepository {
    override suspend fun replacePermissions(roleId: Long, permissionIds: Set<Long>, tenantId: Long) {
        entityRepository.deleteByRoleIdAndTenantId(roleId, tenantId)
        if (permissionIds.isNotEmpty()) {
            entityRepository.saveAll(
                permissionIds.map { RolePermissionEntity(roleId = roleId, permissionId = it, tenantId = tenantId) },
            )
        }
    }
}
