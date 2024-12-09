package aoc_2024

import java.io.File

fun readInput(name: String) = File("src\\test\\resources\\aoc_2024", "$name.txt")
    .readLines()