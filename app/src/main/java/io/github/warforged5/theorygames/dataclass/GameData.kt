package io.github.warforged5.theorygames.dataclass

object GameData {

    private val hdiQuestions = listOf(
        GameQuestion("hdi_1", "What is Norway's Human Development Index (2023)?", 0.961, "", GameCategory.HDI, QuestionDifficulty.EASY, "One of the highest in the world", "Norway consistently ranks #1-3 globally", "Norway", "🇳🇴"),
        GameQuestion("hdi_2", "What is Switzerland's Human Development Index (2023)?", 0.962, "", GameCategory.HDI, QuestionDifficulty.EASY, "Alpine nation with high living standards", "Switzerland has excellent healthcare and education", "Switzerland", "🇨🇭"),
        GameQuestion("hdi_3", "What is Chad's Human Development Index (2023)?", 0.394, "", GameCategory.HDI, QuestionDifficulty.HARD, "Central African nation", "One of the lowest HDI scores globally", "Chad", "🇹🇩"),
        GameQuestion("hdi_4", "What is Germany's Human Development Index (2023)?", 0.942, "", GameCategory.HDI, QuestionDifficulty.MEDIUM, "Major European economy", "Strong in all HDI components", "Germany", "🇩🇪"),
        GameQuestion("hdi_5", "What is Niger's Human Development Index (2023)?", 0.400, "", GameCategory.HDI, QuestionDifficulty.HARD, "Landlocked West African nation", "Faces significant development challenges", "Niger", "🇳🇪")
    )

    private val gdpQuestions = listOf(
        GameQuestion("gdp_1", "What is Luxembourg's GDP per capita (2023)?", 125112.0, "$", GameCategory.GDP, QuestionDifficulty.HARD, "Small European financial hub", "Highest GDP per capita globally", "Luxembourg", "🇱🇺"),
        GameQuestion("gdp_2", "What is India's GDP per capita (2023)?", 2612.0, "$", GameCategory.GDP, QuestionDifficulty.MEDIUM, "Large emerging economy", "Over 1.4 billion people", "India", "🇮🇳"),
        GameQuestion("gdp_3", "What is Ireland's GDP per capita (2023)?", 99013.0, "$", GameCategory.GDP, QuestionDifficulty.MEDIUM, "European tech hub", "Benefits from multinational corporations", "Ireland", "🇮🇪"),
        GameQuestion("gdp_4", "What is the USA's GDP per capita (2023)?", 76398.0, "$", GameCategory.GDP, QuestionDifficulty.EASY, "World's largest economy", "Around 330 million people", "United States", "🇺🇸"),
        GameQuestion("gdp_5", "What is Nigeria's GDP per capita (2023)?", 2184.0, "$", GameCategory.GDP, QuestionDifficulty.MEDIUM, "Africa's most populous nation", "Over 200 million people", "Nigeria", "🇳🇬")
    )

    private val atomicQuestions = listOf(
        GameQuestion("atomic_1", "What is the atomic number of Carbon?", 6.0, "", GameCategory.ATOMIC_NUMBER, QuestionDifficulty.EASY, "Basis of organic chemistry", "Found in all living things"),
        GameQuestion("atomic_2", "What is the atomic number of Gold?", 79.0, "", GameCategory.ATOMIC_NUMBER, QuestionDifficulty.MEDIUM, "Precious metal, symbol Au", "From Latin 'aurum'"),
        GameQuestion("atomic_3", "What is the atomic number of Francium?", 87.0, "", GameCategory.ATOMIC_NUMBER, QuestionDifficulty.HARD, "Rarest naturally occurring element", "Extremely radioactive alkali metal"),
        GameQuestion("atomic_4", "What is the atomic number of Iron?", 26.0, "", GameCategory.ATOMIC_NUMBER, QuestionDifficulty.EASY, "Essential for blood", "Symbol Fe from Latin 'ferrum'"),
        GameQuestion("atomic_5", "What is the atomic number of Ruthenium?", 44.0, "", GameCategory.ATOMIC_NUMBER, QuestionDifficulty.HARD, "Transition metal, symbol Ru", "Named after Russia")
    )

