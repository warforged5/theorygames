package io.github.warforged5.theorygames.dataclass
import kotlinx.serialization.Serializable

@Serializable
enum class QuestionDifficulty(
    val displayName: String,
    val pointMultiplier: Int,
    val colorHex: String  // Changed from Color to String for serialization
) {
    EASY("Easy", 1, "#4CAF50"),
    MEDIUM("Medium", 2, "#FF9800"),
    HARD("Hard", 3, "#F44336");

    // Helper property to get the actual Color object
    val color: androidx.compose.ui.graphics.Color
        get() = androidx.compose.ui.graphics.Color(android.graphics.Color.parseColor(colorHex))
}