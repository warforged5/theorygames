package io.github.warforged5.theorygames.dataclass

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun StockMarketQuestionCard(
    question: GameQuestion,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp),
        modifier = modifier,
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(24.dp)
        ) {
            // Financial market header with special styling
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFF1B5E20).copy(alpha = 0.1f), // Stock market green
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF1B5E20))
                    ) {
                        Text(
                            "📊",
                            modifier = Modifier.padding(12.dp),
                            style = MaterialTheme.typography.headlineSmall
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            "Stock Market Values",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1B5E20) // Market green
                        )
                        Text(
                            getMarketSector(question.id),
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF2E7D32)
                        )
                    }
                }

                // Financial difficulty badge
                FinancialDifficultyBadge(difficulty = question.difficulty)
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Question with enhanced typography for financial content
            Text(
                question.question,
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontFamily = FontFamily.Default,
                    lineHeight = 32.sp
                ),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth()
            )

            // Market context helper
            if (question.unit.contains("USD") || question.unit.contains("trillion") || question.unit.contains("%")) {
                Spacer(modifier = Modifier.height(12.dp))
                MarketContextHelper(question = question)
            }

            // Enhanced hint section for financial context
            if (question.hint.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                FinancialHintCard(hint = question.hint)
            }

            // Market data indicator
            if (question.unit.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                MarketDataCard(unit = question.unit, questionId = question.id)
            }

            // Live market disclaimer
            Spacer(modifier = Modifier.height(16.dp))
            LiveMarketDisclaimer()
        }
    }
}

