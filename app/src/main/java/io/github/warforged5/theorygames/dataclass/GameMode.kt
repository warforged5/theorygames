package io.github.warforged5.theorygames.dataclass

enum class GameMode(val displayName: String, val description: String) {
    CLASSIC("Classic", "Standard 10-round game with 30s per question"),
    SPEED("Speed Round", "Quick 5-round game with 15s per question"),
    ELIMINATION("Elimination", "Last place eliminated each round"),
    POWERUP("Power-Up Mode", "Classic mode with power-ups enabled")
}