    private val populationQuestions = listOf(
        GameQuestion("pop_1", "What is the population of Tokyo metropolitan area (millions)?", 37.4, "M", GameCategory.POPULATION, QuestionDifficulty.MEDIUM, "World's largest urban area", "Includes surrounding prefectures", "Japan", "🇯🇵"),
        GameQuestion("pop_2", "What is the population of Vatican City?", 825.0, "", GameCategory.POPULATION, QuestionDifficulty.HARD, "World's smallest country", "Mostly clergy and Swiss Guard", "Vatican City", "🇻🇦"),
        GameQuestion("pop_3", "What is the population of Shanghai (millions)?", 28.5, "M", GameCategory.POPULATION, QuestionDifficulty.MEDIUM, "China's largest city", "Major financial center", "China", "🇨🇳"),
        GameQuestion("pop_4", "What is the population of Iceland (thousands)?", 382.0, "K", GameCategory.POPULATION, QuestionDifficulty.EASY, "Nordic island nation", "Land of fire and ice", "Iceland", "🇮🇸"),
        GameQuestion("pop_5", "What is the population of Monaco (thousands)?", 39.0, "K", GameCategory.POPULATION, QuestionDifficulty.HARD, "Tiny Mediterranean principality", "Most densely populated country", "Monaco", "🇲🇨")
    )

    private val areaQuestions = listOf(
        GameQuestion("area_1", "What is Russia's area in million km²?", 17.1, "M km²", GameCategory.AREA, QuestionDifficulty.EASY, "World's largest country", "Spans 11 time zones", "Russia", "🇷🇺"),
        GameQuestion("area_2", "What is Vatican City's area in km²?", 0.17, "km²", GameCategory.AREA, QuestionDifficulty.HARD, "World's smallest country", "Smaller than many city blocks", "Vatican City", "🇻🇦"),
        GameQuestion("area_3", "What is China's area in million km²?", 9.6, "M km²", GameCategory.AREA, QuestionDifficulty.MEDIUM, "Third or fourth largest country", "Depends on territorial disputes", "China", "🇨🇳"),
        GameQuestion("area_4", "What is Monaco's area in km²?", 2.02, "km²", GameCategory.AREA, QuestionDifficulty.MEDIUM, "Second smallest country", "About the size of Central Park", "Monaco", "🇲🇨"),
        GameQuestion("area_5", "What is Australia's area in million km²?", 7.7, "M km²", GameCategory.AREA, QuestionDifficulty.EASY, "Island continent", "About the size of the continental US", "Australia", "🇦🇺")
    )

    // GPU Performance Database - Flexible system supporting multiple games
    private val gamesList = listOf(
        "Cyberpunk 2077",
        "Red Dead Redemption 2",
        "Assassin's Creed Valhalla",
        "Call of Duty: MW3",
        "Fortnite"
    )

