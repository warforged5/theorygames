package io.github.warforged5.theorygames.dataclass

enum class QuestionDifficulty(val displayName: String, val pointMultiplier: Int, val color: androidx.compose.ui.graphics.Color) {
    EASY("Easy", 1, androidx.compose.ui.graphics.Color(0xFF4CAF50)),
    MEDIUM("Medium", 2, androidx.compose.ui.graphics.Color(0xFFFF9800)),
    HARD("Hard", 3, androidx.compose.ui.graphics.Color(0xFFF44336))
}