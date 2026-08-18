package com.arrocean.dev.framework.common.util.net

import java.math.BigInteger
import java.net.DatagramSocket
import java.net.HttpCookie
import java.net.IDN
import java.net.Inet4Address
import java.net.Inet6Address
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.NetworkInterface
import java.net.ServerSocket
import java.net.Socket
import java.util.Collections
import kotlin.random.Random

/**
 * 网络工具类。
 *
 * 说明：
 * - 参考 Hutool `NetUtil`，优先补充当前项目更可能会用到、且 JVM 原生已支持的能力
 * - 不额外引入 Hutool 依赖，保持实现轻量、可控
 */
object NetUtils {

    private const val UNKNOWN = "unknown"
    const val LOCAL_IP: String = "127.0.0.1"
    const val PORT_RANGE_MIN: Int = 1024
    const val PORT_RANGE_MAX: Int = 0xFFFF

    @Volatile
    private var localhostName: String? = null

    /**
     * 根据 long 值获取 IPv4 地址。
     */
    @JvmStatic
    fun longToIpv4(longIP: Long): String {
        require(longIP in 0..0xFFFF_FFFFL) { "IPv4 数值超出范围: $longIP" }
        return listOf(
            (longIP shr 24 and 0xFF),
            (longIP shr 16 and 0xFF),
            (longIP shr 8 and 0xFF),
            (longIP and 0xFF),
        ).joinToString(".")
    }

    /**
     * 根据 IPv4 地址计算 long 值。
     */
    @JvmStatic
    fun ipv4ToLong(strIP: String): Long {
        val parts = strIP.trim().split('.')
        require(parts.size == 4) { "非法 IPv4 地址: $strIP" }
        return parts.fold(0L) { acc, part ->
            val value = part.toIntOrNull()
            require(value != null && value in 0..255) { "非法 IPv4 地址: $strIP" }
            (acc shl 8) or value.toLong()
        }
    }

    /**
     * 将 IPv6 地址字符串转为大整数。
     */
    @JvmStatic
    fun ipv6ToBigInteger(ipv6Str: String): BigInteger? {
        return runCatching {
            val address = InetAddress.getByName(ipv6Str)
            if (address is Inet6Address) {
                BigInteger(1, address.address)
            } else {
                null
            }
        }.getOrNull()
    }

    /**
     * 兼容 Hutool 的历史拼写。
     */
    @Deprecated("拼写错误，请使用 ipv6ToBigInteger")
    @JvmStatic
    fun ipv6ToBitInteger(ipv6Str: String): BigInteger? = ipv6ToBigInteger(ipv6Str)

    /**
     * 将大整数转换为 IPv6 字符串。
     */
    @JvmStatic
    fun bigIntegerToIPv6(bigInteger: BigInteger): String? {
        val max = BigInteger("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF", 16)
        require(bigInteger >= BigInteger.ZERO && bigInteger <= max) {
            "BigInteger value is out of IPv6 range"
        }

        var bytes = bigInteger.toByteArray()
        bytes = when {
            bytes.size > 16 -> {
                val offset = if (bytes[0].toInt() == 0) 1 else 0
                ByteArray(16).also { System.arraycopy(bytes, offset, it, 0, 16) }
            }
            bytes.size < 16 -> ByteArray(16).also { System.arraycopy(bytes, 0, it, 16 - bytes.size, bytes.size) }
            else -> bytes
        }

        return runCatching { Inet6Address.getByAddress(bytes).hostAddress }.getOrNull()
    }

    /**
     * 检测本地端口可用性。
     */
    @JvmStatic
    fun isUsableLocalPort(port: Int): Boolean {
        if (!isValidPort(port)) {
            return false
        }

        return try {
            ServerSocket(port).use { it.reuseAddress = true }
            DatagramSocket(port).use { it.reuseAddress = true }
            true
        } catch (_: Exception) {
            false
        }
    }

    /**
     * 是否为有效端口。
     */
    @JvmStatic
    fun isValidPort(port: Int): Boolean = port in 0..PORT_RANGE_MAX

    @JvmStatic
    fun getUsableLocalPort(): Int = getUsableLocalPort(PORT_RANGE_MIN, PORT_RANGE_MAX)

    @JvmStatic
    fun getUsableLocalPort(minPort: Int): Int = getUsableLocalPort(minPort, PORT_RANGE_MAX)

