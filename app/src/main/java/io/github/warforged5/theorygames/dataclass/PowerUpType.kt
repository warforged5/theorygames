package io.github.warforged5.theorygames.dataclass

enum class PowerUpType(
    val displayName: String,
    val description: String,
    val icon: String,
    val cost: Int
) {
    DOUBLE_POINTS("Double Points", "Double your points for this round", "⭐", 2),
    STEAL_POINT("Steal Point", "Steal a point from the winner", "🎯", 3),
    EXTRA_TIME("Extra Time", "Get 15 extra seconds", "⏰", 2),
    HINT("Hint", "Reveal a helpful hint", "💡", 1),
    FREEZE("Freeze Others", "Freeze other players for 5 seconds", "❄️", 4),
    SHIELD("Shield", "Protect from point theft", "🛡️", 2)
}