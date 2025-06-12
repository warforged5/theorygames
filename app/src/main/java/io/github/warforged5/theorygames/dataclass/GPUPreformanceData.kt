package io.github.warforged5.theorygames.dataclass

data class GPUPerformanceData(
    val fullName: String,        // "RTX 4070"
    val shortName: String,       // "4070"
    val gamePerformance: Map<String, Double>, // Game name -> FPS
    val powerConsumption: Int = 0, // watts
    val msrp: Int = 0            // USD
) {
    fun getAveragePerformance(): Double {
        return gamePerformance.values.average()
    }

    fun getPerformance(gameName: String): Double {
        return gamePerformance[gameName] ?: 0.0
    }
}


