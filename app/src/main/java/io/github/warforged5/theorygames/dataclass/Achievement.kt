package io.github.warforged5.theorygames.dataclass

data class Achievement(
    val type: AchievementType,
    val unlockedAt: Long = System.currentTimeMillis()
)

enum class AchievementType(
    val displayName: String,
    val description: String,
    val icon: String,
    val requirement: Int
) {
    FIRST_WIN("First Victory", "Win your first round", "🥇", 1),
    HAT_TRICK("Hat Trick", "Win 3 rounds in a row", "🎩", 3),
    PERFECT_GAME("Perfect Game", "Win all rounds in a game", "💎", 10),
    SPEED_DEMON("Speed Demon", "Answer in under 5 seconds", "⚡", 1),
    CLOSE_CALL("Close Call", "Answer within 1% of correct answer", "🎯", 1),
    COMEBACK_KID("Comeback Kid", "Win after being in last place", "🔄", 1),
    POWER_USER("Power User", "Use 5 power-ups in one game", "🚀", 5),
    KNOWLEDGE_MASTER("Knowledge Master", "Play 50 games", "📚", 50)
}