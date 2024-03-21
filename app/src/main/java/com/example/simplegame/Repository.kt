package com.example.simplegame

class Repository {
    init {
        field = Data.get()
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