package day13

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

class Day13Test : FreeSpec({

    val sampleInput: List<String> = listOf(
        "6,10",
        "0,14",
        "9,10",
        "0,3",
        "10,4",
        "4,11",
        "6,0",
        "6,12",
        "4,1",
        "0,13",
        "10,12",
        "3,4",
        "3,0",
        "8,4",
        "1,10",
        "2,14",
        "8,10",
        "9,0",
        "",
        "fold along y=7",
        "fold along x=5",
    )

    val sampleSolutionPart1: Int = 17

    val sampleSolutionPart2: Int = -1

    "Solving day 1" - {
        "part 1 for the sample input should return the correct output" {
            solveDay13Part1(sampleInput) shouldBe sampleSolutionPart1
        }

        "part 2 for the sample input should return the correct output" {
            solveDay13Part2(sampleInput) shouldBe sampleSolutionPart2
        }
    }
})
