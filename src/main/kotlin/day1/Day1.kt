package io.github.arnabkaycee.day1

import java.net.URL
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.PriorityQueue
import kotlin.math.abs
import kotlin.streams.asSequence

fun findTotalDistance(lines: Sequence<String>): Long {
    var sum = 0L
    val firstPriorityQueue: PriorityQueue<Int> = PriorityQueue()
    val secondPriorityQueue: PriorityQueue<Int> = PriorityQueue()
    lines.iterator().forEachRemaining {
        val (first, second) = it.split("   ").map { n -> n.toInt() }
        firstPriorityQueue.add(first)
        secondPriorityQueue.add(second)
    }
    repeat(firstPriorityQueue.size) {
        sum += abs(firstPriorityQueue.poll() - secondPriorityQueue.poll())
    }
    return sum
}

fun findSimilarityScore(lines: Sequence<String>): Long {
    var score = 0L
    val rightOccurrenceMap = mutableMapOf<Int, Int>()
    val leftList = mutableListOf<Int>()
    lines.iterator().forEachRemaining {
        val (first, second) = it.split("   ").map { n -> n.toInt() }
        leftList.add(first)
        rightOccurrenceMap.compute(second) { _, v ->
            if (v == null) 1
            else v + 1
        }
    }
    leftList.forEach {
        score += it * (rightOccurrenceMap[it] ?: 0)
    }
    return score
}

fun main() {
    val path = Paths.get(System.getProperty("file"))
    println("Total Distance " + findTotalDistance(Files.lines(path).asSequence()))
    println("Total similarity score "+ findSimilarityScore(Files.lines(path).asSequence()))
}