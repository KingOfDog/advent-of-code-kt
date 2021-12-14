package day14

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

class Day14Test : FreeSpec({

    val sampleInput: List<String> = listOf(
        "NNCB",
        "",
        "CH -> B",
        "HH -> N",
        "CB -> H",
        "NH -> C",
        "HB -> C",
        "HC -> B",
        "HN -> C",
        "NN -> C",
        "BH -> H",
        "NC -> B",
        "NB -> B",
        "BN -> B",
        "BB -> N",
        "BC -> B",
        "CC -> N",
        "CN -> C",
    )

    val sampleSolutionPart1: Long = 1588

    val sampleSolutionPart2: Long = 2188189693529

    "Solving day 14" - {
        "part 1 for the sample input should return the correct output" {
            solveDay14Part1(sampleInput) shouldBe sampleSolutionPart1
        }

        "part 2 for the sample input should return the correct output" {
            solveDay14Part2(sampleInput) shouldBe sampleSolutionPart2
        }
    }
})
