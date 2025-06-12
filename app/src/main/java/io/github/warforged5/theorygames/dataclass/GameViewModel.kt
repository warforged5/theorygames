package io.github.warforged5.theorygames.dataclass

// Enhanced Game Logic with Sequential Turn-Based Timer System

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.round

class GameViewModel : ViewModel() {

    private var _gameState = mutableStateOf(GameState())
    val gameState: State<GameState> = _gameState

    private var _gameResults = mutableStateListOf<GameResult>()
    val gameResults: List<GameResult> = _gameResults

    private var _currentCategory = mutableStateOf(GameCategory.HDI)
    val currentCategory: State<GameCategory> = _currentCategory

    private var _selectedGameMode = mutableStateOf(GameMode.CLASSIC)
    val selectedGameMode: State<GameMode> = _selectedGameMode

    private var _questionStartTime = 0L
    private var _showAnswerVisualization = mutableStateOf(false)
    val showAnswerVisualization: State<Boolean> = _showAnswerVisualization

    private var _lastAchievements = mutableStateListOf<AchievementType>()
    val lastAchievements: List<AchievementType> = _lastAchievements

    private var _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    // Single timer job for current player's turn
    private var timerJob: Job? = null

    fun addPlayer(playerName: String, avatar: PlayerAvatar = GameData.getRandomAvatar()) {
        val newPlayer = Player(
            id = "player_${System.currentTimeMillis()}",
            name = playerName.trim(),
            avatar = avatar,
            powerUps = if (_selectedGameMode.value == GameMode.POWERUP) {
                listOf(
                    PowerUp(PowerUpType.HINT, 2),
                    PowerUp(PowerUpType.EXTRA_TIME, 1)
                )
            } else emptyList()
        )
        _gameState.value = _gameState.value.copy(
            players = _gameState.value.players + newPlayer
        )
    }

    fun removePlayer(playerId: String) {
        _gameState.value = _gameState.value.copy(
            players = _gameState.value.players.filter { it.id != playerId }
        )
    }

    fun selectCategory(category: GameCategory) {
        _currentCategory.value = category
    }

    fun selectGameMode(gameMode: GameMode) {
        _selectedGameMode.value = gameMode
        val (rounds, powerUpsEnabled) = when (gameMode) {
            GameMode.CLASSIC -> Pair(10, false)
            GameMode.SPEED -> Pair(5, false)
            GameMode.ELIMINATION -> Pair(8, false)
            GameMode.POWERUP -> Pair(10, true)
        }

        _gameState.value = _gameState.value.copy(
            maxRounds = rounds,
            powerUpsEnabled = powerUpsEnabled,
            gameMode = gameMode
        )
    }

    fun toggleTimer(enabled: Boolean) {
        _gameState.value = _gameState.value.copy(timerEnabled = enabled)
    }

    fun startGame() {
        if (_gameState.value.players.isEmpty()) return

        _isLoading.value = true
        _gameResults.clear()
        _lastAchievements.clear()
        _showAnswerVisualization.value = false

        _gameState.value = _gameState.value.copy(
            isGameActive = true,
            currentRound = 1,
            currentPlayerTurnIndex = 0,
            players = _gameState.value.players.map {
                it.copy(
                    score = 0,
                    currentStreak = 0,
                    totalGamesPlayed = it.totalGamesPlayed + 1
                )
            }
        )

        viewModelScope.launch {
            delay(500) // Loading animation
            _isLoading.value = false
            nextQuestion()
        }
    }

    fun nextQuestion() {
        // Cancel any existing timer
        timerJob?.cancel()

        val difficulty = when (_gameState.value.currentRound) {
            in 1..3 -> QuestionDifficulty.EASY
            in 4..7 -> QuestionDifficulty.MEDIUM
            else -> QuestionDifficulty.HARD
        }

        val question = GameData.getRandomQuestion(_currentCategory.value, difficulty)
        val timePerQuestion = when (_selectedGameMode.value) {
            GameMode.SPEED -> 15
            else -> 30
        }

        _gameState.value = _gameState.value.copy(
            currentQuestion = question,
            playerAnswers = emptyList(),
            currentPlayerTurnIndex = 0, // Start with first player
            timeRemaining = timePerQuestion,
            frozenPlayers = emptySet(),
            isPaused = false,
            isWaitingForNextPlayer = false
        )
        _questionStartTime = System.currentTimeMillis()
        _showAnswerVisualization.value = false

        startCurrentPlayerTurn()
    }

