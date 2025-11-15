package com.qapil.android.gameoflife

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
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

    Canvas(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(grid) {
                detectTapGestures { offset ->
                    val cellWidth = size.width / grid.cols
                    val cellHeight = size.height / grid.rows
                    val col = (offset.x / cellWidth).toInt()
                    val row = (offset.y / cellHeight).toInt()

                    if (row in 0 until grid.rows && col in 0 until grid.cols) {
                        onCellTap(row, col)
                    }
                }
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
