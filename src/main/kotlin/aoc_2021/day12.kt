internal sealed class Day12Node {
    data object Start : Day12Node()
    data object Finish : Day12Node()
    data class BigCave(val id: String) : Day12Node()
    data class SmallCave(val id: String) : Day12Node()
    companion object {
        fun parse(input: String): Day12Node {
            return when {
                input == "start" -> Start
                input == "end" -> Finish
                input.all { it.isUpperCase() } -> BigCave(input)
                input.all { it.isLowerCase() } -> SmallCave(input)
                else -> TODO()
            }
        }
    }
}


fun main() {
    data class Edge(val from: Day12Node, val to: Day12Node)

    data class SearchState(
        val currentPath: List<Day12Node>,
    ) {
        val exploredSmallCaves: Set<Day12Node.SmallCave>
            get() = currentPath.filterIsInstance<Day12Node.SmallCave>().toSet()
    }

    data class SearchState2(
        val currentPath: List<Day12Node>,
        val allowedSmallCaveToVisitTwice: Day12Node.SmallCave
    ) {
        val exploredSmallCaves: Set<Day12Node.SmallCave>
            get() = currentPath.filterIsInstance<Day12Node.SmallCave>().toSet()

        val allowedSmallCaveHasBeenVisitedTwice: Boolean
            get() = currentPath.count { it == allowedSmallCaveToVisitTwice } == 2
    }

    fun getEdgesWithFrom(edges: List<Edge>, from: Day12Node): List<Edge> {
        return edges.filter { it.from == from }
    }

    fun getAllAvailableSmallCaves(edges: List<Edge>): Set<Day12Node.SmallCave> {
        return edges.map { it.from }.filterIsInstance<Day12Node.SmallCave>().toSet()
    }

    fun part1(inputs: List<String>) {
        val edges = mutableListOf<Edge>()
        for (input in inputs) {
            val t1 = input.split("-")
            val from = Day12Node.parse(t1.first())
            val to = Day12Node.parse(t1.last())
            edges.add(Edge(from, to))
            edges.add(Edge(to, from))
            println(t1)
        }

        println(edges.joinToString("\n"))

        val endPaths = mutableListOf<List<Day12Node>>()

        val queue = mutableListOf<SearchState>()
        queue.add(SearchState(listOf(Day12Node.Start)))

        while (queue.isNotEmpty()) {
            val current = queue.removeFirst()
            val currentLastNode = current.currentPath.last()
            if (currentLastNode is Day12Node.Finish) {
                endPaths.add(current.currentPath)
                continue
            }
            val availableEdges = getEdgesWithFrom(edges, currentLastNode).filter {
                it.to !is Day12Node.Start &&
                        !current.exploredSmallCaves.contains(it.to)
            }
            println(
                """
                current: $current
                availableEdges: $availableEdges
            """.trimIndent()
            )
            for (edge in availableEdges) {
                queue.add(
                    SearchState(
                        current.currentPath + edge.to
                    )
                )
            }
        }

        println(endPaths.joinToString("\n"))
        println(
            """
            num end paths: ${endPaths.size}
        """.trimIndent()
        )
    }

    fun part2(inputs: List<String>) {
        val edges = mutableListOf<Edge>()
        for (input in inputs) {
            val t1 = input.split("-")
            val from = Day12Node.parse(t1.first())
            val to = Day12Node.parse(t1.last())
            edges.add(Edge(from, to))
            edges.add(Edge(to, from))
            println(t1)
        }

        println(edges.joinToString("\n"))

        val allAvailableSmallCaves = getAllAvailableSmallCaves(edges)

        println(
            """
            allAvailableSmallCaves: $allAvailableSmallCaves
        """.trimIndent()
        )

        val endPaths = mutableSetOf<List<Day12Node>>()

        val queue = mutableListOf<SearchState2>()
        for (smallCave in allAvailableSmallCaves) {
            queue.add(SearchState2(listOf(Day12Node.Start), smallCave))
        }

        while (queue.isNotEmpty()) {
            val current = queue.removeFirst()
            val currentLastNode = current.currentPath.last()
            if (currentLastNode is Day12Node.Finish) {
                endPaths.add(current.currentPath)
                continue
            }
            val availableEdges = getEdgesWithFrom(edges, currentLastNode).filter {
                it.to !is Day12Node.Start &&
                        (!current.exploredSmallCaves.contains(it.to) ||
                                (it.to == current.allowedSmallCaveToVisitTwice && !current.allowedSmallCaveHasBeenVisitedTwice))
            }
            println(
                """
                current: $current
                availableEdges: $availableEdges
            """.trimIndent()
            )
            for (edge in availableEdges) {
                queue.add(
                    SearchState2(
                        current.currentPath + edge.to,
                        current.allowedSmallCaveToVisitTwice
                    )
                )
            }
        }

        println(endPaths.joinToString("\n"))
        println(
            """
            num end paths: ${endPaths.size}
        """.trimIndent()
        )
    }

    val testInput = readInput("day12_test")
    val test2Input = readInput("day12_test2")
    val test3Input = readInput("day12_test3")
    val mainInput = readInput("day12")

    //    part1(testInput)
    //    part1(test2Input)
    //    part1(test3Input)
    //    part1(mainInput)
    //
    //    part2(testInput)
    //        part2(test2Input)
    //        part2(test3Input)
    part2(mainInput)
}