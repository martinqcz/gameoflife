package com.qapil.android.gameoflife

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp

@Composable
fun GameOfLifeCanvas(
    grid: GameOfLifeGrid,
    onCellTap: (row: Int, col: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val gridLineColor = Color.Gray.copy(alpha = 0.3f)
    val aliveCellColor = Color(0xFF4CAF50) // Green
    val deadCellColor = Color(0xFF212121) // Dark gray

    val gridLineWidth = with(LocalDensity.current) { 1.dp.toPx() }

    // Track cells affected during the current drag session
    val affectedCells = remember { mutableStateOf<MutableSet<Pair<Int, Int>>>(mutableSetOf()) }
    val drawingMode = remember { mutableStateOf<Boolean?>(null) } // true = drawing alive, false = drawing dead, null = not drawing

    Canvas(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    val cellWidth = size.width / grid.cols.toFloat()
                    val cellHeight = size.height / grid.rows.toFloat()
                    val col = (offset.x / cellWidth).toInt()
                    val row = (offset.y / cellHeight).toInt()

                    if (row in 0 until grid.rows && col in 0 until grid.cols) {
                        onCellTap(row, col)
                    }
                }
            }
            .pointerInput(grid.rows, grid.cols) {
                detectDragGestures(
                    onDragStart = { offset ->
                        // Clear affected cells for new drag session
                        affectedCells.value.clear()

                        val cellWidth = size.width / grid.cols.toFloat()
                        val cellHeight = size.height / grid.rows.toFloat()
                        val col = (offset.x / cellWidth).toInt()
                        val row = (offset.y / cellHeight).toInt()

                        if (row in 0 until grid.rows && col in 0 until grid.cols) {
                            // Determine drawing mode based on first cell's current state
                            drawingMode.value = !grid[row, col]
                            affectedCells.value.add(Pair(row, col))
                            onCellTap(row, col)
                        }
                    },
                    onDrag = { change, _ ->
                        val cellWidth = size.width / grid.cols.toFloat()
                        val cellHeight = size.height / grid.rows.toFloat()
                        val col = (change.position.x / cellWidth).toInt()
                        val row = (change.position.y / cellHeight).toInt()

                        if (row in 0 until grid.rows && col in 0 until grid.cols) {
                            val cellKey = Pair(row, col)
                            // Only affect each cell once per drag session
                            if (!affectedCells.value.contains(cellKey)) {
                                affectedCells.value.add(cellKey)
                                // Set cell to the drawing mode state (alive or dead)
                                val currentState = grid[row, col]
                                if (currentState != drawingMode.value) {
                                    onCellTap(row, col)
                                }
                            }
                        }
                        change.consume()
                    },
                    onDragEnd = {
                        // Reset drawing mode after drag ends
                        drawingMode.value = null
                        affectedCells.value.clear()
                    },
                    onDragCancel = {
                        // Reset drawing mode if drag is cancelled
                        drawingMode.value = null
                        affectedCells.value.clear()
                    }
                )
            }
    ) {
        val cellWidth = size.width / grid.cols
        val cellHeight = size.height / grid.rows

        // Draw cells
        for (row in 0 until grid.rows) {
            for (col in 0 until grid.cols) {
                val x = col * cellWidth
                val y = row * cellHeight

                val cellColor = if (grid[row, col]) aliveCellColor else deadCellColor

                drawRect(
                    color = cellColor,
                    topLeft = Offset(x, y),
                    size = Size(cellWidth, cellHeight)
                )
            }
        }

        // Draw grid lines - vertical
        for (col in 0..grid.cols) {
            val x = col * cellWidth
            drawLine(
                color = gridLineColor,
                start = Offset(x, 0f),
                end = Offset(x, size.height),
                strokeWidth = gridLineWidth
            )
        }

        // Draw grid lines - horizontal
        for (row in 0..grid.rows) {
            val y = row * cellHeight
            drawLine(
                color = gridLineColor,
                start = Offset(0f, y),
                end = Offset(size.width, y),
                strokeWidth = gridLineWidth
            )
        }
    }
}
