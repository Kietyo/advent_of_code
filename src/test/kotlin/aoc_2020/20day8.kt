package aoc_2020

import utils.splitStringIntPartsOrNull
import kotlin.test.Test
import kotlin.test.assertEquals

internal class `20day8` {
    private val fileName = "day8"
    private val testFileName = "${fileName}_test"

    private enum class Operation {
        NOP, ACC, JMP
    }

    private data class Instruction(val op: Operation, val arg: Int)

    private fun List<String>.convertToDataObjectList() = run {
        map {
            val (opString, numString) = it.split(" ")
            val (signString, num) = numString.splitStringIntPartsOrNull()!!
            Instruction(when (opString) {
                "nop" -> Operation.NOP
                "acc" -> Operation.ACC
                "jmp" -> Operation.JMP
                else -> TODO()
            }, num * (when (signString) {
                "+" -> 1
                "-" -> -1
                else -> TODO()
            }))
        }
    }

    private fun List<Instruction>.runInstructions(): Int? {
        val instructionsPerformed = mutableSetOf<Int>()
        var acc = 0
        var idx = 0
        while (true) {
            if (idx in instructionsPerformed) return null
            instructionsPerformed.add(idx)
            val op = get(idx)
            println(op)
            when (op.op) {
                Operation.NOP -> Unit
                Operation.ACC -> acc += op.arg
                Operation.JMP -> idx += op.arg - 1
            }
            idx++
            if (idx == size) {
                break
            }
        }
        return acc
    }

    private fun part1Calculation(input: List<String>): Int {
        val converted = input.convertToDataObjectList()
        println(converted.joinToString("\n") { it.toString() })
        println("--------------------------------")

        val instructionsPerformed = mutableSetOf<Int>()
        var acc = 0
        var idx = 0
        while (true) {
            if (idx in instructionsPerformed) break
            instructionsPerformed.add(idx)
            val op = converted[idx]
            println(op)
            when (op.op) {
                Operation.NOP -> Unit
                Operation.ACC -> acc += op.arg
                Operation.JMP -> idx += op.arg - 1
            }
            idx++
            if (idx == converted.size) {
                break
            }
        }
        return acc
    }

    private fun part2Calculation(input: List<String>): Int {
        val converted = input.convertToDataObjectList()
        println(converted)

        for (i in converted.indices) {
            val instruction = converted[i]
            val newInstructions = when (instruction.op) {
                Operation.NOP -> {
                    val newInstructions = converted.toMutableList()
                    newInstructions[i] = Instruction(Operation.JMP, instruction.arg)
                    newInstructions
                }
                Operation.ACC -> continue
                Operation.JMP -> {
                    val newInstructions = converted.toMutableList()
                    newInstructions[i] = Instruction(Operation.NOP, instruction.arg)
                    newInstructions
                }
            }
            val acc = newInstructions.runInstructions()
            if (acc != null) {
                return acc
            }
        }
        return 0
    }

    @Test
    fun part1Test() {
        val input = readInput(testFileName)
        assertEquals(5, part1Calculation(input))
    }

    @Test
    fun part1() {
        val input = readInput(fileName)
        assertEquals(2014, part1Calculation(input))
    }

    @Test
    fun part2Test() {
        val input = readInput(testFileName)
        assertEquals(8, part2Calculation(input))
    }

    @Test
    fun part2() {
        val input = readInput(fileName)
        assertEquals(2251, part2Calculation(input))
    }
}