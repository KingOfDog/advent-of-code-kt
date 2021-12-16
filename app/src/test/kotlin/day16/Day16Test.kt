package day16

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

class Day16Test : FreeSpec({

    val sampleInputsPart1 = mapOf(
        "8A004A801A8002F478" to 16,
        "620080001611562C8802118E34" to 12,
        "C0015000016115A2E0802F182340" to 21,
        "A0016C880162017C3686B18A3D4780" to 31,
    )
    val sampleInputsPart2 = mapOf(
        "C200B40A82" to 3,
        "04005AC33890" to 54,
        "880086C3E88112" to 7,
        "CE00C43D881120" to 9,
        "D8005AC2A8F0" to 1,
        "F600BC2D8F" to 0,
        "9C005AC2F8F0" to 0,
        "9C0141080250320F1802104A08" to 1,
    )

    "Solving day 16" - {
        "part 1 for the sample input should return the correct output" {
            sampleInputsPart1.forEach { input, expected ->
                solveDay16Part1(listOf(input)) shouldBe expected
            }
        }

        "part 2 for the sample input should return the correct output" {
            sampleInputsPart2.forEach { input, expected ->
                solveDay16Part2(listOf(input)) shouldBe expected
            }
        }
    }
})
