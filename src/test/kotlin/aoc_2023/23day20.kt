package aoc_2023

import com.kietyo.ktruth.assertThat
import java.util.LinkedList
import java.util.Queue
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
        val lowInputs: MutableList<Int>,
        val highInputs: MutableList<Int>
    ) {
        fun getPulse(): PulseType {
            if (lowInputs.isEmpty() && highInputs.isNotEmpty()) return PulseType.LOW
            return PulseType.HIGH
        }
        fun updateState(input: Int, pulse: PulseType) {
            when (pulse) {
                PulseType.LOW -> if (input in lowInputs) Unit else {
                    lowInputs += input
                    highInputs -= input
                }
                PulseType.HIGH -> if (input in highInputs) Unit else {
                    lowInputs -= input
                    highInputs += input
                }
            }
        }
    }

    data class State(
        val onFlipFlopModules: MutableList<Int>,
        val conjunctionModulesState: Map<Int, ConjunctionModuleState>,
    ) {
        fun getPulseOfFlipFlopModule(module: Int): PulseType {
            return if (module in onFlipFlopModules) PulseType.LOW else PulseType.HIGH
        }

        fun getPulseOfConjunctionModule(module: Int): PulseType {
            return conjunctionModulesState[module]!!.getPulse()
        }

        fun updateConjunctionModuleState(conjunctionModule: Int, inputModule: Int, pulse: PulseType) {
            conjunctionModulesState[conjunctionModule]!!.updateState(inputModule, pulse)
        }

        fun updateStateWithFlipFlopToggled(module: Int) {
            if (module in onFlipFlopModules) {
                onFlipFlopModules -= module
            } else {
                onFlipFlopModules += module
            }
        }
    }

    data class Pulse(
        val pulseType: PulseType,
        val sender: Int,
        val receiver: Int
    )

    object IdGenerator {
        private var currId = 0
        private val nameToId = mutableMapOf<String, Int>()
        fun getOrCreateId(name: String): Int {
            return nameToId.computeIfAbsent(name) { currId++}
        }
        fun maxId() = currId
    }

    class Calculator(input: List<String>) {
        var broadcasterReceivers = IntArray(0)
        var flipFlopModuleToReceiversOptimized = Array<IntArray>(0) { IntArray(0) }
        val conjunctionModuleToReceivers = mutableMapOf<Int, List<Int>>()
        val conjunctionModuleToInputs = mutableMapOf<Int, MutableList<Int>>()
        var numLowPulsesSent  = 0
        var numHighPulsesSent  = 0

        private val BROADCASTER_STRING = "broadcaster"
        private val BROADCASTER_ID = IdGenerator.getOrCreateId(BROADCASTER_STRING)

        private val BUTTON_STRING = "button"
        private val BUTTON_ID = IdGenerator.getOrCreateId(BUTTON_STRING)

        private val RX_STRING = "rx"
        val RX_ID = IdGenerator.getOrCreateId(RX_STRING)

        init {
            val flipFlopModuleToReceivers = mutableMapOf<Int, List<Int>>()

            input.forEach {
                val (part1, part2) = it.split(" -> ")
                println("$part1, $part2")
                when {
                    part1 == "broadcaster" -> broadcasterReceivers = part2.split(", ").map { IdGenerator.getOrCreateId(it) }.toIntArray()
                    part1.startsWith("%") -> flipFlopModuleToReceivers.put(
                        IdGenerator.getOrCreateId(part1.drop(1)),
                        part2.split(", ").map { IdGenerator.getOrCreateId(it) }
                    )

                    part1.startsWith("&") -> conjunctionModuleToReceivers.put(
                        IdGenerator.getOrCreateId(part1.drop(1)),
                        part2.split(", ").map { IdGenerator.getOrCreateId(it) }
                    )

                    else -> TODO()
                }
            }

            conjunctionModuleToReceivers.forEach {
                conjunctionModuleToInputs.put(it.key, mutableListOf())
            }

            broadcasterReceivers.forEach {
                if (it in conjunctionModuleToInputs) {
                    conjunctionModuleToInputs[it]!!.add(BROADCASTER_ID)
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

            val flipFlopMaxKeyId = flipFlopModuleToReceivers.keys.max()
            flipFlopModuleToReceiversOptimized = Array(IdGenerator.maxId()) {
                val receivers = flipFlopModuleToReceivers.get(it)
                if (receivers == null) {
                    IntArray(0)
                } else {
                    receivers.toIntArray()
                }
            }

            println("broadcasterReceivers: $broadcasterReceivers")
            println("flipFlopModuleToReceivers: $flipFlopModuleToReceivers")
            println("conjunctionModuleToReceivers: $conjunctionModuleToReceivers")
            println("conjunctionModuleToInputs: $conjunctionModuleToInputs")
        }

        var startTimeMillis = System.currentTimeMillis()
        val NUM_ITRS_PER_LOG = 100_000
        var pressesNeededForRx = 0L
        var found = false

        private val BUTTON_PULSE = Pulse(PulseType.LOW, BUTTON_ID, BROADCASTER_ID)

        fun pressButton(currState: State) {
            pressesNeededForRx++
            val pulses = LinkedList<Pulse>()
            pulses.add(BUTTON_PULSE)

            if (pressesNeededForRx % NUM_ITRS_PER_LOG == 0L) {
                val currentTimeMillis = System.currentTimeMillis()
                val speed = (currentTimeMillis - startTimeMillis) / NUM_ITRS_PER_LOG.toDouble()
                println("pressesNeededForRx: $pressesNeededForRx, millis per press: ${speed}")
                startTimeMillis = System.currentTimeMillis()
            }

            while (pulses.isNotEmpty()) {
                val currPulse = pulses.removeFirst()
                when (currPulse.pulseType) {
                    PulseType.LOW -> numLowPulsesSent++
                    PulseType.HIGH -> numHighPulsesSent++
                }
                if (currPulse.receiver == RX_ID && currPulse.pulseType == PulseType.LOW) {
                    println("Found after $pressesNeededForRx presses.")
                    found = true
                    break
                }
                when {
                    currPulse.receiver == BROADCASTER_ID -> {
                        for (receiver in broadcasterReceivers) {
                            pulses.add(Pulse(currPulse.pulseType, currPulse.receiver, receiver))
                        }
                    }

                    flipFlopModuleToReceiversOptimized[currPulse.receiver].isNotEmpty() -> {
                        val nextReceivers = flipFlopModuleToReceiversOptimized[currPulse.receiver]
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
                                currState.updateStateWithFlipFlopToggled(currPulse.receiver)
                            }
                            PulseType.HIGH -> Unit
                        }
                    }

                    conjunctionModuleToReceivers.contains(currPulse.receiver) -> {
                        currState.updateConjunctionModuleState(currPulse.receiver, currPulse.sender, currPulse.pulseType)
                        val nextReceivers = conjunctionModuleToReceivers[currPulse.receiver]!!
                        for (receiver in nextReceivers) {
                            val pulse = currState.getPulseOfConjunctionModule(currPulse.receiver)
                            pulses.add(Pulse(pulse, currPulse.receiver, receiver))
                        }
                    }
                }
            }
        }

        fun createInitialState() = State(mutableListOf(), buildMap {
            conjunctionModuleToInputs.forEach {
                put(it.key, ConjunctionModuleState(it.value, mutableListOf()))
            }
        })

        fun calculatePart1(): Long {
            val currState = createInitialState()
            repeat(1000) {
                pressButton(currState)
            }

            return numLowPulsesSent.toLong() * numHighPulsesSent
        }
    }

    private fun part1Calculation(input: List<String>): Long {
        val converted = input.convertToDataObjectList()
        println(converted)

        val calculator = Calculator(converted)

        return calculator.calculatePart1()
    }

    private fun part2Calculation(input: List<String>): Int {
        val converted = input.convertToDataObjectList()
        println(converted)

        val calculator = Calculator(converted)
        val currState = calculator.createInitialState()

        while (!calculator.found) {
            calculator.pressButton(currState)
        }

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
        assertThat(part1Calculation(input)).isEqualTo(949764474L)
    }

//    @Test
//    fun part2Test() {
//        val input = readInput(testFileName)
//        assertThat(part2Calculation(input)).isEqualTo(0)
//    }
//
    @Test
    fun part2() {
        val input = readInput(fileName)
        assertThat(part2Calculation(input)).isEqualTo(0)
    }
}