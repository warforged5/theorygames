package io.github.warforged5.theorygames.dataclass

data class GPUChartData(
    val games: List<String>,
    val mysteryGpu: GPUPerformanceData,
    val comparisonGpus: List<GPUPerformanceData>,
    val unit: String = "FPS"
)