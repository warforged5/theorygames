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
        // Game 1 Chart
        Text(
            text = "${chartData.game1Name} Performance",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        PerformanceBarChart(
            mysteryValue = chartData.mysteryGpu.cyberpunk2077Fps,
            comparisonData = chartData.comparisonGpus.map {
                it.gpuName to it.cyberpunk2077Fps
            },
            metricName = chartData.game1MetricName,
            modifier = Modifier.padding(bottom = 20.dp)
        )

        // Game 2 Chart
        Text(
            text = "${chartData.game2Name} Performance",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        PerformanceBarChart(
            mysteryValue = chartData.mysteryGpu.redDeadRedemption2Fps,
            comparisonData = chartData.comparisonGpus.map {
                it.gpuName to it.redDeadRedemption2Fps
            },
            metricName = chartData.game2MetricName
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Mystery GPU indicator
        Surface(
            shape = RoundedCornerShape(8.dp),
            color = MaterialTheme.colorScheme.primaryContainer
        ) {
            Text(
                text = "❓ Mystery GPU - What performance does it achieve?",
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth(),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun PerformanceBarChart(
    mysteryValue: Double,
    comparisonData: List<Pair<String, Double>>,
    metricName: String,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current

    // Animation for bars
    val animatedProgress by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(durationMillis = 1000, easing = EaseOutCubic),
        label = "barAnimation"
    )

    // All values including mystery (for scaling)
    val allValues = comparisonData.map { it.second } + mysteryValue
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
                    .height(200.dp)
            ) {
                val chartWidth = size.width
                val chartHeight = size.height - 80.dp.toPx() // Leave space for labels

                val totalBars = comparisonData.size + 1 // +1 for mystery GPU
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
                            textSize = with(density) { 24.sp.toPx() }
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
                comparisonData.forEachIndexed { index, (gpuName, value) ->
                    val barHeight = ((value - minValue) / valueRange * chartHeight * animatedProgress).toFloat()
                    val x = (index + 1.5f) * barSpacing - barWidth / 2 // Offset for mystery GPU
                    val y = chartHeight - barHeight

                    // Bar
                    drawRect(
                        color = Color(0xFF2196F3),
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
                                textSize = with(density) { 12.sp.toPx() }
                                isFakeBoldText = true
                            }
                        )
                    }

                    // GPU name at bottom
                    drawContext.canvas.nativeCanvas.apply {
                        // Truncate long GPU names
                        val displayName = if (gpuName.length > 10) {
                            gpuName.take(8) + "..."
                        } else gpuName

                        drawText(
                            displayName,
                            x + barWidth / 2,
                            chartHeight + 20.dp.toPx(),
                            android.graphics.Paint().apply {
                                color = android.graphics.Color.BLACK
                                textAlign = android.graphics.Paint.Align.CENTER
                                textSize = with(density) { 10.sp.toPx() }
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
                        text = metricName,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}