package io.github.warforged5.theorygames.dataclass

data class PlayerTimer(
    val playerId: String,
    val timeRemaining: Int,
    val hasAnswered: Boolean = false,
    val isActive: Boolean = true
)