package com.example.mindgame.models

import com.example.mindgame.utils.DEFAULT_ICONS

class MemoryGame(private val boardSize: BoardSize) {

    val cards: List<MemoryCard>
    var numPairsFound = 0
    var indexOfSingleSelectedCard: Int? = null
    var numCardFlips: Int = 0

    init {
        val imageList = DEFAULT_ICONS.shuffled().take(boardSize.getNumPairs())
        val randomImageList = (imageList + imageList).shuffled()
        cards = randomImageList.map { MemoryCard(it) }
    }

    fun flipCard(position: Int): Boolean{
        numCardFlips++
        val card = cards[position]
        //3 cases:
        // 0 cards prev flipped => restore + flip over the selected card
        // 1 cards prev flipped => check if the image is matched + flip over the selected card
        // 2 cards prev flipped => restore + flip over the selected card
        var foundMatch = false
        if(indexOfSingleSelectedCard == null){
            // 0 or 2 cards prev
            restoreCards()
            indexOfSingleSelectedCard = position
        } else{
            //exactly 1
            foundMatch = checkForMatch(indexOfSingleSelectedCard!!, position)
            indexOfSingleSelectedCard = null
        }
        cards[position].isFaceUp = !cards[position].isFaceUp

        return foundMatch
    }

    private fun checkForMatch(indexOfSingleSelectedCard: Int, position: Int): Boolean {
        if(cards[position].indentifier != cards[indexOfSingleSelectedCard].indentifier)
            return false
        cards[position].isMatched = true
        cards[indexOfSingleSelectedCard].isMatched = true
        numPairsFound++
        return true
    }

    private fun restoreCards() {
        for (card in cards){
            if (!card.isMatched)
                card.isFaceUp = false
        }
    }

    fun haveWonGame(): Boolean {
        return numPairsFound == boardSize.getNumPairs()

    }

    fun isFaceCardUp(position: Int): Boolean {
        return cards[position].isFaceUp
    }

    fun getNumMoves(): Int {
        return numCardFlips/2
    }
}