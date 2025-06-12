package io.github.warforged5.theorygames.dataclass

data class PlayerAnswer(
    val playerId: String,
    val answer: Double = 0.0,           // For numeric answers (HDI, GDP, etc.)
    val textAnswer: String = "",        // For text answers (GPU names)
    val timeSubmitted: Long = System.currentTimeMillis(),
    val timeTaken: Double = 0.0,        // seconds
    val powerUpUsed: PowerUpType? = null
) {
    // Helper methods to get the appropriate answer type
    fun getNumericAnswer(): Double = answer
    fun getTextAnswers(): String = textAnswer

    // Determine if this is a text-based answer
    fun isTextAnswer(): Boolean = textAnswer.isNotEmpty()
}
