package io.github.warforged5.theorygames.dataclass

object GameData {

    private val hdiQuestions = listOf(
        GameQuestion("hdi_1", "What is Norway's Human Development Index (2023)?", 0.961, "", GameCategory.HDI, QuestionDifficulty.EASY, "One of the highest in the world", "Norway consistently ranks #1-3 globally"),
        GameQuestion("hdi_2", "What is Switzerland's Human Development Index (2023)?", 0.962, "", GameCategory.HDI, QuestionDifficulty.EASY, "Alpine nation with high living standards", "Switzerland has excellent healthcare and education"),
        GameQuestion("hdi_3", "What is Chad's Human Development Index (2023)?", 0.394, "", GameCategory.HDI, QuestionDifficulty.HARD, "Central African nation", "One of the lowest HDI scores globally"),
        GameQuestion("hdi_4", "What is Germany's Human Development Index (2023)?", 0.942, "", GameCategory.HDI, QuestionDifficulty.MEDIUM, "Major European economy", "Strong in all HDI components"),
        GameQuestion("hdi_5", "What is Niger's Human Development Index (2023)?", 0.400, "", GameCategory.HDI, QuestionDifficulty.HARD, "Landlocked West African nation", "Faces significant development challenges")
    )

    private val gdpQuestions = listOf(
        GameQuestion("gdp_1", "What is Luxembourg's GDP per capita (2023)?", 125112.0, "$", GameCategory.GDP, QuestionDifficulty.HARD, "Small European financial hub", "Highest GDP per capita globally"),
        GameQuestion("gdp_2", "What is India's GDP per capita (2023)?", 2612.0, "$", GameCategory.GDP, QuestionDifficulty.MEDIUM, "Large emerging economy", "Over 1.4 billion people"),
        GameQuestion("gdp_3", "What is Ireland's GDP per capita (2023)?", 99013.0, "$", GameCategory.GDP, QuestionDifficulty.MEDIUM, "European tech hub", "Benefits from multinational corporations"),
        GameQuestion("gdp_4", "What is the USA's GDP per capita (2023)?", 76398.0, "$", GameCategory.GDP, QuestionDifficulty.EASY, "World's largest economy", "Around 330 million people"),
        GameQuestion("gdp_5", "What is Nigeria's GDP per capita (2023)?", 2184.0, "$", GameCategory.GDP, QuestionDifficulty.MEDIUM, "Africa's most populous nation", "Over 200 million people")
    )

    private val atomicQuestions = listOf(
        GameQuestion("atomic_1", "What is the atomic number of Carbon?", 6.0, "", GameCategory.ATOMIC_NUMBER, QuestionDifficulty.EASY, "Basis of organic chemistry", "Found in all living things"),
        GameQuestion("atomic_2", "What is the atomic number of Gold?", 79.0, "", GameCategory.ATOMIC_NUMBER, QuestionDifficulty.MEDIUM, "Precious metal, symbol Au", "From Latin 'aurum'"),
        GameQuestion("atomic_3", "What is the atomic number of Francium?", 87.0, "", GameCategory.ATOMIC_NUMBER, QuestionDifficulty.HARD, "Rarest naturally occurring element", "Extremely radioactive alkali metal"),
        GameQuestion("atomic_4", "What is the atomic number of Iron?", 26.0, "", GameCategory.ATOMIC_NUMBER, QuestionDifficulty.EASY, "Essential for blood", "Symbol Fe from Latin 'ferrum'"),
        GameQuestion("atomic_5", "What is the atomic number of Ruthenium?", 44.0, "", GameCategory.ATOMIC_NUMBER, QuestionDifficulty.HARD, "Transition metal, symbol Ru", "Named after Russia")
    )

    private val populationQuestions = listOf(
        GameQuestion("pop_1", "What is the population of Tokyo metropolitan area (millions)?", 37.4, "M", GameCategory.POPULATION, QuestionDifficulty.MEDIUM, "World's largest urban area", "Includes surrounding prefectures"),
        GameQuestion("pop_2", "What is the population of Vatican City?", 825.0, "", GameCategory.POPULATION, QuestionDifficulty.HARD, "World's smallest country", "Mostly clergy and Swiss Guard"),
        GameQuestion("pop_3", "What is the population of Shanghai (millions)?", 28.5, "M", GameCategory.POPULATION, QuestionDifficulty.MEDIUM, "China's largest city", "Major financial center"),
        GameQuestion("pop_4", "What is the population of Iceland (thousands)?", 382.0, "K", GameCategory.POPULATION, QuestionDifficulty.EASY, "Nordic island nation", "Land of fire and ice"),
        GameQuestion("pop_5", "What is the population of Monaco (thousands)?", 39.0, "K", GameCategory.POPULATION, QuestionDifficulty.HARD, "Tiny Mediterranean principality", "Most densely populated country")
    )

    private val areaQuestions = listOf(
        GameQuestion("area_1", "What is Russia's area in million km²?", 17.1, "M km²", GameCategory.AREA, QuestionDifficulty.EASY, "World's largest country", "Spans 11 time zones"),
        GameQuestion("area_2", "What is Vatican City's area in km²?", 0.17, "km²", GameCategory.AREA, QuestionDifficulty.HARD, "World's smallest country", "Smaller than many city blocks"),
        GameQuestion("area_3", "What is China's area in million km²?", 9.6, "M km²", GameCategory.AREA, QuestionDifficulty.MEDIUM, "Third or fourth largest country", "Depends on territorial disputes"),
        GameQuestion("area_4", "What is Monaco's area in km²?", 2.02, "km²", GameCategory.AREA, QuestionDifficulty.MEDIUM, "Second smallest country", "About the size of Central Park"),
        GameQuestion("area_5", "What is Australia's area in million km²?", 7.7, "M km²", GameCategory.AREA, QuestionDifficulty.EASY, "Island continent", "About the size of the continental US")
    )

