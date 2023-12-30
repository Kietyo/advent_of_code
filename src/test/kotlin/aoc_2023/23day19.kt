package aoc_2023

import com.kietyo.ktruth.assertThat
import length
import utils.splitByNewLine
import kotlin.test.Test

internal class `23day19` {
    private val fileName = "day19"
    private val testFileName = "${fileName}_test"

    private fun List<String>.convertToDataObjectList() = run {
        this
    }

    data class Args(
        val map: Map<String, Int>
    ) {
        val sum get() = map.values.sum()
        fun get(argName: String): Int {
            return map.get(argName)!!
        }
    }

    data class ArgRanges(
        val map: Map<String, IntRange> = mapOf(
            "x" to 1..4000,
            "m" to 1..4000,
            "a" to 1..4000,
            "s" to 1..4000,
        )
    ) {
        fun calculateNumCombinations(): Long {
            return get("x").length().toLong() *
                    get("m").length().toLong() *
                    get("a").length().toLong() *
                    get("s").length().toLong()
        }
        fun get(argName: String): IntRange {
            return map[argName]!!
        }

        // Returns <True range, False range>
        fun getTrueRangeOrNull(p: Predicate): Pair<ArgRanges?, ArgRanges?> {
            when (p) {
                is Predicate.GreaterThan -> {
                    val argRange = get(p.arg)
                    val trueRange = if (p.num+1 in argRange) {
                        ArgRanges(buildMap {
                            putAll(map)
                            put(p.arg, (p.num+1)..argRange.endInclusive)
                        })
                    } else {
                        null
                    }
                    val falseRange = if (p.num in argRange) {
                        ArgRanges(buildMap {
                            putAll(map)
                            put(p.arg, argRange.first..p.num)
                        })
                    } else {
                        null
                    }
                    return trueRange to falseRange
                }
                is Predicate.LessThan -> {
                    val argRange = get(p.arg)
                    val trueRange = if (p.num-1 in argRange) {
                        val newMap = map.toMutableMap()
                        newMap.put(p.arg, argRange.first..(p.num-1))
                        ArgRanges(newMap)
                    } else {
                        null
                    }

                    val falseRange = if (p.num in argRange) {
                        val newMap = map.toMutableMap()
                        newMap.put(p.arg, p.num..argRange.last)
                        ArgRanges(newMap)
                    } else {
                        null
                    }
                    return trueRange to falseRange
                }
                Predicate.True -> return this to null
            }
        }
    }

    sealed class Predicate {
        data class GreaterThan(val arg: String, val num: Int): Predicate() {
            override fun use(args: Args): Boolean {
                return args.get(arg) > num
            }
        }

        data class LessThan(val arg: String, val num: Int): Predicate() {
            override fun use(args: Args): Boolean {
                return args.get(arg) < num
            }
        }

        data object True: Predicate() {
            override fun use(args: Args): Boolean {
                return true
            }
        }

        abstract fun use(args: Args): Boolean
    }

    data class Rule(
        val nextWorkflow: String,
        val predicate: Predicate
    )

    data class Workflow(
        val name: String,
        val rules: List<Rule>
    )

