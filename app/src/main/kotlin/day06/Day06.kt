package day06

import common.InputRepo
import common.readSessionCookie
import common.solve

fun main(args: Array<String>) {
    val day = 6
    val input = InputRepo(args.readSessionCookie()).get(day = day)

    solve(day, input, ::solveDay06Part1, ::solveDay06Part2)
}

fun solveDay06Part1(input: List<String>): Int {
    val numberOfDays = 80
    val fish = input.first().split(",").map { it.toInt() }
        .overfishing(numberOfDays)
    return howMuchIsTheFish(fish)
}

fun solveDay06Part2(input: List<String>): Long {
    val numberOfDays = 256
    val fishingNet = parseFishingNet(input)

    for (d in 1..numberOfDays) {
        decreaseAllCounters(fishingNet)
        resetAndGiveBirth(fishingNet)
    }

    return howMuchIsTheFish(fishingNet)
}

private fun decreaseAllCounters(fishingNet: MutableMap<Int, Long>) {
    for (i in 0..8) {
        fishingNet[i - 1] = fishingNet.getOrZero(i)
        fishingNet[i] = 0
    }
}

private fun resetAndGiveBirth(fishingNet: MutableMap<Int, Long>) {
    fishingNet[6] = fishingNet.getOrZero(6) + fishingNet.getOrZero(-1)
    fishingNet[8] = fishingNet.getOrZero(8) + fishingNet.getOrZero(-1)
    fishingNet[-1] = 0
}

@OptIn(ExperimentalStdlibApi::class)
private fun parseFishingNet(input: List<String>): MutableMap<Int, Long> {
    val fish = input.first().split(",").map { it.toInt() }
    val fishingNet = buildMap<Int, Long> {
        fish.forEach {
            this.increase(it)
        }
    }.toMutableMap()
    return fishingNet
}

private fun howMuchIsTheFish(fish: List<Int>) = fish.size
private fun howMuchIsTheFish(fishingNet: Map<Int, Long>) = fishingNet.values.sum()

private fun List<Int>.overfishing(
    numberOfDays: Int
): List<Int> {
    var soundsFishy = this
    for (d in 1..numberOfDays) {
        val offspring = soundsFishy.filter { it == 0 }.map { 8 }
        soundsFishy = soundsFishy.map { it - 1 }.map { if (it < 0) it + 7 else it }
        soundsFishy = soundsFishy + offspring
    }
    return soundsFishy
}

fun MutableMap<Int, Long>.increase(key: Int) {
    this[key] = getOrDefault(key, 0) + 1
}

private fun Map<Int, Long>.getOrZero(number: Int) =
    getOrDefault(number, 0)