    // GPU Performance Data
    private val gpuPerformanceDatabase = listOf(
        GPUPerformanceData("RTX 4090", 98.0, 112.0, 89.0, 285.0, 450, 1599),
        GPUPerformanceData("RTX 4080", 82.0, 95.0, 74.0, 228.0, 320, 1199),
        GPUPerformanceData("RTX 4070 Ti", 71.0, 81.0, 64.0, 195.0, 285, 799),
        GPUPerformanceData("RTX 4070", 62.0, 71.0, 56.0, 168.0, 200, 599),
        GPUPerformanceData("RTX 4060 Ti", 52.0, 59.0, 47.0, 142.0, 165, 399),
        GPUPerformanceData("RTX 4060", 44.0, 51.0, 40.0, 125.0, 115, 299),
        GPUPerformanceData("RX 7900 XTX", 91.0, 103.0, 82.0, 268.0, 355, 999),
        GPUPerformanceData("RX 7900 XT", 79.0, 89.0, 71.0, 225.0, 315, 899),
        GPUPerformanceData("RX 7800 XT", 68.0, 77.0, 61.0, 185.0, 263, 499),
        GPUPerformanceData("RX 7700 XT", 58.0, 66.0, 52.0, 155.0, 245, 449),
        GPUPerformanceData("RTX 3080", 64.0, 74.0, 58.0, 172.0, 320, 699),
        GPUPerformanceData("RTX 3070", 54.0, 62.0, 49.0, 145.0, 220, 499)
    )

    private val gpuQuestions = listOf(
        GameQuestion("gpu_1", "What FPS does this mystery GPU achieve in Cyberpunk 2077?", 98.0, "FPS", GameCategory.GPU, QuestionDifficulty.MEDIUM, "High-end consumer GPU", "Look at the performance pattern across games"),
        GameQuestion("gpu_2", "What FPS does this mystery GPU achieve in Red Dead Redemption 2?", 112.0, "FPS", GameCategory.GPU, QuestionDifficulty.MEDIUM, "Top-tier performance GPU", "Compare relative performance to other GPUs"),
        GameQuestion("gpu_3", "What FPS does this mystery GPU achieve in Cyberpunk 2077?", 82.0, "FPS", GameCategory.GPU, QuestionDifficulty.EASY, "High-performance gaming GPU", "Strong performance in demanding games"),
        GameQuestion("gpu_4", "What FPS does this mystery GPU achieve in Fortnite (1080p)?", 285.0, "FPS", GameCategory.GPU, QuestionDifficulty.HARD, "Flagship gaming GPU", "Exceptional performance in competitive games"),
        GameQuestion("gpu_5", "What FPS does this mystery GPU achieve in Red Dead Redemption 2?", 89.0, "FPS", GameCategory.GPU, QuestionDifficulty.MEDIUM, "AMD flagship GPU", "Strong performance across all games")
    )

    fun getQuestionsForCategory(category: GameCategory): List<GameQuestion> {
        return when (category) {
            GameCategory.HDI -> hdiQuestions
            GameCategory.GDP -> gdpQuestions
            GameCategory.ATOMIC_NUMBER -> atomicQuestions
            GameCategory.POPULATION -> populationQuestions
            GameCategory.AREA -> areaQuestions
            GameCategory.GPU -> gpuQuestions
        }
    }

    fun getRandomQuestion(category: GameCategory, difficulty: QuestionDifficulty? = null): GameQuestion {
        val questions = getQuestionsForCategory(category)
        return if (difficulty != null) {
            questions.filter { it.difficulty == difficulty }.randomOrNull()
                ?: questions.random()
        } else {
            questions.random()
        }
    }

    fun getGPUChartData(questionId: String): GPUChartData? {
        val question = gpuQuestions.find { it.id == questionId } ?: return null

        // Find the mystery GPU based on the correct answer
        val mysteryGpu = when (questionId) {
            "gpu_1", "gpu_2" -> gpuPerformanceDatabase.find { it.gpuName == "RTX 4090" }
            "gpu_3" -> gpuPerformanceDatabase.find { it.gpuName == "RTX 4080" }
            "gpu_4" -> gpuPerformanceDatabase.find { it.gpuName == "RTX 4090" }
            "gpu_5" -> gpuPerformanceDatabase.find { it.gpuName == "RX 7900 XTX" }
            else -> null
        } ?: return null

        // Select 3 comparison GPUs (different performance tiers)
        val comparisonGpus = gpuPerformanceDatabase
            .filter { it.gpuName != mysteryGpu.gpuName }
            .shuffled()
            .take(3)

        return GPUChartData(
            game1Name = "Cyberpunk 2077",
            game2Name = "Red Dead Redemption 2",
            mysteryGpu = mysteryGpu,
            comparisonGpus = comparisonGpus,
            game1MetricName = "FPS at 1440p Ultra",
            game2MetricName = "FPS at 1440p Ultra"
        )
    }

    fun getAllCategories(): List<GameCategory> {
        return GameCategory.values().toList()
    }

    fun getAllGameModes(): List<GameMode> {
        return GameMode.values().toList()
    }

    fun getAvailablePowerUps(): List<PowerUpType> {
        return PowerUpType.values().toList()
    }

    fun calculateStreakBonus(streak: Int): Int {
        return when {
            streak >= 5 -> 3
            streak >= 3 -> 2
            streak >= 2 -> 1
            else -> 0
        }
    }

    fun getRandomAvatar(): PlayerAvatar {
        return PlayerAvatar.values().random()
    }
}