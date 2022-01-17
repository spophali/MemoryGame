package com.example.mindgame

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.mindgame.models.BoardSize
import com.example.mindgame.models.MemoryCard
import kotlin.math.min

class GameAdapter(private val context: Context,
                  private val boardSize: BoardSize,
                  private val cards: List<MemoryCard>,
                  private val clickListener: CardClickListener
) : RecyclerView.Adapter<GameAdapter.ViewHolder>() {

    companion object{
        private const val MARGIN_SIZE = 10
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameAdapter.ViewHolder {
        val cardWidth = parent.width/boardSize.getColumnsCount() - (2 * MARGIN_SIZE )
        val cardHeight = parent.height/boardSize.getRowsCount() - (2 * MARGIN_SIZE )
        val cardSideLength = min(cardHeight, cardWidth)
        val view = LayoutInflater.from(context).inflate(R.layout.memory_card, parent, false)
        val layoutParams = view.findViewById<CardView>(R.id.card_view).layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.width = cardSideLength
        layoutParams.width = cardSideLength
        layoutParams.setMargins(MARGIN_SIZE,MARGIN_SIZE,MARGIN_SIZE,MARGIN_SIZE )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: GameAdapter.ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return boardSize.numCards
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val imageButton = itemView.findViewById<ImageButton>(R.id.image_res)
        fun bind(position: Int) {
            val memoryCard = cards[position]
            imageButton.setImageResource(if (memoryCard.isFaceUp) memoryCard.indentifier else R.drawable.ic_launcher_background)
            imageButton.alpha = if (memoryCard.isMatched) .4f else 1.0f
            val colorStateList = if(memoryCard.isMatched) ContextCompat.getColorStateList(context, R.color.grey_color) else null
            ViewCompat.setBackgroundTintList(imageButton, colorStateList)
            imageButton.setOnClickListener {
                clickListener.onCardClick(position)
            }
        }
    }

    interface CardClickListener{
        fun onCardClick(position: Int)
    }

}
