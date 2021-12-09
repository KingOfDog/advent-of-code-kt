package day09

import common.InputRepo
import common.readSessionCookie
import common.solve

fun main(args: Array<String>) {
    val day = 9
    val input = InputRepo(args.readSessionCookie()).get(day = day)

    solve(day, input, ::solveDay09Part1, ::solveDay09Part2)
}

fun solveDay09Part1(input: List<String>): Int {
    val heights = input.map { row -> row.toCharArray().map { it.digitToInt() } }
    val width = heights[0].size
    val height = heights.size
    val lowPoints = mutableListOf<Int>()
    heights.forEachIndexed { y, row ->
        row.forEachIndexed { x, point ->
            val surroundingPoints = listOf(0 to 1, 0 to -1, -1 to 0, 1 to 0)
                .map { x + it.first to y + it.second }
                .filterNot { it.first < 0 || it.second < 0 || it.first >= width || it.second >= height }
                .map { heights[it.second][it.first] }
            if (surroundingPoints.none { it <= point }) {
                lowPoints.add(point)
            }
        }
    }
    return lowPoints.sumOf { it + 1 }
}

fun solveDay09Part2(input: List<String>): Int {
    val heights = input.map { row -> row.toCharArray().map { it.digitToInt() } }
    val width = heights[0].size
    val height = heights.size
    val points = heights.mapIndexed { y, row -> row.mapIndexed { x, _ -> Point(x, y) } }.flatten()
    val availablePoints = points.toMutableList()
    val usedPoints = mutableListOf<Point>()

    val basins = points.asSequence()
        .filterNot { heights[it.y][it.x] == 9 }
        .filter { availablePoints.contains(it) }
        .map {
            heights.findBasin(it, width, height, usedPoints).also { points ->
                availablePoints.removeAll(points)
                usedPoints.addAll(points)
                println("Completed step for $it and found $points")
            }
        }
        .toList()

    return basins.map { it.size }.sortedDescending().subList(0, 3).reduce { acc, i -> acc * i }
}

data class Point(val x: Int, val y: Int)

fun List<List<Int>>.findBasin(
    point: Point,
    width: Int,
    height: Int,
    alreadyVisited: List<Point> = emptyList()
): List<Point> {
    if (this[point.y][point.x] == 9) {
        return emptyList()
    }
    val neighbors = point.getNeighbors(width, height).filterNot { alreadyVisited.contains(it) }
    val visited = alreadyVisited + neighbors + listOf(point)
    val neighborBasins = mutableSetOf<Point>()
    for (neighbor in neighbors) {
        val basin = this.findBasin(neighbor, width, height, visited + neighborBasins)
        neighborBasins.addAll(basin)
    }
    return neighborBasins.toList() + listOf(point)
}

fun Point.getNeighbors(width: Int, height: Int): List<Point> {
    return listOf(0 to 1, 0 to -1, -1 to 0, 1 to 0)
        .map { Point(x + it.first, y + it.second) }
        .filterNot { it.x < 0 || it.y < 0 || it.x >= width || it.y >= height }
}
