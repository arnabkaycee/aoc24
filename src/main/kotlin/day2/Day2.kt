package io.github.arnabkaycee.day2

import java.nio.file.Files
import java.nio.file.Paths
import kotlin.math.absoluteValue
import kotlin.math.sign
import kotlin.streams.asSequence

fun isSafeReport(numbers: List<Int>) : Boolean {
    val minIncreasingThreshold = 1
    val maxIncreasingThreshold = 3
    var prev = numbers[0]
    var sign: Int = 0
    for (i in 1..numbers.lastIndex) {
        val diff = prev - numbers[i]
        sign = if (sign == 0) diff.sign else sign
        if (sign != 0 && diff.sign != sign) { // sign changed
            return false
        }
        if (diff.absoluteValue !in minIncreasingThreshold..maxIncreasingThreshold) {
            return false
        }
        prev = numbers[i]
    }
    return true
}
fun countSafeReports(lines: Sequence<String>): Int {
    return lines.count { line ->
        val numbers = line.getNumbers()
        isSafeReport(numbers)
    }
}

private fun String.getNumbers(): List<Int> {
    return this.split(" ").map { it.toInt() }.toList()
}

fun countSafeReportsWithDampening(
    lines: Sequence<String>,
): Int {
    return lines.count { line ->
        val numbers = line.getNumbers()
        val combinations = numbers.indices.map { i -> numbers.filterIndexed { idx, _ -> idx != i } }
        combinations.count { isSafeReport(it) } > 0
    }
}


fun main() {
    val path = Paths.get(System.getProperty("file"))
    println("Total safe reports " + countSafeReports(Files.lines(path).asSequence()))
    println("Total safe reports " + countSafeReportsWithDampening(Files.lines(path).asSequence()))
}