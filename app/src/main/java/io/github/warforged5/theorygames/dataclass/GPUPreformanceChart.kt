package io.github.warforged5.theorygames.dataclass

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun GPUPerformanceChart(
    chartData: GPUChartData,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        // Title
        Text(
            text = "GPU Performance Comparison",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Charts for each game
        chartData.games.forEachIndexed { index, gameName ->
            Text(
                text = gameName,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            MultiGPUPerformanceBarChart(
                gameName = gameName,
                mysteryGpu = chartData.mysteryGpu,
                comparisonGpus = chartData.comparisonGpus,
                unit = chartData.unit,
                modifier = Modifier.padding(bottom = if (index < chartData.games.size - 1) 20.dp else 0.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Mystery GPU indicator
        Surface(
            shape = RoundedCornerShape(8.dp),
            color = MaterialTheme.colorScheme.primaryContainer
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "❓ Mystery GPU",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = "What GPU is this? Enter the GPU name (e.g., 'RTX 4070' or '4070')",
                    modifier = Modifier.padding(top = 4.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun MultiGPUPerformanceBarChart(
    gameName: String,
    mysteryGpu: GPUPerformanceData,
    comparisonGpus: List<GPUPerformanceData>,
    unit: String,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current

    // Animation for bars
    val animatedProgress by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(durationMillis = 1000, easing = EaseOutCubic),
        label = "barAnimation"
    )

    // Get performance values for this game
    val mysteryValue = mysteryGpu.getPerformance(gameName)
    val comparisonValues = comparisonGpus.map { it.shortName to it.getPerformance(gameName) }

    // All values for scaling
    val allValues = comparisonValues.map { it.second } + mysteryValue
    val maxValue = allValues.maxOrNull() ?: 100.0
    val minValue = 0.0 // Start from 0 for FPS charts
    val valueRange = maxValue - minValue

    ElevatedCard(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Chart
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            ) {
                val chartWidth = size.width
                val chartHeight = size.height - 80.dp.toPx() // Leave space for labels

                val totalBars = comparisonValues.size + 1 // +1 for mystery GPU
                val barWidth = chartWidth / totalBars * 0.6f
                val barSpacing = chartWidth / totalBars

                // Draw mystery GPU bar first (leftmost position)
                val mysteryBarHeight = ((mysteryValue - minValue) / valueRange * chartHeight).toFloat()
                val mysteryX = barSpacing / 2 - barWidth / 2
                val mysteryY = chartHeight - mysteryBarHeight

                // Dashed/outline style for mystery GPU
                drawRect(
                    color = Color(0xFFFF6B35),
                    topLeft = Offset(mysteryX, mysteryY),
                    size = Size(barWidth, mysteryBarHeight),
                    alpha = 0.4f
                )

                // Mystery label in the middle of the bar
                drawContext.canvas.nativeCanvas.apply {
                    drawText(
                        "❓",
                        mysteryX + barWidth / 2,
                        mysteryY + mysteryBarHeight / 2,
                        android.graphics.Paint().apply {
                            color = android.graphics.Color.parseColor("#FF6B35")
                            textAlign = android.graphics.Paint.Align.CENTER
                            textSize = with(density) { 20.sp.toPx() }
                        }
                    )
                }

                // Mystery GPU name at bottom
                drawContext.canvas.nativeCanvas.apply {
                    drawText(
                        "Mystery GPU",
                        mysteryX + barWidth / 2,
                        chartHeight + 20.dp.toPx(),
                        android.graphics.Paint().apply {
                            color = android.graphics.Color.BLACK
                            textAlign = android.graphics.Paint.Align.CENTER
                            textSize = with(density) { 10.sp.toPx() }
                            isFakeBoldText = true
                        }
                    )
                }

                // Draw comparison GPU bars
                comparisonValues.forEachIndexed { index, (gpuName, value) ->
                    val barHeight = ((value - minValue) / valueRange * chartHeight * animatedProgress).toFloat()
                    val x = (index + 1.5f) * barSpacing - barWidth / 2 // Offset for mystery GPU
                    val y = chartHeight - barHeight

                    // Bar color based on brand
                    val barColor = when {
                        gpuName.contains("RTX") -> Color(0xFF76B900) // NVIDIA Green
                        gpuName.contains("RX") -> Color(0xFFED1C24)  // AMD Red
                        else -> Color(0xFF2196F3) // Default Blue
                    }

                    // Bar
                    drawRect(
                        color = barColor,
                        topLeft = Offset(x, y),
                        size = Size(barWidth, barHeight)
                    )

                    // Value label on top of bar
                    drawContext.canvas.nativeCanvas.apply {
                        drawText(
                            "${value.toInt()}",
                            x + barWidth / 2,
                            y - 10.dp.toPx(),
                            android.graphics.Paint().apply {
                                color = android.graphics.Color.BLACK
                                textAlign = android.graphics.Paint.Align.CENTER
                                textSize = with(density) { 11.sp.toPx() }
                                isFakeBoldText = true
                            }
                        )
                    }

                    // GPU name at bottom
                    drawContext.canvas.nativeCanvas.apply {
                        drawText(
                            gpuName,
                            x + barWidth / 2,
                            chartHeight + 20.dp.toPx(),
                            android.graphics.Paint().apply {
                                color = android.graphics.Color.BLACK
                                textAlign = android.graphics.Paint.Align.CENTER
                                textSize = with(density) { 9.sp.toPx() }
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Chart legend
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = MaterialTheme.colorScheme.surfaceContainer
                ) {
                    Text(
                        text = "$unit (1440p Ultra Settings)",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}