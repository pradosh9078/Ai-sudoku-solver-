package com.pradosh.sudokusolver

class SudokuSolver {
    companion object {
        fun solve(board: Array<IntArray>): Array<IntArray>? {
            val working = Array(9) { row -> board[row].copyOf() }
            if (solveBoard(working)) {
                return working
            }
            return null
        }

        private fun solveBoard(board: Array<IntArray>): Boolean {
            val emptyCell = findEmptyCell(board) ?: return true
            val row = emptyCell.first
            val col = emptyCell.second

            for (value in 1..9) {
                if (isValidPlacement(board, row, col, value)) {
                    board[row][col] = value
                    if (solveBoard(board)) {
                        return true
                    }
                    board[row][col] = 0
                }
            }
            return false
        }

        private fun findEmptyCell(board: Array<IntArray>): Pair<Int, Int>? {
            for (row in 0 until 9) {
                for (col in 0 until 9) {
                    if (board[row][col] == 0) {
                        return Pair(row, col)
                    }
                }
            }
            return null
        }

        private fun isValidPlacement(board: Array<IntArray>, row: Int, col: Int, value: Int): Boolean {
            for (index in 0 until 9) {
                if (board[row][index] == value && index != col) return false
                if (board[index][col] == value && index != row) return false
            }

            val boxRow = row / 3 * 3
            val boxCol = col / 3 * 3
            for (r in boxRow until boxRow + 3) {
                for (c in boxCol until boxCol + 3) {
                    if (board[r][c] == value && (r != row || c != col)) return false
                }
            }
            return true
        }
    }
}
