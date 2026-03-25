package com.whitesprite.dev.module.system.adapter.web.admin.auth

import com.whitesprite.dev.framework.common.http.ApiPrefix
import io.micronaut.http.annotation.Controller
import io.micronaut.validation.Validated

/**
 * 权限控制器
 *
 * @author WhiteSprite
 */
@Validated
@Controller(ApiPrefix.ADMIN + "/system/auth")
open class AdminAuthController(
    /**
     *
     */
) {

}