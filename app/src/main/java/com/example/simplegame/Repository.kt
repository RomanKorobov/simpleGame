package com.example.simplegame

import android.util.Log
import kotlin.math.abs
import kotlin.random.Random

class Repository {
    init {
        field = Data.get()
        var check = true
        for (i in field.indices) {
            for (j in field[i].indices) {
                if (field[i][j] != 0) {
                    check = false
                    break
                }
            }
        }
        if (check) startedPosition()
    }
    fun getField() = field

    fun setField(curField: Array<IntArray>) {
        field = curField
    }

    fun setNewGameField() {
        field = Array(4) { IntArray(4) }
        startedPosition()
    }

    fun startedPosition() {
        getNewNum(field)
        getNewNum(field)
    }

    fun getNewNum(curField: Array<IntArray>) {
        val i = abs(Random.nextInt()) % 4
        val j = abs(Random.nextInt()) % 4
        if (curField[i][j] != 0) {
            getNewNum(curField)
        } else {
            curField[i][j] = listOf(2, 4).random()
        }
    }
    private fun predictLeft() {
        leftField = Array(4) { IntArray(4) }
        for (i in 0 until 4) {
            val row = mutableListOf<Int>()
            for (j in 0 until 4) if (field[i][j] != 0) row.add(field[i][j])
            var k = 1
            while (k < row.size) {
                if (row[k] == row[k - 1]) {
                    row[k - 1] += row[k]
                    row.removeAt(k)
                }
                k++
            }
            for (j in row.indices) {
                leftField[i][j] = row[j]
            }
            row.clear()
        }
    }

    private fun predictRight() {
        rightField = Array(4) { IntArray(4) }
        for (i in 0 until 4) {
            val row = mutableListOf<Int>()
            for (j in 3 downTo 0) if (field[i][j] != 0) row.add(field[i][j])
            var k = 1
            while (k < row.size) {
                if (row[k] == row[k - 1]) {
                    row[k - 1] += row[k]
                    row.removeAt(k)
                }
                k++
            }
            for (j in row.indices) {
                rightField[i][3 - j] = row[j]
            }
            Log.d("GEST", rightField[i].joinToString(" "))
            row.clear()
        }
    }

    private fun predictUp() {
        upField = Array(4) { IntArray(4) }
        for (i in 0 until 4) {
            val col = mutableListOf<Int>()
            for (j in 0 until 4) if (field[j][i] != 0) col.add(field[j][i])
            var k = 1
            while (k < col.size) {
                if (col[k] == col[k - 1]) {
                    col[k - 1] += col[k]
                    col.removeAt(k)
                }
                k++
            }
            for (j in col.indices) {
                upField[j][i] = col[j]
            }
            col.clear()
        }
    }

    private fun predictDown() {
        downField = Array(4) { IntArray(4) }
        for (i in 0 until 4) {
            val col = mutableListOf<Int>()
            for (j in 3 downTo 0) if (field[j][i] != 0) col.add(field[j][i])
            var k = 1
            while (k < col.size) {
                if (col[k] == col[k - 1]) {
                    col[k - 1] += col[k]
                    col.removeAt(k)
                }
                k++
            }
            for (j in col.indices) {
                downField[3 - j][i] = col[j]
            }
            col.clear()
        }
    }

    fun swipeLeft(): Array<IntArray> {
        predictLeft()
        return leftField
    }

    fun swipeRight(): Array<IntArray> {
        predictRight()
        return rightField
    }

    fun swipeUp(): Array<IntArray> {
        predictUp()
        return upField
    }

    fun swipeDown(): Array<IntArray> {
        predictDown()
        return downField
    }

    companion object {
        var field = Array(4) { IntArray(4) }
        lateinit var leftField: Array<IntArray>
        lateinit var rightField: Array<IntArray>
        lateinit var upField: Array<IntArray>
        lateinit var downField: Array<IntArray>
    }
}