@Composable
fun FinancialDifficultyBadge(difficulty: QuestionDifficulty) {
    val (icon, color) = when (difficulty) {
        QuestionDifficulty.EASY -> Icons.Default.TrendingUp to Color(0xFF2E7D32)
        QuestionDifficulty.MEDIUM -> Icons.Default.Analytics to Color(0xFFE65100)
        QuestionDifficulty.HARD -> Icons.Default.PieChart to Color(0xFFD32F2F)
    }

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = color.copy(alpha = 0.15f),
        border = androidx.compose.foundation.BorderStroke(1.dp, color)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                icon,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = color
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                difficulty.displayName,
                style = MaterialTheme.typography.labelMedium,
                color = color,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun MarketContextHelper(question: GameQuestion) {
    val contextText = when {
        question.unit.contains("trillion") -> "Enter as decimal (e.g., for 3 trillion, enter 3.0)"
        question.unit.contains("billion") -> "Enter as decimal (e.g., for 500 billion, enter 500.0)"
        question.unit.contains("million") -> "Enter as decimal (e.g., for 150 million, enter 150.0)"
        question.unit.contains("thousand") -> "Enter as decimal (e.g., for 67 thousand, enter 67.0)"
        question.unit.contains("%") -> "Enter as percentage (e.g., for 2.5%, enter 2.5)"
        else -> "Enter the numeric value in ${question.unit}"
    }

    Surface(
        shape = RoundedCornerShape(8.dp),
        color = Color(0xFF1976D2).copy(alpha = 0.1f)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.Info,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = Color(0xFF1976D2)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                contextText,
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF1565C0),
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun FinancialHintCard(hint: String) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceContainer
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                Icons.Default.Lightbulb,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = Color(0xFFE65100)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    "Market Insight",
                    style = MaterialTheme.typography.labelLarge,
                    color = Color(0xFFE65100),
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    hint,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun MarketDataCard(unit: String, questionId: String) {
    val stockSymbol = getStockSymbol(questionId)
    val dataType = getDataType(unit)

    Surface(
        shape = RoundedCornerShape(8.dp),
        color = Color(0xFF1B5E20).copy(alpha = 0.1f)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        when (dataType) {
                            "Stock Price" -> Icons.Default.AttachMoney
                            "Market Cap" -> Icons.Default.AccountBalance
                            "Ratio" -> Icons.Default.Percent
                            "Index" -> Icons.Default.ShowChart
                            else -> Icons.Default.TrendingUp
                        },
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = Color(0xFF2E7D32)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Answer in: $unit",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF1B5E20),
                        fontWeight = FontWeight.Bold
                    )
                }

                if (stockSymbol.isNotEmpty()) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Color(0xFF2E7D32)
                    ) {
                        Text(
                            stockSymbol,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LiveMarketDisclaimer() {
    Surface(
        shape = RoundedCornerShape(6.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.AccessTime,
                contentDescription = null,
                modifier = Modifier.size(12.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                "Market values are approximate and change constantly",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun StockMarketAnswerVisualization(
    visualizations: List<AnswerVisualization>,
    question: GameQuestion
) {
    ElevatedCard(
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Analytics,
                    contentDescription = null,
                    tint = Color(0xFF1B5E20)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Market Results",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            visualizations.forEachIndexed { index, vis ->
                FinancialResultRow(
                    position = index + 1,
                    visualization = vis,
                    isWinner = vis.isWinner,
                    unit = question.unit
                )
                if (index < visualizations.size - 1) {
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(16.dp))

            // Enhanced correct answer display for financial data
            FinancialCorrectAnswer(
                correctAnswer = question.correctAnswer,
                unit = question.unit,
                explanation = question.explanation,
                questionId = question.id
            )
        }
    }
}

@Composable
fun FinancialResultRow(
    position: Int,
    visualization: AnswerVisualization,
    isWinner: Boolean,
    unit: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isWinner) {
                Icon(
                    Icons.Default.EmojiEvents,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = Color(0xFFFFD700) // Gold color
                )
            } else {
                Surface(
                    modifier = Modifier.size(24.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceContainer
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            "$position",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    visualization.playerName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = if (isWinner) FontWeight.Bold else FontWeight.Normal
                )
                Text(
                    "Answer: ${formatFinancialNumber(visualization.answer, unit)}",
                    style = MaterialTheme.typography.bodySmall,
                    fontFamily = FontFamily.Monospace,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Surface(
            shape = RoundedCornerShape(12.dp),
            color = if (isWinner) Color(0xFF1B5E20).copy(alpha = 0.2f)
            else MaterialTheme.colorScheme.surfaceContainer
        ) {
            Text(
                "${visualization.percentageError}% error",
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                style = MaterialTheme.typography.labelMedium,
                color = if (isWinner) Color(0xFF1B5E20)
                else MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun FinancialCorrectAnswer(
    correctAnswer: Double,
    unit: String,
    explanation: String,
    questionId: String
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFF1B5E20).copy(alpha = 0.15f)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = Color(0xFF1B5E20)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Market Value",
                    style = MaterialTheme.typography.labelLarge,
                    color = Color(0xFF1B5E20),
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                formatFinancialNumber(correctAnswer, unit),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                color = Color(0xFF1B5E20)
            )

            val stockSymbol = getStockSymbol(questionId)
            if (stockSymbol.isNotEmpty()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    stockSymbol,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2E7D32)
                )
            }

            if (explanation.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    explanation,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF1B5E20).copy(alpha = 0.8f)
                )
            }
        }
    }
}

// Helper functions for financial data
private fun getMarketSector(questionId: String): String {
    return when {
        questionId.contains("stock_1") || questionId.contains("stock_2") || questionId.contains("stock_3") || questionId.contains("stock_4") || questionId.contains("stock_5") -> "Technology"
        questionId.contains("stock_6") || questionId.contains("stock_7") || questionId.contains("stock_8") -> "Market Capitalization"
        questionId.contains("stock_11") || questionId.contains("stock_12") || questionId.contains("stock_13") || questionId.contains("stock_14") || questionId.contains("stock_15") -> "Financial Ratios"
        questionId.contains("stock_16") || questionId.contains("stock_17") || questionId.contains("stock_18") || questionId.contains("stock_19") || questionId.contains("stock_20") -> "Market Indices"
        questionId.contains("stock_21") || questionId.contains("stock_22") || questionId.contains("stock_23") || questionId.contains("stock_24") || questionId.contains("stock_25") -> "Individual Stocks"
        questionId.contains("stock_26") || questionId.contains("stock_27") || questionId.contains("stock_28") || questionId.contains("stock_29") || questionId.contains("stock_30") -> "Alternative Assets"
        else -> "Finance"
    }
}

private fun getStockSymbol(questionId: String): String {
    return when (questionId) {
        "stock_1" -> "AAPL"
        "stock_2" -> "MSFT"
        "stock_3" -> "TSLA"
        "stock_4" -> "GOOGL"
        "stock_5" -> "AMZN"
        "stock_7" -> "NVDA"
        "stock_10" -> "BRK.A"
        "stock_21" -> "JPM"
        "stock_22" -> "KO"
        "stock_23" -> "JNJ"
        "stock_24" -> "WMT"
        "stock_25" -> "BA"
        "stock_26" -> "BTC"
        "stock_27" -> "ETH"
        else -> ""
    }
}

private fun getDataType(unit: String): String {
    return when {
        unit.contains("USD") && !unit.contains("trillion") && !unit.contains("billion") -> "Stock Price"
        unit.contains("trillion") || unit.contains("billion") -> "Market Cap"
        unit.contains("%") -> "Ratio"
        unit == "" -> "Index"
        else -> "Financial Data"
    }
}

private fun formatFinancialNumber(number: Double, unit: String): String {
    return when {
        unit.contains("trillion") -> "$${String.format("%.2f", number)} trillion"
        unit.contains("billion") -> "$${String.format("%.0f", number)} billion"
        unit.contains("million") -> "$${String.format("%.1f", number)} million"
        unit.contains("thousand") -> "$${String.format("%.0f", number)}K"
        unit.contains("USD") -> "$${String.format("%.2f", number)}"
        unit.contains("%") -> "${String.format("%.1f", number)}%"
        number >= 10000 -> String.format("%.0f", number)
        number >= 1000 -> String.format("%.1f", number)
        number >= 100 -> String.format("%.1f", number)
        else -> String.format("%.2f", number)
    }
}