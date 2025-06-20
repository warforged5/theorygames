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
fun ScientificQuestionCard(
    question: GameQuestion,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(24.dp)
        ) {
            // Scientific category header with special styling
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
                        color = MaterialTheme.colorScheme.secondaryContainer
                    ) {
                        Text(
                            "🔬",
                            modifier = Modifier.padding(12.dp),
                            style = MaterialTheme.typography.headlineSmall
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            "Scientific Constants",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            getScientificField(question.id),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }

                // Difficulty with special scientific styling
                ScientificDifficultyBadge(difficulty = question.difficulty)
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Question with enhanced typography for scientific content
            Text(
                question.question,
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontFamily = FontFamily.Default,
                    lineHeight = 32.sp
                ),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth()
            )

            // Scientific notation helper if applicable
            if (question.unit.contains("×10")) {
                Spacer(modifier = Modifier.height(12.dp))
                ScientificNotationHelper(unit = question.unit)
            }

            // Enhanced hint section for scientific context
            if (question.hint.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                ScientificHintCard(hint = question.hint)
            }

            // Unit reminder for scientific questions
            if (question.unit.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                UnitReminderCard(unit = question.unit)
            }
        }
    }
}

@Composable
fun ScientificDifficultyBadge(difficulty: QuestionDifficulty) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = difficulty.color.copy(alpha = 0.15f),
        border = androidx.compose.foundation.BorderStroke(1.dp, difficulty.color)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                when (difficulty) {
                    QuestionDifficulty.EASY -> Icons.Default.Science
                    QuestionDifficulty.MEDIUM -> Icons.Default.Biotech
                    QuestionDifficulty.HARD -> Icons.Default.GraphicEq
                },
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = difficulty.color
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                difficulty.displayName,
                style = MaterialTheme.typography.labelMedium,
                color = difficulty.color,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun ScientificNotationHelper(unit: String) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.5f)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.Calculate,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = MaterialTheme.colorScheme.tertiary
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                "Scientific notation: Use decimal form (e.g., for ×10²³, enter 6.022)",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onTertiaryContainer,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun ScientificHintCard(hint: String) {
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
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    "Scientific Hint",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
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
fun UnitReminderCard(unit: String) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.Straighten,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                "Answer in: $unit",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun ScientificAnswerVisualization(
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
                    Icons.Default.Science,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Scientific Results",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            visualizations.forEachIndexed { index, vis ->
                ScientificResultRow(
                    position = index + 1,
                    visualization = vis,
                    isWinner = vis.isWinner
                )
                if (index < visualizations.size - 1) {
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(16.dp))

            // Enhanced correct answer display
            ScientificCorrectAnswer(
                correctAnswer = question.correctAnswer,
                unit = question.unit,
                explanation = question.explanation
            )
        }
    }
}

@Composable
fun ScientificResultRow(
    position: Int,
    visualization: AnswerVisualization,
    isWinner: Boolean
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
                    tint = MaterialTheme.colorScheme.primary
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
                    "Answer: ${formatScientificNumber(visualization.answer)}",
                    style = MaterialTheme.typography.bodySmall,
                    fontFamily = FontFamily.Monospace,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Surface(
            shape = RoundedCornerShape(12.dp),
            color = if (isWinner) MaterialTheme.colorScheme.primaryContainer
            else MaterialTheme.colorScheme.surfaceContainer
        ) {
            Text(
                "${visualization.percentageError}% error",
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                style = MaterialTheme.typography.labelMedium,
                color = if (isWinner) MaterialTheme.colorScheme.onPrimaryContainer
                else MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun ScientificCorrectAnswer(
    correctAnswer: Double,
    unit: String,
    explanation: String
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.primaryContainer
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
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Correct Answer",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                "${formatScientificNumber(correctAnswer)} $unit",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            if (explanation.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    explanation,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                )
            }
        }
    }
}

// Helper functions
private fun getScientificField(questionId: String): String {
    return when {
        questionId.contains("sci_1") || questionId.contains("sci_2") || questionId.contains("sci_16") -> "Physics"
        questionId.contains("sci_6") || questionId.contains("sci_21") || questionId.contains("sci_23") -> "Chemistry"
        questionId.contains("sci_11") || questionId.contains("sci_13") || questionId.contains("sci_14") -> "Astronomy"
        questionId.contains("sci_26") || questionId.contains("sci_27") || questionId.contains("sci_28") -> "Mathematics"
        questionId.contains("sci_29") || questionId.contains("sci_30") -> "Biology"
        else -> "Science"
    }
}

private fun formatScientificNumber(number: Double): String {
    return when {
        number >= 1000000000 -> String.format("%.3f×10⁹", number / 1000000000)
        number >= 1000000 -> String.format("%.3f×10⁶", number / 1000000)
        number >= 1000 -> String.format("%.3f×10³", number / 1000)
        number >= 100 -> String.format("%.1f", number)
        number >= 10 -> String.format("%.2f", number)
        number >= 1 -> String.format("%.3f", number)
        number >= 0.01 -> String.format("%.3f", number)
        number >= 0.0001 -> String.format("%.4f", number)
        else -> String.format("%.3e", number)
    }
}

// Enhanced QuestionCard that detects scientific constants
@Composable
fun EnhancedQuestionCard(
    question: GameQuestion,
    gameViewModel: GameViewModel? = null
) {
    if (question.category == GameCategory.SCIENTIFIC_CONSTANTS) {
        ScientificQuestionCard(question = question)
    } else {
        // Use existing QuestionCard for other categories
        QuestionCard(question = question, gameViewModel = gameViewModel)
    }
}