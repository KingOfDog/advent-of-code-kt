package day07

import common.InputRepo
import common.readSessionCookie
import common.solve
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.roundToInt

fun main(args: Array<String>) {
    val day = 7
    val input = InputRepo(args.readSessionCookie()).get(day = day)

    solve(day, input, ::solveDay07Part1, ::solveDay07Part2)
}


fun solveDay07Part1(input: List<String>): Int {
    val initialPositions = parseInput(input)
    val targetPosition = initialPositions.median()
    return initialPositions.sumOf { abs(targetPosition - it) }
}

fun solveDay07Part2(input: List<String>): Int {
    val initialPositions = parseInput(input)
    val average = initialPositions.average().roundToInt()
    val targetPositions = listOf(average - 1, average, average + 1)
    return targetPositions.calculateCosts(initialPositions).minOf { it }
}

fun solveDay07Part2BruteForce(input: List<String>): Int {
    val initialPositions = parseInput(input)
    val possiblePositions = (initialPositions.minOrNull()!!..initialPositions.maxOrNull()!!).toList()
    return possiblePositions.calculateCosts(initialPositions).minOf { it }
}

private fun List<Int>.calculateCosts(
    initialPositions: List<Int>
): List<Int> = map { targetPosition -> initialPositions.sumOf { fuelCost(abs(targetPosition - it)) } }

private fun parseInput(input: List<String>): List<Int> {
    val initialPositions = input.first().split(",").map { it.toInt() }
    return initialPositions
}

fun List<Int>.median(): Int {
    val sorted = sorted()
    val middle = size / 2.0
    return if (middle % 1 == 0.0) {
        sorted[middle.toInt()]
    } else {
        (sorted[floor(middle).toInt()] + sorted[ceil(middle).toInt()]) / 2
    }
}

fun fuelCost(distance: Int): Int {
    if (distance == 0) return 0
    return fuelCost(distance - 1) + distance
}
