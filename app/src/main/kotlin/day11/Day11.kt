package day11

import common.InputRepo
import common.readSessionCookie
import common.solve

fun main(args: Array<String>) {
    val day = 11
    val input = InputRepo(args.readSessionCookie()).get(day = day)

    solve(day, input, ::solveDay11Part1, ::solveDay11Part2)
}


fun solveDay11Part1(input: List<String>): Int {
    var grid = parseInput(input)
    val steps = 100

    var counter = 0
    repeat(steps) {
        grid = increaseAllCounters(grid)
        val flashed = triggerFlashes(grid)
        flashed.forEach { grid[it.y][it.x] = 0 }

        counter += flashed.size
    }

    return counter
}

fun solveDay11Part2(input: List<String>): Int {
    var grid = parseInput(input)
    val octopusCount = grid.size * grid[0].size

    var step = 1
    while (true) {
        grid = increaseAllCounters(grid)
        val flashed = triggerFlashes(grid)
        if (flashed.size == octopusCount) return step

        flashed.forEach { grid[it.y][it.x] = 0 }

        step++
    }
}

private fun triggerFlashes(grid: Array<Array<Int>>): MutableSet<Point> {
    val flashed = mutableSetOf<Point>()
    var previousFlashed = -1
    while (previousFlashed != flashed.size) {
        previousFlashed = flashed.size

        grid.forEachIndexed { y, row ->
            row.forEachIndexed { x, octopus ->
                val point = Point(x, y)
                if (octopus > 9 && !flashed.contains(point)) {
                    flashed.add(point)
                    val neighbors = point.getNeighbors()
                    neighbors.forEach { grid[it.y][it.x]++ }
                }
            }
        }
    }
    return flashed
}

private fun increaseAllCounters(grid: Array<Array<Int>>): Array<Array<Int>> {
    return grid.map { row -> row.map { it + 1 }.toTypedArray() }.toTypedArray()
}

private fun parseInput(input: List<String>): Array<Array<Int>> {
    return input.map { row -> row.toCharArray().map { it.digitToInt() }.toTypedArray() }.toTypedArray()
}

data class Point(val x: Int, val y: Int)

fun Point.getNeighbors(width: Int = 10, height: Int = 10): List<Point> {
    return listOf(
        -1 to -1,
        -1 to 0,
        -1 to 1,
        0 to -1,
        0 to 1,
        1 to -1,
        1 to 0,
        1 to 1,
    )
        .map { Point(x + it.first, y + it.second) }
        .filterNot { it.x < 0 || it.y < 0 || it.x >= width || it.y >= height }
}
