package io.github.warforged5.theorygames.dataclass

data class PowerUp(
    val type: PowerUpType,
    val usesRemaining: Int = 1
)
