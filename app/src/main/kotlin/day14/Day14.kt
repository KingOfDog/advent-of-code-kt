package day14

import common.InputRepo
import common.readSessionCookie
import common.solve

fun main(args: Array<String>) {
    val day = 14
    val input = InputRepo(args.readSessionCookie()).get(day = day)

    solve(day, input, ::solveDay14Part1, ::solveDay14Part2)
}


fun solveDay14Part1(input: List<String>): Long {
    return execute(input, 10)
}

fun solveDay14Part2(input: List<String>): Long {
    return execute(input, 40)
}

private fun execute(input: List<String>, steps: Int): Long {
    val dictionary = input.subList(2, input.size).map { it.split(" -> ") }.associate { it[0] to it[1] }

    var pairCounts = input.first().windowed(2).groupBy { it }.map { it.key to it.value.size.toLong() }.toMap()

    repeat(steps) {
        pairCounts = calculateNextStep(pairCounts, dictionary)
    }

    val elementCounts = getElementCounts(pairCounts, input)

    val mostCommon = elementCounts.maxOfOrNull { it.value } ?: 0L
    val leastCommon = elementCounts.minOfOrNull { it.value } ?: 0L

    return mostCommon - leastCommon
}

private fun calculateNextStep(
    pairCounts: Map<String, Long>,
    dictionary: Map<String, String>
): Map<String, Long> {
    val nextPairCounts = pairCounts.toMutableMap()

    pairCounts.forEach { (pair, count) ->
        val insertElement = dictionary[pair]
        val newPairLeft = pair.substring(0, 1) + insertElement
        val newPairRight = insertElement + pair.substring(1, 2)

        nextPairCounts.decreaseBy(pair, count)
        nextPairCounts.increaseBy(newPairLeft, count)
        nextPairCounts.increaseBy(newPairRight, count)
    }

    return nextPairCounts.toMap()
}

private fun getElementCounts(
    pairCounts: Map<String, Long>,
    input: List<String>
): MutableMap<Char, Long> {
    val elementCounts = mutableMapOf<Char, Long>()
    pairCounts.forEach { (pair, count) ->
        elementCounts[pair[0]] = elementCounts.getOrDefault(pair[0], 0L) + count
    }
    elementCounts[input.first().last()] = elementCounts[input.first().last()]!! + 1
    return elementCounts
}

private fun executeSlow(input: List<String>, steps: Int): Long {
    val dictionary = input.subList(2, input.size).map { it.split(" -> ") }.associate { it[0] to it[1] }

    var template = input.first()
    val nextTemplate = template.toCharArray().toMutableList()

    repeat(steps) {
        var insertIndex = 1
        template.windowed(2) { pair ->
            val insert = dictionary.getOrDefault(pair, "")
            nextTemplate.addAll(insertIndex, insert.toCharArray().toList())
            insertIndex += insert.length + 1
        }

        template = nextTemplate.toCharArray().concatToString()
    }

    val mostCommon = nextTemplate.groupBy { it }.maxOfOrNull { it.value.size.toLong() } ?: 0L
    val leastCommon = nextTemplate.groupBy { it }.minOfOrNull { it.value.size.toLong() } ?: 0L

    return mostCommon - leastCommon
}

fun MutableMap<String, Long>.increaseBy(key: String, add: Long) {
    this[key] = getOrDefault(key, 0L) + add
}

fun MutableMap<String, Long>.decreaseBy(key: String, sub: Long) {
    this[key] = getOrDefault(key, 0L) - sub
}
