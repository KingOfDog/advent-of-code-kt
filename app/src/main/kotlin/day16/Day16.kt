package day16

import common.InputRepo
import common.readSessionCookie
import common.solve

fun main(args: Array<String>) {
    val day = 16
    val input = InputRepo(args.readSessionCookie()).get(day = day)

    solve(day, input, ::solveDay16Part1, ::solveDay16Part2)
}


fun solveDay16Part1(input: List<String>): Int {
    val packet = parsePacketFromInput(input)

    return packet.versionSum()
}

fun solveDay16Part2(input: List<String>): Long {
    val packet = parsePacketFromInput(input)

    return packet.result()
}

private fun parsePacketFromInput(input: List<String>): Packet {
    val hexadecimal = input.first()
    val binary = hexadecimal.hexToBinary()

    println(binary)

    val packet = parsePacketType(binary)
    packet.parseContent(binary.withoutHeader())
    println(packet)
    return packet
}

fun String.hexToBinary(): String {
    val hexadecimalDigits = mapOf(
        '0' to "0000",
        '1' to "0001",
        '2' to "0010",
        '3' to "0011",
        '4' to "0100",
        '5' to "0101",
        '6' to "0110",
        '7' to "0111",
        '8' to "1000",
        '9' to "1001",
        'A' to "1010",
        'B' to "1011",
        'C' to "1100",
        'D' to "1101",
        'E' to "1110",
        'F' to "1111",
    )

    return map { hexadecimalDigits[it] }.joinToString("")
}

abstract class Packet(
    val version: Int,
    val typeID: Int,
) {

    abstract fun parseContent(content: String): String

    open fun versionSum(): Int {
        return version
    }

    abstract fun result(): Long
}

class ValuePacket(version: Int) : Packet(version, 4) {

    var value: Long = 0

    override fun parseContent(content: String): String {
        var valueBits = ""

        var remaining = content
        var goOn = true
        while (goOn && remaining.isNotEmpty()) {
            if (remaining[0] == '0') goOn = false
            valueBits += remaining.substring(1, 5)
            remaining = remaining.removeRange(0, 5)
        }

        value = valueBits.toLong(2)

        return remaining

    }

    override fun toString(): String {
        return "ValuePacket($version, $value)"
    }

    override fun result(): Long = value
}

class OperatorPacket(version: Int, typeID: Int) : Packet(version, typeID) {

    val subpackets = mutableListOf<Packet>()

    override fun parseContent(content: String): String {
        val lengthTypeID = content.substring(0, 1).toInt(2)

        if (lengthTypeID == 0) {
            val length = content.substring(1, 16).toInt(2)
            var remaining = content.substring(16, 16 + length)
            while (remaining.length > 6 && remaining.isNotBlank() && remaining.contains('1')) {
                val packet = parsePacketType(remaining)
                remaining = packet.parseContent(remaining.withoutHeader())
                subpackets.add(packet)
            }

            return content.substring(16 + length, content.length)
        } else if (lengthTypeID == 1) {
            val count = content.substring(1, 12).toInt(2)
            var remaining = content.substring(12, content.length)
            repeat(count) {
                val packet = parsePacketType(remaining)
                remaining = packet.parseContent(remaining.withoutHeader())
                subpackets.add(packet)
            }

            return remaining
        }

        return content
    }

    override fun toString(): String {
        return "OperatorPacket($version, $typeID, $subpackets)"
    }

    override fun versionSum(): Int {
        return version + subpackets.sumOf { it.versionSum() }
    }

    override fun result(): Long {
        val results = subpackets.map { it.result() }
        return when (typeID) {
            0 -> results.sum()
            1 -> results.reduce { acc, r -> acc * r }
            2 -> results.minOf { it }
            3 -> results.maxOf { it }
            5 -> if (results[0] > results[1]) 1 else 0
            6 -> if (results[0] < results[1]) 1 else 0
            7 -> if (results[0] == results[1]) 1 else 0
            else -> 0
        }
    }
}

fun parsePacketType(rawPacket: String): Packet {
    val version = rawPacket.substring(0, 3).toInt(radix = 2)
    val typeID = rawPacket.substring(3, 6).toInt(radix = 2)

    val packet = if (typeID == 4) {
        ValuePacket(version)
    } else {
        OperatorPacket(version, typeID)
    }

    return packet
}

fun String.withoutHeader(): String {
    return substring(6, length)
}
