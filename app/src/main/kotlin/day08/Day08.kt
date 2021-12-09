package day08

import common.InputRepo
import common.readSessionCookie
import common.solve

fun main(args: Array<String>) {
    val day = 8
    val input = InputRepo(args.readSessionCookie()).get(day = day)

    solve(day, input, ::solveDay08Part1, ::solveDay08Part2)
}


fun solveDay08Part1(input: List<String>): Int {
    val entries = input.map { it.split(" | ") }.map { line -> line[0].split(" ") to line[1].split(" ") }

    var count = 0

    entries.forEach { entry ->
        val (tests, outputs) = entry
        val numberCombinations = mutableMapOf<Int, List<Char>>()

        tests.forEach { test ->
            val chars = test.toCharArray().sorted()
            when (chars.size) {
                2 -> numberCombinations[1] = chars
                4 -> numberCombinations[4] = chars
                3 -> numberCombinations[7] = chars
                7 -> numberCombinations[8] = chars
            }
        }

        outputs.forEach { output ->
            val chars = output.toCharArray().sorted()
            if (numberCombinations.containsValue(chars)) count++
        }
    }

    return count
}

fun solveDay08Part2(input: List<String>): Int {
    val entries = input.map { it.split(" | ") }.map { line -> line[0].split(" ") to line[1].split(" ") }

    return entries.sumOf { entry ->
        val (tests, outputs) = entry
        val display = SevenSegmentDisplay()
        display.discriminateAll(tests)
        println(display)

        outputs.map { display.guess(it) }.joinToString("").toInt()
    }
}

@OptIn(ExperimentalStdlibApi::class)
class SevenSegmentDisplay() {
    private val matrix = Array(7) { Array(7) { true } }
    val guaranteed = mutableMapOf<Char, Char>()

    fun discriminateAll(rawDigits: List<String>) {
        rawDigits
            .map { it.toSegments() }
            .map { segments -> segments to digitsWithSameSegmentCount(segments) }
            .sortedBy { it.second.size }
            .forEach { rawDigit ->
                val rawSegmentIndices = rawDigit.first.map { it.toIndex() }

                val digitCandidate = calculateDigitCandidate(rawSegmentIndices)
                val possibleDigits = getPossibleDigitsForCandidate(rawDigit.second, digitCandidate)
                val digit = findMatchingDigit(possibleDigits.values.toList(), rawSegmentIndices)

                val d = digit.map { it - 'a' }
                for (i in matrix.indices) {
                    if (!d.contains(i)) {
                        rawSegmentIndices.forEach { matrix[it][i] = false }
                    }
                    if (!rawSegmentIndices.contains(i)) {
                        d.forEach { matrix[i][it] = false }
                    }
                }
            }
    }

    private fun findMatchingDigit(
        possibleDigits: List<List<Char>>,
        rawSegmentIndices: List<Int>
    ): List<Char> {
        return possibleDigits.first { possibleDigit -> hasNoUnusedSegments(rawSegmentIndices, possibleDigit) }
    }

    private fun hasNoUnusedSegments(
        rawSegmentIndices: List<Int>,
        possibleDigit: List<Char>
    ): Boolean = findUnusedSegmentsForDigit(rawSegmentIndices, possibleDigit).isEmpty()

    private fun findUnusedSegmentsForDigit(
        rawSegmentIndices: List<Int>,
        possibleDigit: List<Char>
    ): List<Char> {
        val rawSegmentIndicesToBeUsed = rawSegmentIndices.toMutableList()
        val requiredSegments = possibleDigit.toMutableList()
        while (rawSegmentIndicesToBeUsed.size > 0 && requiredSegments.size > 0) {
            val actualSegment = requiredSegments.random()
            val possibleSegmentIndices = findPossibleSegments(rawSegmentIndicesToBeUsed, actualSegment.toIndex())
            when(possibleSegmentIndices.size) {
                0 -> break
                1 -> {
                    rawSegmentIndicesToBeUsed.remove(possibleSegmentIndices.first())
                    requiredSegments.remove(actualSegment)
                }
                else -> {
                    if (areAllSegmentsPossible(rawSegmentIndicesToBeUsed, possibleSegmentIndices)) {
                        rawSegmentIndicesToBeUsed.removeAll(possibleSegmentIndices)
                        requiredSegments.removeAllPossibleSegments()
                    }
                }
            }
        }
        return requiredSegments
    }

