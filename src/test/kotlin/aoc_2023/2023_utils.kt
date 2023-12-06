package aoc_2023

import java.io.File

fun readInput(name: String) = File("src\\test\\resources\\aoc_2023", "$name.txt")
    .readLines()