    private fun startCurrentPlayerTurn() {
        if (!_gameState.value.timerEnabled) return

        // Cancel existing timer
        timerJob?.cancel()

        val timePerTurn = when (_selectedGameMode.value) {
            GameMode.SPEED -> 15
            else -> 30
        }

        _gameState.value = _gameState.value.copy(
            timeRemaining = timePerTurn,
            isWaitingForNextPlayer = false
        )

        timerJob = viewModelScope.launch {
            for (i in timePerTurn downTo 0) {
                // Check if game is still active and not paused
                if (!_gameState.value.isGameActive || _gameState.value.isPaused || !_gameState.value.timerEnabled) {
                    break
                }

                _gameState.value = _gameState.value.copy(timeRemaining = i)
                delay(1000)

                if (i == 0) {
                    // Current player's time is up, move to next player
                    moveToNextPlayer()
                    break
                }
            }
        }
    }

    fun pauseGame() {
        _gameState.value = _gameState.value.copy(isPaused = true)
        timerJob?.cancel()
    }

    fun resumeGame() {
        _gameState.value = _gameState.value.copy(isPaused = false)
        if (_gameState.value.timeRemaining > 0) {
            startCurrentPlayerTurn()
        }
    }

    fun submitAnswer(playerId: String, answer: Double, powerUpUsed: PowerUpType? = null) {
        submitNumericAnswer(playerId, answer, powerUpUsed)
    }

    fun submitTextAnswer(playerId: String, textAnswer: String, powerUpUsed: PowerUpType? = null) {
        // Only allow current player to submit
        val currentPlayerIndex = _gameState.value.currentPlayerTurnIndex
        val currentPlayer = _gameState.value.players.getOrNull(currentPlayerIndex)

        if (currentPlayer?.id != playerId) return
        if (_gameState.value.frozenPlayers.contains(playerId)) return

        // Check if player has already answered this round
        val existingAnswer = _gameState.value.playerAnswers.find { it.playerId == playerId }
        if (existingAnswer != null) return

        val timeTaken = (System.currentTimeMillis() - _questionStartTime) / 1000.0
        val playerAnswer = PlayerAnswer(
            playerId = playerId,
            textAnswer = textAnswer.trim(),
            timeTaken = timeTaken,
            powerUpUsed = powerUpUsed
        )
        val updatedAnswers = _gameState.value.playerAnswers + playerAnswer

        _gameState.value = _gameState.value.copy(playerAnswers = updatedAnswers)

        // Handle power-up usage
        powerUpUsed?.let { powerUp ->
            usePowerUp(playerId, powerUp)
        }

        // Check for speed achievements
        if (timeTaken < 5.0) {
            unlockAchievement(playerId, AchievementType.SPEED_DEMON)
        }

        // Stop current timer and move to next player
        timerJob?.cancel()
        moveToNextPlayer()
    }

    private fun submitNumericAnswer(playerId: String, answer: Double, powerUpUsed: PowerUpType? = null) {
        // Only allow current player to submit
        val currentPlayerIndex = _gameState.value.currentPlayerTurnIndex
        val currentPlayer = _gameState.value.players.getOrNull(currentPlayerIndex)

        if (currentPlayer?.id != playerId) return
        if (_gameState.value.frozenPlayers.contains(playerId)) return

        // Check if player has already answered this round
        val existingAnswer = _gameState.value.playerAnswers.find { it.playerId == playerId }
        if (existingAnswer != null) return

        val timeTaken = (System.currentTimeMillis() - _questionStartTime) / 1000.0
        val playerAnswer = PlayerAnswer(
            playerId = playerId,
            answer = answer,
            timeTaken = timeTaken,
            powerUpUsed = powerUpUsed
        )
        val updatedAnswers = _gameState.value.playerAnswers + playerAnswer

        _gameState.value = _gameState.value.copy(playerAnswers = updatedAnswers)

        // Handle power-up usage
        powerUpUsed?.let { powerUp ->
            usePowerUp(playerId, powerUp)
        }

        // Check for speed achievements
        if (timeTaken < 5.0) {
            unlockAchievement(playerId, AchievementType.SPEED_DEMON)
        }

        // Stop current timer and move to next player
        timerJob?.cancel()
        moveToNextPlayer()
    }

