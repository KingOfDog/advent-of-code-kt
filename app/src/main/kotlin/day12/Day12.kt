package day12

import common.InputRepo
import common.readSessionCookie
import common.solve
import java.nio.file.Path

fun main(args: Array<String>) {
    val day = 12
    val input = InputRepo(args.readSessionCookie()).get(day = day)

    solve(day, input, ::solveDay12Part1, ::solveDay12Part2)
}


fun solveDay12Part1(input: List<String>): Int {
    return execute(input, canVisitSmallTwice = false)
}

private fun execute(input: List<String>, canVisitSmallTwice: Boolean): Int {
    val caveConnections = input.map { line -> line.split("-") }.map { parts -> Cave(parts[0]) to Cave(parts[1]) }
    val caveGraph = Graph<Cave>()
    caveConnections.forEach { connection ->
        caveGraph.addEdge(connection.first, connection.second)
    }
    val start = Cave("start")

    val paths = caveGraph.getPossiblePaths(start, emptyList(), canVisitSmallTwice)

    return paths.size
}

fun solveDay12Part2(input: List<String>): Int {
    return execute(input, canVisitSmallTwice = true)
}

private fun Graph<Cave>.getPossiblePaths(from: Cave, path: List<Cave>, canVisitSmallTwice: Boolean): List<List<Cave>> {
    val pathWithSelf = path + from
    if (from.isEnd) {
        return listOf(pathWithSelf)
    }

    val possibilities = this.adjacencyMap.getOrDefault(from, emptySet())
        .filterNot { it.isStart }
        .filterNot { !canVisitSmallTwice && it.isSmall && path.contains(it) }
    return possibilities.map {
        getPossiblePaths(
            it,
            pathWithSelf,
            canVisitSmallTwice = canVisitSmallTwice && (!it.isSmall || !path.contains(it))
        )
    }.flatten().toSet().toList()
}

class Graph<T> {
    val adjacencyMap: HashMap<T, HashSet<T>> = HashMap()

    fun addEdge(sourceVertex: T, destinationVertex: T) {
        // Add edge to source vertex / node.
        adjacencyMap
            .computeIfAbsent(sourceVertex) { HashSet() }
            .add(destinationVertex)
        // Add edge to destination vertex / node.
        adjacencyMap
            .computeIfAbsent(destinationVertex) { HashSet() }
            .add(sourceVertex)
    }

    override fun toString(): String = StringBuffer().apply {
        for (key in adjacencyMap.keys) {
            append("$key -> ")
            append(adjacencyMap[key]?.joinToString(", ", "[", "]\n"))
        }
    }.toString()
}

data class Cave(val name: String) {
    val isSmall = name.lowercase() == name
    val isStart = name == "start"
    val isEnd = name == "end"
}
