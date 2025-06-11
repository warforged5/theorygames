package io.github.warforged5.theorygames.dataclass

data class GPUPerformanceData(
    val gpuName: String,
    val cyberpunk2077Fps: Double,
    val redDeadRedemption2Fps: Double,
    val assassinsCreedValhallFps: Double,
    val fortnite1080pFps: Double,
    val powerConsumption: Int, // watts
    val msrp: Int // USD
)

data class GPUChartData(
    val game1Name: String,
    val game2Name: String,
    val mysteryGpu: GPUPerformanceData,
    val comparisonGpus: List<GPUPerformanceData>,
    val game1MetricName: String,
    val game2MetricName: String
)