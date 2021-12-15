package day15

import common.InputRepo
import common.readSessionCookie
import common.solve
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sqrt

fun main(args: Array<String>) {
    val day = 15
    val input = InputRepo(args.readSessionCookie()).get(day = day)

    solve(day, input, ::solveDay15Part1, ::solveDay15Part2)
}


fun solveDay15Part1(input: List<String>): Int {
    val grid = parseDigits(input).convertToNodes()
    val start = grid[0][0]
    val end = grid.last().last()

    val pathfinder = DontKnowIfIRememberThisAlgorithm(grid, start, end)
    val path = pathfinder.findPath()

    return calculateTotalRisk(path)
}

fun solveDay15Part2(input: List<String>): Int {
    val grid = parseDigits(input).repeat(5).convertToNodes()
    val start = grid[0][0]
    val end = grid.last().last()

    val pathfinder = DontKnowIfIRememberThisAlgorithm(grid, start, end)
    val path = pathfinder.findPath()

    return calculateTotalRisk(path)
}

private fun calculateTotalRisk(path: List<Node>) =
    path.subList(1, path.size).sumOf { it.risk }

private fun List<List<Int>>.repeat(count: Int): List<List<Int>> {
    val horizontallyRepeated = map { line -> List(count) { line.increaseAllBy(it) }.flatten() }
    return List(count) { horizontallyRepeated.map { line -> line.increaseAllBy(it) } }.flatten()
}

private fun List<Int>.increaseAllBy(
    increase: Int,
    maxValue: Int = 9
) = map { it + increase }.map { if (it > maxValue) it - maxValue else it }

private fun parseDigits(input: List<String>): List<List<Int>> =
    input.map { line -> line.toCharArray().map { it.digitToInt() } }

private fun List<List<Int>>.convertToNodes(): Array<Array<Node>> =
    mapIndexed { y, line -> line.mapIndexed { x, risk -> Node(x, y, risk) }.toTypedArray() }
        .toTypedArray()

class DontKnowIfIRememberThisAlgorithm(
    private val grid: Array<Array<Node>>,
    start: Node,
    private val end: Node,
) {
    private val open = mutableSetOf<Node>(start)
    private val closed = mutableSetOf<Node>()

    private val width = grid[0].size
    private val height = grid.size

    fun findPath(): List<Node> {
        while (open.isNotEmpty()) {
            val lowestCostNode = open.minByOrNull { it.totalCost() }!!
            open.removeAll { it.x == lowestCostNode.x && it.y == lowestCostNode.y }
            closed.add(lowestCostNode)

            if (lowestCostNode == end) {
                return lowestCostNode.buildPath()
            }

            val neighbors = lowestCostNode.getNeighbors().filterNot { closed.contains(it) }
            val openNeighbors = neighbors.filter { open.contains(it) }
            neighbors.filterNot { open.contains(it) }.forEach { node ->
                node.recalculateCostWithParent(lowestCostNode)
                open.add(node)
            }
            openNeighbors.forEach { node ->
                val newCost = lowestCostNode.distanceToStart + lowestCostNode.risk + node.heuristicToEnd() + node.risk
                if (newCost < node.totalCost()) {
                    node.recalculateCostWithParent(lowestCostNode)
                }
            }
        }

        return emptyList()
    }

    private fun Node.recalculateCostWithParent(newParent: Node) {
        parent = newParent
        distanceToStart = newParent.distanceToStart + newParent.risk
    }

    private fun Node.totalCost(): Int {
        return distanceToStart + heuristicToEnd() + risk
    }

    private fun Node.heuristicToEnd(): Int {
        return sqrt((x - end.x).toDouble().pow(2.0) + (y - end.y.toDouble()).pow(2.0)).roundToInt()
    }

    private fun Node.getNeighbors(): List<Node> {
        return listOf(0 to 1, 0 to -1, -1 to 0, 1 to 0)
            .map { x + it.first to y + it.second }
            .filterNot { it.first < 0 || it.second < 0 || it.first >= width || it.second >= height }
            .map { grid[it.second][it.first] }
    }

    private fun Node.buildPath(): List<Node> {
        return (parent?.buildPath() ?: emptyList()) + this
    }
}

data class Node(val x: Int, val y: Int, val risk: Int, var distanceToStart: Int = 0, var parent: Node? = null)
