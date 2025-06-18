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

    private val movieEntertainmentQuestions = listOf(
        // CLASSIC MOVIES & BOX OFFICE - EASY
        GameQuestion(
            "movie_1",
            "What year was 'The Godfather' released?",
            1972.0,
            "",
            GameCategory.MOVIE_ENTERTAINMENT,
            QuestionDifficulty.EASY,
            "Francis Ford Coppola's masterpiece",
            "Often considered one of the greatest films ever made, launched the crime saga"
        ),
        GameQuestion(
            "movie_2",
            "What is the IMDb rating of 'The Shawshank Redemption' (out of 10)?",
            9.3,
            "/10",
            GameCategory.MOVIE_ENTERTAINMENT,
            QuestionDifficulty.EASY,
            "Highest-rated movie on IMDb",
            "Stephen King adaptation starring Tim Robbins and Morgan Freeman"
        ),
        GameQuestion(
            "movie_3",
            "What year was 'Star Wars: A New Hope' released?",
            1977.0,
            "",
            GameCategory.MOVIE_ENTERTAINMENT,
            QuestionDifficulty.EASY,
            "The original Star Wars movie",
            "George Lucas launched the iconic space opera franchise"
        ),
        GameQuestion(
            "movie_4",
            "What is 'Avatar' (2009) worldwide box office total (billion USD)?",
            2.92,
            "billion USD",
            GameCategory.MOVIE_ENTERTAINMENT,
            QuestionDifficulty.MEDIUM,
            "James Cameron's 3D blockbuster",
            "Held the worldwide box office record for over a decade"
        ),
        GameQuestion(
            "movie_5",
            "What year was 'Titanic' released?",
            1997.0,
            "",
            GameCategory.MOVIE_ENTERTAINMENT,
            QuestionDifficulty.EASY,
            "James Cameron's epic romance",
            "Dominated the 1997 box office and won 11 Oscars"
        ),

        // MODERN BLOCKBUSTERS - MEDIUM
        GameQuestion(
            "movie_6",
            "What is 'Avengers: Endgame' worldwide box office (billion USD)?",
            2.798,
            "billion USD",
            GameCategory.MOVIE_ENTERTAINMENT,
            QuestionDifficulty.MEDIUM,
            "Marvel's epic conclusion to the Infinity Saga",
            "Briefly held the worldwide box office record before Avatar re-releases"
        ),
        GameQuestion(
            "movie_7",
            "What is the runtime of 'Avengers: Endgame' (minutes)?",
            181.0,
            "minutes",
            GameCategory.MOVIE_ENTERTAINMENT,
            QuestionDifficulty.MEDIUM,
            "One of the longest Marvel movies",
            "3 hours and 1 minute of superhero action"
        ),
        GameQuestion(
            "movie_8",
            "What is 'Top Gun: Maverick' domestic box office (million USD)?",
            719.0,
            "million USD",
            GameCategory.MOVIE_ENTERTAINMENT,
            QuestionDifficulty.MEDIUM,
            "Tom Cruise's long-awaited sequel",
            "Massive hit that revived the franchise after 36 years"
        ),
        GameQuestion(
            "movie_9",
            "What is 'Spider-Man: No Way Home' worldwide box office (billion USD)?",
            1.92,
            "billion USD",
            GameCategory.MOVIE_ENTERTAINMENT,
            QuestionDifficulty.MEDIUM,
            "Multi-verse Spider-Man crossover",
            "Featured three Spider-Man actors and multiple villains"
        ),
        GameQuestion(
            "movie_10",
            "What year was 'The Dark Knight' released?",
            2008.0,
            "",
            GameCategory.MOVIE_ENTERTAINMENT,
            QuestionDifficulty.EASY,
            "Christopher Nolan's Batman masterpiece",
            "Heath Ledger's iconic Joker performance"
        ),

        // OSCAR WINNERS & CRITICAL ACCLAIM - MEDIUM/HARD
        GameQuestion(
            "movie_11",
            "How many Oscars did 'Lord of the Rings: Return of the King' win?",
            11.0,
            "",
            GameCategory.MOVIE_ENTERTAINMENT,
            QuestionDifficulty.MEDIUM,
            "Swept the Academy Awards",
            "Won every category it was nominated for, tying the record"
        ),
        GameQuestion(
            "movie_12",
            "What is 'Parasite' (2019) IMDb rating (out of 10)?",
            8.5,
            "/10",
            GameCategory.MOVIE_ENTERTAINMENT,
            QuestionDifficulty.HARD,
            "Bong Joon-ho's Oscar-winning thriller",
            "First non-English film to win Best Picture"
        ),
        GameQuestion(
            "movie_13",
            "How many Oscars did 'Titanic' win?",
            11.0,
            "",
            GameCategory.MOVIE_ENTERTAINMENT,
            QuestionDifficulty.MEDIUM,
            "Tied the record for most Oscar wins",
            "Dominated the 1998 Academy Awards ceremony"
        ),
        GameQuestion(
            "movie_14",
            "What is 'Citizen Kane' IMDb rating (out of 10)?",
            8.3,
            "/10",
            GameCategory.MOVIE_ENTERTAINMENT,
            QuestionDifficulty.HARD,
            "Orson Welles' cinematic masterpiece",
            "Often cited by critics as the greatest film ever made"
        ),
        GameQuestion(
            "movie_15",
            "What year did 'Everything Everywhere All at Once' win Best Picture?",
            2023.0,
            "",
            GameCategory.MOVIE_ENTERTAINMENT,
            QuestionDifficulty.MEDIUM,
            "Multiverse indie film phenomenon",
            "Won 7 Oscars including Best Picture, Director, and Acting categories"
        ),

        // FRANCHISE & SERIES DATA - MEDIUM/HARD
        GameQuestion(
            "movie_16",
            "What is the total worldwide box office of all Marvel MCU films (billion USD)?",
            29.5,
            "billion USD",
            GameCategory.MOVIE_ENTERTAINMENT,
            QuestionDifficulty.HARD,
            "Most successful film franchise ever",
            "30+ films spanning over 15 years of interconnected storytelling"
        ),
        GameQuestion(
            "movie_17",
            "How many 'Fast & Furious' main series films are there?",
            10.0,
            "",
            GameCategory.MOVIE_ENTERTAINMENT,
            QuestionDifficulty.MEDIUM,
            "Vin Diesel's action franchise",
            "Started with street racing, evolved into global action spectacle"
        ),
        GameQuestion(
            "movie_18",
            "What is 'Harry Potter' franchise total worldwide box office (billion USD)?",
            7.7,
            "billion USD",
            GameCategory.MOVIE_ENTERTAINMENT,
            QuestionDifficulty.HARD,
            "8-film wizarding world saga",
            "Based on J.K. Rowling's beloved book series"
        ),
        GameQuestion(
            "movie_19",
            "How many 'John Wick' movies have been released?",
            4.0,
            "",
            GameCategory.MOVIE_ENTERTAINMENT,
            QuestionDifficulty.MEDIUM,
            "Keanu Reeves action franchise",
            "Started with a simple revenge story, became action movie phenomenon"
        ),
        GameQuestion(
            "movie_20",
            "What is 'Star Wars' franchise total worldwide box office (billion USD)?",
            10.3,
            "billion USD",
            GameCategory.MOVIE_ENTERTAINMENT,
            QuestionDifficulty.HARD,
            "Galaxy far, far away",
            "12 theatrical films spanning 45+ years"
        ),

        // STREAMING & MODERN ENTERTAINMENT - MEDIUM/HARD
        GameQuestion(
            "movie_21",
            "What year did Netflix release 'Stranger Things' Season 1?",
            2016.0,
            "",
            GameCategory.MOVIE_ENTERTAINMENT,
            QuestionDifficulty.MEDIUM,
            "Netflix's flagship original series",
            "80s nostalgia sci-fi that launched the streaming wars"
        ),
        GameQuestion(
            "movie_22",
            "How many Emmy nominations did 'Game of Thrones' receive total?",
            161.0,
            "",
            GameCategory.MOVIE_ENTERTAINMENT,
            QuestionDifficulty.HARD,
            "HBO's fantasy epic",
            "Most Emmy-nominated drama series in television history"
        ),
        GameQuestion(
            "movie_23",
            "What is 'Squid Game' viewership in first 28 days (million hours)?",
            1650.0,
            "million hours",
            GameCategory.MOVIE_ENTERTAINMENT,
            QuestionDifficulty.HARD,
            "Netflix's Korean survival thriller",
            "Became the most-watched Netflix series of all time"
        ),
        GameQuestion(
            "movie_24",
            "What year did Disney+ launch?",
            2019.0,
            "",
            GameCategory.MOVIE_ENTERTAINMENT,
            QuestionDifficulty.MEDIUM,
            "Disney's streaming service",
            "Launched with The Mandalorian as flagship content"
        ),
        GameQuestion(
            "movie_25",
            "How many seasons did 'Breaking Bad' have?",
            5.0,
            "",
            GameCategory.MOVIE_ENTERTAINMENT,
            QuestionDifficulty.EASY,
            "Vince Gilligan's crime drama masterpiece",
            "Widely considered one of the greatest TV series ever made"
        ),

        // ANIMATION & FAMILY FILMS - EASY/MEDIUM
        GameQuestion(
            "movie_26",
            "What is 'Frozen' (2013) worldwide box office (billion USD)?",
            1.28,
            "billion USD",
            GameCategory.MOVIE_ENTERTAINMENT,
            QuestionDifficulty.MEDIUM,
            "Disney's animated musical phenomenon",
            "'Let It Go' became a global cultural phenomenon"
        ),
        GameQuestion(
            "movie_27",
            "What year was 'Toy Story' released?",
            1995.0,
            "",
            GameCategory.MOVIE_ENTERTAINMENT,
            QuestionDifficulty.EASY,
            "First fully computer-animated feature film",
            "Pixar's groundbreaking debut launched modern animation"
        ),
        GameQuestion(
            "movie_28",
            "What is 'The Lion King' (1994) worldwide box office (million USD)?",
            968.0,
            "million USD",
            GameCategory.MOVIE_ENTERTAINMENT,
            QuestionDifficulty.MEDIUM,
            "Disney's animated classic",
            "Highest-grossing traditionally animated film of all time"
        ),
        GameQuestion(
            "movie_29",
            "How many 'Shrek' movies are there?",
            4.0,
            "",
            GameCategory.MOVIE_ENTERTAINMENT,
            QuestionDifficulty.EASY,
            "DreamWorks' ogre franchise",
            "Subversive fairy tale parody that became a cultural phenomenon"
        ),
        GameQuestion(
            "movie_30",
            "What is 'Spider-Man: Into the Spider-Verse' IMDb rating (out of 10)?",
            8.4,
            "/10",
            GameCategory.MOVIE_ENTERTAINMENT,
            QuestionDifficulty.MEDIUM,
            "Revolutionary animated superhero film",
            "Won the Oscar for Best Animated Feature with innovative animation style"
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
            GameCategory.SCIENTIFIC_CONSTANTS -> scientificConstantsQuestions
            GameCategory.STOCK_MARKET -> stockMarketQuestions
            GameCategory.MOVIE_ENTERTAINMENT -> movieEntertainmentQuestions
        }
    }
    private val scientificConstantsQuestions = listOf(
        // PHYSICS CONSTANTS - EASY
        GameQuestion(
            "sci_1",
            "What is the speed of light in vacuum (million m/s)?",
            299.79,
            "million m/s",
            GameCategory.SCIENTIFIC_CONSTANTS,
            QuestionDifficulty.EASY,
            "The fastest speed possible in the universe",
            "Exactly 299,792,458 m/s, often approximated as 3×10⁸ m/s"
        ),
        GameQuestion(
            "sci_2",
            "What is the acceleration due to gravity on Earth (m/s²)?",
            9.81,
            "m/s²",
            GameCategory.SCIENTIFIC_CONSTANTS,
            QuestionDifficulty.EASY,
            "What goes up must come down at this rate",
            "Standard gravity, varies slightly by location but 9.81 m/s² is the standard"
        ),
        GameQuestion(
            "sci_3",
            "What is absolute zero in Celsius?",
            -273.15,
            "°C",
            GameCategory.SCIENTIFIC_CONSTANTS,
            QuestionDifficulty.EASY,
            "The coldest possible temperature",
            "0 Kelvin = -273.15°C, where all molecular motion theoretically stops"
        ),
        GameQuestion(
            "sci_4",
            "What is the value of π (pi) to 2 decimal places?",
            3.14,
            "",
            GameCategory.SCIENTIFIC_CONSTANTS,
            QuestionDifficulty.EASY,
            "The ratio of circumference to diameter",
            "π = 3.14159265359... but 3.14 is the common approximation"
        ),
        GameQuestion(
            "sci_5",
            "What is normal human body temperature in Celsius?",
            37.0,
            "°C",
            GameCategory.SCIENTIFIC_CONSTANTS,
            QuestionDifficulty.EASY,
            "The normal internal temperature of the human body",
            "98.6°F = 37°C, though it can vary slightly between individuals"
        ),

        // CHEMISTRY & PHYSICS - MEDIUM
        GameQuestion(
            "sci_6",
            "What is Avogadro's number (×10²³)?",
            6.022,
            "×10²³",
            GameCategory.SCIENTIFIC_CONSTANTS,
            QuestionDifficulty.MEDIUM,
            "Number of particles in one mole of substance",
            "6.022×10²³, fundamental constant in chemistry for counting atoms/molecules"
        ),
        GameQuestion(
            "sci_7",
            "What is the boiling point of water at sea level (°C)?",
            100.0,
            "°C",
            GameCategory.SCIENTIFIC_CONSTANTS,
            QuestionDifficulty.EASY,
            "When H₂O becomes steam at standard pressure",
            "100°C at 1 atmosphere pressure, varies with altitude"
        ),
        GameQuestion(
            "sci_8",
            "What is the mass of an electron (×10⁻³¹ kg)?",
            9.109,
            "×10⁻³¹ kg",
            GameCategory.SCIENTIFIC_CONSTANTS,
            QuestionDifficulty.MEDIUM,
            "The rest mass of the fundamental particle",
            "9.109×10⁻³¹ kg, about 1/1836 the mass of a proton"
        ),
        GameQuestion(
            "sci_9",
            "What is standard atmospheric pressure (kPa)?",
            101.325,
            "kPa",
            GameCategory.SCIENTIFIC_CONSTANTS,
            QuestionDifficulty.MEDIUM,
            "Air pressure at sea level",
            "101.325 kPa = 1 atmosphere = 760 mmHg"
        ),
        GameQuestion(
            "sci_10",
            "What is the melting point of ice (°C)?",
            0.0,
            "°C",
            GameCategory.SCIENTIFIC_CONSTANTS,
            QuestionDifficulty.EASY,
            "When solid water becomes liquid",
            "0°C at standard pressure, also the freezing point"
        ),

        // ASTRONOMY & EARTH SCIENCE - MEDIUM/HARD
        GameQuestion(
            "sci_11",
            "What is the average distance from Earth to the Sun (million km)?",
            149.6,
            "million km",
            GameCategory.SCIENTIFIC_CONSTANTS,
            QuestionDifficulty.MEDIUM,
            "One Astronomical Unit (AU)",
            "149.6 million km = 1 AU, the baseline for measuring solar system distances"
        ),
        GameQuestion(
            "sci_12",
            "What is the diameter of Earth (thousand km)?",
            12.756,
            "thousand km",
            GameCategory.SCIENTIFIC_CONSTANTS,
            QuestionDifficulty.MEDIUM,
            "Distance through the center of our planet",
            "12,756 km equatorial diameter, slightly flattened due to rotation"
        ),
        GameQuestion(
            "sci_13",
            "How long is one light-year (trillion km)?",
            9.461,
            "trillion km",
            GameCategory.SCIENTIFIC_CONSTANTS,
            QuestionDifficulty.HARD,
            "Distance light travels in one year",
            "9.461 trillion km, used to measure interstellar distances"
        ),
        GameQuestion(
            "sci_14",
            "What is the mass of Earth (×10²⁴ kg)?",
            5.972,
            "×10²⁴ kg",
            GameCategory.SCIENTIFIC_CONSTANTS,
            QuestionDifficulty.HARD,
            "Total mass of our planet",
            "5.972×10²⁴ kg, determined through gravitational measurements"
        ),
        GameQuestion(
            "sci_15",
            "What is the radius of Earth (thousand km)?",
            6.371,
            "thousand km",
            GameCategory.SCIENTIFIC_CONSTANTS,
            QuestionDifficulty.MEDIUM,
            "Distance from center to surface",
            "6,371 km average radius, varies from equator to poles"
        ),

        // ADVANCED PHYSICS - HARD
        GameQuestion(
            "sci_16",
            "What is Planck's constant (×10⁻³⁴ J⋅s)?",
            6.626,
            "×10⁻³⁴ J⋅s",
            GameCategory.SCIENTIFIC_CONSTANTS,
            QuestionDifficulty.HARD,
            "Fundamental constant of quantum mechanics",
            "6.626×10⁻³⁴ J⋅s, relates energy and frequency in quantum theory"
        ),
        GameQuestion(
            "sci_17",
            "What is the elementary charge (×10⁻¹⁹ C)?",
            1.602,
            "×10⁻¹⁹ C",
            GameCategory.SCIENTIFIC_CONSTANTS,
            QuestionDifficulty.HARD,
            "Electric charge of a single proton/electron",
            "1.602×10⁻¹⁹ coulombs, the smallest unit of electric charge"
        ),
        GameQuestion(
            "sci_18",
            "What is the universal gas constant R (J/mol⋅K)?",
            8.314,
            "J/mol⋅K",
            GameCategory.SCIENTIFIC_CONSTANTS,
            QuestionDifficulty.HARD,
            "Constant in the ideal gas law PV = nRT",
            "8.314 J/mol⋅K, relates pressure, volume, temperature and amount of gas"
        ),
        GameQuestion(
            "sci_19",
            "What is the Stefan-Boltzmann constant (×10⁻⁸ W/m²K⁴)?",
            5.670,
            "×10⁻⁸ W/m²K⁴",
            GameCategory.SCIENTIFIC_CONSTANTS,
            QuestionDifficulty.HARD,
            "Relates temperature to radiated power",
            "5.670×10⁻⁸ W/m²K⁴, used in blackbody radiation calculations"
        ),
        GameQuestion(
            "sci_20",
            "What is the gravitational constant G (×10⁻¹¹ m³/kg⋅s²)?",
            6.674,
            "×10⁻¹¹ m³/kg⋅s²",
            GameCategory.SCIENTIFIC_CONSTANTS,
            QuestionDifficulty.HARD,
            "Universal constant in Newton's law of gravitation",
            "6.674×10⁻¹¹ m³/kg⋅s², determines gravitational force strength"
        ),

        // CHEMISTRY SPECIFICS - MEDIUM/HARD
        GameQuestion(
            "sci_21",
            "What is the atomic mass of carbon-12 (u)?",
            12.000,
            "u",
            GameCategory.SCIENTIFIC_CONSTANTS,
            QuestionDifficulty.MEDIUM,
            "Standard reference for atomic mass units",
            "Exactly 12.000 u by definition, basis for all other atomic masses"
        ),
        GameQuestion(
            "sci_22",
            "What is the pH of pure water at 25°C?",
            7.0,
            "",
            GameCategory.SCIENTIFIC_CONSTANTS,
            QuestionDifficulty.EASY,
            "Neutral pH value",
            "pH 7.0 is neutral, below 7 is acidic, above 7 is basic"
        ),
        GameQuestion(
            "sci_23",
            "What is the boiling point of nitrogen (°C)?",
            -195.8,
            "°C",
            GameCategory.SCIENTIFIC_CONSTANTS,
            QuestionDifficulty.MEDIUM,
            "When liquid nitrogen becomes gas",
            "-195.8°C at standard pressure, why liquid nitrogen is so cold"
        ),
        GameQuestion(
            "sci_24",
            "What is the density of water at 4°C (g/cm³)?",
            1.000,
            "g/cm³",
            GameCategory.SCIENTIFIC_CONSTANTS,
            QuestionDifficulty.EASY,
            "Maximum density of H₂O",
            "1.000 g/cm³ at 4°C, water is densest at this temperature"
        ),
        GameQuestion(
            "sci_25",
            "What is the half-life of carbon-14 (years)?",
            5730.0,
            "years",
            GameCategory.SCIENTIFIC_CONSTANTS,
            QuestionDifficulty.MEDIUM,
            "Used for radiocarbon dating",
            "5,730 years, allows dating of organic materials up to ~50,000 years old"
        ),

        // MATHEMATICAL CONSTANTS - EASY/MEDIUM
        GameQuestion(
            "sci_26",
            "What is Euler's number (e) to 2 decimal places?",
            2.72,
            "",
            GameCategory.SCIENTIFIC_CONSTANTS,
            QuestionDifficulty.MEDIUM,
            "Base of natural logarithms",
            "e = 2.71828..., fundamental in calculus and exponential growth"
        ),
        GameQuestion(
            "sci_27",
            "What is the golden ratio (φ) to 2 decimal places?",
            1.62,
            "",
            GameCategory.SCIENTIFIC_CONSTANTS,
            QuestionDifficulty.MEDIUM,
            "Ratio found throughout nature and art",
            "φ = 1.618..., appears in spirals, proportions, and Fibonacci sequence"
        ),
        GameQuestion(
            "sci_28",
            "What is the square root of 2 to 2 decimal places?",
            1.41,
            "",
            GameCategory.SCIENTIFIC_CONSTANTS,
            QuestionDifficulty.EASY,
            "Diagonal of a unit square",
            "√2 = 1.414..., first known irrational number"
        ),

        // BIOLOGICAL CONSTANTS - EASY/MEDIUM
        GameQuestion(
            "sci_29",
            "How many chromosomes do humans have?",
            46.0,
            "",
            GameCategory.SCIENTIFIC_CONSTANTS,
            QuestionDifficulty.EASY,
            "Total number in diploid cells",
            "46 chromosomes (23 pairs), including XY or XX sex chromosomes"
        ),
        GameQuestion(
            "sci_30",
            "What is a normal resting heart rate (beats per minute)?",
            70.0,
            "bpm",
            GameCategory.SCIENTIFIC_CONSTANTS,
            QuestionDifficulty.EASY,
            "Average healthy adult heart rate",
            "60-100 bpm is normal range, 70 bpm is typical average"
        )
    )

    private val stockMarketQuestions = listOf(
        // MAJOR TECH STOCKS - EASY
        GameQuestion(
            "stock_1",
            "What is Apple's approximate stock price (USD)?",
            190.0,
            "USD",
            GameCategory.STOCK_MARKET,
            QuestionDifficulty.EASY,
            "World's most valuable company by market cap",
            "Apple (AAPL) trades around $190, making it the most valuable public company"
        ),
        GameQuestion(
            "stock_2",
            "What is Microsoft's approximate stock price (USD)?",
            415.0,
            "USD",
            GameCategory.STOCK_MARKET,
            QuestionDifficulty.EASY,
            "Software giant and cloud computing leader",
            "Microsoft (MSFT) benefits from Azure cloud growth and Office subscriptions"
        ),
        GameQuestion(
            "stock_3",
            "What is Tesla's approximate stock price (USD)?",
            240.0,
            "USD",
            GameCategory.STOCK_MARKET,
            QuestionDifficulty.EASY,
            "Electric vehicle and clean energy company",
            "Tesla (TSLA) is known for high volatility and Elon Musk's leadership"
        ),
        GameQuestion(
            "stock_4",
            "What is Google's (Alphabet) approximate stock price (USD)?",
            140.0,
            "USD",
            GameCategory.STOCK_MARKET,
            QuestionDifficulty.EASY,
            "Search engine and advertising giant",
            "Alphabet (GOOGL) dominates online search and digital advertising"
        ),
        GameQuestion(
            "stock_5",
            "What is Amazon's approximate stock price (USD)?",
            155.0,
            "USD",
            GameCategory.STOCK_MARKET,
            QuestionDifficulty.EASY,
            "E-commerce and cloud computing leader",
            "Amazon (AMZN) leads in AWS cloud services and online retail"
        ),

        // MARKET CAPITALIZATIONS - MEDIUM
        GameQuestion(
            "stock_6",
            "What is Apple's approximate market cap (trillion USD)?",
            3.0,
            "trillion USD",
            GameCategory.STOCK_MARKET,
            QuestionDifficulty.MEDIUM,
            "Market value of all outstanding shares",
            "Market cap = stock price × number of shares outstanding"
        ),
        GameQuestion(
            "stock_7",
            "What is NVIDIA's approximate market cap (trillion USD)?",
            1.8,
            "trillion USD",
            GameCategory.STOCK_MARKET,
            QuestionDifficulty.MEDIUM,
            "AI and graphics processing leader",
            "NVIDIA benefits massively from AI boom and GPU demand"
        ),
        GameQuestion(
            "stock_8",
            "What is Bitcoin's approximate market cap (trillion USD)?",
            1.2,
            "trillion USD",
            GameCategory.STOCK_MARKET,
            QuestionDifficulty.MEDIUM,
            "Largest cryptocurrency by market value",
            "Bitcoin's market cap fluctuates significantly with price changes"
        ),
        GameQuestion(
            "stock_9",
            "What is the total US stock market cap (trillion USD)?",
            45.0,
            "trillion USD",
            GameCategory.STOCK_MARKET,
            QuestionDifficulty.HARD,
            "Combined value of all US publicly traded companies",
            "US stock market represents about 40% of global stock market value"
        ),
        GameQuestion(
            "stock_10",
            "What is Berkshire Hathaway's market cap (billion USD)?",
            880.0,
            "billion USD",
            GameCategory.STOCK_MARKET,
            QuestionDifficulty.MEDIUM,
            "Warren Buffett's investment conglomerate",
            "Known for long-term value investing and diverse holdings"
        ),

        // FINANCIAL RATIOS & METRICS - MEDIUM/HARD
        GameQuestion(
            "stock_11",
            "What is the S&P 500's approximate P/E ratio?",
            25.0,
            "",
            GameCategory.STOCK_MARKET,
            QuestionDifficulty.MEDIUM,
            "Price-to-earnings ratio of the index",
            "P/E ratio shows how much investors pay per dollar of earnings"
        ),
        GameQuestion(
            "stock_12",
            "What is Tesla's approximate P/E ratio?",
            65.0,
            "",
            GameCategory.STOCK_MARKET,
            QuestionDifficulty.HARD,
            "Tesla trades at a premium valuation",
            "High P/E ratios often indicate growth expectations or overvaluation"
        ),
        GameQuestion(
            "stock_13",
            "What is the average dividend yield of S&P 500 (%)?",
            1.8,
            "%",
            GameCategory.STOCK_MARKET,
            QuestionDifficulty.MEDIUM,
            "Annual dividends as percentage of stock price",
            "Dividend yields have been declining as companies prefer buybacks"
        ),
        GameQuestion(
            "stock_14",
            "What is Apple's approximate P/E ratio?",
            29.0,
            "",
            GameCategory.STOCK_MARKET,
            QuestionDifficulty.MEDIUM,
            "Premium valuation for quality company",
            "Apple's P/E reflects its strong brand and consistent profits"
        ),
        GameQuestion(
            "stock_15",
            "What is the VIX volatility index typical level?",
            20.0,
            "",
            GameCategory.STOCK_MARKET,
            QuestionDifficulty.HARD,
            "Fear gauge of the stock market",
            "VIX above 30 indicates high fear, below 15 indicates complacency"
        ),

        // HISTORICAL & INDEX VALUES - EASY/MEDIUM
        GameQuestion(
            "stock_16",
            "What is the Dow Jones approximate level?",
            38000.0,
            "",
            GameCategory.STOCK_MARKET,
            QuestionDifficulty.EASY,
            "Price-weighted index of 30 large companies",
            "Dow Jones Industrial Average is one of the oldest stock indices"
        ),
        GameQuestion(
            "stock_17",
            "What is the S&P 500 approximate level?",
            5000.0,
            "",
            GameCategory.STOCK_MARKET,
            QuestionDifficulty.EASY,
            "Market-cap weighted index of 500 companies",
            "S&P 500 is considered the best gauge of US stock market"
        ),
        GameQuestion(
            "stock_18",
            "What is the NASDAQ approximate level?",
            16000.0,
            "",
            GameCategory.STOCK_MARKET,
            QuestionDifficulty.EASY,
            "Tech-heavy index including many growth stocks",
            "NASDAQ Composite includes over 3,000 stocks"
        ),
        GameQuestion(
            "stock_19",
            "What was the Dow Jones peak in 2021?",
            36799.0,
            "",
            GameCategory.STOCK_MARKET,
            QuestionDifficulty.MEDIUM,
            "All-time high before 2022 bear market",
            "Market peaked in late 2021 before inflation concerns hit"
        ),
        GameQuestion(
            "stock_20",
            "What is the Russell 2000 approximate level?",
            2000.0,
            "",
            GameCategory.STOCK_MARKET,
            QuestionDifficulty.HARD,
            "Small-cap stock index",
            "Russell 2000 tracks performance of 2,000 small-cap companies"
        ),

        // SPECIFIC COMPANY VALUES - MEDIUM/HARD
        GameQuestion(
            "stock_21",
            "What is JPMorgan Chase's stock price (USD)?",
            185.0,
            "USD",
            GameCategory.STOCK_MARKET,
            QuestionDifficulty.MEDIUM,
            "Largest US bank by assets",
            "JPMorgan (JPM) benefits from rising interest rates"
        ),
        GameQuestion(
            "stock_22",
            "What is Coca-Cola's approximate stock price (USD)?",
            60.0,
            "USD",
            GameCategory.STOCK_MARKET,
            QuestionDifficulty.MEDIUM,
            "Dividend aristocrat and Warren Buffett holding",
            "Coca-Cola (KO) is known for consistent dividends and global brand"
        ),
        GameQuestion(
            "stock_23",
            "What is Johnson & Johnson's stock price (USD)?",
            160.0,
            "USD",
            GameCategory.STOCK_MARKET,
            QuestionDifficulty.MEDIUM,
            "Healthcare and pharmaceutical giant",
            "J&J (JNJ) is a dividend aristocrat with diversified healthcare portfolio"
        ),
        GameQuestion(
            "stock_24",
            "What is Walmart's approximate stock price (USD)?",
            165.0,
            "USD",
            GameCategory.STOCK_MARKET,
            QuestionDifficulty.MEDIUM,
            "World's largest retailer",
            "Walmart (WMT) dominates discount retail and growing e-commerce"
        ),
        GameQuestion(
            "stock_25",
            "What is Boeing's approximate stock price (USD)?",
            210.0,
            "USD",
            GameCategory.STOCK_MARKET,
            QuestionDifficulty.HARD,
            "Aerospace and defense contractor",
            "Boeing (BA) has faced challenges with 737 MAX and supply chain issues"
        ),

        // CRYPTOCURRENCY & ALTERNATIVES - MEDIUM/HARD
        GameQuestion(
            "stock_26",
            "What is Bitcoin's approximate price (thousand USD)?",
            67.0,
            "thousand USD",
            GameCategory.STOCK_MARKET,
            QuestionDifficulty.MEDIUM,
            "Leading cryptocurrency by market cap",
            "Bitcoin price is highly volatile and influenced by institutional adoption"
        ),
        GameQuestion(
            "stock_27",
            "What is Ethereum's approximate price (thousand USD)?",
            3.5,
            "thousand USD",
            GameCategory.STOCK_MARKET,
            QuestionDifficulty.HARD,
            "Second-largest cryptocurrency platform",
            "Ethereum enables smart contracts and decentralized applications"
        ),
        GameQuestion(
            "stock_28",
            "What is gold's price per ounce (USD)?",
            2050.0,
            "USD",
            GameCategory.STOCK_MARKET,
            QuestionDifficulty.MEDIUM,
            "Traditional safe-haven asset",
            "Gold prices rise during economic uncertainty and inflation"
        ),
        GameQuestion(
            "stock_29",
            "What is crude oil's price per barrel (USD)?",
            78.0,
            "USD",
            GameCategory.STOCK_MARKET,
            QuestionDifficulty.MEDIUM,
            "WTI crude oil benchmark price",
            "Oil prices affected by OPEC decisions and global demand"
        ),
        GameQuestion(
            "stock_30",
            "What is the 10-year Treasury yield (%)?",
            4.3,
            "%",
            GameCategory.STOCK_MARKET,
            QuestionDifficulty.HARD,
            "Benchmark interest rate for US government debt",
            "10-year yield influences mortgage rates and stock valuations"
        )
    )

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

        // FIXED: Select only 2 games instead of 3
        val selectedGames = gamesList.shuffled().take(2)

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