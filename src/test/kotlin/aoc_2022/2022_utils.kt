package aoc_2022

import java.io.File

fun readInput(name: String) = File("src\\test\\resources\\aoc_2022", "$name.txt")
    .readLines()