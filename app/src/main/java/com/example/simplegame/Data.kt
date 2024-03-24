package com.example.simplegame

object Data {
    private val savedField = Array(4) { IntArray(4) }
    fun save(currentField: Array<IntArray>) {
        for (i in currentField.indices) {
            for (j in 0 until 4) {
                savedField[i][j] = currentField[i][j]
            }
        }
    }

    fun get() = savedField
}