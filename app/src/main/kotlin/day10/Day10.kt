package day10

import common.InputRepo
import common.readSessionCookie
import common.solve
import java.util.*
import kotlin.math.floor

fun main(args: Array<String>) {
    val day = 10
    val input = InputRepo(args.readSessionCookie()).get(day = day)

    solve(day, input, ::solveDay10Part1, ::solveDay10Part2)
}


fun solveDay10Part1(input: List<String>): Int {
    val scores = mapOf(
        null to 0,
        BracketType.ROUND to 3,
        BracketType.FLAT to 57,
        BracketType.CURLY to 1197,
        BracketType.POINTY to 25137,
    )

    return input.sumOf { line ->
        val corrupted = line.firstCorrupted()
        scores[corrupted]!!
    }
}

fun solveDay10Part2(input: List<String>): Int {
    val scoreTable = mapOf(
        BracketType.ROUND to 1,
        BracketType.FLAT to 2,
        BracketType.CURLY to 3,
        BracketType.POINTY to 4,
    )

    val scores = input
        .filter { it.firstCorrupted() == null }
        .map { line ->
            line.autocomplete()
                .map { scoreTable[it]!!.toULong() }
                .reduce { acc, i -> acc * (5).toULong() + i }
        }.sorted()
    val middle = scores.size / 2
    return scores[middle].toInt()
}

fun String.firstCorrupted(): BracketType? {
    var corruptedBracket: BracketType? = null
    parseBrackets { bracketType, lastOpened ->
        if (corruptedBracket == null && bracketType != lastOpened) {
            corruptedBracket = bracketType
        }
    }
    return corruptedBracket

}

fun String.autocomplete(): List<BracketType> {
    return parseBrackets().reversed()
}

private fun String.parseBrackets(onCloseBracket: ((BracketType, BracketType) -> Unit)? = null): Stack<BracketType> {
    val openedBrackets = Stack<BracketType>()
    this.toCharArray().forEach { bracket ->
        val bracketType = BracketType.getBracketType(bracket)
        val isOpening = bracketType.opening == bracket
        if (isOpening) {
            openedBrackets.push(bracketType)
        } else {
            val lastOpened = openedBrackets.pop()
            onCloseBracket?.invoke(bracketType, lastOpened)
        }
    }
    return openedBrackets
}

enum class BracketType(val opening: Char, val closing: Char) {
    ROUND('(', ')'),
    FLAT('[', ']'),
    CURLY('{', '}'),
    POINTY('<', '>');

    companion object {
        fun getBracketType(c: Char): BracketType {
            return BracketType.values().first { it.opening == c || it.closing == c }
        }
    }
}