    private fun MutableList<Char>.removeAllPossibleSegments() {
        removeAll { segment -> getPossibleSegmentsForRawIndex(0).any { 'a' + it.index == segment } }
    }

    private fun areAllSegmentsPossible(
        rawSegmentIndicesToBeUsed: List<Int>,
        possibleSegmentIndices: List<Int>,
    ) = rawSegmentIndicesToBeUsed.containsAll(possibleSegmentIndices) && possibleSegmentIndices.all {
        getPossibleSegmentsForRawIndex(it) == getPossibleSegmentsForRawIndex(0)
    }

    private fun getPossibleSegmentsForRawIndex(rawIndex: Int) =
        matrix[rawIndex].withIndex().filter { it.value }

    private fun findPossibleSegments(
        rawSegmentIndicesToBeUsed: List<Int>,
        actualSegmentIndex: Int
    ) = rawSegmentIndicesToBeUsed.filter { index -> isOpen(index, actualSegmentIndex) }

    private fun isOpen(rawIndex: Int, actualIndex: Int) = matrix[rawIndex][actualIndex]

    private fun getPossibleDigitsForCandidate(
        digitPool: Map<Int, List<Char>>,
        digitCandidate: MutableMap<Char, SegmentState>
    ): Map<Int, List<Char>> = digitPool.filterValues { digit ->
        digit.all { segment -> digitCandidate[segment] != SegmentState.OFF }
    }

    private fun calculateDigitCandidate(rawSegmentIndices: List<Int>): MutableMap<Char, SegmentState> {
        val digitCandidate = mutableMapOf<Char, SegmentState>()
        rawSegmentIndices.forEach { index ->
            val stuff = matrix[index].withIndex().filter { it.value }
            if (stuff.size > 1) {
                stuff.forEach { i ->
                    val state = digitCandidate['a' + i.index]
                    digitCandidate['a' + i.index] =
                        if (state == SegmentState.POSSIBLE) SegmentState.ON else SegmentState.POSSIBLE
                }
            } else if (stuff.size == 1) {
                digitCandidate['a' + stuff.first().index] = SegmentState.ON
            }
        }
        return digitCandidate
    }

    private fun digitsWithSameSegmentCount(segments: List<Char>) =
        digits.filterValues { it.size == segments.size }

    fun guess(digit: String): Int {
        val chars = digit.toCharArray()
        val segments =
            chars.map { char -> matrix[char.toIndex()].withIndex().first { it.value }.index.toSegment() }.sorted()
        return digits.filterValues { it == segments }.keys.first()
    }

    override fun toString(): String {
        var result = ""
        matrix.forEach { row ->
            row.forEach { cell ->
                result += if (cell) " " else "X"
            }
            result += "\n"
        }
        return result
    }

    companion object {
        private val digits = mapOf(
            0 to listOf('a', 'b', 'c', 'e', 'f', 'g'),
            1 to listOf('c', 'f'),
            2 to listOf('a', 'c', 'd', 'e', 'g'),
            3 to listOf('a', 'c', 'd', 'f', 'g'),
            4 to listOf('b', 'c', 'd', 'f'),
            5 to listOf('a', 'b', 'd', 'f', 'g'),
            6 to listOf('a', 'b', 'd', 'e', 'f', 'g'),
            7 to listOf('a', 'c', 'f'),
            8 to listOf('a', 'b', 'c', 'd', 'e', 'f', 'g'),
            9 to listOf('a', 'b', 'c', 'd', 'f', 'g'),
        )
    }
}

enum class SegmentState {
    OFF,
    POSSIBLE,
    ON
}

private fun String.toSegments() = toCharArray().sorted()

private fun Int.toSegment(): Char = 'a' + this

private fun Char.toIndex(): Int = this - 'a'
