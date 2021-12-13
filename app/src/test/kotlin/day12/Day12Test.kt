package day12

import io.kotest.core.datatest.forAll
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

class Day12Test : FreeSpec({

    val sampleInput1: List<String> = listOf(
        "start-A",
        "start-b",
        "A-c",
        "A-b",
        "b-d",
        "A-end",
        "b-end",
    )
    val sampleInput2: List<String> = listOf(
        "dc-end",
        "HN-start",
        "start-kj",
        "dc-start",
        "dc-HN",
        "LN-dc",
        "HN-end",
        "kj-sa",
        "kj-HN",
        "kj-dc",
    )
    val sampleInput3 = listOf(
        "fs-end",
                "he-DX",
                "fs-he",
                "start-DX",
                "pj-DX",
                "end-zg",
                "zg-sl",
                "zg-pj",
                "pj-he",
                "RW-he",
                "fs-DX",
                "pj-RW",
                "zg-RW",
                "start-pj",
                "he-WI",
                "zg-he",
                "pj-fs",
                "start-RW",
    )

    val sampleSolutionPart1_1: Int = 10
    val sampleSolutionPart1_2: Int = 19
    val sampleSolutionPart1_3: Int = 226

    val sampleSolutionPart2_1: Int = 36
    val sampleSolutionPart2_2: Int = 103
    val sampleSolutionPart2_3: Int = 3509

    "Solving day 12" - {

        "part 1 for the sample input should return the correct output" {
            forAll(
                sampleInput1 to sampleSolutionPart1_1,
                sampleInput2 to sampleSolutionPart1_2,
                sampleInput3 to sampleSolutionPart1_3
            ) {
                solveDay12Part1(it.first) shouldBe it.second
            }
        }

        "part 2 for the sample input should return the correct output" {
            forAll(
                sampleInput1 to sampleSolutionPart2_1,
                sampleInput2 to sampleSolutionPart2_2,
                sampleInput3 to sampleSolutionPart2_3
            ) {
                solveDay12Part2(it.first) shouldBe it.second
            }
        }
    }
})
