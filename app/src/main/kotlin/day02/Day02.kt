package day02

import common.InputRepo
import common.readSessionCookie
import common.solve

fun main(args: Array<String>) {
    val day = 2
    val input = InputRepo(args.readSessionCookie()).get(day = day)

    solve(day, input, ::solveDay02Part1, ::solveDay02Part2)
}


fun solveDay02Part1(input: List<String>): Int {
    return input
        .map { Command.fromString(it) }
        .map { command ->
            when (command.direction) {
                "forward" -> Position(x = command.amount)
                "down" -> Position(depth = command.amount)
                "up" -> Position(depth = -command.amount)
                else -> Position()
            }
        }
        .sum()
        .product()
}

fun solveDay02Part2(input: List<String>): Int {
    var aim = 0

    return input
        .map { Command.fromString(it) }
        .mapNotNull { command ->
            when (command.direction) {
                "forward" -> Position(x = command.amount, depth = command.amount * aim)
                "down" -> null.also { aim += command.amount }
                "up" -> null.also { aim -= command.amount }
                else -> null
            }
        }
        .sum()
        .product()
}

data class Command(val direction: String, val amount: Int) {
    companion object {
        fun fromString(input: String): Command {
            val parts = input.split(" ")
            val direction = parts[0]
            val amount = parts[1].toInt()
            return Command(direction, amount)
        }
    }
}

data class Position(val x: Int = 0, val depth: Int = 0) {
    operator fun plus(pos: Position): Position {
        return Position(x + pos.x, depth + pos.depth)
    }

    fun product(): Int = x * depth
}

private fun List<Position>.sum(): Position = reduce { acc, position -> acc + position }

