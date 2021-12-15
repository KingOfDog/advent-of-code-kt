package day15

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

class Day15Test : FreeSpec({

    val sampleInput: List<String> = listOf(
        "1163751742",
         "1381373672",
         "2136511328",
         "3694931569",
         "7463417111",
         "1319128137",
         "1359912421",
         "3125421639",
         "1293138521",
         "2311944581",
    )

    val sampleSolutionPart1: Int = 40

    val sampleSolutionPart2: Int = 315

    "Solving day 15" - {
        "part 1 for the sample input should return the correct output" {
            solveDay15Part1(sampleInput) shouldBe sampleSolutionPart1
        }

        "part 2 for the sample input should return the correct output" {
            solveDay15Part2(sampleInput) shouldBe sampleSolutionPart2
        }
    }
})