    @JvmStatic
    fun getUsableLocalPort(minPort: Int, maxPort: Int): Int {
        require(minPort in 0..PORT_RANGE_MAX && maxPort in 0..PORT_RANGE_MAX && minPort <= maxPort) {
            "非法端口范围: [$minPort, $maxPort]"
        }

        val attemptCount = maxPort - minPort + 1
        repeat(attemptCount) {
            val randomPort = Random.nextInt(minPort, maxPort + 1)
            if (isUsableLocalPort(randomPort)) {
                return randomPort
            }
        }

        throw IllegalStateException("Could not find an available port in the range [$minPort, $maxPort] after $attemptCount attempts")
    }

    @JvmStatic
    fun getUsableLocalPorts(numRequested: Int, minPort: Int, maxPort: Int): java.util.TreeSet<Int> {
        require(numRequested > 0) { "numRequested 必须大于 0" }
        val availablePorts = java.util.TreeSet<Int>()
        var attemptCount = 0
        while (attemptCount++ <= numRequested + 100 && availablePorts.size < numRequested) {
            availablePorts += getUsableLocalPort(minPort, maxPort)
        }
        check(availablePorts.size == numRequested) {
            "Could not find $numRequested available ports in the range [$minPort, $maxPort]"
        }
        return availablePorts
    }

    /**
     * 判定是否为内网 IPv4。
     */
    @JvmStatic
    fun isInnerIP(ipAddress: String): Boolean {
        val ip = runCatching { InetAddress.getByName(ipAddress) }.getOrNull() ?: return false
        if (ip.isAnyLocalAddress || ip.isLoopbackAddress) {
            return true
        }
        if (ip !is Inet4Address) {
            return false
        }
        val bytes = ip.address
        val b0 = bytes[0].toInt() and 0xFF
        val b1 = bytes[1].toInt() and 0xFF
        return when {
            b0 == 10 -> true
            b0 == 172 && b1 in 16..31 -> true
            b0 == 192 && b1 == 168 -> true
            b0 == 127 -> true
            else -> false
        }
    }

    @JvmStatic
    fun toAbsoluteUrl(absoluteBasePath: String, relativePath: String): String {
        return java.net.URI(absoluteBasePath).resolve(relativePath).toString()
    }

    @JvmStatic
    fun hideIpPart(ip: String): String {
        val normalized = ip.trim()
        return when {
            normalized.contains('.') -> {
                val lastIndex = normalized.lastIndexOf('.')
                if (lastIndex >= 0) normalized.substring(0, lastIndex + 1) + "*" else normalized
            }
            normalized.contains(':') -> {
                val lastIndex = normalized.lastIndexOf(':')
                if (lastIndex >= 0) normalized.substring(0, lastIndex + 1) + "*" else normalized
            }
            else -> normalized
        }
    }

    @JvmStatic
    fun hideIpPart(ip: Long): String = hideIpPart(longToIpv4(ip))

    @JvmStatic
    fun buildInetSocketAddress(host: String?, defaultPort: Int): InetSocketAddress {
        val normalizedHost = host?.trim().takeUnless { it.isNullOrBlank() } ?: LOCAL_IP
        if (normalizedHost.startsWith("[")) {
            val endIndex = normalizedHost.indexOf(']')
            if (endIndex > 0) {
                val actualHost = normalizedHost.substring(1, endIndex)
                val port = normalizedHost.substring(endIndex + 1).removePrefix(":").toIntOrNull() ?: defaultPort
                return InetSocketAddress(actualHost, port)
            }
        }

        val colonCount = normalizedHost.count { it == ':' }
        return if (colonCount == 1) {
            val index = normalizedHost.indexOf(':')
            InetSocketAddress(normalizedHost.substring(0, index), normalizedHost.substring(index + 1).toInt())
        } else {
            InetSocketAddress(normalizedHost, defaultPort)
        }
    }

    @JvmStatic
    fun getIpByHost(hostName: String): String {
        return runCatching { InetAddress.getByName(hostName).hostAddress }.getOrDefault(hostName)
    }

    @JvmStatic
    fun getNetworkInterface(name: String): NetworkInterface? {
        return runCatching {
            Collections.list(NetworkInterface.getNetworkInterfaces())
                .firstOrNull { it.name == name }
        }.getOrNull()
    }

