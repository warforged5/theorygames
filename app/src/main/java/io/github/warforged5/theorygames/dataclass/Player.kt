package io.github.warforged5.theorygames.dataclass
import kotlinx.serialization.Serializable

@Serializable
data class Player(
    val id: String,
    val name: String,
    val score: Int = 0,
    val avatar: PlayerAvatar = PlayerAvatar.SCIENTIST,
    val currentStreak: Int = 0,
    val longestStreak: Int = 0,
    val powerUps: List<PowerUp> = emptyList(),
    val achievements: List<Achievement> = emptyList(),
    val totalGamesPlayed: Int = 0,
    val totalWins: Int = 0
)