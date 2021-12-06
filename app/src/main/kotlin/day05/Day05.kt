package day05

import common.InputRepo
import common.readSessionCookie
import common.solve
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.roundToInt

fun main(args: Array<String>) {
    val day = 5
    val input = InputRepo(args.readSessionCookie()).get(day = day)

    solve(day, input, ::solveDay05Part1, ::solveDay05Part2)
}


fun solveDay05Part1(input: List<String>): Int {
    val lines = parseInput(input)

    val points = mutableMapOf<Point, Int>()
    lines
        .filterNot { it.isDiagonal }
        .forEach { line ->
            for (i in 0..line.distance) {
                val point = line.getPointAt(i)
                points[point] = points.getOrDefault(point, 0) + 1
            }
        }

    return points.countDuplicates()
}


fun solveDay05Part2(input: List<String>): Int {
    val lines = parseInput(input)

    val points = mutableMapOf<Point, Int>()
    lines.forEach { line ->
        for (i in 0..line.distance) {
            val point = line.getPointAt(i)
            points[point] = points.getOrDefault(point, 0) + 1
        }
    }

    return points.countDuplicates()
}

private fun Map<Point, Int>.countDuplicates() = filterValues { it > 1 }.count()

private fun parseInput(input: List<String>): List<day05.Line> {
    val regex = Regex("(\\d+),(\\d+) -> (\\d+),(\\d+)")
    return input.map { regex.find(it)!!.groupValues }
        .map { values -> values.subList(1, values.size).map { it.toInt() } }
        .map { numbers -> Line(Point(numbers[0], numbers[1]), Point(numbers[2], numbers[3])) }
}

data class Point(val x: Int, val y: Int)
data class Line(val a: Point, val b: Point) {

    val isHorizontal: Boolean get() = a.y == b.y
    val isVertical: Boolean get() = a.x == b.x
    val isDiagonal: Boolean get() = !isHorizontal && !isVertical

    val distance: Int get() = max(abs(a.x - b.x), abs(a.y - b.y))

    fun getPointAt(pos: Int): Point {
        val x = lerp(a.x.toDouble(), b.x.toDouble(), pos / distance.toDouble()).roundToInt()
        val y = lerp(a.y.toDouble(), b.y.toDouble(), pos / distance.toDouble()).roundToInt()
        return Point(x, y)
    }
}

private fun lerp(x: Double, y: Double, t: Double): Double {
    return x * (1 - t) + y * t
}