    private fun moveToNextPlayer() {
        val nextPlayerIndex = _gameState.value.currentPlayerTurnIndex + 1

        if (nextPlayerIndex < _gameState.value.players.size) {
            // Move to next player
            _gameState.value = _gameState.value.copy(
                currentPlayerTurnIndex = nextPlayerIndex,
                isWaitingForNextPlayer = true
            )

            // Brief pause before starting next player's turn
            viewModelScope.launch {
                delay(1500)
                startCurrentPlayerTurn()
            }
        } else {
            // All players have had their turn, process results
            processRoundResults()
        }
    }

    fun getCurrentPlayer(): Player? {
        val currentIndex = _gameState.value.currentPlayerTurnIndex
        return _gameState.value.players.getOrNull(currentIndex)
    }

    fun isPlayersTurn(playerId: String): Boolean {
        val currentPlayer = getCurrentPlayer()
        return currentPlayer?.id == playerId && !_gameState.value.isWaitingForNextPlayer
    }

    fun hasPlayerAnswered(playerId: String): Boolean {
        return _gameState.value.playerAnswers.any { it.playerId == playerId }
    }

    fun usePowerUp(playerId: String, powerUpType: PowerUpType) {
        val player = _gameState.value.players.find { it.id == playerId } ?: return
        val powerUp = player.powerUps.find { it.type == powerUpType && it.usesRemaining > 0 } ?: return

        // Update player's power-ups
        val updatedPowerUps = player.powerUps.map {
            if (it.type == powerUpType) it.copy(usesRemaining = it.usesRemaining - 1)
            else it
        }.filter { it.usesRemaining > 0 }

        val updatedPlayer = player.copy(powerUps = updatedPowerUps)
        val updatedPlayers = _gameState.value.players.map {
            if (it.id == playerId) updatedPlayer else it
        }

        // Apply power-up effects
        when (powerUpType) {
            PowerUpType.EXTRA_TIME -> {
                val newTime = (_gameState.value.timeRemaining + 15).coerceAtMost(60)
                _gameState.value = _gameState.value.copy(
                    timeRemaining = newTime,
                    players = updatedPlayers
                )
            }
            PowerUpType.FREEZE -> {
                // In turn-based mode, freeze doesn't make as much sense, but we can skip other players' turns
                val otherPlayerIds = _gameState.value.players
                    .filter { it.id != playerId }
                    .map { it.id }
                    .toSet()
                _gameState.value = _gameState.value.copy(
                    frozenPlayers = otherPlayerIds,
                    players = updatedPlayers
                )

                viewModelScope.launch {
                    delay(5000)
                    _gameState.value = _gameState.value.copy(frozenPlayers = emptySet())
                }
            }
            else -> {
                _gameState.value = _gameState.value.copy(players = updatedPlayers)
            }
        }
    }

    private fun processRoundResults() {
        // Cancel timer to ensure no multiple timers
        timerJob?.cancel()

        val question = _gameState.value.currentQuestion ?: return
        val answers = _gameState.value.playerAnswers

        // Calculate accuracy and find winner
        val winner = findWinner(answers, question.correctAnswer)
        val pointsAwarded = calculatePoints(answers, question, winner)

        // Update scores and streaks
        val updatedPlayers = updatePlayersAfterRound(pointsAwarded, winner?.id)

        // Check for achievements
        checkAchievements(answers, question, winner)

        // Save result
        val result = GameResult(
            question = question,
            playerAnswers = answers,
            winner = winner,
            correctAnswer = question.correctAnswer,
            pointsAwarded = pointsAwarded
        )
        _gameResults.add(result)

        _gameState.value = _gameState.value.copy(
            players = updatedPlayers,
            lastRoundWinner = winner?.id
        )

        // Show answer visualization
        _showAnswerVisualization.value = true

        // Check if game is over or handle elimination
        viewModelScope.launch {
            delay(4000) // Show results for 4 seconds
            _showAnswerVisualization.value = false

            if (_gameState.value.gameMode == GameMode.ELIMINATION && _gameState.value.currentRound > 2) {
                eliminateLastPlace()
            }

            if (_gameState.value.currentRound >= _gameState.value.maxRounds ||
                (_gameState.value.gameMode == GameMode.ELIMINATION && _gameState.value.players.size <= 1)) {
                endGame()
            } else {
                _gameState.value = _gameState.value.copy(
                    currentRound = _gameState.value.currentRound + 1
                )
                nextQuestion()
            }
        }
    }

