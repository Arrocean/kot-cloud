package com.arrocean.dev.module.system.application.user.core.command

import com.arrocean.dev.framework.common.util.web.WebUtils
import com.arrocean.dev.module.system.adapter.web.admin.user.UpdateUserRequest
import java.net.InetAddress
import java.time.Instant

data class UpdateUserCommand(
    val id: Long,
    val name: String
) {
    companion object {
        fun fromRequest(id: Long, request: UpdateUserRequest): UpdateUserCommand {
            return UpdateUserCommand(
                id = id,
                name = request.username ?: ""
            )
        }
    }
}

data class LoginUserCommand(
    val id: Long,
    val loginIp: InetAddress?,
    val loginTime: Instant
) {
    companion object {
        fun fromRequest(id: Long): LoginUserCommand {
            return LoginUserCommand(
                id = id,
                loginIp = WebUtils.getClientIP(),
                loginTime = Instant.now()
            )
        }
    }
}
