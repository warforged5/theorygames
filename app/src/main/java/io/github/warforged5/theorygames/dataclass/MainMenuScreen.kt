package io.github.warforged5.theorygames.dataclass

// Enhanced Screen Composables with All New Features

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlin.math.abs

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainMenuScreen(
    onNavigateToSetup: () -> Unit,
    onNavigateToRules: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "🧬 TheoryGames",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.background,
                            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                        )
                    )
                )
                .padding(paddingValues)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Spacer(modifier = Modifier.weight(1f))

            // Animated logo
            val infiniteTransition = rememberInfiniteTransition(label = "logo")
            val scale by infiniteTransition.animateFloat(
                initialValue = 1f,
                targetValue = 1.1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(2000),
                    repeatMode = RepeatMode.Reverse
                ), label = "scale"
            )

            Box(
                modifier = Modifier
                    .size(120.dp)
                    .background(
                        MaterialTheme.colorScheme.primaryContainer,
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "🧬",
                    style = MaterialTheme.typography.displayLarge,
                    modifier = Modifier.graphicsLayer { scaleX = scale; scaleY = scale }
                )
            }

            Text(
                text = "Welcome to TheoryGames",
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "🎯 Compete with friends in nerdy trivia!\n⚡ Use power-ups and unlock achievements\n🏆 Prove who's the ultimate theory master",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = onNavigateToSetup,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(Icons.Default.PlayArrow, contentDescription = null)
                Spacer(modifier = Modifier.width(12.dp))
                Text("Start New Game", style = MaterialTheme.typography.titleMedium)
            }

            OutlinedButton(
                onClick = onNavigateToRules,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Icon(Icons.Default.Help, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("How to Play", style = MaterialTheme.typography.titleMedium)
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameSetupScreen(
    gameViewModel: GameViewModel,
    onNavigateToGame: () -> Unit,
    onNavigateBack: () -> Unit
) {
    val gameState by gameViewModel.gameState
    val currentCategory by gameViewModel.currentCategory
    val selectedGameMode by gameViewModel.selectedGameMode
    var newPlayerName by remember { mutableStateOf("") }
    var selectedAvatar by remember { mutableStateOf(PlayerAvatar.SCIENTIST) }
    var showAvatarPicker by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("🎮 Game Setup") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Game Mode Selection
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        "🎯 Select Game Mode",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    GameData.getAllGameModes().forEach { gameMode ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable { gameViewModel.selectGameMode(gameMode) },
                            colors = CardDefaults.cardColors(
                                containerColor = if (selectedGameMode == gameMode)
                                    MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.surface
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp)
                            ) {
                                Text(
                                    gameMode.displayName,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = if (selectedGameMode == gameMode)
                                        MaterialTheme.colorScheme.onPrimary
                                    else MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    gameMode.description,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = if (selectedGameMode == gameMode)
                                        MaterialTheme.colorScheme.onPrimary
                                    else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }

            // Category Selection
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        "📚 Select Category",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(GameData.getAllCategories()) { category ->
                            FilterChip(
                                onClick = { gameViewModel.selectCategory(category) },
                                label = {
                                    Text("${category.icon} ${category.displayName}")
                                },
                                selected = currentCategory == category
                            )
                        }
                    }
                }
            }

            // Player Management
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        "👥 Players (${gameState.players.size})",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    // Add player section
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Avatar picker
                                Box(
                                    modifier = Modifier
                                        .size(48.dp)
                                        .background(
                                            MaterialTheme.colorScheme.primary,
                                            CircleShape
                                        )
                                        .clickable { showAvatarPicker = true },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        selectedAvatar.emoji,
                                        style = MaterialTheme.typography.headlineSmall
                                    )
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                OutlinedTextField(
                                    value = newPlayerName,
                                    onValueChange = { newPlayerName = it },
                                    label = { Text("Player Name") },
                                    modifier = Modifier.weight(1f)
                                )

                                Spacer(modifier = Modifier.width(8.dp))

                                Button(
                                    onClick = {
                                        if (newPlayerName.isNotBlank()) {
                                            gameViewModel.addPlayer(newPlayerName.trim(), selectedAvatar)
                                            newPlayerName = ""
                                            selectedAvatar = GameData.getRandomAvatar()
                                        }
                                    },
                                    enabled = newPlayerName.isNotBlank()
                                ) {
                                    Icon(Icons.Default.Add, contentDescription = "Add")
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Player list
                    gameState.players.forEach { player ->
                        PlayerCard(
                            player = player,
                            onRemove = { gameViewModel.removePlayer(player.id) },
                            showPowerUps = selectedGameMode == GameMode.POWERUP
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    gameViewModel.startGame()
                    onNavigateToGame()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                enabled = gameState.players.size >= 2
            ) {
                Icon(Icons.Default.PlayArrow, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Start Game! 🚀", style = MaterialTheme.typography.titleMedium)
            }

            if (gameState.players.size < 2) {
                Text(
                    "⚠️ Add at least 2 players to start",
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }

    // Avatar picker dialog
    if (showAvatarPicker) {
        AlertDialog(
            onDismissRequest = { showAvatarPicker = false },
            title = { Text("Choose Avatar") },
            text = {
                LazyColumn {
                    items(PlayerAvatar.values().toList()) { avatar ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedAvatar = avatar
                                    showAvatarPicker = false
                                }
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(avatar.emoji, style = MaterialTheme.typography.headlineMedium)
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(avatar.name)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showAvatarPicker = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun PlayerCard(
    player: Player,
    onRemove: () -> Unit,
    showPowerUps: Boolean = false
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            MaterialTheme.colorScheme.primary,
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        player.avatar.emoji,
                        style = MaterialTheme.typography.titleLarge
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        player.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    if (showPowerUps && player.powerUps.isNotEmpty()) {
                        Text(
                            "Power-ups: ${player.powerUps.joinToString { "${it.type.icon}×${it.usesRemaining}" }}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            IconButton(onClick = onRemove) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Remove",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameplayScreen(
    gameViewModel: GameViewModel,
    onNavigateToResults: () -> Unit,
    onNavigateToMenu: () -> Unit
) {
    val gameState by gameViewModel.gameState
    val showAnswerVisualization by gameViewModel.showAnswerVisualization
    val lastAchievements by remember { derivedStateOf { gameViewModel.lastAchievements } }

    LaunchedEffect(gameState.isGameActive) {
        if (!gameState.isGameActive && gameState.currentRound > 1) {
            onNavigateToResults()
        }
    }

    // Achievement popup
    LaunchedEffect(lastAchievements.size) {
        if (lastAchievements.isNotEmpty()) {
            delay(2000)
            gameViewModel.clearLastAchievements()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Round ${gameState.currentRound}/${gameState.maxRounds}")
                        Text(
                            gameState.gameMode.displayName,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onNavigateToMenu) {
                        Icon(Icons.Default.Home, contentDescription = "Home")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            gameState.currentQuestion?.let { question ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Timer with enhanced visual
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = when {
                                gameState.timeRemaining <= 5 -> MaterialTheme.colorScheme.errorContainer
                                gameState.timeRemaining <= 10 -> MaterialTheme.colorScheme.tertiaryContainer
                                else -> MaterialTheme.colorScheme.primaryContainer
                            }
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    "⏱️ Time: ${gameState.timeRemaining}s",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    question.difficulty.displayName,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = question.difficulty.color,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            LinearProgressIndicator(
                                progress = gameState.timeRemaining / 30f,
                                modifier = Modifier.fillMaxWidth(),
                                color = when {
                                    gameState.timeRemaining <= 5 -> MaterialTheme.colorScheme.error
                                    gameState.timeRemaining <= 10 -> MaterialTheme.colorScheme.tertiary
                                    else -> MaterialTheme.colorScheme.primary
                                }
                            )
                        }
                    }

                    // Question card with enhanced design
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    question.category.icon,
                                    style = MaterialTheme.typography.headlineMedium
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    question.category.displayName,
                                    style = MaterialTheme.typography.labelLarge,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                question.question,
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold
                            )
                            if (question.hint.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    "💡 Hint: ${question.hint}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }

                    // Answer visualization
                    AnimatedVisibility(
                        visible = showAnswerVisualization,
                        enter = slideInVertically() + fadeIn()
                    ) {
                        AnswerVisualizationCard(gameViewModel.getAnswerVisualization())
                    }

                    // Player answers
                    if (!showAnswerVisualization) {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(gameState.players) { player ->
                                EnhancedPlayerAnswerCard(
                                    player = player,
                                    hasAnswered = gameState.playerAnswers.any { it.playerId == player.id },
                                    isFrozen = gameState.frozenPlayers.contains(player.id),
                                    onSubmitAnswer = { answer, powerUp ->
                                        gameViewModel.submitAnswer(player.id, answer, powerUp)
                                    },
                                    onUsePowerUp = { powerUp ->
                                        gameViewModel.usePowerUp(player.id, powerUp)
                                    },
                                    gameViewModel = gameViewModel
                                )
                            }
                        }
                    }
                }
            }

            // Achievement notifications
            lastAchievements.forEach { achievement ->
                AchievementNotification(
                    achievement = achievement,
                    modifier = Modifier.align(Alignment.TopCenter)
                )
            }
        }
    }
}

@Composable
fun EnhancedPlayerAnswerCard(
    player: Player,
    hasAnswered: Boolean,
    isFrozen: Boolean,
    onSubmitAnswer: (Double, PowerUpType?) -> Unit,
    onUsePowerUp: (PowerUpType) -> Unit,
    gameViewModel: GameViewModel
) {
    var answer by remember(hasAnswered) { mutableStateOf("") }
    var showPowerUps by remember { mutableStateOf(false) }
    var selectedPowerUp by remember { mutableStateOf<PowerUpType?>(null) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = when {
                isFrozen -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                hasAnswered -> MaterialTheme.colorScheme.primaryContainer
                else -> MaterialTheme.colorScheme.surface
            }
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(
                                MaterialTheme.colorScheme.primary,
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            player.avatar.emoji,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            player.name,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Row {
                            Text(
                                "Score: ${player.score}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                            if (player.currentStreak > 0) {
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    "🔥${player.currentStreak}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.tertiary
                                )
                            }
                        }
                    }
                }

                if (player.powerUps.isNotEmpty() && !hasAnswered && !isFrozen) {
                    IconButton(onClick = { showPowerUps = !showPowerUps }) {
                        Icon(
                            Icons.Default.Star,
                            contentDescription = "Power-ups",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            if (isFrozen) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "❄️ Frozen for 5 seconds",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            } else if (!hasAnswered) {
                Spacer(modifier = Modifier.height(8.dp))

                // Power-ups selection
                AnimatedVisibility(visible = showPowerUps) {
                    PowerUpSelector(
                        powerUps = player.powerUps,
                        selectedPowerUp = selectedPowerUp,
                        onPowerUpSelected = { selectedPowerUp = it }
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = answer,
                        onValueChange = { answer = it },
                        label = { Text("Your Answer") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f),
                        suffix = {
                            gameViewModel.gameState.value.currentQuestion?.unit?.let { unit ->
                                if (unit.isNotEmpty()) Text(unit)
                            }
                        }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            answer.toDoubleOrNull()?.let { answerValue ->
                                onSubmitAnswer(answerValue, selectedPowerUp)
                                answer = ""
                                selectedPowerUp = null
                                showPowerUps = false
                            }
                        },
                        enabled = answer.toDoubleOrNull() != null
                    ) {
                        Text("Submit")
                    }
                }
            } else {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Answer submitted ✅",
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun PowerUpSelector(
    powerUps: List<PowerUp>,
    selectedPowerUp: PowerUpType?,
    onPowerUpSelected: (PowerUpType?) -> Unit
) {
    Column {
        Text(
            "⚡ Select Power-up:",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                FilterChip(
                    onClick = { onPowerUpSelected(null) },
                    label = { Text("None") },
                    selected = selectedPowerUp == null
                )
            }
            items(powerUps) { powerUp ->
                FilterChip(
                    onClick = { onPowerUpSelected(powerUp.type) },
                    label = {
                        Text("${powerUp.type.icon} ${powerUp.type.displayName} (${powerUp.usesRemaining})")
                    },
                    selected = selectedPowerUp == powerUp.type
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
fun AnswerVisualizationCard(visualizations: List<AnswerVisualization>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                "📊 Round Results",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))

            visualizations.forEach { vis ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (vis.isWinner) {
                            Text("🏆", style = MaterialTheme.typography.titleMedium)
                        } else {
                            Text("  ", style = MaterialTheme.typography.titleMedium)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                vis.playerName,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = if (vis.isWinner) FontWeight.Bold else FontWeight.Normal
                            )
                            Text(
                                "Answer: ${vis.answer}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Text(
                        "${vis.percentageError}% error",
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (vis.isWinner) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "✅ Correct Answer: ${visualizations.firstOrNull()?.correctAnswer ?: "N/A"}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun AchievementNotification(
    achievement: AchievementType,
    modifier: Modifier = Modifier
) {
    val animatedVisibility = remember { mutableStateOf(true) }

    LaunchedEffect(achievement) {
        delay(3000)
        animatedVisibility.value = false
    }

    AnimatedVisibility(
        visible = animatedVisibility.value,
        enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
        exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut(),
        modifier = modifier
    ) {
        Card(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    achievement.icon,
                    style = MaterialTheme.typography.headlineMedium
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        "🎉 Achievement Unlocked!",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        achievement.displayName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        achievement.description,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultsScreen(
    gameViewModel: GameViewModel,
    onNavigateToMenu: () -> Unit,
    onPlayAgain: () -> Unit
) {
    val topPlayers = gameViewModel.getTopPlayers()
    val gameResults = gameViewModel.gameResults

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("🏆 Game Results") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Winner announcement with celebration
            if (topPlayers.isNotEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            topPlayers.first().avatar.emoji,
                            style = MaterialTheme.typography.displayLarge
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "🎉 ${topPlayers.first().name} Wins! 🎉",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            "Score: ${topPlayers.first().score}/${gameResults.size}",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                        if (topPlayers.first().longestStreak > 0) {
                            Text(
                                "🔥 Best Streak: ${topPlayers.first().longestStreak}",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.tertiary
                            )
                        }
                    }
                }
            }

            // Detailed player statistics
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        "📊 Final Statistics",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    topPlayers.forEachIndexed { index, player ->
                        EnhancedPlayerResultCard(
                            player = player,
                            position = index + 1,
                            accuracy = gameViewModel.calculateAccuracy(player.id)
                        )
                        if (index < topPlayers.size - 1) {
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }

            // Achievements unlocked this game
            val allAchievements = topPlayers.flatMap { it.achievements }
            if (allAchievements.isNotEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            "🏅 Achievements Unlocked",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        allAchievements.forEach { achievement ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(vertical = 4.dp)
                            ) {
                                Text(
                                    achievement.type.icon,
                                    style = MaterialTheme.typography.headlineSmall
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        achievement.type.displayName,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        achievement.type.description,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Action buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onNavigateToMenu,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Home, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Main Menu")
                }
                Button(
                    onClick = onPlayAgain,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Refresh, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Play Again")
                }
            }
        }
    }
}

@Composable
fun EnhancedPlayerResultCard(
    player: Player,
    position: Int,
    accuracy: Double
) {
    val medal = when (position) {
        1 -> "🥇"
        2 -> "🥈"
        3 -> "🥉"
        else -> "#$position"
    }

    Card(
        colors = CardDefaults.cardColors(
            containerColor = when (position) {
                1 -> MaterialTheme.colorScheme.primaryContainer
                2 -> MaterialTheme.colorScheme.secondaryContainer
                3 -> MaterialTheme.colorScheme.tertiaryContainer
                else -> MaterialTheme.colorScheme.surfaceVariant
            }
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    medal,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    player.avatar.emoji,
                    style = MaterialTheme.typography.headlineSmall
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        player.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "${player.score} wins • ${String.format("%.1f", accuracy)}% accuracy",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    if (player.longestStreak > 1) {
                        Text(
                            "🔥 Best streak: ${player.longestStreak}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.tertiary
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RulesScreen(
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("📖 How to Play") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        "🎯 Basic Rules",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    val rules = listOf(
                        "🏁 Choose a game mode and category",
                        "👥 Add at least 2 players with avatars",
                        "📚 Answer trivia questions from your chosen category",
                        "🎯 The player with the closest answer wins the round",
                        "⏰ Players have 15-30 seconds depending on game mode",
                        "🏆 Player with the most round wins is the champion!",
                        "🔥 Build streaks for bonus points",
                        "⚡ Use power-ups strategically in Power-Up mode"
                    )

                    rules.forEach { rule ->
                        Text(
                            rule,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        "🎮 Game Modes",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    GameData.getAllGameModes().forEach { mode ->
                        Column(
                            modifier = Modifier.padding(vertical = 4.dp)
                        ) {
                            Text(
                                mode.displayName,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                mode.description,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        "⚡ Power-ups",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    GameData.getAvailablePowerUps().forEach { powerUp ->
                        Row(
                            modifier = Modifier.padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                powerUp.icon,
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    powerUp.displayName,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    powerUp.description,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        "📚 Categories",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    GameData.getAllCategories().forEach { category ->
                        Text(
                            "${category.icon} ${category.displayName}",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(vertical = 2.dp)
                        )
                    }
                }
            }
        }
    }
}