    private fun calculatePoints(answers: List<PlayerAnswer>, question: GameQuestion, winner: Player?): Map<String, Int> {
        val pointsMap = mutableMapOf<String, Int>()

        answers.forEach { answer ->
            val player = _gameState.value.players.find { it.id == answer.playerId }
            if (player != null) {
                var points = 0

                // Winner gets base points
                if (answer.playerId == winner?.id) {
                    points = question.difficulty.pointMultiplier

                    // Apply power-up effects
                    if (answer.powerUpUsed == PowerUpType.DOUBLE_POINTS) {
                        points *= 2
                    }

                    // Add streak bonus
                    points += GameData.calculateStreakBonus(player.currentStreak + 1)
                }

                pointsMap[answer.playerId] = points
            }
        }

        // Handle steal point power-up
        answers.forEach { answer ->
            if (answer.powerUpUsed == PowerUpType.STEAL_POINT && answer.playerId != winner?.id) {
                winner?.let { winnerPlayer ->
                    pointsMap[winnerPlayer.id] = (pointsMap[winnerPlayer.id] ?: 0) - 1
                    pointsMap[answer.playerId] = (pointsMap[answer.playerId] ?: 0) + 1
                }
            }
        }

        return pointsMap
    }

    private fun updatePlayersAfterRound(pointsAwarded: Map<String, Int>, winnerId: String?): List<Player> {
        return _gameState.value.players.map { player ->
            val pointsGained = pointsAwarded[player.id] ?: 0
            val newScore = player.score + pointsGained

            val newStreak = if (player.id == winnerId) {
                player.currentStreak + 1
            } else 0

            val newLongestStreak = maxOf(player.longestStreak, newStreak)
            val newTotalWins = if (player.id == winnerId) player.totalWins + 1 else player.totalWins

            // Award new power-ups occasionally
            val newPowerUps = if (_gameState.value.powerUpsEnabled && player.id == winnerId &&
                _gameState.value.currentRound % 3 == 0) {
                player.powerUps + PowerUp(GameData.getAvailablePowerUps().random())
            } else player.powerUps

            player.copy(
                score = newScore,
                currentStreak = newStreak,
                longestStreak = newLongestStreak,
                totalWins = newTotalWins,
                powerUps = newPowerUps
            )
        }
    }

    private fun eliminateLastPlace() {
        val sortedPlayers = _gameState.value.players.sortedBy { it.score }
        if (sortedPlayers.size > 1) {
            val eliminatedPlayer = sortedPlayers.first()
            _gameState.value = _gameState.value.copy(
                players = _gameState.value.players.filter { it.id != eliminatedPlayer.id }
            )
        }
    }

    private fun checkAchievements(answers: List<PlayerAnswer>, question: GameQuestion, winner: Player?) {
        winner?.let { winnerPlayer ->
            // First win
            if (winnerPlayer.totalWins == 0) {
                unlockAchievement(winnerPlayer.id, AchievementType.FIRST_WIN)
            }

            // Hat trick (3 in a row)
            if (winnerPlayer.currentStreak + 1 >= 3) {
                unlockAchievement(winnerPlayer.id, AchievementType.HAT_TRICK)
            }

            // Close call (within 1%)
            val winnerAnswer = answers.find { it.playerId == winnerPlayer.id }
            winnerAnswer?.let { answer ->
                val errorPercent = abs(answer.answer - question.correctAnswer) / question.correctAnswer * 100
                if (errorPercent <= 1.0) {
                    unlockAchievement(winnerPlayer.id, AchievementType.CLOSE_CALL)
                }
            }
        }

        // Check power user achievement
        _gameState.value.players.forEach { player ->
            val powerUpsUsedThisGame = _gameResults.sumOf { result ->
                result.playerAnswers.count { it.playerId == player.id && it.powerUpUsed != null }
            }
            if (powerUpsUsedThisGame >= 5) {
                unlockAchievement(player.id, AchievementType.POWER_USER)
            }
        }
    }

