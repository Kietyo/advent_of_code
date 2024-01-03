package utils

class CircularIntArray(maxSize: Int) {
    val backingArray = IntArray(maxSize)

    var currSize = 0
    var frontIdx = 0
    var backIdx = 0

    val maxSize get() = backingArray.size
    val size get() = currSize

    fun clear() {
        currSize = 0
        frontIdx = 0
        backIdx = 0
    }

    private fun getThenIncrementBackIndex(): Int {
        if (currSize+1 > backingArray.size)
            throw UnsupportedOperationException("Exceeded backing array size (${backingArray.size})")
        val curr = backIdx
        backIdx = (backIdx + 1) % backingArray.size
        currSize++
        return curr
    }

    private fun incrementFrontIndex() {
        frontIdx = (frontIdx + 1) % backingArray.size
        currSize--
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

    fun isEmpty() = currSize == 0
    fun isNotEmpty() = currSize > 0
}