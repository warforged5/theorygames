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
fun MovieQuestionCard(
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
            // Hollywood-style header with special styling
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
                        color = Color(0xFFFFD700).copy(alpha = 0.2f), // Hollywood gold
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFFD700))
                    ) {
                        Text(
                            "🎬",
                            modifier = Modifier.padding(12.dp),
                            style = MaterialTheme.typography.headlineSmall
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            "Movie & Entertainment",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFB8860B) // Dark golden rod
                        )
                        Text(
                            getMovieGenre(question.id),
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFFDC143C) // Crimson red
                        )
                    }
                }

                // Cinema difficulty badge
                CinemaDifficultyBadge(difficulty = question.difficulty)
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Question with enhanced typography for entertainment content
            Text(
                question.question,
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontFamily = FontFamily.Default,
                    lineHeight = 32.sp
                ),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth()
            )

            // Movie context helper
            if (question.unit.contains("billion") || question.unit.contains("million") || question.unit.contains("/10")) {
                Spacer(modifier = Modifier.height(12.dp))
                MovieContextHelper(question = question)
            }

            // Enhanced hint section for movie context
            if (question.hint.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                MovieHintCard(hint = question.hint)
            }

            // Movie data indicator
            if (question.unit.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                MovieDataCard(unit = question.unit, questionId = question.id)
            }

            // Cinema disclaimer for ratings and box office
            Spacer(modifier = Modifier.height(16.dp))
            CinemaDisclaimer(questionId = question.id)
        }
    }
}

