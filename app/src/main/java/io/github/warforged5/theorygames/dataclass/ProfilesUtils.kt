package io.github.warforged5.theorygames.dataclass

object ProfileUtils {

    fun formatLastPlayed(timestamp: Long): String {
        val now = System.currentTimeMillis()
        val diff = now - timestamp

        return when {
            diff < 60_000 -> "Just now"
            diff < 3600_000 -> "${diff / 60_000} minutes ago"
            diff < 86400_000 -> "${diff / 3600_000} hours ago"
            diff < 604800_000 -> "${diff / 86400_000} days ago"
            else -> java.text.SimpleDateFormat("MMM dd", java.util.Locale.getDefault()).format(java.util.Date(timestamp))
        }
    }

    fun getMotivationalMessage(profile: UserProfile): String {
        return when {
            profile.totalGamesPlayed == 0 -> "Ready for your first challenge?"
            profile.getWinRate() > 80 -> "You're on fire! 🔥"
            profile.bestStreak > 5 -> "Streak master! Keep it up!"
            profile.getLevel() >= 5 -> "Theory expert in the making!"
            profile.totalWins == 0 -> "Your first victory awaits!"
            else -> "Keep learning and improving!"
        }
    }

    fun calculateNextLevelProgress(profile: UserProfile): Float {
        val currentLevel = profile.getLevel()
        if (currentLevel >= 6) return 1f

        val currentThreshold = when (currentLevel) {
            1 -> 0
            2 -> 5
            3 -> 15
            4 -> 30
            5 -> 50
            else -> 100
        }

        val nextThreshold = when (currentLevel) {
            1 -> 5
            2 -> 15
            3 -> 30
            4 -> 50
            5 -> 100
            else -> 100
        }

        return (profile.totalGamesPlayed - currentThreshold).toFloat() / (nextThreshold - currentThreshold).toFloat()
    }
}