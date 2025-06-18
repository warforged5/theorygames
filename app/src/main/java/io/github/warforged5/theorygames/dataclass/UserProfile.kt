// UserProfile.kt
package io.github.warforged5.theorygames.dataclass

import kotlinx.serialization.Serializable

@Serializable
data class UserProfile(
    val id: String,
    val name: String,
    val preferredAvatar: PlayerAvatar = PlayerAvatar.SCIENTIST,
    val totalGamesPlayed: Int = 0,
    val totalWins: Int = 0,
    val favoriteCategory: GameCategory = GameCategory.HDI,
    val preferredGameMode: GameMode = GameMode.CLASSIC,
    val bestStreak: Int = 0,
    val achievements: List<Achievement> = emptyList(),
    val categoryStats: Map<GameCategory, CategoryStats> = emptyMap(),
    val createdAt: Long = System.currentTimeMillis(),
    val lastPlayedAt: Long = System.currentTimeMillis()
) {
    fun getWinRate(): Double {
        return if (totalGamesPlayed > 0) (totalWins.toDouble() / totalGamesPlayed) * 100 else 0.0
    }

    fun getLevel(): Int {
        return when (totalGamesPlayed) {
            in 0..4 -> 1
            in 5..14 -> 2
            in 15..29 -> 3
            in 30..49 -> 4
            in 50..99 -> 5
            else -> 6
        }
    }

    fun getLevelTitle(): String {
        return when (getLevel()) {
            1 -> "Novice Scholar"
            2 -> "Apprentice Theorist"
            3 -> "Theory Enthusiast"
            4 -> "Knowledge Seeker"
            5 -> "Theory Master"
            6 -> "Legendary Theorist"
            else -> "Scholar"
        }
    }
}

@Serializable
data class CategoryStats(
    val gamesPlayed: Int = 0,
    val wins: Int = 0,
    val bestStreak: Int = 0,
    val averageAccuracy: Double = 0.0,
    val lastPlayed: Long = 0L
) {
    fun getWinRate(): Double {
        return if (gamesPlayed > 0) (wins.toDouble() / gamesPlayed) * 100 else 0.0
    }
}

@Serializable
data class GameHistoryEntry(
    val id: String,
    val date: Long,
    val category: GameCategory,
    val gameMode: GameMode,
    val playerCount: Int,
    val finalPosition: Int,
    val score: Int,
    val totalRounds: Int,
    val accuracy: Double,
    val streak: Int,
    val duration: Long // in milliseconds
)

@Serializable
data class AppSettings(
    val selectedTheme: AppTheme = AppTheme.SYSTEM,
    val isDarkMode: Boolean? = null, // null = follow system
    val enableAnimations: Boolean = true,
    val enableSoundEffects: Boolean = true,
    val defaultGameMode: GameMode = GameMode.CLASSIC,
    val defaultTimer: Boolean = true
)

enum class DarkModePreference {
    LIGHT, DARK, SYSTEM
}

