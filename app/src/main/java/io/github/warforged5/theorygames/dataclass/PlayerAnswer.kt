package io.github.warforged5.theorygames.dataclass

data class PlayerAnswer(
    val playerId: String,
    val answer: Double,
    val timeSubmitted: Long = System.currentTimeMillis(),
    val timeTaken: Double = 0.0, // seconds
    val powerUpUsed: PowerUpType? = null
)
