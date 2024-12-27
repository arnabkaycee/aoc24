package io.github.arnabkaycee.day5

import java.nio.file.Files
import java.nio.file.Paths
import kotlin.streams.asSequence


class PrintQueueOrderer(orderingList: List<Pair<Int, Int>>) : Comparator<Int> {
    private val orderingMap = mutableMapOf<Int, MutableSet<Int>>()

    init {
        orderingList.forEach { (first, second) ->
            orderingMap.compute(first) { _, v ->
                val list: MutableSet<Int>
                if (v == null) {
                    list = mutableSetOf(second)
                } else {
                    list = v
                    list.add(second)
                }
                list
            }
        }
    }

    override fun compare(o1: Int, o2: Int): Int {
        return if (o1 in orderingMap && o2 in orderingMap[o1]!!) -1
        else if (o2 in orderingMap && o1 in orderingMap[o2]!!) 1
        else 0
    }
}

fun <T> List<T>.isSortedWith(comparator: Comparator<T>): Boolean {
    return this.zipWithNext { a, b -> comparator.compare(a, b) < 0 }.all { it }
}

fun <T> List<T>.midElement() : T {
    return this[(this.size - 1) / 2]
}

fun sumOfMidElementsOfOrderedPrintQueue(lines: Sequence<String>): Int {
    val list = mutableListOf<Pair<Int, Int>>()
    val printQueue = mutableListOf<List<Int>>()
    var switched = false
    lines.forEach { line ->
        if (line.isBlank()) {
            switched = true
        } else {
            if (!switched) {
                val (first, second) = line.split("|").map { it.toInt() }
                list.add(first to second)
            } else {
                val numbers = line.split(",").map { it.toInt() }
                printQueue.add(numbers)
            }
        }
    }
    val printQueueOrderer = PrintQueueOrderer(list)
    val sortedOnes = printQueue.filter {
        it.isSortedWith(printQueueOrderer)
    }
    return sortedOnes.map { it.midElement() }.sum()
}

fun sumAfterCorrectlyOrderingElements(lines: Sequence<String>) : Int {
    val list = mutableListOf<Pair<Int, Int>>()
    val printQueue = mutableListOf<List<Int>>()
    var switched = false
    lines.forEach { line ->
        if (line.isBlank()) {
            switched = true
        } else {
            if (!switched) {
                val (first, second) = line.split("|").map { it.toInt() }
                list.add(first to second)
            } else {
                val numbers = line.split(",").map { it.toInt() }
                printQueue.add(numbers)
            }
        }
    }
    val printQueueOrderer = PrintQueueOrderer(list)
    val unSortedOnes = printQueue.filter {
        !it.isSortedWith(printQueueOrderer)
    }
    val nowSortedOnes =  unSortedOnes.map {
        it.sortedWith(printQueueOrderer)
    }
    return nowSortedOnes.map { it.midElement() }.sum()
}
fun main() {
    val path = Paths.get(System.getProperty("file"))
    println("Sum of Mid Elements " + sumOfMidElementsOfOrderedPrintQueue(Files.lines(path).asSequence()))
    println("Sum of Mid Elements " + sumAfterCorrectlyOrderingElements(Files.lines(path).asSequence()))
}
