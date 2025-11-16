package com.qapil.android.gameoflife

/**
 * Represents the grid state for Conway's Game of Life
 */
data class GameOfLifeGrid(
    val rows: Int,
    val cols: Int,
    private val cells: BooleanArray
) {
    init {
        require(rows > 0 && cols > 0) { "Grid dimensions must be positive" }
        require(cells.size == rows * cols) { "Cell array size must match grid dimensions" }
    }

    /**
     * Get the state of a cell at (row, col)
     */
    operator fun get(row: Int, col: Int): Boolean {
        if (row !in 0 until rows || col !in 0 until cols) return false
        return cells[row * cols + col]
    }

    /**
     * Set the state of a cell at (row, col)
     */
    fun set(row: Int, col: Int, alive: Boolean): GameOfLifeGrid {
        if (row !in 0 until rows || col !in 0 until cols) return this
        val newCells = cells.copyOf()
        newCells[row * cols + col] = alive
        return copy(cells = newCells)
    }

    /**
     * Toggle the state of a cell at (row, col)
     */
    fun toggle(row: Int, col: Int): GameOfLifeGrid {
        return set(row, col, !get(row, col))
    }

    /**
     * Count the number of alive neighbors for a cell at (row, col)
     */
    private fun countNeighbors(row: Int, col: Int): Int {
        var count = 0
        for (dr in -1..1) {
            for (dc in -1..1) {
                if (dr == 0 && dc == 0) continue
                if (get(row + dr, col + dc)) count++
            }
        }
        return count
    }

    /**
     * Compute the next generation based on Conway's Game of Life rules:
     * 1. Any live cell with 2-3 live neighbors survives
     * 2. Any dead cell with exactly 3 live neighbors becomes alive
     * 3. All other cells die or stay dead
     */
    fun nextGeneration(): GameOfLifeGrid {
        val newCells = BooleanArray(rows * cols)
        for (row in 0 until rows) {
            for (col in 0 until cols) {
                val neighbors = countNeighbors(row, col)
                val alive = get(row, col)
                newCells[row * cols + col] = when {
                    alive && neighbors in 2..3 -> true
                    !alive && neighbors == 3 -> true
                    else -> false
                }
            }
        }
        return copy(cells = newCells)
    }

    /**
     * Clear all cells
     */
    fun clear(): GameOfLifeGrid {
        return copy(cells = BooleanArray(rows * cols))
    }

    /**
     * Randomize the grid
     */
    fun randomize(density: Float = 0.3f): GameOfLifeGrid {
        val newCells = BooleanArray(rows * cols) { Math.random() < density }
        return copy(cells = newCells)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GameOfLifeGrid

        if (rows != other.rows) return false
        if (cols != other.cols) return false
        if (!cells.contentEquals(other.cells)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = rows
        result = 31 * result + cols
        result = 31 * result + cells.contentHashCode()
        return result
    }

    companion object {
        /**
         * Create an empty grid
         */
        fun empty(rows: Int, cols: Int): GameOfLifeGrid {
            return GameOfLifeGrid(rows, cols, BooleanArray(rows * cols))
        }
    }
}