    private val gpuPerformanceDatabase = listOf(
        GPUPerformanceData(
            fullName = "RTX 4090",
            shortName = "4090",
            gamePerformance = mapOf(
                "Cyberpunk 2077" to 98.0,
                "Red Dead Redemption 2" to 112.0,
                "Assassin's Creed Valhalla" to 89.0,
                "Call of Duty: MW3" to 195.0,
                "Fortnite" to 285.0
            ),
            powerConsumption = 450,
            msrp = 1599
        ),
        GPUPerformanceData(
            fullName = "RTX 4080",
            shortName = "4080",
            gamePerformance = mapOf(
                "Cyberpunk 2077" to 82.0,
                "Red Dead Redemption 2" to 95.0,
                "Assassin's Creed Valhalla" to 74.0,
                "Call of Duty: MW3" to 165.0,
                "Fortnite" to 228.0
            ),
            powerConsumption = 320,
            msrp = 1199
        ),
        GPUPerformanceData(
            fullName = "RTX 4070 Ti",
            shortName = "4070 Ti",
            gamePerformance = mapOf(
                "Cyberpunk 2077" to 71.0,
                "Red Dead Redemption 2" to 81.0,
                "Assassin's Creed Valhalla" to 64.0,
                "Call of Duty: MW3" to 142.0,
                "Fortnite" to 195.0
            ),
            powerConsumption = 285,
            msrp = 799
        ),
        GPUPerformanceData(
            fullName = "RTX 4070",
            shortName = "4070",
            gamePerformance = mapOf(
                "Cyberpunk 2077" to 62.0,
                "Red Dead Redemption 2" to 71.0,
                "Assassin's Creed Valhalla" to 56.0,
                "Call of Duty: MW3" to 125.0,
                "Fortnite" to 168.0
            ),
            powerConsumption = 200,
            msrp = 599
        ),
        GPUPerformanceData(
            fullName = "RTX 4060 Ti",
            shortName = "4060 Ti",
            gamePerformance = mapOf(
                "Cyberpunk 2077" to 52.0,
                "Red Dead Redemption 2" to 59.0,
                "Assassin's Creed Valhalla" to 47.0,
                "Call of Duty: MW3" to 108.0,
                "Fortnite" to 142.0
            ),
            powerConsumption = 165,
            msrp = 399
        ),
        GPUPerformanceData(
            fullName = "RTX 4060",
            shortName = "4060",
            gamePerformance = mapOf(
                "Cyberpunk 2077" to 44.0,
                "Red Dead Redemption 2" to 51.0,
                "Assassin's Creed Valhalla" to 40.0,
                "Call of Duty: MW3" to 95.0,
                "Fortnite" to 125.0
            ),
            powerConsumption = 115,
            msrp = 299
        ),
        GPUPerformanceData(
            fullName = "RX 7900 XTX",
            shortName = "7900 XTX",
            gamePerformance = mapOf(
                "Cyberpunk 2077" to 91.0,
                "Red Dead Redemption 2" to 103.0,
                "Assassin's Creed Valhalla" to 82.0,
                "Call of Duty: MW3" to 178.0,
                "Fortnite" to 268.0
            ),
            powerConsumption = 355,
            msrp = 999
        ),
        GPUPerformanceData(
            fullName = "RX 7900 XT",
            shortName = "7900 XT",
            gamePerformance = mapOf(
                "Cyberpunk 2077" to 79.0,
                "Red Dead Redemption 2" to 89.0,
                "Assassin's Creed Valhalla" to 71.0,
                "Call of Duty: MW3" to 155.0,
                "Fortnite" to 225.0
            ),
            powerConsumption = 315,
            msrp = 899
        ),
        GPUPerformanceData(
            fullName = "RX 7800 XT",
            shortName = "7800 XT",
            gamePerformance = mapOf(
                "Cyberpunk 2077" to 68.0,
                "Red Dead Redemption 2" to 77.0,
                "Assassin's Creed Valhalla" to 61.0,
                "Call of Duty: MW3" to 132.0,
                "Fortnite" to 185.0
            ),
            powerConsumption = 263,
            msrp = 499
        ),
        GPUPerformanceData(
            fullName = "RTX 3080",
            shortName = "3080",
            gamePerformance = mapOf(
                "Cyberpunk 2077" to 64.0,
                "Red Dead Redemption 2" to 74.0,
                "Assassin's Creed Valhalla" to 58.0,
                "Call of Duty: MW3" to 128.0,
                "Fortnite" to 172.0
            ),
            powerConsumption = 320,
            msrp = 699
        ),
        GPUPerformanceData(
            fullName = "RTX 3070",
            shortName = "3070",
            gamePerformance = mapOf(
                "Cyberpunk 2077" to 54.0,
                "Red Dead Redemption 2" to 62.0,
                "Assassin's Creed Valhalla" to 49.0,
                "Call of Duty: MW3" to 108.0,
                "Fortnite" to 145.0
            ),
            powerConsumption = 220,
            msrp = 499
        ),
        GPUPerformanceData(
            fullName = "RTX 5070",
            shortName = "5070",
            gamePerformance = mapOf(
                "Cyberpunk 2077" to 78.0,
                "Red Dead Redemption 2" to 88.0,
                "Assassin's Creed Valhalla" to 71.0,
                "Call of Duty: MW3" to 148.0,
                "Fortnite" to 185.0
            ),
            powerConsumption = 220,
            msrp = 549
        )
    )

