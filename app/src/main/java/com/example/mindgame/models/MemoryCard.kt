package com.example.mindgame.models

data class MemoryCard(
        val indentifier: Int,
        var isFaceUp: Boolean = false,
        var isMatched: Boolean = false
)