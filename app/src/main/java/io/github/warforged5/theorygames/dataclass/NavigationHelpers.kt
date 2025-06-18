package io.github.warforged5.theorygames.dataclass

object NavigationHelpers {

    fun getUserFriendlyMessage(profile: UserProfile?): String {
        return when {
            profile == null -> "Create a profile to track your progress!"
            profile.totalGamesPlayed == 0 -> "Ready for your first challenge, ${profile.name}?"
            profile.getWinRate() > 80 -> "You're crushing it, ${profile.name}! 🔥"
            profile.bestStreak > 5 -> "Streak master ${profile.name}! Keep it up!"
            else -> "Welcome back, ${profile.name}! Ready to play?"
        }
    }

    fun getRecommendedCategories(profile: UserProfile?): List<GameCategory> {
        if (profile == null) return GameData.getAllCategories().take(3)

        val playedCategories = profile.categoryStats.keys
        val unplayedCategories = GameData.getAllCategories().filter { it !in playedCategories }

        return listOfNotNull(
            profile.favoriteCategory,
            *unplayedCategories.take(2).toTypedArray(),
            *profile.categoryStats.entries
                .filter { it.value.getWinRate() > 60.0 }
                .map { it.key }
                .take(2)
                .toTypedArray()
        ).distinct().take(4)
    }
}