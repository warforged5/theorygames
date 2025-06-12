package io.github.warforged5.theorygames.dataclass

data class GPUGuess(
    val playerId: String,
    val guessedGpuName: String,
    val actualGpu: GPUPerformanceData,
    val isExactMatch: Boolean,
    val performanceDistance: Double
)
