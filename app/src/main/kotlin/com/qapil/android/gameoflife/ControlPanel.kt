package com.qapil.android.gameoflife

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp

@Composable
fun ControlPanel(
    isPlaying: Boolean,
    generationCount: Int,
    speed: Int,
    onPlayPause: () -> Unit,
    onStep: () -> Unit,
    onClear: () -> Unit,
    onRandomize: () -> Unit,
    onSpeedChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    // Get screen configuration for responsive sizing
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val screenHeight = configuration.screenHeightDp

    // Calculate responsive sizes based on screen dimensions
    val padding = when {
        screenWidth < 360 -> 8.dp  // Small devices
        screenWidth < 400 -> 12.dp // Medium devices
        else -> 16.dp              // Large devices
    }

    val spacing = when {
        screenHeight < 600 -> 6.dp  // Very small screens
        screenHeight < 700 -> 8.dp  // Small screens
        else -> 12.dp               // Normal screens
    }

    val buttonSize = when {
        screenWidth < 360 -> 48.dp // Small devices
        else -> 56.dp              // Normal devices
    }

    val iconSize = when {
        screenWidth < 360 -> 24.dp // Small devices
        else -> 32.dp              // Normal devices
    }

    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.surfaceVariant,
        tonalElevation = 2.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(padding),
            verticalArrangement = Arrangement.spacedBy(spacing)
        ) {
            // Generation counter
            Text(
                text = "Generation: $generationCount",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            // Control buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Play/Pause button
                IconButton(
                    onClick = onPlayPause,
                    modifier = Modifier.size(buttonSize)
                ) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = if (isPlaying) "Pause" else "Play",
                        modifier = Modifier.size(iconSize)
                    )
                }

                // Step button
                IconButton(
                    onClick = onStep,
                    enabled = !isPlaying,
                    modifier = Modifier.size(buttonSize)
                ) {
                    Icon(
                        imageVector = Icons.Default.SkipNext,
                        contentDescription = "Step",
                        modifier = Modifier.size(iconSize)
                    )
                }

                // Clear button
                IconButton(
                    onClick = onClear,
                    modifier = Modifier.size(buttonSize)
                ) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Clear",
                        modifier = Modifier.size(iconSize)
                    )
                }

                // Randomize button
                IconButton(
                    onClick = onRandomize,
                    modifier = Modifier.size(buttonSize)
                ) {
                    Icon(
                        imageVector = Icons.Default.Shuffle,
                        contentDescription = "Randomize",
                        modifier = Modifier.size(iconSize)
                    )
                }
            }

            // Speed control
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Speed",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "$speed",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Slider(
                    value = speed.toFloat(),
                    onValueChange = { onSpeedChange(it.toInt()) },
                    valueRange = 1f..10f,
                    steps = 8,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
