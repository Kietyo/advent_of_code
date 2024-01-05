package benchmarks

import aoc_2023.day20.Pulse
import aoc_2023.day20.PulseI
import aoc_2023.day20.PulseType
import com.kietyo.ktruth.assertThat
import kotlinx.benchmark.*
import utils.CircularIntArray
import java.util.LinkedList
import java.util.concurrent.TimeUnit

@State(Scope.Benchmark)
@Warmup(iterations = 0)
@Measurement(iterations = 1, time = 1, timeUnit = TimeUnit.NANOSECONDS)
private class TestBenchmark {
    val pulseValueClass = PulseI(PulseType.HIGH, 66, 65)
    val pulseDataClass = Pulse(PulseType.HIGH, 66, 65)

//    @Benchmark
//    fun pulseValueClassGetPulseType(): Unit {
//        assertThat(pulseValueClass.pulseType).isEqualTo(PulseType.HIGH)
//    }
//
//    @Benchmark
//    fun pulseValueClassGetPulseTypeV2(): Unit {
//        assertThat(pulseValueClass.pulseTypeV2).isEqualTo(PulseType.HIGH)
//    }
//
//    @Benchmark
//    fun pulseValueClassGetPulseTypeV3(): Unit {
//        assertThat(pulseValueClass.pulseTypeV3).isEqualTo(1)
//    }

    @Benchmark
    fun pulseValueClassGetPulseType(): Unit {
        assert(pulseValueClass.pulseType == PulseType.HIGH)
    }

    @Benchmark
    fun pulseValueClassGetPulseTypeV2(): Unit {
        assert(pulseValueClass.pulseTypeV2 == PulseType.HIGH)
    }

    @Benchmark
    fun pulseValueClassGetPulseTypeV3(): Unit {
        assert(pulseValueClass.pulseTypeV3 == 1)
    }

    //
    //    @Benchmark
    //    fun pulseValueClassGetSender(): Unit {
    //        assertThat(pulseValueClass.sender).isEqualTo(66)
    //    }
    //
    //    @Benchmark
    //    fun pulseValueClassGetReceiver(): Unit {
    //        assertThat(pulseValueClass.receiver).isEqualTo(65)
    //    }
    //
    //    @Benchmark
    //    fun pulseDataClassGetPulseType(): Unit {
    //        assertThat(pulseDataClass.pulseType).isEqualTo(PulseType.HIGH)
    //    }
    //
    //    @Benchmark
    //    fun pulseDataClassGetSender(): Unit {
    //        assertThat(pulseDataClass.sender).isEqualTo(66)
    //    }
    //
    //    @Benchmark
    //    fun pulseDataClassGetReceiver(): Unit {
    //        assertThat(pulseDataClass.receiver).isEqualTo(65)
    //    }
    //
    //    @Benchmark
    //    fun constructPulseValueClass(): Int {
    //        val p = PulseI(PulseType.HIGH, 66, 65)
    //        return p.receiver
    //    }
    //
    //    @Benchmark
    //    fun constructPulseDataClass(): Int {
    //        val p = Pulse(PulseType.HIGH, 66, 65)
    //        return p.receiver
    //    }

    //    @Benchmark
    //    fun pulseValueClass2(): Unit {
    //        repeat(2) { pulseTypeInt ->
    //            val pulseType = if (pulseTypeInt == 0) PulseType.LOW else PulseType.HIGH
    //            repeat(66) { sender ->
    //                repeat(66) { receiver ->
    //                    val pulse = PulseI(pulseType, sender, receiver)
    //                    assertThat(pulse.pulseTypeV2).isEqualTo(pulseType)
    //                    //                    assertThat(pulse.sender).isEqualTo(sender)
    //                    //                    assertThat(pulse.receiver).isEqualTo(receiver)
    //                }
    //            }
    //        }
    //    }

//    @Benchmark
//    fun pulseValueClass(): Unit {
//        repeat(2) { pulseTypeInt ->
//            val pulseType = if (pulseTypeInt == 0) PulseType.LOW else PulseType.HIGH
//            repeat(66) { sender ->
//                repeat(66) { receiver ->
//                    val pulse = PulseI(pulseType, sender, receiver)
//                    //                    assertThat(pulse.pulseType).isEqualTo(pulseType)
//                    assertThat(pulse.sender).isEqualTo(sender)
//                    //                    assertThat(pulse.receiver).isEqualTo(receiver)
//                }
//            }
//        }
//    }
//
//    @Benchmark
//    fun pulseDataClass() {
//        repeat(2) { pulseTypeInt ->
//            val pulseType = if (pulseTypeInt == 0) PulseType.LOW else PulseType.HIGH
//            repeat(66) { sender ->
//                repeat(66) { receiver ->
//                    val pulse = Pulse(pulseType, sender, receiver)
//                    //                    assertThat(pulse.pulseType).isEqualTo(pulseType)
//                    assertThat(pulse.sender).isEqualTo(sender)
//                    //                    assertThat(pulse.receiver).isEqualTo(receiver)
//                }
//            }
//        }
//    }

    //    @Benchmark
    //    fun listPulseValueClass(): Unit {
    //        val pulses = CircularIntArray(2 * 66 * 66 + 1)
    //        repeat(2) { pulseTypeInt ->
    //            val pulseType = if (pulseTypeInt == 0) PulseType.LOW else PulseType.HIGH
    //            repeat(66) { sender ->
    //                repeat(66) { receiver ->
    //                    val pulse = PulseI(pulseType, sender, receiver)
    //                    assertThat(pulse.pulseType).isEqualTo(pulseType)
    //                    assertThat(pulse.sender).isEqualTo(sender)
    //                    assertThat(pulse.receiver).isEqualTo(receiver)
    //                    pulses.add(pulse.data)
    //                }
    //            }
    //        }
    //        while (pulses.isNotEmpty()) {
    //            pulses.removeFirst()
    //        }
    //    }
    //
    //    @Benchmark
    //    fun listPulseDataClass() {
    //        val pulses = LinkedList<Pulse>()
    //        repeat(2) { pulseTypeInt ->
    //            val pulseType = if (pulseTypeInt == 0) PulseType.LOW else PulseType.HIGH
    //            repeat(66) { sender ->
    //                repeat(66) { receiver ->
    //                    val pulse = Pulse(pulseType, sender, receiver)
    //                    assertThat(pulse.pulseType).isEqualTo(pulseType)
    //                    assertThat(pulse.sender).isEqualTo(sender)
    //                    assertThat(pulse.receiver).isEqualTo(receiver)
    //                    pulses.add(pulse)
    //                }
    //            }
    //        }
    //
    //        while (pulses.isNotEmpty()) {
    //            pulses.removeFirst()
    //        }
    //    }
    // 2254308930
    // 1134410660
}