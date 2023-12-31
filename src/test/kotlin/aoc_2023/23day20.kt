package aoc_2023

import com.kietyo.ktruth.assertThat
import kotlin.test.Test

internal class `23day20` {
    private val fileName = "day20"
    private val testFileName = "${fileName}_test"

    private fun List<String>.convertToDataObjectList() = run {
        this
    }

    enum class PulseType {
        LOW,HIGH
    }

    data class ConjunctionModuleState(
        val lowInputs: List<String>,
        val highInputs: List<String>
    ) {
        fun getPulse(): PulseType {
            if (lowInputs.isEmpty() && highInputs.isNotEmpty()) return PulseType.LOW
            return PulseType.HIGH
        }
    }

    data class State(
        val onFlipFlopModules: List<String>,
        val conjunctionModulesState: Map<String, ConjunctionModuleState>,
    ) {
        fun getPulseOfFlipFlopModule(module: String): PulseType {
            return if (module in onFlipFlopModules) PulseType.LOW else PulseType.HIGH
        }
        fun getPulseOfConjunctionModule(module: String): PulseType {
            return conjunctionModulesState[module]!!.getPulse()
        }
        fun newStateWithFlipFlopToggled(module: String): State {
            return if (module in onFlipFlopModules) {
                State(onFlipFlopModules - module, highConjunctionModules)
            } else {
                State(onFlipFlopModules + module, highConjunctionModules)
            }
        }
    }


    data class Pulse(
        val pulseType: PulseType,
        val receiver: String
    )

    private fun part1Calculation(input: List<String>): Int {
        val converted = input.convertToDataObjectList()
        println(converted)

        var broadcasterReceivers = emptyList<String>()
        val flipFlopModuleToReceivers = mutableMapOf<String, List<String>>()
        val conjunctionModuleToReceivers = mutableMapOf<String, List<String>>()

        converted.forEach {
            val (part1, part2) = it.split(" -> ")
            println("$part1, $part2")
            when {
                part1 == "broadcaster" -> broadcasterReceivers = part2.split(", ")
                part1.startsWith("%") -> flipFlopModuleToReceivers.put(part1.drop(1), part2.split(", "))
                part1.startsWith("&") -> conjunctionModuleToReceivers.put(part1.drop(1), part2.split(", "))
                else -> TODO()
            }
        }

        val conjunctionModuleToInputs = mutableMapOf<String, MutableList<String>>()
        conjunctionModuleToReceivers.forEach {
            conjunctionModuleToInputs.put(it.key, mutableListOf())
        }

        broadcasterReceivers.forEach {
            if (it in conjunctionModuleToInputs) {
                conjunctionModuleToInputs[it]!!.add("broadcaster")
            }
        }
        flipFlopModuleToReceivers.forEach {
            for (receiver in it.value) {
                if (receiver in conjunctionModuleToInputs) {
                    conjunctionModuleToInputs[receiver]!!.add(it.key)
                }
            }
        }
        conjunctionModuleToReceivers.forEach {
            for (receiver in it.value) {
                if (receiver in conjunctionModuleToInputs) {
                    conjunctionModuleToInputs[receiver]!!.add(it.key)
                }
            }
        }

        println("broadcasterReceivers: $broadcasterReceivers")
        println("flipFlopModuleToReceivers: $flipFlopModuleToReceivers")
        println("conjunctionModuleToReceivers: $conjunctionModuleToReceivers")
        println("conjunctionModuleToInputs: $conjunctionModuleToReceivers")

        var currState = State(emptyList(), emptyList())
        val pulses = mutableListOf(Pulse(PulseType.LOW, "broadcaster"))

//        while (pulses.isNotEmpty()) {
//            val currPulse = pulses.removeFirst()
//            when {
//                currPulse.receiver == "broadcaster" -> {
//                    for (receiver in broadcasterReceivers) {
//                        pulses.add(Pulse(currPulse.pulseType, receiver))
//                    }
//                }
//                flipFlopModuleToReceivers.contains(currPulse.receiver) -> {
//                    val nextReceivers = flipFlopModuleToReceivers[currPulse.receiver]!!
//                    when (currPulse.pulseType) {
//                        PulseType.LOW -> {
//                            for (receiver in nextReceivers) {
//                                pulses.add(Pulse(currState.getPulseOfFlipFlopModule(receiver), receiver))
//                            }
//                            currState = currState.newStateWithFlipFlopToggled(currPulse.receiver)
//                        }
//                        PulseType.HIGH -> Unit
//                    }
//                }
//                conjunctionModuleToReceivers.contains(currPulse.receiver) -> {
//                    val nextReceivers = conjunctionModuleToReceivers[currPulse.receiver]!!
//                    for (receiver in nextReceivers) {
//
//                    }
//                }
//            }
//        }

        return 0
    }

    private fun part2Calculation(input: List<String>): Int {
        val converted = input.convertToDataObjectList()
        println(converted)

        return 0
    }

    @Test
    fun understandWhen() {
        val blah = "abc"
        var executedA = false
        var executedB = false
        var executedC = false
        when {
            blah.contains("a") -> executedA = true
            blah.contains("b") -> executedB = true
            blah.contains("c") -> executedC = true
        }
        assertThat(executedA).isTrue()
        assertThat(executedB).isFalse()
        assertThat(executedC).isFalse()
    }

    @Test
    fun part1Test() {
        val input = readInput(testFileName)
        assertThat(part1Calculation(input)).isEqualTo(0)
    }

    @Test
    fun part1() {
        val input = readInput(fileName)
        assertThat(part1Calculation(input)).isEqualTo(0)
    }

    @Test
    fun part2Test() {
        val input = readInput(testFileName)
        assertThat(part2Calculation(input)).isEqualTo(0)
    }

    @Test
    fun part2() {
        val input = readInput(fileName)
        assertThat(part2Calculation(input)).isEqualTo(0)
    }
}