    @JvmStatic
    fun getNetworkInterfaces(): Collection<NetworkInterface> {
        return runCatching { Collections.list(NetworkInterface.getNetworkInterfaces()) }.getOrDefault(emptyList())
    }

    @JvmStatic
    fun localIpv4s(): LinkedHashSet<String> = toIpList(localAddressList(addressFilter = { it is Inet4Address }))

    @JvmStatic
    fun localIpv6s(): LinkedHashSet<String> = toIpList(localAddressList(addressFilter = { it is Inet6Address }))

    @JvmStatic
    fun toIpList(addressList: Set<InetAddress>): LinkedHashSet<String> {
        return LinkedHashSet(addressList.map { it.hostAddress })
    }

    @JvmStatic
    fun localIps(): LinkedHashSet<String> = toIpList(localAddressList())

    @JvmStatic
    fun localAddressList(addressFilter: ((InetAddress) -> Boolean)? = null): LinkedHashSet<InetAddress> {
        return localAddressList(networkInterfaceFilter = null, addressFilter = addressFilter)
    }

    @JvmStatic
    fun localAddressList(
        networkInterfaceFilter: ((NetworkInterface) -> Boolean)? = null,
        addressFilter: ((InetAddress) -> Boolean)? = null,
    ): LinkedHashSet<InetAddress> {
        val networkInterfaces = runCatching { Collections.list(NetworkInterface.getNetworkInterfaces()) }.getOrDefault(emptyList())
        val result = LinkedHashSet<InetAddress>()
        for (networkInterface in networkInterfaces) {
            if (networkInterfaceFilter != null && !networkInterfaceFilter(networkInterface)) {
                continue
            }
            for (address in Collections.list(networkInterface.inetAddresses)) {
                if (addressFilter == null || addressFilter(address)) {
                    result += address
                }
            }
        }
        return result
    }

    @JvmStatic
    fun getLocalhostStr(): String? = getLocalhost()?.hostAddress

    @JvmStatic
    fun getLocalhost(): InetAddress? {
        val addresses = localAddressList { !it.isLoopbackAddress && it is Inet4Address }
        var siteLocalCandidate: InetAddress? = null
        for (address in addresses) {
            if (!address.isSiteLocalAddress) {
                return address
            }
            if (siteLocalCandidate == null) {
                siteLocalCandidate = address
            }
        }
        return siteLocalCandidate ?: runCatching { InetAddress.getLocalHost() }.getOrNull()
    }

    @JvmStatic
    fun getLocalMacAddress(): String? = getMacAddress(getLocalhost())

    @JvmStatic
    fun getMacAddress(inetAddress: InetAddress?): String? = getMacAddress(inetAddress, "-")

    @JvmStatic
    fun getMacAddress(inetAddress: InetAddress?, separator: String): String? {
        val mac = getHardwareAddress(inetAddress) ?: return null
        return mac.joinToString(separator) { byte -> "%02x".format(byte.toInt() and 0xFF) }
    }

    @JvmStatic
    fun getHardwareAddress(inetAddress: InetAddress?): ByteArray? {
        if (inetAddress == null) {
            return null
        }
        return runCatching { NetworkInterface.getByInetAddress(inetAddress)?.hardwareAddress }.getOrNull()
    }

    @JvmStatic
    fun getLocalHardwareAddress(): ByteArray? = getHardwareAddress(getLocalhost())

    @JvmStatic
    fun getLocalHostName(): String? {
        localhostName?.let { return it }
        val localhost = getLocalhost() ?: return null
        val resolved = localhost.hostName?.takeIf(String::isNotBlank) ?: localhost.hostAddress
        localhostName = resolved
        return resolved
    }

    @JvmStatic
    fun createAddress(host: String?, port: Int): InetSocketAddress {
        return if (host.isNullOrBlank()) InetSocketAddress(port) else InetSocketAddress(host, port)
    }

