package com.corey.challenges

import java.io.File
import java.util.ArrayList
import java.util.HashSet
import java.util.LinkedList
import java.util.Queue


data class Point(val x: Int, val y: Int)
data class Path(val position: Point, val visitedList: ArrayList<Point>)

/**
 * Returns a list of valid paths that can be reached by moving the ogre one step
 * up, down, left or right. Checks for bounds and sinkholes. Does not move back
 * to a previously visited location.
 *
 * @param path the current Path used to reach this position.
 * @param map the 2d array representing the map.
 * @param visitedSet the set of Points visited so far by the algorithm.
 * @return a list of Paths which contain the valid, new adjacent positions.
 */
fun getValidAdjacentPaths(path: Path, map: Array<CharArray>, visitedSet: HashSet<Point>): List<Path> {
    val adjacentPaths = ArrayList<Path>()

    // Go up
    if (path.position.y > 0) {
        val newPosition = Point(path.position.x, path.position.y - 1)
        if (!visitedSet.contains(newPosition)) {
            val newPath = checkValidPositionAndCreatePath(newPosition, path.visitedList, map)
            if (newPath != null) adjacentPaths.add(newPath)
        }
    }

    // Go down
    if (path.position.y < map.size - 2) {
        val newPosition = Point(path.position.x, path.position.y + 1)
        if (!visitedSet.contains(newPosition)) {
            val newPath = checkValidPositionAndCreatePath(newPosition, path.visitedList, map)
            if (newPath != null) adjacentPaths.add(newPath)
        }
    }

    // Go left
    if (path.position.x > 0) {
        val newPosition = Point(path.position.x - 1, path.position.y)
        if (!visitedSet.contains(newPosition)) {
            val newPath = checkValidPositionAndCreatePath(newPosition, path.visitedList, map)
            if (newPath != null) adjacentPaths.add(newPath)
        }
    }

    // Go right
    if (path.position.x < map[0].size - 2) {
        val newPosition = Point(path.position.x + 1, path.position.y)
        if (!visitedSet.contains(newPosition)) {
            val newPath = checkValidPositionAndCreatePath(newPosition, path.visitedList, map)
            if (newPath != null) adjacentPaths.add(newPath)
        }
    }

    return adjacentPaths
}

/**
 * Returns a Path if the new position of the ogre is valid (not on a sinkhole)
 * or returns null otherwise. The function checks the current position, the
 * position at (x + 1, y), the position at (x, y + 1) and finally at
 * (x + 1, y + 1). These four coordinates represent the position of the ogre.
 *
 * @param newPosition the new position of the ogre to check.
 * @param visitedList a list of Points traversed on the path.
 * @param map the 2d array representing the map.
 * @return a Path if there is no sinkhole at the ogre's 2x2 position else null.
 */
fun checkValidPositionAndCreatePath(newPosition: Point, visitedList: ArrayList<Point>, map: Array<CharArray>): Path? {
    if (map[newPosition.y][newPosition.x] != 'O' && map[newPosition.y + 1][newPosition.x] != 'O'
            && map[newPosition.y][newPosition.x + 1] != 'O' && map[newPosition.y + 1][newPosition.x + 1] != 'O') {
        val newVisitedList = ArrayList(visitedList)
        newVisitedList.add(newPosition)
        return Path(newPosition, newVisitedList)
    }
    return null
}

/**
 * Returns whether or not the current position is the "goal"
 *
 * @param position the position to check.
 * @param goal the position of the goal.
 * @param map the 2d array representing the map.
 * @return a Boolean for whether the current position is equal to the goal.
 */
fun goalPosition(position: Point, goal: Point, map: Array<CharArray>): Boolean {
    return (position.equals(goal))
        || (position.x < map[0].size - 1 && position.x + 1 == goal.x && position.y == goal.y)
        || (position.y < map.size - 1 && position.x == goal.x && position.y + 1 == goal.y)
        || (position.x < map[0].size - 1 && position.y < map.size - 1 && position.x + 1 == goal.x && position.y + 1 == goal.y)
}

fun formatSolution(solution: ArrayList<Point>, map: Array<CharArray>): Array<CharArray> {
    for (point in solution) {
        map[point.y][point.x] = '&'
        map[point.y][point.x + 1] = '&'
        map[point.y + 1][point.x] = '&'
        map[point.y + 1][point.x + 1] = '&'
    }
    return map
}

fun printMap(map: Array<CharArray>) {
    for (row in map) {
        for (char in row) {
            print(char)
        }
        print('\n')
    }
}

/**
 * Algorithm uses BFS with an early exit to find the shortest path to the
 * goal, or "No path found" if no path exists.
 *
 * @param filename the name of the file used to read information about the map.
 */
fun findPath(filename: String) {
    val inputList = ArrayList<String>()
    File(filename).forEachLine { inputList.add(it) }
    val map: Array<CharArray> = Array(inputList.size, { CharArray(inputList[0].length)})

    var start: Point? = null
    var goal: Point? = null

    for (y in 0..inputList.size - 1) {
        for (x in 0..inputList[0].length - 1) {
            val char = inputList[y][x]
            when (char) {
                '@' -> {
                    if (start == null) {
                        start = Point(x, y)
                    }
                    map[y][x] = '.'
                }
                '$' -> {
                    goal = Point(x, y)
                }
                else -> {
                    map[y][x] = char
                }
            }
        }
    }

    if (start == null) throw KotlinNullPointerException("No start position found.")
    if (goal == null) throw KotlinNullPointerException("No goal position found.")

    val queue: Queue<Path> = LinkedList<Path>()
    var solution: ArrayList<Point>? = null
    val visitedSet = HashSet<Point>()
    var done: Boolean = false

    queue.add(Path(start, arrayListOf(start)))

    while (!done && queue.isNotEmpty()) {
        val path = queue.remove()
        visitedSet.add(path.position)
        if (goalPosition(path.position, goal, map)) {
            solution = path.visitedList
            done = true
        }
        queue.addAll(getValidAdjacentPaths(path, map, visitedSet))
    }

    if (solution != null) {
        printMap(formatSolution(solution, map))
    } else {
        println("No Path")
    }

    println("\n\n")
}

fun main(args: Array<String>) {
    findPath("input1.txt")
    findPath("input2.txt")
    findPath("challenge_input1a.txt")
    findPath("challenge_input1b.txt")
    findPath("challenge_input1c.txt")
    findPath("challenge_input2.txt")
}