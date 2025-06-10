package io.github.warforged5.theorygames.dataclass

data class GameQuestion(
    val id: String,
    val question: String,
    val correctAnswer: Double,
    val unit: String = "",
    val category: GameCategory,
    val difficulty: QuestionDifficulty = QuestionDifficulty.MEDIUM,
    val hint: String = "",
    val explanation: String = ""
)