    private fun unlockAchievement(playerId: String, achievementType: AchievementType) {
        val player = _gameState.value.players.find { it.id == playerId } ?: return

        if (player.achievements.none { it.type == achievementType }) {
            val newAchievement = Achievement(achievementType)
            val updatedPlayer = player.copy(
                achievements = player.achievements + newAchievement
            )

            val updatedPlayers = _gameState.value.players.map {
                if (it.id == playerId) updatedPlayer else it
            }

            _gameState.value = _gameState.value.copy(players = updatedPlayers)
            _lastAchievements.add(achievementType)
        }
    }

    private fun findWinner(answers: List<PlayerAnswer>, correctAnswer: Double): Player? {
        if (answers.isEmpty()) return null

        val closestAnswer = answers.minByOrNull { abs(it.answer - correctAnswer) }
        return _gameState.value.players.find { it.id == closestAnswer?.playerId }
    }

    private fun endGame() {
        // Cancel any running timer
        timerJob?.cancel()

        // Check for perfect game achievement
        val topPlayer = _gameState.value.players.maxByOrNull { it.score }
        topPlayer?.let { player ->
            if (player.score == _gameResults.size) {
                unlockAchievement(player.id, AchievementType.PERFECT_GAME)
            }
        }

        _gameState.value = _gameState.value.copy(
            isGameActive = false,
            currentQuestion = null
        )
    }

    fun resetGame() {
        // Cancel any running timer
        timerJob?.cancel()

        _gameState.value = GameState(
            players = _gameState.value.players.map { it.copy(score = 0, currentStreak = 0) },
            gameMode = _selectedGameMode.value,
            powerUpsEnabled = _selectedGameMode.value == GameMode.POWERUP
        )
        _gameResults.clear()
        _lastAchievements.clear()
        _showAnswerVisualization.value = false
        _isLoading.value = false
    }

    fun getTopPlayers(): List<Player> {
        return _gameState.value.players.sortedByDescending { it.score }
    }

    fun getAnswerVisualization(): List<AnswerVisualization> {
        val question = _gameState.value.currentQuestion ?: return emptyList()
        val answers = _gameState.value.playerAnswers
        val winner = findWinner(answers, question.correctAnswer)

        return answers.map { answer ->
            val player = _gameState.value.players.find { it.id == answer.playerId }
            val error = abs(answer.answer - question.correctAnswer) / question.correctAnswer * 100

            AnswerVisualization(
                playerId = answer.playerId,
                playerName = player?.name ?: "Unknown",
                answer = answer.answer,
                correctAnswer = question.correctAnswer,
                percentageError = round(error * 10) / 10,
                isWinner = answer.playerId == winner?.id
            )
        }.sortedBy { it.percentageError }
    }

    fun calculateAccuracy(playerId: String): Double {
        val playerResults = _gameResults.filter { result ->
            result.playerAnswers.any { it.playerId == playerId }
        }

        if (playerResults.isEmpty()) return 0.0

        val totalError = playerResults.sumOf { result ->
            val playerAnswer = result.playerAnswers.find { it.playerId == playerId }?.answer ?: 0.0
            abs(playerAnswer - result.correctAnswer) / result.correctAnswer
        }

        return ((1.0 - (totalError / playerResults.size)) * 100).coerceAtLeast(0.0)
    }

    fun getPlayerPowerUps(playerId: String): List<PowerUp> {
        return _gameState.value.players.find { it.id == playerId }?.powerUps ?: emptyList()
    }

    fun findGPUWinner(answers: List<PlayerAnswer>, question: GameQuestion): Player? {
        return findGPUWinner(answers, question)
    }

    override fun onCleared() {
        super.onCleared()
        // Cancel timer when ViewModel is cleared
        timerJob?.cancel()
    }
}