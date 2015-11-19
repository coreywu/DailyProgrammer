package com.corey.challenges

import java.io.File
import java.util.ArrayList
import java.util.HashSet
import java.util.LinkedList
import java.util.Queue


data class Point(val x: Int, val y: Int)
data class Path(val position: Point, val visitedList: ArrayList<Point>)

fun getValidAdjacentPaths(path: Path, map: Array<CharArray>, visitedSet: HashSet<Point>): List<Path> {
    val adjacentPaths: ArrayList<Path> = ArrayList<Path>()

    // Go up
    if (path.position.y > 1) {
        val newPosition = Point(path.position.x, path.position.y - 1)
        if (!visitedSet.contains(newPosition)) {
            val newPath = checkValidPositionAndCreatePath(newPosition, path.visitedList, map)
            if (newPath != null) adjacentPaths.add(newPath)
        }
    }

    // Go down
    if (path.position.y < map.size - 1) {
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
    if (path.position.x < map[0].size - 1) {
        val newPosition = Point(path.position.x + 1, path.position.y)
        if (!visitedSet.contains(newPosition)) {
            val newPath = checkValidPositionAndCreatePath(newPosition, path.visitedList, map)
            if (newPath != null) adjacentPaths.add(newPath)
        }
    }

    return adjacentPaths
}

fun checkValidPositionAndCreatePath(newPosition: Point, visitedList: ArrayList<Point>, map: Array<CharArray>): Path? {
    if (map[newPosition.x][newPosition.y] != 'O' && map[newPosition.x][newPosition.y] != 'O') {
        val newVisitedList = ArrayList(visitedList)
        newVisitedList.add(newPosition)
        return Path(newPosition, newVisitedList)
    }
    return null
}

fun goalPosition(position: Point, goal: Point, map: Array<CharArray>): Boolean {
    return (position.equals(goal))
        || (position.x < map[0].size - 1 && position.x + 1 == goal.x && position.y == goal.y)
        || (position.y < map.size - 1 && position.x == goal.x && position.y + 1 == goal.y)
        || (position.x < map[0].size - 1 && position.y < map.size - 1 && position.x + 1 == goal.x && position.y + 1 == goal.y)
}

fun main(args: Array<String>) {
    val inputList = ArrayList<String>()
    File("input1.txt").forEachLine { inputList.add(it) }
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
    val visitedSet: HashSet<Point> = HashSet<Point>()
    var done: Boolean = false

    queue.add(Path(start, arrayListOf(start)))

    while (!done && queue.isNotEmpty()) {
        val path = queue.remove()
        if (goalPosition(path.position, goal, map)) {
            solution = path.visitedList
            done = true
        }
        queue.addAll(getValidAdjacentPaths(path, map, visitedSet))
    }

    println(solution)
}