    private val gpuQuestions = listOf(
        GameQuestion("gpu_1", "What GPU is this?", 0.0, "", GameCategory.GPU, QuestionDifficulty.MEDIUM, "High-end flagship GPU from NVIDIA", "Look at the performance pattern across different games"),
        GameQuestion("gpu_2", "What GPU is this?", 0.0, "", GameCategory.GPU, QuestionDifficulty.EASY, "Popular high-end gaming GPU", "Compare relative performance to other GPUs shown"),
        GameQuestion("gpu_3", "What GPU is this?", 0.0, "", GameCategory.GPU, QuestionDifficulty.MEDIUM, "Mid-range gaming GPU", "Notice the performance scaling across games"),
        GameQuestion("gpu_4", "What GPU is this?", 0.0, "", GameCategory.GPU, QuestionDifficulty.HARD, "AMD flagship GPU", "Strong performance competitor to NVIDIA's high-end"),
        GameQuestion("gpu_5", "What GPU is this?", 0.0, "", GameCategory.GPU, QuestionDifficulty.MEDIUM, "Next-gen gaming GPU", "Latest generation with improved efficiency")
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
        // Select mystery GPU based on question
        val mysteryGpu = when (questionId) {
            "gpu_1" -> gpuPerformanceDatabase.find { it.shortName == "4090" }
            "gpu_2" -> gpuPerformanceDatabase.find { it.shortName == "4080" }
            "gpu_3" -> gpuPerformanceDatabase.find { it.shortName == "4070" }
            "gpu_4" -> gpuPerformanceDatabase.find { it.shortName == "7900 XTX" }
            "gpu_5" -> gpuPerformanceDatabase.find { it.shortName == "5070" }
            else -> null
        } ?: return null

        // Select 4-5 comparison GPUs (different performance tiers)
        val comparisonGpus = gpuPerformanceDatabase
            .filter { it.fullName != mysteryGpu.fullName }
            .shuffled()
            .take(4)

        // Select 3-4 games to show (random selection from available games)
        val selectedGames = gamesList.shuffled().take(3)

        return GPUChartData(
            games = selectedGames,
            mysteryGpu = mysteryGpu,
            comparisonGpus = comparisonGpus,
            unit = "FPS"
        )
    }

    fun findGPUByName(guessedName: String): GPUPerformanceData? {
        val normalizedGuess = guessedName.trim().lowercase()

        // Try exact matches first (both full and short names)
        return gpuPerformanceDatabase.find { gpu ->
            gpu.fullName.lowercase() == normalizedGuess ||
                    gpu.shortName.lowercase() == normalizedGuess
        } ?: gpuPerformanceDatabase.find { gpu ->
            // Try partial matches
            gpu.fullName.lowercase().contains(normalizedGuess) ||
                    gpu.shortName.lowercase().contains(normalizedGuess) ||
                    normalizedGuess.contains(gpu.shortName.lowercase())
        }
    }

    fun isExactGPUMatch(guessedName: String, actualGpu: GPUPerformanceData): Boolean {
        val normalizedGuess = guessedName.trim().lowercase()
        return actualGpu.fullName.lowercase() == normalizedGuess ||
                actualGpu.shortName.lowercase() == normalizedGuess
    }

    fun calculateGPUPerformanceDistance(gpu1: GPUPerformanceData, gpu2: GPUPerformanceData, games: List<String>): Double {
        val gpu1Avg = games.map { gpu1.getPerformance(it) }.average()
        val gpu2Avg = games.map { gpu2.getPerformance(it) }.average()
        return kotlin.math.abs(gpu1Avg - gpu2Avg)
    }

    fun processGPUGuesses(
        playerAnswers: List<PlayerAnswer>,
        actualGpu: GPUPerformanceData,
        chartData: GPUChartData
    ): List<GPUGuess> {
        return playerAnswers.map { answer ->
            val guessedGpuName = answer.answer.toString() // This will be the GPU name string
            val isExact = isExactGPUMatch(guessedGpuName, actualGpu)

            val performanceDistance = if (isExact) {
                0.0
            } else {
                val guessedGpu = findGPUByName(guessedGpuName)
                if (guessedGpu != null) {
                    calculateGPUPerformanceDistance(guessedGpu, actualGpu, chartData.games)
                } else {
                    Double.MAX_VALUE // Invalid GPU name gets maximum distance
                }
            }

            GPUGuess(
                playerId = answer.playerId,
                guessedGpuName = guessedGpuName,
                actualGpu = actualGpu,
                isExactMatch = isExact,
                performanceDistance = performanceDistance
            )
        }
    }

    fun getAllGPUs(): List<GPUPerformanceData> {
        return gpuPerformanceDatabase
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