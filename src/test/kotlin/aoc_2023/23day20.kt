package aoc_2023

import com.kietyo.ktruth.assertThat
import java.util.LinkedList
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

    class ConjunctionModuleState(
        private val inputIds: IntArray,
        private val lowInputs: BooleanArray,
        private val highInputs: BooleanArray
    ) {
        private fun hasNoLowInputs() = inputIds.all { !lowInputs[it.toInt()] }
        fun getPulse(): PulseType {
            if (hasNoLowInputs()) return PulseType.LOW
            return PulseType.HIGH
        }

        fun updateState(input: Int, pulse: PulseType) {
            val inputInt = input.toInt()
            when (pulse) {
                PulseType.LOW -> if (lowInputs[inputInt]) Unit else {
                    lowInputs[inputInt] = true
                    highInputs[inputInt] = false
                }
                PulseType.HIGH -> if (highInputs[inputInt]) Unit else {
                    lowInputs[inputInt] = false
                    highInputs[inputInt] = true
                }
            }
        }
    }

    class State(
        private val onFlipFlopModules: BooleanArray,
        private val conjunctionModulesState: Array<ConjunctionModuleState?>,
    ) {
        fun getPulseOfFlipFlopModule(module: Int): PulseType {
            return if (onFlipFlopModules[module.toInt()]) PulseType.LOW else PulseType.HIGH
        }

        fun getPulseOfConjunctionModule(module: Int): PulseType {
            return conjunctionModulesState[module.toInt()]!!.getPulse()
        }

        fun updateConjunctionModuleState(
            conjunctionModule: Int,
            inputModule: Int,
            pulse: PulseType
        ) {
            conjunctionModulesState[conjunctionModule.toInt()]!!.updateState(inputModule, pulse)
        }

        fun updateStateWithFlipFlopToggled(module: Int) {
            onFlipFlopModules[module.toInt()] = !onFlipFlopModules[module.toInt()]
        }
    }

    data class Pulse(
        val pulseType: PulseType,
        val sender: Int,
        val receiver: Int
    )


    @JvmInline
    value class PulseI(private val data: Int) {
        constructor(pulseType: PulseType, sender: Int, receiver: Int):
                this((((pulseType.ordinal shl MASK_LENGTH) or receiver) shl MASK_LENGTH) or sender)
        val pulseType: PulseType get() = if ((data ushr MASK_LENGTH_DOUBLE) == 1) PulseType.HIGH else PulseType.LOW
        val sender: Int get() = (data and PACK_6_MASK)
        val receiver: Int get() = ((data ushr MASK_LENGTH) and PACK_6_MASK)
        companion object {
            const val PACK_6_MASK = 0b1111111
            const val MASK_LENGTH = 7
            const val MASK_LENGTH_DOUBLE = MASK_LENGTH * 2
        }
    }

    object IdGenerator {
        private var currId = 0
        private val nameToId = mutableMapOf<String, Int>()
        fun getOrCreateId(name: String): Int {
            return nameToId.computeIfAbsent(name) { currId++ }
        }
        fun maxId() = currId
    }

    class Calculator(input: List<String>) {
        private var broadcasterReceivers = IntArray(0)
        private var flipFlopModuleToReceiversOptimized = Array(0) { IntArray(0) }
        private var conjunctionModuleToReceiversOptimized = Array(0) { IntArray(0) }
        private var conjunctionModuleToInputsOptimized = Array(0) { IntArray(0) }
        var numLowPulsesSent = 0
        var numHighPulsesSent = 0

        private val BROADCASTER_STRING = "broadcaster"
        private val BROADCASTER_ID = IdGenerator.getOrCreateId(BROADCASTER_STRING)

        private val BUTTON_STRING = "button"
        private val BUTTON_ID = IdGenerator.getOrCreateId(BUTTON_STRING)

        private val RX_STRING = "rx"
        val RX_ID = IdGenerator.getOrCreateId(RX_STRING)

        init {
            val flipFlopModuleToReceivers = mutableMapOf<Int, List<Int>>()
            val conjunctionModuleToReceivers = mutableMapOf<Int, List<Int>>()
            val conjunctionModuleToInputs = mutableMapOf<Int, MutableList<Int>>()

            input.forEach {
                val (part1, part2) = it.split(" -> ")
                println("$part1, $part2")
                when {
                    part1 == "broadcaster" -> broadcasterReceivers =
                        part2.split(", ").map { IdGenerator.getOrCreateId(it) }.toIntArray()

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

            flipFlopModuleToReceiversOptimized = Array(IdGenerator.maxId()) {
                val receivers = flipFlopModuleToReceivers.get(it)
                receivers?.toIntArray() ?: IntArray(0)
            }

            conjunctionModuleToReceiversOptimized = Array(IdGenerator.maxId()) {
                conjunctionModuleToReceivers.get(it)?.toIntArray()
                    ?: IntArray(0)
            }

            conjunctionModuleToInputsOptimized = Array(IdGenerator.maxId()) {
                conjunctionModuleToInputs.get(it)?.toIntArray()
                    ?: IntArray(0)
            }

            println("broadcasterReceivers: $broadcasterReceivers")
            println("flipFlopModuleToReceivers: $flipFlopModuleToReceivers")
            println("conjunctionModuleToReceivers: $conjunctionModuleToReceivers")
            println("conjunctionModuleToInputs: $conjunctionModuleToInputs")
            println("IdGenerator.maxId(): ${IdGenerator.maxId()}")
        }

        var startTimeNanos = System.nanoTime()
        val NUM_ITRS_PER_LOG = 1_000_000
        var pressesNeededForRx = 0L
        var found = false
        var maxPulsesSeen = 0

        private val BUTTON_PULSE = PulseI(PulseType.LOW, BUTTON_ID, BROADCASTER_ID)

        fun pressButton(currState: State) {
            pressesNeededForRx++
            val pulses = LinkedList<PulseI>()
            pulses.add(BUTTON_PULSE)

            if (pressesNeededForRx % NUM_ITRS_PER_LOG == 0L) {
                val currentTimeNano = System.nanoTime()
                val speed = (currentTimeNano - startTimeNanos) / NUM_ITRS_PER_LOG.toDouble()
                println("pressesNeededForRx: $pressesNeededForRx, nanos per press: ${speed}")
                startTimeNanos = System.nanoTime()
            }

            // 9,223,372,036,854,775,807
            //               906,000,000

            while (pulses.isNotEmpty()) {
                if (pulses.size > maxPulsesSeen) {
                    maxPulsesSeen = pulses.size
                    println("maxPulsesSeen: $maxPulsesSeen")
                }
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
                            pulses.add(PulseI(currPulse.pulseType, currPulse.receiver, receiver))
                        }
                    }
                    flipFlopModuleToReceiversOptimized[currPulse.receiver].isNotEmpty() -> {
                        val nextReceivers = flipFlopModuleToReceiversOptimized[currPulse.receiver]
                        when (currPulse.pulseType) {
                            PulseType.LOW -> {
                                for (receiver in nextReceivers) {
                                    pulses.add(
                                        PulseI(
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
                    conjunctionModuleToReceiversOptimized[currPulse.receiver.toInt()].isNotEmpty() -> {
                        currState.updateConjunctionModuleState(
                            currPulse.receiver,
                            currPulse.sender,
                            currPulse.pulseType
                        )
                        val nextReceivers =
                            conjunctionModuleToReceiversOptimized[currPulse.receiver.toInt()]
                        for (receiver in nextReceivers) {
                            val pulse = currState.getPulseOfConjunctionModule(currPulse.receiver)
                            pulses.add(PulseI(pulse, currPulse.receiver, receiver))
                        }
                    }
                }
            }
        }

        fun createInitialState() = State(
            BooleanArray(IdGenerator.maxId().toInt()),
            Array(IdGenerator.maxId().toInt()) {
                val ints = conjunctionModuleToInputsOptimized[it]
                if (ints.isNotEmpty()) {
                    ConjunctionModuleState(
                        ints,
                        BooleanArray(IdGenerator.maxId().toInt()) {
                            it in ints
                        },
                        BooleanArray(IdGenerator.maxId().toInt())
                    )
                } else {
                    null
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
    fun pulseI1() {
        val pulse = PulseI(PulseType.LOW, 1, 2)
        assertThat(pulse.pulseType).isEqualTo(PulseType.LOW)
        assertThat(pulse.sender).isEqualTo(1)
        assertThat(pulse.receiver).isEqualTo(2)
    }

    @Test
    fun pulseI2() {
        val pulse = PulseI(PulseType.HIGH, 53, 60)
        assertThat(pulse.pulseType).isEqualTo(PulseType.HIGH)
        assertThat(pulse.sender).isEqualTo(53)
        assertThat(pulse.receiver).isEqualTo(60)
    }

    @Test
    fun pulseI3() {
        repeat(2) { pulseTypeInt ->
            val pulseType = if (pulseTypeInt == 0) PulseType.LOW else PulseType.HIGH
            repeat(66) { sender ->
                repeat(66) { receiver ->
                    val pulse = PulseI(pulseType, sender, receiver)
                    assertThat(pulse.pulseType).isEqualTo(pulseType)
                    assertThat(pulse.sender).isEqualTo(sender)
                    assertThat(pulse.receiver).isEqualTo(receiver)
                }
            }
        }
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