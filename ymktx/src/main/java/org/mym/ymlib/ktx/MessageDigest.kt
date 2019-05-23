package org.mym.ymlib.ktx

import java.io.File
import java.io.FileInputStream
import java.math.BigInteger
import java.nio.charset.Charset
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

/**
 * 计算文件的 MD5 值。等同于调用 ```digest("MD5")```。
 *
 * @see [File.digest]
 */
fun File.md5(): String? = digest("MD5")

/**
 * 使用给定的 [algorithm] 计算摘要字符串。如果发生任何错误（例如文件不存在，或无法找到指定算法）则返回 `null`。
 *
 * @param[algorithm] 要使用的摘要算法, e.g. `MD5`.
 *
 * @see MessageDigest
 */
fun File.digest(algorithm: String): String? {
    return try {
        val digest = MessageDigest.getInstance(algorithm)
        if (digest == null || !this.exists()) {
            null
        } else {
            val fis = FileInputStream(this)
            val buffer = ByteArray(8192)
            fis.use {
                while (true) {
                    val read = fis.read(buffer)
                    if (read <= 0) {
                        //EOF reached
                        break
                    }
                    digest.update(buffer, 0, read)
                }
            }
            String.format("%0${digest.digestLength * 2}x", BigInteger(1, digest.digest()))
            //Another implementation:
//            String.format("%32s", BigInteger(1, digest.digest()).toString(16)).replace(" ", "0")
        }
    } catch (t: Throwable) {
        null
    }
}

/**
 * 计算字符串的 MD5 值。等同于调用 ```digest("MD5")```。
 *
 * @see [String.digest]
 */
fun String.md5(): String? = digest("MD5")

/**
 * 使用给定的 [algorithm] 计算摘要字符串，出错时返回 `null`。
 *
 * @param[algorithm] 要使用的摘要算法, e.g. `MD5`.
 *
 * @see MessageDigest
 */
fun String.digest(algorithm: String): String? {
    try {
        val md5 = MessageDigest.getInstance(algorithm)
        val digestByteArray = md5.digest(this.toByteArray(Charset.forName("UTF-8")))

        val sb = StringBuilder()
        for (b in digestByteArray) {
            val hexString = Integer.toHexString(b.toInt() and 0xFF)
            sb.append(
                //如果仅有一位则需要补0
                if (hexString.length < 2) {
                    "0$hexString"
                } else {
                    hexString
                }
            )

        }
        return sb.toString()
    } catch (e: NoSuchAlgorithmException) {
        return null
    }
}