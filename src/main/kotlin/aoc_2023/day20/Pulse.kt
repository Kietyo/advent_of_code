package aoc_2023.day20

enum class PulseType {
    LOW, HIGH
}

data class Pulse(
    val pulseType: PulseType,
    val sender: Int,
    val receiver: Int
)

@JvmInline
value class PulseI(val data: Int) {
    constructor(pulseType: PulseType, sender: Int, receiver: Int):
            this((((pulseType.ordinal shl MASK_LENGTH) or receiver) shl MASK_LENGTH) or sender)
    val pulseType: PulseType get() = if ((data ushr MASK_LENGTH_DOUBLE) == 1) PulseType.HIGH else PulseType.LOW
    val sender: Int get() = (data and PACK_7_MASK)
    val receiver: Int get() = ((data ushr MASK_LENGTH) and PACK_7_MASK)
    companion object {
        const val PACK_7_MASK = 0b1111111
        const val MASK_LENGTH = 7
        const val MASK_LENGTH_DOUBLE = MASK_LENGTH * 2
    }
}