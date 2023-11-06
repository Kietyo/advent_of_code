package aoc_2020

import java.io.File

fun readInput(name: String) = File("src\\test\\resources\\aoc_2020", "$name.txt")
    .readLines()