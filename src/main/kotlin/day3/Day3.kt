package io.github.arnabkaycee.day3

import java.nio.file.Files
import java.nio.file.Paths
import java.util.regex.Matcher
import java.util.regex.Pattern
import kotlin.streams.asSequence

val numberPattern = Regex("mul\\((\\d{1,3}),(\\d{1,3})\\)")

fun multiplyCorrupted(lines: Sequence<String>): Int {
    var sum = 0
    lines.forEach { line ->
        numberPattern.findAll(line).forEach {
            val innerGroups = it.groupValues
            val (first, second) = innerGroups.subList(1, 3).map { s -> s.toInt() }
            sum += first * second
        }
    }
    return sum
}

private fun findNearestInstruction(
    previousValue: Boolean,
    currentPosition: Int,
    doList: List<Int>,
    dontList: List<Int>
): Boolean {
    val surrogateList = listOf(-1)
    val closestDoPosition =
        (if (doList.isEmpty()) surrogateList else doList.reversed()).firstOrNull { it < currentPosition }
            ?: -1
    val closestDontPosition =
        (if (dontList.isEmpty()) surrogateList else dontList.reversed()).firstOrNull { it < currentPosition }
            ?: -1
    return when {
        closestDoPosition < closestDontPosition -> false
        closestDontPosition < closestDoPosition -> true
        else -> previousValue
    }
}

fun multiplyCorruptedWithConditional(lines: Sequence<String>): Int {
    val regexConditionalDo = Regex("do\\(\\)")
    val regexConditionalDont = Regex("don't\\(\\)")
    var isOn = true
    var sum = 0
    lines.forEach { s ->
        val conditionalDo =
            listOf(0) + regexConditionalDo
                .findAll(s)
                .toList()
                .flatMap { it.groups.map { it?.range?.last } }
                .filterNotNull()
        val conditionalDont = regexConditionalDont
            .findAll(s)
            .toList()
            .flatMap { it.groups.map { it?.range?.last } }
            .filterNotNull()
//        isOn = true
        numberPattern.findAll(s).forEach {
            val innerGroups = it.groups
            val (first, second) = innerGroups.map { it?.value }.subList(1, 3).mapNotNull { it!!.toInt() }
            val index = innerGroups.map { it?.range }.subList(1, 2).map { it?.first }.single()!!
            isOn = findNearestInstruction(isOn, index, conditionalDo, conditionalDont)
            if (isOn) {
                sum += first * second
            }
        }
    }
    return sum
}

private fun partTwo(string: String): Int {
    val regex = "do\\(\\)|don't\\(\\)|mul\\((\\d{1,3}),(\\d{1,3})\\)"
    val pattern: Pattern = Pattern.compile(regex, Pattern.MULTILINE)
    val matcher: Matcher = pattern.matcher(string)
    var isEnabled = true
    var result = 0
    while (matcher.find()) {
        when (matcher.group(0)) {
            "do()" -> isEnabled = true
            "don't()" -> isEnabled = false
            else -> {
                if (isEnabled) {
                    val a: Int = matcher.group(1).toInt()
                    val b: Int = matcher.group(2).toInt()
                    result += a * b
                }
            }
        }
    }
    return result
}

fun main() {
    val path = Paths.get(System.getProperty("file"))
//    println("Total " + multiplyCorrupted(Files.lines(path).asSequence()))
//    println("Total " + multiplyCorruptedWithConditional(Files.lines(path).asSequence()))
    println("Total " + partTwo(Files.lines(path).toList().joinToString()))
    // 102631226 xxx

//    println("Total " + multiplyCorruptedWithConditional(listOf("xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))").asSequence()))
}