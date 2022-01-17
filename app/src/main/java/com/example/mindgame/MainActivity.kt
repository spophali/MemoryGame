package com.example.mindgame

import android.animation.ArgbEvaluator
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mindgame.models.BoardSize
import com.example.mindgame.models.MemoryGame
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity(), GameAdapter.CardClickListener {

    private lateinit var numBoard: RecyclerView
    private lateinit var rootLayout: ConstraintLayout
    private lateinit var numMoves: TextView
    private lateinit var numPair: TextView

    private lateinit var memoryGame: MemoryGame
    private lateinit var gamesAdapter: GameAdapter

    private var boardSize: BoardSize = BoardSize.HARD

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rootLayout = findViewById(R.id.root_layout)
        numBoard = findViewById(R.id.game_data);
        numMoves = findViewById(R.id.numMoves)
        numPair = findViewById(R.id.numPairs)
        setUpBoard()
    }

    override fun onCardClick(position: Int) {
        if(memoryGame.haveWonGame()){
            Snackbar.make(rootLayout, "You have already won!!!", Snackbar.LENGTH_LONG).show()
            return
        }

        if(memoryGame.isFaceCardUp(position)){
            Snackbar.make(rootLayout, "Invalid move", Snackbar.LENGTH_SHORT).show()
            return
        }
        //actual flip over of cards
        if(memoryGame.flipCard(position)){
            val color = ArgbEvaluator().evaluate(
                    memoryGame.numPairsFound.toFloat()/boardSize.getNumPairs(),
                    ContextCompat.getColor(this,R.color.progress_none),
                    ContextCompat.getColor(this,R.color.progress_full)
            ) as Int
            numPair.setTextColor(color)
            numPair.text = "Pairs: ${memoryGame.numPairsFound} / ${boardSize.getNumPairs()}"
            if(memoryGame.haveWonGame()){
                Snackbar.make(rootLayout, "You won! Congrats.", Snackbar.LENGTH_LONG).show()
            }
        }

        numMoves.text = "Moves: ${memoryGame.getNumMoves()}"
        gamesAdapter.notifyDataSetChanged()
    }

    fun setUpBoard(){
        when(boardSize){
            BoardSize.EASY ->{
                numPair.text = "Pairs: 0 / 4"
                numMoves.text = "Easy: 4 x 2"
            }
            BoardSize.MEDIUM ->{
                numPair.text = "Pairs: 0 / 9"
                numMoves.text = "Medium: 6 x 3"
            }
            BoardSize.HARD ->{
                numPair.text = "Pairs: 0 / 12"
                numMoves.text = "Hard: 6 x 6 "
            }
        }
        numPair.text = "Pairs: 0"
        numMoves.text = "Moves: 0"
        numPair.setTextColor(ContextCompat.getColor(this, R.color.progress_none))
        memoryGame = MemoryGame(boardSize)
        gamesAdapter = GameAdapter(this,boardSize, memoryGame.cards, this)
        numBoard.adapter = gamesAdapter
        numBoard.setHasFixedSize(true)
        numBoard.layoutManager = GridLayoutManager(this,boardSize.getColumnsCount())
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.refresh_btn -> {
                if(memoryGame.getNumMoves() > 0 && !memoryGame.haveWonGame()){
                    showAlertDialog("Quit your current game?",null, {
                        setUpBoard()
                    })
                }else{
                    setUpBoard()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showAlertDialog(title: String, view: View?, onClickListener: View.OnClickListener) {
        AlertDialog.Builder(this)
                .setTitle(title)
                .setView(view)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Ok"){_,_ ->
                    onClickListener.onClick(null)
                }
                .show()
    }
}