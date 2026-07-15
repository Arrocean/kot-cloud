package com.arrocean.dev.module.system.application.rbac.core.authorization

import com.arrocean.dev.framework.common.exception.constants.GlobalErrorCodeConstants
import com.arrocean.dev.framework.common.exception.util.ServiceExceptionFactory
import com.arrocean.dev.module.system.domain.rbac.model.Menu
import com.arrocean.dev.module.system.domain.rbac.model.UserAuthorization
import com.arrocean.dev.module.system.domain.rbac.repository.MenuRepository
import com.arrocean.dev.module.system.domain.rbac.repository.PermissionRepository
import com.arrocean.dev.module.system.domain.rbac.repository.RoleRepository
import com.arrocean.dev.module.system.domain.rbac.repository.UserRoleRepository
import io.micronaut.serde.annotation.Serdeable
import jakarta.inject.Singleton

/**
 * RBAC 授权处理器，聚合用户角色、功能权限并构造可见菜单树。
 *
 * 当前阶段直接读取持久化数据，因此角色或权限变更会在下一请求立即生效。缓存应在安全框架提供
 * Redis 抽象后接入，避免业务模块直接依赖 Lettuce。
 *
 * @author WhiteSprite
 */
@Singleton
open class RbacAuthorizationHandler(
    private val userRoleRepository: UserRoleRepository,
    private val roleRepository: RoleRepository,
    private val permissionRepository: PermissionRepository,
    private val menuRepository: MenuRepository,
) {
    /**
     * 获取用户当前有效授权信息。
     *
     * @param userId 用户 ID
     * @param tenantId 租户 ID
     * @return 有效角色与功能权限集合
     */
    open fun getAuthorization(userId: Long, tenantId: Long): UserAuthorization {
        return loadAuthorization(userId, tenantId)
    }

    /**
     * 校验用户是否具备所需功能权限。
     *
     * @param userId 用户 ID
     * @param tenantId 租户 ID
     * @param required 所需权限码集合
     * @param requireAll 为 true 时要求全部满足；为 false 时任一满足即可
     */
    open fun requirePermissions(userId: Long, tenantId: Long, required: Set<String>, requireAll: Boolean) {
        if (required.isEmpty()) return
        val authorization = getAuthorization(userId, tenantId)
        if (authorization.isSuperAdmin) return
        val allowed = if (requireAll) required.all(authorization.permissionCodes::contains) else required.any(authorization.permissionCodes::contains)
        if (!allowed) {
            throw ServiceExceptionFactory.exception(GlobalErrorCodeConstants.FORBIDDEN)
        }
    }

    /**
     * 获取按有效功能权限过滤后的可见菜单树。
     *
     * @param userId 用户 ID
     * @param tenantId 租户 ID
     * @return 可见菜单树
     */
    open fun getMenus(userId: Long, tenantId: Long): List<MenuNode> {
        val authorization = getAuthorization(userId, tenantId)
        val allowed = authorization.permissionCodes
        val menus = menuRepository.findAll(tenantId)
            .asSequence()
            .filter { it.visible }
            .filter { authorization.isSuperAdmin || it.permissionCode.isNullOrBlank() || it.permissionCode in allowed }
            .toList()
        return toTree(menus)
    }

    /**
     * 从角色与权限关联加载有效授权信息。
     *
     * @param userId 用户 ID
     * @param tenantId 租户 ID
     * @return 授权聚合结果
     */
    private fun loadAuthorization(userId: Long, tenantId: Long): UserAuthorization {
        val roleIds = userRoleRepository.findRoleIdsByUserId(userId, tenantId)
        val roles = roleRepository.findByIds(roleIds).filter { it.tenantId == tenantId && it.status == 0.toShort() }
        val roleCodes = roles.mapTo(linkedSetOf()) { it.code }
        if (UserAuthorization.SUPER_ADMIN_ROLE_CODE in roleCodes) {
            return UserAuthorization(roleCodes, emptySet())
        }
        val permissionCodes = permissionRepository.findEffectiveByUserId(userId, tenantId)
            .mapTo(linkedSetOf()) { it.code }
        return UserAuthorization(roleCodes, permissionCodes)
    }

    /**
     * 将扁平菜单集合转换为父子菜单树。
     *
     * 父菜单因权限过滤而不可见时，子菜单提升为根节点，避免错误丢失仍可访问的功能入口。
     *
     * @param menus 已过滤菜单集合
     * @return 排序后的菜单树
     */
    private fun toTree(menus: List<Menu>): List<MenuNode> {
        val nodes = menus.associate { menu ->
            menu.code to MenuNode(
                code = menu.code,
                title = menu.title,
                type = menu.type.name,
                path = menu.path,
                icon = menu.icon,
                sort = menu.sort,
                permissionCode = menu.permissionCode,
            )
        }
        val roots = mutableListOf<MenuNode>()
        menus.forEach { menu ->
            val node = nodes.getValue(menu.code)
            val parent = menu.parentCode?.let(nodes::get)
            if (parent == null) roots += node else parent.children += node
        }
        fun sort(nodes: List<MenuNode>) {
            nodes.sortedWith(compareBy<MenuNode> { it.sort }.thenBy { it.code }).forEach { sort(it.children) }
        }
        sort(roots)
        return roots.sortedWith(compareBy<MenuNode> { it.sort }.thenBy { it.code })
    }
}

/**
 * 面向前端的菜单树节点。
 *
 * @property code 稳定菜单编码
 * @property title 显示名称
 * @property type 菜单类型
 * @property path 路由或外部 URL
 * @property icon 前端图标标识
 * @property sort 排序号
 * @property permissionCode 进入菜单所需权限码
 * @property children 子菜单节点
 */
@Serdeable
data class MenuNode(
    val code: String,
    val title: String,
    val type: String,
    val path: String?,
    val icon: String?,
    val sort: Int,
    val permissionCode: String?,
    val children: MutableList<MenuNode> = mutableListOf(),
)
