package com.arrocean.dev.framework.common.core

import io.micronaut.data.annotation.DateCreated
import io.micronaut.data.annotation.DateUpdated
import io.micronaut.data.annotation.TenantId
import java.time.Instant

class MDBaseEntity : BaseEntity() {
    @DateCreated
    override var createTime: Instant? = null

    @DateUpdated
    override var updateTime: Instant? = null
}

open class MDTenantBaseEntity : TenantBaseEntity() {
    @DateCreated
    override var createTime: Instant? = null

    @DateUpdated
    override var updateTime: Instant? = null

//    @TenantId
    override var tenantId: Long = 0L
}
