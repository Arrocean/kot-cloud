package com.whitesprite.dev.framework.common.core

/**
 * 键值对
 * @property key 键
 * @property value 值
 * @author WhiteSprite
 */
data class KeyValue<K, V>(
    var key: K,
    var value: V
)

interface ArrayValuable<T> {
    var array: Array<T>
}