@Composable
fun CinemaDifficultyBadge(difficulty: QuestionDifficulty) {
    val (icon, color) = when (difficulty) {
        QuestionDifficulty.EASY -> Icons.Default.MovieFilter to Color(0xFF4CAF50)
        QuestionDifficulty.MEDIUM -> Icons.Default.Theaters to Color(0xFFFF9800)
        QuestionDifficulty.HARD -> Icons.Default.EmojiEvents to Color(0xFFFFD700)
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
fun MovieContextHelper(question: GameQuestion) {
    val contextText = when {
        question.unit.contains("billion") -> "Enter as decimal (e.g., for 2.9 billion, enter 2.9)"
        question.unit.contains("million") -> "Enter as whole number (e.g., for 150 million, enter 150)"
        question.unit.contains("/10") -> "Enter rating with decimal (e.g., for 8.5/10, enter 8.5)"
        question.unit.contains("minutes") -> "Enter total runtime in minutes"
        question.unit.isEmpty() && question.question.contains("year") -> "Enter the 4-digit year"
        question.unit.isEmpty() && question.question.contains("many") -> "Enter the count as a whole number"
        else -> "Enter the numeric value in ${question.unit}"
    }

    Surface(
        shape = RoundedCornerShape(8.dp),
        color = Color(0xFF9C27B0).copy(alpha = 0.1f) // Purple for entertainment
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.Info,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = Color(0xFF9C27B0)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                contextText,
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF7B1FA2),
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun MovieHintCard(hint: String) {
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
                tint = Color(0xFFFFD700) // Gold bulb
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    "Movie Insight",
                    style = MaterialTheme.typography.labelLarge,
                    color = Color(0xFFFFD700),
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
fun MovieDataCard(unit: String, questionId: String) {
    val movieTitle = getMovieTitle(questionId)
    val dataType = getMovieDataType(unit, questionId)

    Surface(
        shape = RoundedCornerShape(8.dp),
        color = Color(0xFFDC143C).copy(alpha = 0.1f) // Red carpet red
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
                            "Box Office" -> Icons.Default.AttachMoney
                            "IMDb Rating" -> Icons.Default.Star
                            "Year" -> Icons.Default.DateRange
                            "Runtime" -> Icons.Default.Schedule
                            "Awards" -> Icons.Default.EmojiEvents
                            "Count" -> Icons.Default.Numbers
                            else -> Icons.Default.Movie
                        },
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = Color(0xFFDC143C)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        if (unit.isNotEmpty()) "Answer in: $unit" else dataType,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFFB71C1C),
                        fontWeight = FontWeight.Bold
                    )
                }

                if (movieTitle.isNotEmpty()) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Color(0xFFFFD700)
                    ) {
                        Text(
                            movieTitle,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CinemaDisclaimer(questionId: String) {
    val disclaimerText = when {
        questionId.contains("movie_2") || questionId.contains("movie_12") || questionId.contains("movie_14") || questionId.contains("movie_30") ->
            "IMDb ratings can fluctuate slightly over time"
        questionId.contains("movie_4") || questionId.contains("movie_6") || questionId.contains("movie_8") || questionId.contains("movie_9") ->
            "Box office figures include re-releases and may vary by source"
        questionId.contains("movie_22") || questionId.contains("movie_23") ->
            "TV/streaming viewership data is approximate"
        else -> "Entertainment data is approximate and may vary by source"
    }

    Surface(
        shape = RoundedCornerShape(6.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.Movie,
                contentDescription = null,
                modifier = Modifier.size(12.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                disclaimerText,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun MovieAnswerVisualization(
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
                    Icons.Default.MovieCreation,
                    contentDescription = null,
                    tint = Color(0xFFFFD700)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Cinema Results",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            visualizations.forEachIndexed { index, vis ->
                MovieResultRow(
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

            // Enhanced correct answer display for movie data
            MovieCorrectAnswer(
                correctAnswer = question.correctAnswer,
                unit = question.unit,
                explanation = question.explanation,
                questionId = question.id
            )
        }
    }
}

@Composable
fun MovieResultRow(
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
                    tint = Color(0xFFFFD700) // Oscar gold
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
                    "Answer: ${formatMovieNumber(visualization.answer, unit)}",
                    style = MaterialTheme.typography.bodySmall,
                    fontFamily = FontFamily.Monospace,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Surface(
            shape = RoundedCornerShape(12.dp),
            color = if (isWinner) Color(0xFFFFD700).copy(alpha = 0.2f)
            else MaterialTheme.colorScheme.surfaceContainer
        ) {
            Text(
                "${visualization.percentageError}% error",
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                style = MaterialTheme.typography.labelMedium,
                color = if (isWinner) Color(0xFFB8860B)
                else MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun MovieCorrectAnswer(
    correctAnswer: Double,
    unit: String,
    explanation: String,
    questionId: String
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFFDC143C).copy(alpha = 0.15f) // Red carpet background
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
                    tint = Color(0xFFDC143C)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Correct Answer",
                    style = MaterialTheme.typography.labelLarge,
                    color = Color(0xFFDC143C),
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                formatMovieNumber(correctAnswer, unit),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                color = Color(0xFFB71C1C)
            )

            val movieTitle = getMovieTitle(questionId)
            if (movieTitle.isNotEmpty()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    movieTitle,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFFD700)
                )
            }

            if (explanation.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    explanation,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFFDC143C).copy(alpha = 0.8f)
                )
            }
        }
    }
}

// Helper functions for movie data
private fun getMovieGenre(questionId: String): String {
    return when {
        questionId.contains("movie_1") || questionId.contains("movie_5") || questionId.contains("movie_10") -> "Drama"
        questionId.contains("movie_3") || questionId.contains("movie_6") || questionId.contains("movie_9") -> "Action/Sci-Fi"
        questionId.contains("movie_4") || questionId.contains("movie_8") -> "Adventure"
        questionId.contains("movie_11") || questionId.contains("movie_18") -> "Fantasy"
        questionId.contains("movie_21") || questionId.contains("movie_22") || questionId.contains("movie_25") -> "TV Series"
        questionId.contains("movie_26") || questionId.contains("movie_27") || questionId.contains("movie_28") || questionId.contains("movie_29") -> "Animation"
        questionId.contains("movie_12") || questionId.contains("movie_14") -> "Art House"
        questionId.contains("movie_23") || questionId.contains("movie_24") -> "Streaming"
        else -> "Cinema"
    }
}

private fun getMovieTitle(questionId: String): String {
    return when (questionId) {
        "movie_1" -> "The Godfather"
        "movie_2" -> "Shawshank Redemption"
        "movie_3" -> "Star Wars"
        "movie_4" -> "Avatar"
        "movie_5" -> "Titanic"
        "movie_6" -> "Avengers: Endgame"
        "movie_7" -> "Avengers: Endgame"
        "movie_8" -> "Top Gun: Maverick"
        "movie_9" -> "Spider-Man: No Way Home"
        "movie_10" -> "The Dark Knight"
        "movie_11" -> "LOTR: Return of the King"
        "movie_12" -> "Parasite"
        "movie_13" -> "Titanic"
        "movie_26" -> "Frozen"
        "movie_27" -> "Toy Story"
        "movie_28" -> "The Lion King"
        "movie_30" -> "Spider-Verse"
        else -> ""
    }
}

private fun getMovieDataType(unit: String, questionId: String): String {
    return when {
        unit.contains("billion") || unit.contains("million") -> "Box Office"
        unit.contains("/10") -> "IMDb Rating"
        unit.contains("minutes") -> "Runtime"
        unit.isEmpty() && questionId.contains("year") -> "Year"
        unit.isEmpty() && questionId.contains("Oscar") -> "Awards"
        unit.isEmpty() && questionId.contains("many") -> "Count"
        unit.contains("hours") -> "Viewership"
        else -> "Movie Data"
    }
}

private fun formatMovieNumber(number: Double, unit: String): String {
    return when {
        unit.contains("billion") -> "$${String.format("%.2f", number)} billion"
        unit.contains("million") -> {
            if (number >= 1000) "$${String.format("%.0f", number)} million"
            else "$${String.format("%.1f", number)} million"
        }
        unit.contains("/10") -> "${String.format("%.1f", number)}/10"
        unit.contains("minutes") -> "${number.toInt()} minutes"
        unit.contains("hours") -> "${String.format("%.0f", number)} million hours"
        unit.isEmpty() && number >= 1900 && number <= 2030 -> number.toInt().toString() // Years
        unit.isEmpty() -> number.toInt().toString() // Counts, awards, etc.
        else -> String.format("%.1f", number)
    }
}