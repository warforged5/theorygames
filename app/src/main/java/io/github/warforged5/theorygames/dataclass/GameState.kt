package io.github.warforged5.theorygames.dataclass

data class GameState(
    val currentQuestion: GameQuestion? = null,
    val players: List<Player> = emptyList(),
    val playerAnswers: List<PlayerAnswer> = emptyList(),
    val currentPlayerTurnIndex: Int = 0,
    val isGameActive: Boolean = false,
    val isPaused: Boolean = false,
    val currentRound: Int = 1,
    val maxRounds: Int = 10,
    val timeRemaining: Int = 30,
    val gameMode: GameMode = GameMode.CLASSIC,
    val powerUpsEnabled: Boolean = false,
    val frozenPlayers: Set<String> = emptySet(),
    val lastRoundWinner: String? = null,
    val timerEnabled: Boolean = true,
    val isWaitingForNextPlayer: Boolean = false
)