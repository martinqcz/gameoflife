package com.qapil.android.gameoflife

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class GameViewModel : ViewModel() {
    // Game state
    var grid by mutableStateOf(GameOfLifeGrid.empty(40, 25))
        private set

    var isPlaying by mutableStateOf(false)
        private set

    var generationCount by mutableStateOf(0)
        private set

    var speed by mutableStateOf(8) // Speed from 1 (slowest) to 10 (fastest)

    private var gameJob: Job? = null

    /**
     * Initialize the grid with dimensions
     */
    fun initGrid(rows: Int, cols: Int) {
        grid = GameOfLifeGrid.empty(rows, cols)
        generationCount = 0
    }

    /**
     * Toggle a cell at the given position
     */
    fun toggleCell(row: Int, col: Int) {
        grid = grid.toggle(row, col)
    }

    /**
     * Start the game simulation
     */
    fun play() {
        if (isPlaying) return
        isPlaying = true
        gameJob = viewModelScope.launch {
            while (isPlaying) {
                delay(getDelayMillis())
                step()
            }
        }
    }

    /**
     * Pause the game simulation
     */
    fun pause() {
        isPlaying = false
        gameJob?.cancel()
        gameJob = null
    }

    /**
     * Advance to the next generation
     */
    fun step() {
        grid = grid.nextGeneration()
        generationCount++
    }

    /**
     * Clear the grid
     */
    fun clear() {
        pause()
        grid = grid.clear()
        generationCount = 0
    }

    /**
     * Randomize the grid
     */
    fun randomize() {
        pause()
        grid = grid.randomize(0.3f)
        generationCount = 0
    }

    /**
     * Update the simulation speed
     */
    fun updateSpeed(newSpeed: Int) {
        speed = newSpeed.coerceIn(1, 10)
    }

    /**
     * Calculate delay based on speed setting
     */
    private fun getDelayMillis(): Long {
        // Speed 1 = 1000ms, Speed 10 = 100ms
        return (1100 - speed * 100).toLong()
    }

    override fun onCleared() {
        super.onCleared()
        gameJob?.cancel()
    }
}
