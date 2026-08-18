package com.arrocean.dev.module.system.adapter.security

import com.arrocean.dev.framework.security.core.context.CurrentLoginUserProvider
import com.arrocean.dev.module.system.application.rbac.core.authorization.RbacAuthorizationHandler
import io.micronaut.aop.Around
import io.micronaut.aop.InterceptorBean
import io.micronaut.aop.MethodInterceptor
import io.micronaut.aop.MethodInvocationContext
import jakarta.inject.Singleton
import kotlinx.coroutines.runBlocking

/**
 * 声明 Controller 或方法所需的功能权限。
 *
 * @property value 所需权限码
 * @property requireAll 是否要求全部权限均满足
 */
@Around
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class RequirePermission(
    vararg val value: String,
    val requireAll: Boolean = true,
)

/**
 * `RequirePermission` 的 AOP 授权拦截器。
 *
 * 说明：Micronaut AOP 的拦截方法为同步执行，而 RBAC 校验依赖 R2DBC 协程仓储，
 * 因此此处通过 [runBlocking] 桥接，在权限校验完成前不继续执行目标方法。
 *
 * @author WhiteSprite
 */
@Singleton
@InterceptorBean(RequirePermission::class)
class RequirePermissionInterceptor(
    private val currentLoginUserProvider: CurrentLoginUserProvider,
    private val authorizationHandler: RbacAuthorizationHandler,
) : MethodInterceptor<Any, Any> {
    /**
     * 在调用目标方法前校验当前登录用户的功能权限。
     *
     * @param context 被拦截方法上下文
     * @return 目标方法执行结果
     */
    override fun intercept(context: MethodInvocationContext<Any, Any>): Any? {
        val annotation = requireNotNull(context.getAnnotation(RequirePermission::class.java)) {
            "RequirePermissionInterceptor 缺少 RequirePermission 注解"
        }
        val loginUser = currentLoginUserProvider.requireLoginUser()
        runBlocking {
            authorizationHandler.requirePermissions(
                userId = loginUser.id,
                tenantId = loginUser.tenantId,
                required = annotation.stringValues("value").toSet(),
                requireAll = annotation.booleanValue("requireAll").orElse(true),
            )
        }
        return context.proceed()
    }
}
