package day04

import common.InputRepo
import common.readSessionCookie
import common.solve

fun main(args: Array<String>) {
    val day = 4
    val input = InputRepo(args.readSessionCookie()).get(day = day)

    solve(day, input, ::solveDay04Part1, ::solveDay04Part2)
}


fun solveDay04Part1(input: List<String>): Int {
    val numbers = parseNumbers(input)
    val boards = parseBoards(input)

    var number: Int = 0
    var hasWon = false
    while (!hasWon && numbers.isNotEmpty()) {
        number = numbers.removeFirst()
        boards.forEach { it.applyNumber(number) }
        hasWon = boards.any { it.hasWon() }
    }

    val winningBoard = boards.first { it.hasWon() }

    return winningBoard.sumOfRemainingNumbers() * number
}

fun solveDay04Part2(input: List<String>): Int {
    val numbers = parseNumbers(input)
    val boards = parseBoards(input)

    var number: Int = 0
    val boardsWon = boards.map { false }.toTypedArray()
    var lastWinningBoardIndex = -1
    while (numbers.isNotEmpty() && boardsWon.any { !it }) {
        number = numbers.removeFirst()
        boards.forEachIndexed { index, board ->
            board.applyNumber(number)
            if (board.hasWon() && !boardsWon[index]) {
                boardsWon[index] = true
                lastWinningBoardIndex = index
            }
        }
    }

    val lastWinningBoard = boards[lastWinningBoardIndex]
    return lastWinningBoard.sumOfRemainingNumbers() * number
}

private fun parseBoards(input: List<String>) =
    input.subList(2, input.size).filterNot { it == "" }.windowed(5, 5, false)
        .map {
            it.map { row ->
                row.split(Regex("\\s+")).map { cell -> cell.toInt() }
            }
        }
        .map { BingoBoard(it) }

private fun parseNumbers(input: List<String>) =
    input[0].split(",").map { it.toInt() }.toMutableList()

class BingoBoard(input: List<List<Int>>) {
    private val width = input[0].size
    private val height = input.size

    private val numbers: Array<Array<Cell>> =
        input.map { it.map { number -> Cell(number, false) }.toTypedArray() }.toTypedArray()

    init {
        input.forEach {
            assert(it.size == width)
        }
    }

    fun applyNumber(number: Int) {
        numbers.forEach { row ->
            row.forEach {
                if (it.number == number)
                    it.checked = true
            }
        }
    }

    fun hasWon(): Boolean {
        val options = getOptions()
        return options.any { it.areAllChecked() }
    }

    fun sumOfRemainingNumbers(): Int = numbers.flatten().filterNot { it.checked }.sumOf { it.number }

    private fun getOptions(): Array<Array<Cell>> = arrayOf(
        *getRows(),
        *getColumns(),
    )

    private fun getRows(): Array<Array<Cell>> = numbers
    private fun getColumns(): Array<Array<Cell>> = numbers.transpose()
}

data class Cell(val number: Int, var checked: Boolean)

fun Array<Cell>.areAllChecked(): Boolean =
    all { it.checked }

fun Array<Array<Cell>>.transpose(): Array<Array<Cell>> {
    val output = Array(this[0].size) { Array(size) { Cell(0, false) } }
    forEachIndexed { index, row ->
        row.forEachIndexed { index2, c ->
            output[index2][index] = c
        }
    }
    return output
}
