package com.example.mindgame.models

enum class BoardSize(val numCards: Int) {

    EASY(8),
    MEDIUM(18),
    HARD(24);

    fun getColumnsCount(): Int {
        return when (this) {
            EASY -> 2
            MEDIUM -> 3
            HARD -> 4
        }
    }

    fun getRowsCount(): Int {
        return numCards / getColumnsCount()
    }

    fun getNumPairs(): Int {
        return numCards / 2
    }
}