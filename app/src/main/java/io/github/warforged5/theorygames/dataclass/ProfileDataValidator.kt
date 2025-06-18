package io.github.warforged5.theorygames.dataclass

object ProfileDataValidator {

    fun validateProfile(profile: UserProfile): UserProfile {
        return profile.copy(
            totalGamesPlayed = maxOf(0, profile.totalGamesPlayed),
            totalWins = minOf(profile.totalWins, profile.totalGamesPlayed),
            bestStreak = maxOf(0, profile.bestStreak),
            categoryStats = profile.categoryStats.mapValues { (_, stats) ->
                stats.copy(
                    gamesPlayed = maxOf(0, stats.gamesPlayed),
                    wins = minOf(stats.wins, stats.gamesPlayed),
                    bestStreak = maxOf(0, stats.bestStreak),
                    averageAccuracy = stats.averageAccuracy.coerceIn(0.0, 100.0)
                )
            }
        )
    }

    fun canStartGame(players: List<Player>): Boolean {
        return players.size >= 2 && players.all { it.name.isNotBlank() }
    }
}