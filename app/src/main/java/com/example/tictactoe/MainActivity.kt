package com.example.tictactoe

import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var statusText: TextView
    private lateinit var buttons: Array<Array<Button>>

    private var board = Array(3) { CharArray(3) { ' ' } }
    private var currentPlayer: Char = 'X'
    private var movesCount = 0
    private var isGameOver = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        statusText = findViewById(R.id.tvStatus)

        buttons = arrayOf(
            arrayOf(findViewById(R.id.b00), findViewById(R.id.b01), findViewById(R.id.b02)),
            arrayOf(findViewById(R.id.b10), findViewById(R.id.b11), findViewById(R.id.b12)),
            arrayOf(findViewById(R.id.b20), findViewById(R.id.b21), findViewById(R.id.b22))
        )

        for (row in 0..2) {
            for (col in 0..2) {
                buttons[row][col].setOnClickListener { handleMove(row, col) }
            }
        }

        updateStatus()
    }

    private fun handleMove(row: Int, col: Int) {
        if (isGameOver) return
        if (board[row][col] != ' ') return

        board[row][col] = currentPlayer
        buttons[row][col].text = currentPlayer.toString()

        buttons[row][col].setTextColor(
            if (currentPlayer == 'X') Color.RED else Color.BLUE
        )

        buttons[row][col].isEnabled = false
        movesCount++

        val winner = getWinner()
        if (winner != null) {
            isGameOver = true
            showEndDialog("Winner: $winner")
            return
        }

        if (movesCount == 9) {
            isGameOver = true
            showEndDialog("It's a draw!")
            return
        }

        currentPlayer = if (currentPlayer == 'X') 'O' else 'X'
        updateStatus()
    }

    private fun updateStatus() {
        statusText.text = "$currentPlayer turn"
    }

    private fun getWinner(): Char? {
        // rows
        for (row in 0..2) {
            if (board[row][0] != ' ' && board[row][0] == board[row][1] && board[row][1] == board[row][2]) {
                return board[row][0]
            }
        }

        // cols
        for (col in 0..2) {
            if (board[0][col] != ' ' && board[0][col] == board[1][col] && board[1][col] == board[2][col]) {
                return board[0][col]
            }
        }

        // diagonals
        if (board[0][0] != ' ' && board[0][0] == board[1][1] && board[1][1] == board[2][2]) {
            return board[0][0]
        }
        if (board[0][2] != ' ' && board[0][2] == board[1][1] && board[1][1] == board[2][0]) {
            return board[0][2]
        }

        return null
    }

    private fun showEndDialog(message: String) {
        AlertDialog.Builder(this)
            .setTitle("Game Finished")
            .setMessage(message)
            .setCancelable(false)
            .setPositiveButton("Play again") { dialog, _ ->
                dialog.dismiss()
                resetGame()
            }
            .show()
    }

    private fun resetGame() {
        board = Array(3) { CharArray(3) { ' ' } }
        currentPlayer = 'X'
        movesCount = 0
        isGameOver = false

        for (row in 0..2) {
            for (col in 0..2) {
                buttons[row][col].text = ""
                buttons[row][col].setTextColor(Color.BLACK)
                buttons[row][col].isEnabled = true
            }
        }

        updateStatus()
    }
}
