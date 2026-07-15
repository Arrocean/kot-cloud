package com.arrocean.dev.framework.postgresql.convert

import io.micronaut.core.convert.ConversionContext
import io.micronaut.core.convert.TypeConverter
import io.micronaut.data.annotation.MappedProperty
import io.micronaut.data.model.DataType
import io.micronaut.data.model.runtime.convert.AttributeConverter
import jakarta.inject.Singleton
import org.postgresql.util.PGobject
import java.net.InetAddress
import java.util.Optional

/**
 * PostgreSQL `inet` <-> `InetAddress` 类型转换。
 *
 * 设计约束：
 * 1. 仅处理 PostgreSQL `inet`，不把所有 `PGobject` 都粗暴视为 IP
 * 2. 让业务层/实体层可以坚持使用 `InetAddress`
 * 3. 将 PostgreSQL 驱动细节收敛在 starter-md-postgresql 中
 */
@Singleton
class PostgreSqlInetToInetAddressConverter : TypeConverter<PGobject, InetAddress> {

    override fun convert(
        `object`: PGobject,
        targetType: Class<InetAddress>,
        context: ConversionContext,
    ): Optional<InetAddress> {
        val pgType = `object`.type?.trim()?.lowercase()
        if (!pgType.isNullOrBlank() && pgType != POSTGRES_INET_TYPE) {
            return Optional.empty()
        }

        val rawValue = `object`.value?.trim()
        if (rawValue.isNullOrBlank()) {
            return Optional.empty()
        }

        return try {
            Optional.of(InetAddress.getByName(rawValue))
        } catch (e: Exception) {
            context.reject(rawValue, e)
            Optional.empty()
        }
    }
}

/**
 * Micronaut Data 持久化层使用的 `InetAddress` <-> PostgreSQL `inet` 属性转换器。
 *
 * 说明：
 * - `TypeConverter` 主要服务于通用转换体系，不能保证 JDBC 参数绑定一定使用
 * - `AttributeConverter` 会被 Micronaut Data JDBC 在实体属性持久化/反序列化时显式调用
 */
@Singleton
class InetAddressAttributeConverter : AttributeConverter<InetAddress, Any> {

    override fun convertToPersistedValue(
        entityValue: InetAddress?,
        context: ConversionContext,
    ): Any? {
        if (entityValue == null) {
            return null
        }
        return PGobject().apply {
            type = POSTGRES_INET_TYPE
            value = entityValue.hostAddress
        }
    }

    override fun convertToEntityValue(
        persistedValue: Any?,
        context: ConversionContext,
    ): InetAddress? {
        if (persistedValue == null) {
            return null
        }
        val rawValue = when (persistedValue) {
            is PGobject -> persistedValue.value
            is InetAddress -> persistedValue.hostAddress
            else -> persistedValue.toString()
        }?.trim()

        require(!rawValue.isNullOrBlank()) {
            "PostgreSQL inet persisted value is blank"
        }

        return try {
            InetAddress.getByName(rawValue)
        } catch (e: Exception) {
            throw IllegalArgumentException("Invalid PostgreSQL inet value: $rawValue", e)
        }
    }
}

/**
 * 将实体字段显式标记为 PostgreSQL `inet` 列。
 */
@Target(AnnotationTarget.FIELD, AnnotationTarget.PROPERTY_GETTER)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@MappedProperty(
    type = DataType.OBJECT,
    converter = InetAddressAttributeConverter::class,
    converterPersistedType = PGobject::class,
    definition = POSTGRES_INET_TYPE,
)
annotation class PostgreSqlInetColumn

/**
 * 将 JVM `InetAddress` 包装为 PostgreSQL `inet` 参数。
 */
@Singleton
class InetAddressToPostgreSqlInetConverter : TypeConverter<InetAddress, PGobject> {

    override fun convert(
        `object`: InetAddress,
        targetType: Class<PGobject>,
        context: ConversionContext,
    ): Optional<PGobject> {
        return try {
            Optional.of(
                PGobject().apply {
                    type = POSTGRES_INET_TYPE
                    value = `object`.hostAddress
                }
            )
        } catch (e: Exception) {
            context.reject(`object`, e)
            Optional.empty()
        }
    }
}

private const val POSTGRES_INET_TYPE = "inet"

