package day13

import common.InputRepo
import common.readSessionCookie
import common.solve

fun main(args: Array<String>) {
    val day = 13
    val input = InputRepo(args.readSessionCookie()).get(day = day)

    solve(day, input, ::solveDay13Part1, ::solveDay13Part2)
}


fun solveDay13Part1(input: List<String>): Int {
    val sectionEnd = input.indexOf("")
    val dots = parseDots(input, sectionEnd)
    val folds = parseFolds(input, sectionEnd)

    val paper = generatePaper(dots)

    val foldPaper = paper.foldAlong(folds[0])

    return foldPaper.flatten().count { it }
}

private fun parseDots(
    input: List<String>,
    sectionEnd: Int
): List<Pair<Int, Int>> {
    val dots = input.subList(0, sectionEnd).map { line -> line.split(",").map { it.toInt() } }
        .map { Pair(it[0], it[1]) }
    return dots
}

private fun parseFolds(
    input: List<String>,
    sectionEnd: Int
): List<FoldInstruction> {
    val regex = Regex("fold along (x|y)=(\\d+)")
    val folds = input.subList(sectionEnd + 1, input.size)
        .map { regex.find(it)!!.groupValues }
        .map { FoldInstruction(it[1] == "y", it[2].toInt()) }
    return folds
}

private fun generatePaper(dots: List<Pair<Int, Int>>): Array<Array<Boolean>> {
    val width = dots.maxOf { it.first } + 1
    val height = dots.maxOf { it.second } + 1
    return Array(height) { y -> Array(width) { x -> dots.contains(Pair(x, y)) } }
}

fun solveDay13Part2(input: List<String>): Int {
    val sectionEnd = input.indexOf("")
    val dots = parseDots(input, sectionEnd)
    val folds = parseFolds(input, sectionEnd)

    val paper = generatePaper(dots)

    val foldedPaper = folds.fold(paper) { paper, fold -> paper.foldAlong(fold) }

    foldedPaper.print()

    return -1
}

private fun Array<Array<Boolean>>.foldAlong(fold: FoldInstruction): Array<Array<Boolean>> {
        val folded = if (fold.horizontal) {
        this.sliceArray(0 until fold.foldPoint)
    } else {
        this.map { it.sliceArray(0 until fold.foldPoint) }.toTypedArray()
    }

    if (fold.horizontal) {
        for (i in (fold.foldPoint + 1) until size) {
            val offset = i - fold.foldPoint
            val y = folded.size - offset
            this[i].forEachIndexed { x, value ->
                folded[y][x] = folded[y][x] || value
            }
        }
    } else {
        for (i in (fold.foldPoint + 1) until (this[0].size)) {
            val offset = i - fold.foldPoint
            val x = folded[0].size - offset
            this.forEachIndexed { y, row ->
                folded[y][x] = folded[y][x] || row[i]
            }
        }
    }

    return folded
}

private fun Array<Array<Boolean>>.print() {
    forEach { row ->
        row.forEach { print(if (it) 'X' else ' ') }
        println()
    }
}

data class FoldInstruction(val horizontal: Boolean, val foldPoint: Int)