    private fun part1Calculation(input: List<String>): Long {
        val converted = input.convertToDataObjectList()
        println(converted)

        val sections = converted.splitByNewLine()
        println(sections)

        val firstSection = sections.first()
        val regexOne = Regex("(\\w+)\\{(.*)}")

        val workflows = firstSection.map {
            val (workflowName, contents) = regexOne.matchEntire(it)!!.destructured
            println(workflowName)
            val rules = contents.split(",").map {
                if (it.contains(":")) {
                    val (predicateString, nextWorkflowName) = it.split(":")
                    if (predicateString.contains('<')) {
                        val (argName, numString) = predicateString.split('<')
                        Rule(nextWorkflowName, Predicate.LessThan(argName, numString.toInt()))
                    } else if (predicateString.contains('>')) {
                        val (argName, numString) = predicateString.split('>')
                        Rule(nextWorkflowName, Predicate.GreaterThan(argName, numString.toInt()))
                    } else {
                        TODO(predicateString)
                    }
                } else {
                    Rule(it, Predicate.True)
                }
            }
            Workflow(workflowName, rules)
        } + Workflow("A", listOf()) + Workflow("R", listOf())
        println(workflows)

        val secondSection = sections[1]
        val args = secondSection.map {
            Args(
                it.drop(1).dropLast(1).split(",").map {
                    val (argName, intString) = it.split("=")
                    argName to intString.toInt()
                }.toMap()
            )
        }
        println(args)

        var sum = 0L

        args.forEach {
            var currWorkflow = workflows.first { it.name == "in" }
            while (true) {
                for (rule in currWorkflow.rules) {
                    if (rule.predicate.use(it)) {
                        currWorkflow = workflows.first { it.name == rule.nextWorkflow }
                        break
                    }
                }
                if (currWorkflow.name == "A") {
                    sum += it.sum
                    break
                }
                if (currWorkflow.name == "R") {
                    break
                }
            }
        }
        println(sum)
        return sum
    }

    data class Node(
        val workflowName: String,
        val argRanges: ArgRanges
    )

    private fun part2Calculation(input: List<String>): Long {
        val converted = input.convertToDataObjectList()
        println(converted)

        val sections = converted.splitByNewLine()
        println(sections)

        val firstSection = sections.first()
        val regexOne = Regex("(\\w+)\\{(.*)}")

        val workflows = firstSection.map {
            val (workflowName, contents) = regexOne.matchEntire(it)!!.destructured
            println(workflowName)
            val rules = contents.split(",").map {
                if (it.contains(":")) {
                    val (predicateString, nextWorkflowName) = it.split(":")
                    if (predicateString.contains('<')) {
                        val (argName, numString) = predicateString.split('<')
                        Rule(nextWorkflowName, Predicate.LessThan(argName, numString.toInt()))
                    } else if (predicateString.contains('>')) {
                        val (argName, numString) = predicateString.split('>')
                        Rule(nextWorkflowName, Predicate.GreaterThan(argName, numString.toInt()))
                    } else {
                        TODO(predicateString)
                    }
                } else {
                    Rule(it, Predicate.True)
                }
            }
            Workflow(workflowName, rules)
        } + Workflow("A", listOf()) + Workflow("R", listOf())
        println(workflows)

        val nodes = mutableListOf<Node>(Node("in", ArgRanges()))
        var sum = 0L

        // 167409079868000

        while (nodes.isNotEmpty()) {
            val currNode = nodes.removeLast()
            if (currNode.workflowName == "A") {
                sum += currNode.argRanges.calculateNumCombinations()
                continue
            }
            if (currNode.workflowName == "R") {
                continue
            }
            var currArg = currNode.argRanges
            val currWorkflow = workflows.first { it.name == currNode.workflowName }
            for (rule in currWorkflow.rules) {
                val (trueRange, falseRange) = currArg.getTrueRangeOrNull(rule.predicate)
                if (trueRange != null) {
                    nodes.add(Node(rule.nextWorkflow, trueRange))
                }
                if (falseRange != null) {
                    currArg = falseRange
                }
            }
        }

        println(sum)

        return sum
    }

    @Test
    fun part1Test() {
        val input = readInput(testFileName)
        assertThat(part1Calculation(input)).isEqualTo(19114)
    }

    @Test
    fun part1() {
        val input = readInput(fileName)
        assertThat(part1Calculation(input)).isEqualTo(532551)
    }

    @Test
    fun part2Test() {
        val input = readInput(testFileName)
        assertThat(part2Calculation(input)).isEqualTo(167409079868000L)
    }

    @Test
    fun part2() {
        val input = readInput(fileName)
        assertThat(part2Calculation(input)).isEqualTo(134343280273968L)
    }
}