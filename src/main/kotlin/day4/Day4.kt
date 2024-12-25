package io.github.arnabkaycee.day4

import io.github.arnabkaycee.day4.Direction.*
import java.nio.file.Files
import java.nio.file.Paths

/**
 * XMAS Word search in 8 directions
 */
// pre-process input - convert n lines of n characters each into a nxn character matrix for easy navigation

enum class Direction {
    LEFT_TO_RIGHT, // ➡️
    DIAGONAL_RIGHT_DOWN, // ↘️
    TOP_TO_BOTTOM, // ⬇️
    DIAGONAL_LEFT_DOWN, // ↙️
    RIGHT_TO_LEFT, // ⬅️
    DIAGONAL_LEFT_UP, // ↖️
    BOTTOM_TO_TOP, // ⬆️
    DIAGONAL_RIGHT_UP // ↗️
}

fun inBounds(r: Int, c: Int, charArray: Array<CharArray>): Boolean {
    return r >= 0 && r <= charArray.lastIndex && c >= 0 && c <= charArray[0].lastIndex
}

fun computeIndices(r: Int, c: Int, direction: Direction): Pair<Int, Int> {
    val (newR, newC) = when (direction) {
        LEFT_TO_RIGHT -> r to c + 1
        DIAGONAL_RIGHT_DOWN -> r + 1 to c + 1
        TOP_TO_BOTTOM -> r + 1 to c
        DIAGONAL_LEFT_DOWN -> r + 1 to c - 1
        RIGHT_TO_LEFT -> r to c - 1
        DIAGONAL_LEFT_UP -> r - 1 to c - 1
        BOTTOM_TO_TOP -> r - 1 to c
        DIAGONAL_RIGHT_UP -> r - 1 to c + 1
    }
    return newR to newC
}

fun look(
    charArray: Array<CharArray>,
    r: Int,
    c: Int,
    direction: Direction,
    word: String = "XMAS",
    index: Int = 0
): Boolean {
    val (newR, newC) = computeIndices(r, c, direction)
    // boundary condition
    return if (inBounds(r, c, charArray) && charArray[r][c] == word[index]) {
        if (index == word.lastIndex)
            true
        else look(charArray, newR, newC, direction, word, index + 1)
    } else false
}

fun countPattern(charArray: Array<CharArray>): Int {
    var count = 0
    for (i in 0..charArray.lastIndex) {
        for (j in 0..charArray[0].lastIndex) {
            for (d in entries) {
                count += if (look(charArray, i, j, d)) {
//                    println("i -> $i, j -> $j, direction -> $d")
                    1
                } else 0
            }
        }
    }
    return count
}

private fun checkIfAdjacentAreMOrS(charArray: Array<CharArray>, r: Int, c: Int): Boolean {
    val arm1 = listOf(DIAGONAL_RIGHT_DOWN, DIAGONAL_LEFT_UP)
    val arm2 = listOf(DIAGONAL_LEFT_DOWN, DIAGONAL_RIGHT_UP)
    val allowedChars = setOf('M', 'S')
    val arm1Elements = mutableSetOf<Char>()
    val arm2Elements = mutableSetOf<Char>()
    arm1.forEach {
        val (newR, newC) = computeIndices(r, c, it)
        if (inBounds(newR, newC, charArray)) {
            arm1Elements.add(charArray[newR][newC])
        }
    }
    arm2.forEach {
        val (newR, newC) = computeIndices(r, c, it)
        if (inBounds(newR, newC, charArray)) {
            arm2Elements.add(charArray[newR][newC])
        }
    }
    return arm1Elements.containsAll(allowedChars) && arm2Elements.containsAll(allowedChars)
}

fun countMASInShapeOfX(charArray: Array<CharArray>): Int {
    var count = 0
    for (i in 0..charArray.lastIndex) {
        for (j in 0..charArray[0].lastIndex) {
            if (charArray[i][j] == 'A' && checkIfAdjacentAreMOrS(charArray, i, j)) count++
        }
    }
    return count
}

fun main() {
    val path = Paths.get(System.getProperty("file"))
    val charArray = Files.lines(path).toList().map { it.toCharArray() }.toTypedArray()
    println("Total XMAS in all 8 directions " + countPattern(charArray))
    println("Total MAS in the Shape of X " + countMASInShapeOfX(charArray))
}