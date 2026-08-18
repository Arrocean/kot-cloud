package com.arrocean.dev.framework.common.exception.util

/**
 * 错误消息格式化工具。
 *
 * 说明：
 * - 使用 {} 作为占位符，而不是 String.format 的 %s / %d
 * - 参数过多或过少时保持尽可能稳定的输出
 * - 不抛出额外异常影响主流程
 *
 * @author WhiteSprite
 */
object ErrorMessageFormatter {

    fun format(code: Int, messagePattern: String, vararg params: Any?): String {
        if (params.isEmpty()) {
            return messagePattern
        }

        val builder = StringBuilder(messagePattern.length + 50)
        var startIndex = 0

        params.forEachIndexed { index, param ->
            val placeholderIndex = messagePattern.indexOf("{}", startIndex)
            if (placeholderIndex == -1) {
                if (startIndex == 0) {
                    return messagePattern
                }
                builder.append(messagePattern.substring(startIndex))
                return builder.toString()
            }

            builder.append(messagePattern, startIndex, placeholderIndex)
            builder.append(param)
            startIndex = placeholderIndex + 2

            if (index == params.lastIndex && messagePattern.indexOf("{}", startIndex) != -1) {
                return@forEachIndexed
            }
        }

        builder.append(messagePattern.substring(startIndex))
        return builder.toString()
    }
}

