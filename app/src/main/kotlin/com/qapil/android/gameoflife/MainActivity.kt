package com.qapil.android.gameoflife

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
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
        val cols = (screenWidth / 20).coerceIn(20, 60)
        val rows = ((screenHeight - 200) / 20).coerceIn(20, 60)
        viewModel.initGrid(rows, cols)
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.systemBars)
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