package io.github.warforged5.theorygames.dataclass
import kotlinx.serialization.Serializable

@Serializable
data class PowerUp(
    val type: PowerUpType,
    val usesRemaining: Int = 1
)
