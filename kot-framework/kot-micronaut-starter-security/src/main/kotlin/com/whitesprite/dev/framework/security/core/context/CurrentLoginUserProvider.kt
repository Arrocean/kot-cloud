package com.whitesprite.dev.framework.security.core.context

import com.whitesprite.dev.framework.security.core.token.TokenService
import io.micronaut.security.utils.SecurityService
import jakarta.inject.Singleton

/**
 * 当前登录用户提供器。
 *
 * @author WhiteSprite
 */
interface CurrentLoginUserProvider {

    /**
     * 获取当前登录用户。
     *
     * @return 登录用户；未登录或上下文不完整时返回 null
     */
    fun getLoginUserOrNull(): LoginUser?

    /**
     * 获取当前登录用户；若未登录则抛出异常。
     *
     * @return 登录用户
     */
    fun requireLoginUser(): LoginUser {
        return getLoginUserOrNull() ?: throw IllegalStateException("当前未登录或登录上下文无效")
    }
}

/**
 * 当前登录用户提供器默认实现。
 *
 * 说明：
 * - 第一阶段仅处理标准 HTTP 请求上下文
 * - 暂不处理协程/异步链路中的上下文透传
 *
 * @author WhiteSprite
 */
@Singleton
open class DefaultCurrentLoginUserProvider(
    private val securityService: SecurityService,
    private val tokenService: TokenService
) : CurrentLoginUserProvider {

    override fun getLoginUserOrNull(): LoginUser? {
        val authentication = securityService.authentication.orElse(null) ?: return null
        return tokenService.toLoginUser(authentication)
    }

    // TODO WhiteSprite：后续补充协程 / 异步任务场景下的身份上下文透传方案
}


