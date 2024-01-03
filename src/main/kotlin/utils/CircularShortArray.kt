package utils

class CircularIntArray(maxSize: Int) {
    val backingArray = IntArray(maxSize)

    var currSize = 0
    var frontIdx = 0
    var backIdx = 0

    val maxSize get() = backingArray.size
    val size get() = currSize

    private fun getThenIncrementBackIndex(): Int {
        val curr = backIdx
        backIdx = (backIdx + 1) % backingArray.size
        currSize++
        if (currSize > backingArray.size)
            throw UnsupportedOperationException("Exceeded backing array size (${backingArray.size})")
        return curr
    }

    private fun incrementFrontIndex() {
        frontIdx = (frontIdx + 1) % backingArray.size
    }

    fun add(data: Int) {
        backingArray[getThenIncrementBackIndex()] = data
    }

    fun removeFirst(): Int {
        if (currSize <= 0) throw NoSuchElementException()
        val first = backingArray[frontIdx]
        incrementFrontIndex()
        return first
    }
}