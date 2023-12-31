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
        LOW, HIGH
    }

    data class ConjunctionModuleState(
        val lowInputs: List<String>,
        val highInputs: List<String>
    ) {
        fun getPulse(): PulseType {
            if (lowInputs.isEmpty() && highInputs.isNotEmpty()) return PulseType.LOW
            return PulseType.HIGH
        }
        fun updateState(input: String, pulse: PulseType): ConjunctionModuleState {
            return when (pulse) {
                PulseType.LOW -> if (input in lowInputs) this else ConjunctionModuleState(lowInputs + input, highInputs - input)
                PulseType.HIGH -> if (input in highInputs) this else ConjunctionModuleState(lowInputs - input, highInputs + input)
            }
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

        fun updateConjunctionModuleState(conjunctionModule: String, inputModule: String, pulse: PulseType): State {
            return State(onFlipFlopModules, buildMap {
                putAll(conjunctionModulesState)
                put(conjunctionModule, get(conjunctionModule)!!.updateState(inputModule, pulse))
            })
        }

        fun newStateWithFlipFlopToggled(module: String): State {
            return if (module in onFlipFlopModules) {
                State(onFlipFlopModules - module, conjunctionModulesState)
            } else {
                State(onFlipFlopModules + module, conjunctionModulesState)
            }
        }
    }

    data class Pulse(
        val pulseType: PulseType,
        val sender: String,
        val receiver: String
    )

    private fun part1Calculation(input: List<String>): Long {
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
                part1.startsWith("%") -> flipFlopModuleToReceivers.put(
                    part1.drop(1),
                    part2.split(", ")
                )

                part1.startsWith("&") -> conjunctionModuleToReceivers.put(
                    part1.drop(1),
                    part2.split(", ")
                )

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
        println("conjunctionModuleToInputs: $conjunctionModuleToInputs")

        var numLowPulsesSent  = 0
        var numHighPulsesSent  = 0

        var currState = State(emptyList(), buildMap {
            conjunctionModuleToInputs.forEach {
                put(it.key, ConjunctionModuleState(it.value, emptyList()))
            }
        })
        repeat(1000) {
            val pulses = mutableListOf(Pulse(PulseType.LOW, "button", "broadcaster"))

            while (pulses.isNotEmpty()) {
                val currPulse = pulses.removeFirst()
                when (currPulse.pulseType) {
                    PulseType.LOW -> numLowPulsesSent++
                    PulseType.HIGH -> numHighPulsesSent++
                }
//                println(currPulse)
                when {
                    currPulse.receiver == "broadcaster" -> {
                        for (receiver in broadcasterReceivers) {
                            pulses.add(Pulse(currPulse.pulseType, currPulse.receiver, receiver))
                        }
                    }

                    flipFlopModuleToReceivers.contains(currPulse.receiver) -> {
                        val nextReceivers = flipFlopModuleToReceivers[currPulse.receiver]!!
                        when (currPulse.pulseType) {
                            PulseType.LOW -> {
                                for (receiver in nextReceivers) {
                                    pulses.add(
                                        Pulse(
                                            currState.getPulseOfFlipFlopModule(currPulse.receiver),
                                            currPulse.receiver,
                                            receiver
                                        )
                                    )
                                }
                                currState = currState.newStateWithFlipFlopToggled(currPulse.receiver)
                            }
                            PulseType.HIGH -> Unit
                        }
                    }

                    conjunctionModuleToReceivers.contains(currPulse.receiver) -> {
                        currState = currState.updateConjunctionModuleState(currPulse.receiver, currPulse.sender, currPulse.pulseType)
                        val nextReceivers = conjunctionModuleToReceivers[currPulse.receiver]!!
                        for (receiver in nextReceivers) {
                            val pulse = currState.getPulseOfConjunctionModule(currPulse.receiver)
                            pulses.add(Pulse(pulse, currPulse.receiver, receiver))
                        }
                    }
                }
            }
        }

        println("numLowPulsesSent: $numLowPulsesSent")
        println("numHighPulsesSent: $numHighPulsesSent")

        return numLowPulsesSent.toLong() * numHighPulsesSent
    }

    private fun part2Calculation(input: List<String>): Int {
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
                part1.startsWith("%") -> flipFlopModuleToReceivers.put(
                    part1.drop(1),
                    part2.split(", ")
                )

                part1.startsWith("&") -> conjunctionModuleToReceivers.put(
                    part1.drop(1),
                    part2.split(", ")
                )

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
        println("conjunctionModuleToInputs: $conjunctionModuleToInputs")

        var numLowPulsesSent  = 0
        var numHighPulsesSent  = 0

        var currState = State(emptyList(), buildMap {
            conjunctionModuleToInputs.forEach {
                put(it.key, ConjunctionModuleState(it.value, emptyList()))
            }
        })
        var pressesNeededForRx = 0L
        var found = false
        while (!found) {
            pressesNeededForRx++
            val pulses = mutableListOf(Pulse(PulseType.LOW, "button", "broadcaster"))

            if (pressesNeededForRx % 100_000 == 0L) {
                println("pressesNeededForRx: $pressesNeededForRx")
            }

            while (pulses.isNotEmpty()) {
                val currPulse = pulses.removeFirst()
                when (currPulse.pulseType) {
                    PulseType.LOW -> numLowPulsesSent++
                    PulseType.HIGH -> numHighPulsesSent++
                }
                if (currPulse.receiver == "rx" && currPulse.pulseType == PulseType.LOW) {
                    println("Found after $pressesNeededForRx presses.")
                    found = true
                    break
                }
                //                println(currPulse)
                when {
                    currPulse.receiver == "broadcaster" -> {
                        for (receiver in broadcasterReceivers) {
                            pulses.add(Pulse(currPulse.pulseType, currPulse.receiver, receiver))
                        }
                    }

                    flipFlopModuleToReceivers.contains(currPulse.receiver) -> {
                        val nextReceivers = flipFlopModuleToReceivers[currPulse.receiver]!!
                        when (currPulse.pulseType) {
                            PulseType.LOW -> {
                                for (receiver in nextReceivers) {
                                    pulses.add(
                                        Pulse(
                                            currState.getPulseOfFlipFlopModule(currPulse.receiver),
                                            currPulse.receiver,
                                            receiver
                                        )
                                    )
                                }
                                currState = currState.newStateWithFlipFlopToggled(currPulse.receiver)
                            }
                            PulseType.HIGH -> Unit
                        }
                    }

                    conjunctionModuleToReceivers.contains(currPulse.receiver) -> {
                        currState = currState.updateConjunctionModuleState(currPulse.receiver, currPulse.sender, currPulse.pulseType)
                        val nextReceivers = conjunctionModuleToReceivers[currPulse.receiver]!!
                        for (receiver in nextReceivers) {
                            val pulse = currState.getPulseOfConjunctionModule(currPulse.receiver)
                            pulses.add(Pulse(pulse, currPulse.receiver, receiver))
                        }
                    }
                }
            }
        }

        println("numLowPulsesSent: $numLowPulsesSent")
        println("numHighPulsesSent: $numHighPulsesSent")

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
        assertThat(part1Calculation(input)).isEqualTo(32000000L)
    }

    @Test
    fun part1Test2() {
        val input = readInput("day20_test2")
        assertThat(part1Calculation(input)).isEqualTo(11687500L)
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