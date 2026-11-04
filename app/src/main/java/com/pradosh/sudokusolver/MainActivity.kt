package com.pradosh.sudokusolver

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.GridLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var boardGrid: GridLayout
    private lateinit var solveButton: Button
    private lateinit var clearButton: Button
    private lateinit var sampleButton: Button
    private val cells = Array(9) { Array(9) { EditText(this) } }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        boardGrid = findViewById(R.id.boardGrid)
        solveButton = findViewById(R.id.solveButton)
        clearButton = findViewById(R.id.clearButton)
        sampleButton = findViewById(R.id.sampleButton)

        buildBoard()
        setupListeners()
        loadSamplePuzzle()
    }

    private fun buildBoard() {
        boardGrid.removeAllViews()
        val gridSize = 9

        for (row in 0 until gridSize) {
            for (col in 0 until gridSize) {
                val cell = EditText(this).apply {
                    layoutParams = GridLayout.LayoutParams().apply {
                        width = 0
                        height = 0
                        columnSpec = GridLayout.spec(col, 1f)
                        rowSpec = GridLayout.spec(row, 1f)
                        setMargins(4, 4, 4, 4)
                    }
                    setBackgroundResource(android.R.color.transparent)
                    setPadding(8, 8, 8, 8)
                    gravity = android.view.Gravity.CENTER
                    setTextSize(18f)
                    inputType = android.text.InputType.TYPE_CLASS_NUMBER
                    filters = arrayOf(android.text.InputFilter.LengthFilter(1))
                    setSingleLine(true)
                    setTextColor(android.graphics.Color.BLACK)
                    setBackgroundColor(android.graphics.Color.WHITE)
                    setBackgroundResource(android.R.drawable.edit_text)
                }
                boardGrid.addView(cell)
                cells[row][col] = cell
            }
        }
    }

    private fun setupListeners() {
        solveButton.setOnClickListener {
            val board = readBoard()
            if (board == null) {
                Toast.makeText(this, "Please enter only digits 1-9 or leave blanks.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val solved = SudokuSolver.solve(board)
            if (!solved) {
                Toast.makeText(this, "No solution exists for this puzzle.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            fillBoard(solved)
            Toast.makeText(this, "Sudoku solved!", Toast.LENGTH_SHORT).show()
        }

        clearButton.setOnClickListener {
            clearBoard()
        }

        sampleButton.setOnClickListener {
            loadSamplePuzzle()
        }
    }

    private fun readBoard(): Array<IntArray>? {
        val board = Array(9) { IntArray(9) }
        for (row in 0 until 9) {
            for (col in 0 until 9) {
                val text = cells[row][col].text?.toString()?.trim() ?: ""
                if (text.isEmpty()) {
                    board[row][col] = 0
                    continue
                }
                val value = text.toIntOrNull() ?: return null
                if (value < 0 || value > 9) {
                    return null
                }
                board[row][col] = value
            }
        }
        return board
    }

    private fun fillBoard(board: Array<IntArray>) {
        for (row in 0 until 9) {
            for (col in 0 until 9) {
                val value = board[row][col]
                cells[row][col].setText(if (value == 0) "" else value.toString())
                cells[row][col].isEnabled = true
            }
        }
    }

    private fun clearBoard() {
        for (row in 0 until 9) {
            for (col in 0 until 9) {
                cells[row][col].setText("")
            }
        }
    }

    private fun loadSamplePuzzle() {
        val sample = arrayOf(
            intArrayOf(5, 3, 0, 0, 7, 0, 0, 0, 0),
            intArrayOf(6, 0, 0, 1, 9, 5, 0, 0, 0),
            intArrayOf(0, 9, 8, 0, 0, 0, 0, 6, 0),
            intArrayOf(8, 0, 0, 0, 6, 0, 0, 0, 3),
            intArrayOf(4, 0, 0, 8, 0, 3, 0, 0, 1),
            intArrayOf(7, 0, 0, 0, 2, 0, 0, 0, 6),
            intArrayOf(0, 6, 0, 0, 0, 0, 2, 8, 0),
            intArrayOf(0, 0, 0, 4, 1, 9, 0, 0, 5),
            intArrayOf(0, 0, 0, 0, 8, 0, 0, 7, 9)
        )
        fillBoard(sample)
    }
}
