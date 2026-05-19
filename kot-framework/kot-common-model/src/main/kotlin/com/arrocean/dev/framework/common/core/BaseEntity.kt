package com.arrocean.dev.framework.common.core

import java.time.Instant

/**
 * 基础 DO（对齐 yudao BaseDO）
 *
 * 说明：
 * - 不在这里做任何 now() 默认值，避免同一请求路径出现多处取时间
 * - creator/updater/tenantId 建议由「持久化前回调/拦截器」统一填充
 * - deleted 建议 DB 默认 false；这里也给默认 false 兜底
 */
open class BaseEntity {

    open var createTime: Instant? = null

    open var updateTime: Instant? = null

    /**
     * 创建者
     */
    open var creatorId: Long? = null

    open var updaterId: Long? = null

    /**
     * 逻辑删除标记
     * - yudao 用 @TableLogic；Micronaut Data JDBC 没有等价“自动拼接”机制
     * - 建议：repository 查询时统一加 deleted=false，或用视图/约束封装
     */
    open var deleted: Boolean = false

    /**
     * 对齐 yudao：清空审计字段，防止前端透传覆盖
     *
     * 注意：这通常应在 Adapter 层/DTO 层完成“白名单字段映射”，
     * 但保留该方法可以作为额外安全兜底。
     */
    open fun clean() {
        creatorId = null
        createTime = null
        updaterId = null
        updateTime = null
    }
}

/**
 * 拓展多租户的 BaseDO（对齐 yudao TenantBaseDO）
 *
 * 说明：
 * - tenantId 建议非空（因为你的规则是从 Header 强制拿）
 * - 但 Kotlin 必须初始化：这里用 0L 作为占位值，保存前务必由拦截器/回调改成真实 tenantId，
 *   否则应直接抛异常拒绝落库。
 */
open class TenantBaseEntity : BaseEntity() {

    open var tenantId: Long = 0L
}
