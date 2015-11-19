package com.corey.challenges

import java.io.File
import java.util.ArrayList

data class Point(val x: Int, val y: Int)

fun main(args: Array<String>) {
    val inputList = ArrayList<String>()
    File("input1.txt").forEachLine { inputList.add(it) }
    val map = Array(inputList.size, { CharArray(inputList[0].length)})

    println(inputList.size)
    println(inputList[0].length)

    var ogre: Point = Point(0, 0)
    var startFound = false

    for (y in 0..inputList.size - 1) {
        for (x in 0..inputList[0].length - 1) {
            val char = inputList[y][x]
            if (char == '@') {
                if (!startFound) {
                    ogre = Point(x, y)
                    startFound = true
                }
                map[y][x] = '.'
            } else {
                map[y][x] = char
            }
        }
    }

    println(ogre)


}
