import java.io.File
import java.math.BigInteger
import java.security.MessageDigest
import java.util.*
import kotlin.Comparator
import kotlin.math.abs

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = File("src\\main\\kotlin\\aoc_2022", "$name.txt")
    .readLines()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')



