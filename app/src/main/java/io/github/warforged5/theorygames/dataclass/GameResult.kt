package io.github.warforged5.theorygames.dataclass

data class GameResult(
    val question: GameQuestion,
    val playerAnswers: List<PlayerAnswer>,
    val winner: Player?,
    val correctAnswer: Double,
    val pointsAwarded: Map<String, Int> = emptyMap(),
    val powerUpsUsed: List<PowerUpType> = emptyList()
)