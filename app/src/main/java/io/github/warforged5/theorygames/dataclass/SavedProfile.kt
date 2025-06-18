package io.github.warforged5.theorygames.dataclass

import kotlinx.serialization.Serializable

@Serializable
data class SavedProfile(
    val id: String,
    val name: String,
    val avatar: PlayerAvatar,
    val totalGamesPlayed: Int = 0,
    val totalWins: Int = 0,
    val longestStreak: Int = 0,
    val achievements: List<AchievementType> = emptyList(),
    val favoriteCategory: GameCategory = GameCategory.HDI,
    val createdAt: Long = System.currentTimeMillis(),
    val lastPlayed: Long = System.currentTimeMillis()
) {
    fun toPlayer(): Player {
        return Player(
            id = id,
            name = name,
            avatar = avatar,
            totalGamesPlayed = totalGamesPlayed,
            totalWins = totalWins,
            longestStreak = longestStreak,
            achievements = achievements.map { Achievement(it) }
        )
    }

    fun updateStats(player: Player): SavedProfile {
        return this.copy(
            totalGamesPlayed = player.totalGamesPlayed,
            totalWins = player.totalWins,
            longestStreak = maxOf(longestStreak, player.longestStreak),
            achievements = (achievements + player.achievements.map { it.type }).distinct(),
            lastPlayed = System.currentTimeMillis()
        )
    }
}

@Serializable
data class ProfileCollection(
    val profiles: List<SavedProfile> = emptyList(),
    val lastSelectedProfiles: List<String> = emptyList() // IDs of last selected profiles
)

@Serializable
enum class AppTheme(
    val displayName: String,
    val description: String
) {
    SYSTEM("System Default", "Follow system theme"),
    DYNAMIC("Dynamic Colors", "Use Material You colors (Android 12+)"),
    CLASSIC("Classic Purple", "Original purple theme"),
    OCEAN("Ocean Blue", "Cool blue theme"),
    FOREST("Forest Green", "Natural green theme"),
    SUNSET("Sunset Orange", "Warm orange theme"),
    MIDNIGHT("Midnight", "Dark theme with purple accents")
}