    @JvmStatic
    fun isInRange(ip: String, cidr: String): Boolean {
        val maskSplitMarkIndex = cidr.lastIndexOf('/')
        require(maskSplitMarkIndex >= 0) { "Invalid cidr: $cidr" }
        val prefix = cidr.substring(maskSplitMarkIndex + 1).toInt()
        require(prefix in 0..32) { "Invalid cidr prefix: $cidr" }
        val mask = if (prefix == 0) 0L else (-1L shl (32 - prefix)) and 0xFFFF_FFFFL
        val cidrIpAddr = ipv4ToLong(cidr.substring(0, maskSplitMarkIndex))
        return (ipv4ToLong(ip) and mask) == (cidrIpAddr and mask)
    }

    @JvmStatic
    fun idnToASCII(unicode: String): String = IDN.toASCII(unicode)

    /**
     * 从多级反向代理中获得第一个非 unknown IP 地址。
     */
    @JvmStatic
    fun getMultistageReverseProxyIp(ip: String?): InetAddress? {
        if (ip.isNullOrBlank()) {
            return null
        }

        return ip.split(',')
            .asSequence()
            .map(String::trim)
            .filterNot(::isUnknown).firstNotNullOfOrNull(::parseInetAddress)
    }

    /**
     * 检测给定字符串是否为未知，多用于检测 HTTP 请求相关。
     */
    @JvmStatic
    fun isUnknown(checkString: String?): Boolean {
        return checkString.isNullOrBlank() || UNKNOWN.equals(checkString.trim(), ignoreCase = true)
    }

    /**
     * 从 RFC 7239 Forwarded 头中提取客户端 IP。
     *
     * 示例：
     * - for=192.0.2.60;proto=http;by=203.0.113.43
     * - for="[2001:db8:cafe::17]:4711"
     */
    @JvmStatic
    fun getIpFromForwardedHeader(forwarded: String?): InetAddress? {
        if (forwarded.isNullOrBlank()) {
            return null
        }

        return forwarded.split(',')
            .asSequence()
            .map(String::trim)
            .firstNotNullOfOrNull { segment ->
                segment.split(';')
                    .asSequence()
                    .map(String::trim)
                    .firstNotNullOfOrNull { part ->
                        if (!part.startsWith("for=", ignoreCase = true)) {
                            return@firstNotNullOfOrNull null
                        }

                        parseInetAddress(part.substringAfter('=', ""))
                    }
            }
    }

    @JvmStatic
    fun ping(ip: String): Boolean = ping(ip, 200)

    @JvmStatic
    fun ping(ip: String, timeout: Int): Boolean {
        return runCatching { InetAddress.getByName(ip).isReachable(timeout) }.getOrDefault(false)
    }

    @JvmStatic
    fun parseCookies(cookieStr: String?): List<HttpCookie> {
        if (cookieStr.isNullOrBlank()) {
            return emptyList()
        }
        return runCatching { HttpCookie.parse(cookieStr) }.getOrDefault(emptyList())
    }

    @JvmStatic
    fun isOpen(address: InetSocketAddress, timeout: Int): Boolean {
        return try {
            Socket().use { it.connect(address, timeout) }
            true
        } catch (_: Exception) {
            false
        }
    }

    @JvmStatic
    fun parseInetAddress(value: String?): InetAddress? {
        val normalized = normalizeIpLiteral(value) ?: return null
        return runCatching { InetAddress.getByName(normalized) }.getOrNull()
    }

    private fun normalizeIpLiteral(value: String?): String? {
        val normalized = value?.trim()?.trim('"') ?: return null
        if (isUnknown(normalized)) {
            return null
        }

        if (normalized.startsWith("[") && normalized.contains(']')) {
            return normalized.substringAfter('[').substringBefore(']').takeIf(String::isNotBlank)
        }

        val colonCount = normalized.count { it == ':' }
        return when {
            colonCount <= 1 -> normalized.substringBefore(':').takeIf(String::isNotBlank)
            else -> normalized.takeIf(String::isNotBlank)
        }
    }

    private fun normalizeForwardedValue(value: String): String? {
        val normalized = value.trim().trim('"')
        if (isUnknown(normalized)) {
            return null
        }

        if (normalized.startsWith("[") && normalized.contains(']')) {
            return normalized.substringAfter('[').substringBefore(']').takeIf(String::isNotBlank)
        }

        val colonCount = normalized.count { it == ':' }
        return when {
            colonCount <= 1 -> normalized.substringBefore(':').takeIf(String::isNotBlank)
            else -> normalized.takeIf(String::isNotBlank)
        }
    }
}



