package com.example.game2048_unknownhero88

import android.app.Application
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import kotlin.random.Random

class GameViewModel(application: Application) : AndroidViewModel(application) {

    private val prefs =
        getApplication<Application>()
            .getSharedPreferences("game_prefs", Context.MODE_PRIVATE)

    var highScore by mutableStateOf(
        prefs.getInt("HIGH_SCORE", 0)
    )
        private set



    val size = 4
    var board = Array(size) { IntArray(size) }
        private set

    var newTilePosition by mutableStateOf<Pair<Int, Int>?>(null)
        private set


    var score = 0
        private set

    init {
        addRandomTile()
        addRandomTile()
    }

    private fun emptyCells(): List<Pair<Int, Int>> {
        val list = mutableListOf<Pair<Int, Int>>()

        for (r in board.indices) {
            for (c in board[r].indices) {
                if (board[r][c] == 0) list.add(r to c)
            }
        }

        return list
    }


    private fun addRandomTile() {
        val empties = emptyCells()
        if (empties.isNotEmpty()) {
            val (r, c) = empties.random()
            board[r][c] = if (Random.nextInt(10) == 0) 4 else 2
            newTilePosition = r to c
        }
    }

    fun moveLeft() {
        var changed = false

        for (r in 0 until size) {
            val row = board[r].filter { it != 0 }.toMutableList()

            var i = 0
            while (i < row.size - 1) {
                if (row[i] == row[i + 1]) {
                    row[i] *= 2
                    score += row[i]
                    if (score > highScore) {
                        highScore = score
                        prefs.edit().putInt("HIGH_SCORE", highScore).apply()
                    }
                    row.removeAt(i + 1)
                }
                i++
            }

            while (row.size < size) row.add(0)

            if (!row.toIntArray().contentEquals(board[r])) {
                changed = true
                board[r] = row.toIntArray()
            }
        }

        if (changed) {
            addRandomTile()
            isGameOver = checkGameOver()
        }
    }

    fun moveRight() {
        reverseRows()
        moveLeft()
        reverseRows()
    }

    fun moveUp() {
        transpose()
        moveLeft()
        transpose()
    }

    fun moveDown() {
        transpose()
        moveRight()
        transpose()
    }

    private fun reverseRows() {
        for (r in 0 until size) board[r].reverse()
    }

    private fun transpose() {
        val newBoard = Array(size) { IntArray(size) }
        for (r in 0 until size)
            for (c in 0 until size)
                newBoard[c][r] = board[r][c]

        board = newBoard
    }

    var isGameOver by mutableStateOf(false)
        private set

    private fun checkGameOver(): Boolean {


        for (r in 0 until size) {
            for (c in 0 until size) {
                if (board[r][c] == 0) return false
            }
        }


        for (r in 0 until size) {
            for (c in 0 until size - 1) {
                if (board[r][c] == board[r][c + 1]) return false
            }
        }


        for (c in 0 until size) {
            for (r in 0 until size - 1) {
                if (board[r][c] == board[r + 1][c]) return false
            }
        }

        return true
    }

    fun resetGame() {
        board = Array(size) { IntArray(size) }
        score = 0
        isGameOver = false
        newTilePosition = null
        addRandomTile()
        addRandomTile()
    }



}
