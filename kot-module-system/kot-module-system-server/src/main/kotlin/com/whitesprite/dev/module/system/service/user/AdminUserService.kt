package com.whitesprite.dev.module.system.service.user

import com.whitesprite.dev.framework.common.poko.PageResult
import com.whitesprite.dev.module.system.api.user.dto.AdminUserDTO
import com.whitesprite.dev.module.system.controller.admin.user.vo.CreateUserRequestVO
import com.whitesprite.dev.module.system.controller.admin.user.vo.UpdateUserRequestVO

/**
 * 用户领域服务（契约）
 *
 * 脚手架建议：对外暴露 interface，便于未来替换实现（不同数据源/不同租户/Mock/灰度等）。
 */
interface AdminUserService {

    /**
     * 创建用户
     * @param req 创建用户请求
     * @return 创建成功的用户 ID
     */
    fun create(req: CreateUserRequestVO): Long

    /**
     * 删除用户
     * @param id 用户 ID
     */
    fun delete(id: Long)

    /**
     * 全量更新用户
     * @param id 用户 ID
     * @param req 更新用户请求
     * @return 更新成功的用户信息
     */
    fun update(id: Long, req: UpdateUserRequestVO): AdminUserDTO

    /**
     * 根据 ID 查询用户
     * @param id 用户 ID
     * @return 用户信息
     */
    fun getById(id: Long): AdminUserDTO?

    /**
     * 列表查询用户
     * @param keyword 关键字
     * @return 用户列表
     */
    fun list(keyword: String = ""): List<AdminUserDTO>

    /**
     * 分页查询用户
     * @param page 页码
     * @param size 页大小
     * @param keyword 关键字
     * @return 用户列表
     */
    fun page(page: Int, size: Int, keyword: String = ""): PageResult<AdminUserDTO>
}