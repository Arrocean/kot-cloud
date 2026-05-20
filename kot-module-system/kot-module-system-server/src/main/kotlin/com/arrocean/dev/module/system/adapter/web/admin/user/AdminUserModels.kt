package com.arrocean.dev.module.system.adapter.web.admin.user

import com.arrocean.dev.framework.common.poko.PageParam
import com.arrocean.dev.framework.common.validation.PageSizeOrNoPage
import io.micronaut.serde.annotation.Serdeable
import jakarta.validation.constraints.Min

/**
 * 创建用户请求
 *
 * @property username 用户名
 * @property password 密码
 *
 * @author WhiteSprite
 */
@Serdeable
data class CreateUserRequest(
    val username: String,
    val password: String

)

/**
 * 更新用户请求
 *
 * @property id 用户 ID
 * @property username 用户名
 * @property nickname 昵称
 *
 * @author WhiteSprite
 */
@Serdeable
data class UpdateUserRequest(
    var id: Long,
    val username: String?,
    val nickname: String?,
)

/**
 * 获取用户响应
 *
 * @property id 用户 ID
 * @property username 用户名
 * @property nickname 昵称
 * @property remark 备注
 * @property createTime 创建时间
 * @property updateTime 更新时间
 *
 * @author WhiteSprite
 */
@Serdeable
data class GetAdminUserResponse(
    val id: Long,
    val username: String,
    val nickname: String,
    val remark: String,
    val createTime: Long,
    val updateTime: Long
)

/**
 * 获取分页用户列表
 */
@Serdeable
data class PageAdminUserRequest(
    @field:Min(1)
    val pageNo: Int = PageParam.DEFAULT_PAGE_NO,

    @field:PageSizeOrNoPage(max = PageParam.MAX_PAGE_SIZE)
    val pageSize: Int = PageParam.DEFAULT_PAGE_SIZE,

    val keyword: String?,
) {
    fun toPageParam(): PageParam = PageParam(pageNo = pageNo, pageSize = pageSize)
}
