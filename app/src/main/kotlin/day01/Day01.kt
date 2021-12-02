package day01

import common.InputRepo
import common.readSessionCookie
import common.solve

fun main(args: Array<String>) {
    val day = 1
    val input = InputRepo(args.readSessionCookie()).get(day = day)

    solve(day, input, ::solveDay01Part1, ::solveDay01Part2)
}


fun solveDay01Part1(input: List<String>): Int {
    var lastInput = input.first().toInt()
    var counter = 0
    input.subList(1, input.size).forEach { it ->
        val number = it.toInt()
        if (number > lastInput) counter++
        lastInput = number
    }
    return counter
}

fun solveDay01Part2(input: List<String>): Int {
    val numbers = parseInputs(input)
    val window = numbers.subList(0, 3).toMutableList()
    var currentSum = window.sum()
    var counter = 0
    numbers.forEach { number ->
        window.removeAt(0)
        window.add(number)
        val nextSum = window.sum()
        if (nextSum > currentSum) counter++
        currentSum = nextSum
    }
    return counter
}

fun parseInputs(input: List<String>): List<Int> = input.map { it.toInt() }
