package com.arrocean.dev.module.system.adapter.web.admin.user

import com.arrocean.dev.framework.common.poko.PageParam
import com.arrocean.dev.framework.common.validation.PageSizeOrNoPage
import io.micronaut.serde.annotation.Serdeable
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Min

/**
 * 创建用户请求
 *
 * @property username 用户名
 * @property password 密码
 *
 * @author WhiteSprite
 */
@Schema(description = "创建用户请求")
@Serdeable
data class CreateUserRequest(
    @field:Schema(description = "用户名", example = "newuser")
    val username: String,
    @field:Schema(description = "密码", example = "123456")
    val password: String,
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
@Schema(description = "更新用户请求")
@Serdeable
data class UpdateUserRequest(
    @field:Schema(description = "用户 ID", example = "1")
    var id: Long,
    @field:Schema(description = "用户名", example = "admin")
    val username: String?,
    @field:Schema(description = "昵称", example = "管理员")
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
@Schema(description = "管理员用户信息")
@Serdeable
data class GetAdminUserResponse(
    @field:Schema(description = "用户 ID", example = "1")
    val id: Long,
    @field:Schema(description = "用户名", example = "admin")
    val username: String,
    @field:Schema(description = "昵称", example = "管理员")
    val nickname: String,
    @field:Schema(description = "备注")
    val remark: String,
    @field:Schema(description = "创建时间（时间戳）")
    val createTime: Long,
    @field:Schema(description = "更新时间（时间戳）")
    val updateTime: Long,
)

/**
 * 获取分页用户列表
 */
@Schema(description = "分页查询用户请求")
@Serdeable
data class PageAdminUserRequest(
    @field:Min(1)
    @field:Schema(description = "页码", example = "1")
    val pageNo: Int = PageParam.DEFAULT_PAGE_NO,

    @field:PageSizeOrNoPage(max = PageParam.MAX_PAGE_SIZE)
    @field:Schema(description = "每页条数", example = "10")
    val pageSize: Int = PageParam.DEFAULT_PAGE_SIZE,

    @field:Schema(description = "关键字搜索（匹配用户名/昵称）")
    val keyword: String?,
) {
    fun toPageParam(): PageParam = PageParam(pageNo = pageNo, pageSize = pageSize)
}
