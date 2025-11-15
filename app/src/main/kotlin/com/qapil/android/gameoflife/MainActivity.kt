package com.qapil.android.gameoflife

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.qapil.android.gameoflife.ui.theme.GameOfLifeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GameOfLifeTheme {
                GameOfLifeScreen()
            }
        }
    }
}

@Composable
fun GameOfLifeScreen(viewModel: GameViewModel = viewModel()) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val screenHeight = configuration.screenHeightDp

    // Initialize grid based on screen size
    LaunchedEffect(Unit) {
        val cols = (screenWidth / 15).coerceIn(20, 60)
        val rows = ((screenHeight - 200) / 15).coerceIn(20, 60)
        viewModel.initGrid(rows, cols)
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Game canvas
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                GameOfLifeCanvas(
                    grid = viewModel.grid,
                    onCellTap = { row, col ->
                        if (!viewModel.isPlaying) {
                            viewModel.toggleCell(row, col)
                        }
                    }
                )
            }

            // Control panel
            ControlPanel(
                isPlaying = viewModel.isPlaying,
                generationCount = viewModel.generationCount,
                speed = viewModel.speed,
                onPlayPause = {
                    if (viewModel.isPlaying) {
                        viewModel.pause()
                    } else {
                        viewModel.play()
                    }
                },
                onStep = { viewModel.step() },
                onClear = { viewModel.clear() },
                onRandomize = { viewModel.randomize() },
                onSpeedChange = { viewModel.updateSpeed(it) },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}