package com.arrocean.dev.framework.common.listener

import com.arrocean.dev.framework.common.core.BaseEntity
import com.arrocean.dev.framework.common.core.TenantBaseEntity
import com.arrocean.dev.framework.security.core.context.CurrentLoginUserProvider
import io.micronaut.data.event.listeners.PrePersistEventListener
import io.micronaut.data.event.listeners.PreUpdateEventListener
import io.micronaut.data.model.runtime.RuntimePersistentEntity
import jakarta.inject.Singleton

@Singleton
class AuditEntityListener(
    private val currentLoginUserProvider: CurrentLoginUserProvider,
) : PrePersistEventListener<BaseEntity>, PreUpdateEventListener<BaseEntity> {

    override fun prePersist(entity: BaseEntity): Boolean {
        val loginUser = currentLoginUserProvider.getLoginUserOrNull()
        entity.creatorId = entity.creatorId ?: loginUser?.id
        entity.updaterId = entity.updaterId ?: loginUser?.id

        if (entity is TenantBaseEntity && entity.tenantId == 0L) {
            entity.tenantId = loginUser?.tenantId
                ?: error("保存实体时缺少 tenantId 上下文")
        }
        return true
    }

    override fun supports(
        entity: RuntimePersistentEntity<BaseEntity>,
        eventType: Class<out Annotation>
    ): Boolean = true

    override fun preUpdate(entity: BaseEntity): Boolean {
        val loginUser = currentLoginUserProvider.getLoginUserOrNull()
        entity.updaterId = loginUser?.id
        return true
    }

}
