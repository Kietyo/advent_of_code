import java.math.BigInteger
import java.security.MessageDigest


/**
 * Converts string to md5 hash.
 */
fun String.md5_2(): String = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray())).toString(16)
