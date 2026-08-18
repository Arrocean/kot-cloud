package com.arrocean.dev.framework.serde

import com.arrocean.dev.framework.common.poko.CommonResult
import com.arrocean.dev.framework.common.poko.PageParam
import com.arrocean.dev.framework.common.poko.PageResult
import io.micronaut.context.annotation.Requires
import io.micronaut.serde.annotation.SerdeImport
import jakarta.inject.Singleton

/**
 * Micronaut Serde 编译期序列化注册
 *
 * 设计原则：
 * 1. 只注册“通用 Web 协议壳子类”
 * 2. 不注册具体业务 Response / Request
 * 3. 不污染 kot-common-model
 *
 * 未来新增规则：
 * - 只有“跨模块、跨服务都会用到的 HTTP 返回封装类”才能加进来
 *
 * @author WhiteSprite
 */
@Singleton
@Requires(classes = [CommonResult::class])
@SerdeImport(CommonResult::class)
@SerdeImport(PageParam::class)
@SerdeImport(PageResult::class)
class SerdeImportsConfig