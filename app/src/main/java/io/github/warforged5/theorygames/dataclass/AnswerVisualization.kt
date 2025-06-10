package io.github.warforged5.theorygames.dataclass

data class AnswerVisualization(
    val playerId: String,
    val playerName: String,
    val answer: Double,
    val correctAnswer: Double,
    val percentageError: Double,
    val isWinner: Boolean
)