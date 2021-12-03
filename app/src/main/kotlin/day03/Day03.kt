package day03

import common.InputRepo
import common.readSessionCookie
import common.solve

fun main(args: Array<String>) {
    val day = 3
    val input = InputRepo(args.readSessionCookie()).get(day = day)

    solve(day, input, ::solveDay03Part1, ::solveDay03Part2)
}


fun solveDay03Part1(input: List<String>): Int {
    val transposed = input.map { it.toCharArray() }.transpose()
    val gamma = transposed.map { it.mostCommonBit() }.toBinaryInt()
    val epsilon = transposed.map { it.leastCommonBit() }.toBinaryInt()

    return gamma * epsilon
}

fun solveDay03Part2(input: List<String>): Int {
    val oxygen = execute(input) { it.mostCommonBit() }
    val co2 = execute(input) { it.leastCommonBit() }

    return oxygen * co2
}

private fun execute(input: List<String>, targetFunction: (Array<Char>) -> Char): Int {
    var chars = input.map { it.toCharArray() }
    var index = 0
    while (chars.size > 1) {
        val targetBit = targetFunction(chars.transpose()[index])
        chars = chars.filter { it[index] == targetBit }

        index++
    }
    return String(chars[0]).toInt(2)
}

fun List<CharArray>.transpose(): Array<Array<Char>> {
    val output = Array(this[0].size) { Array<Char>(size) { '0' } }
    forEachIndexed { index, chars ->
        chars.forEachIndexed { index2, c ->
            output[index2][index] = c
        }
    }
    return output
}

fun Array<Char>.mostCommonBit(): Char {
    val count = countBits()
    return if (count[0] > count[1]) '0' else '1'
}

fun Array<Char>.leastCommonBit(): Char {
    val count = countBits()
    return if (count[0] <= count[1]) '0' else '1'
}

fun Array<Char>.countBits(): Array<Int> {
    val count = arrayOf(0, 0)
    this.forEach { if (it == '0') count[0]++ else count[1]++ }
    return count
}

fun List<Char>.toBinaryInt(): Int {
    return String(this.toCharArray()).toInt(2)
}
