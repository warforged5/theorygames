package io.github.warforged5.theorygames.dataclass

// Polished Screen Composables with Material 3 Design and Animations

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlin.math.abs

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryCard(
    category: GameCategory,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .aspectRatio(1.2f)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected)
                MaterialTheme.colorScheme.primaryContainer
            else MaterialTheme.colorScheme.surfaceContainer
        ),
        border = if (isSelected) {
            BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
        } else null,
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 8.dp else 4.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                category.icon,
                style = MaterialTheme.typography.displaySmall
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                category.displayName,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = if (isSelected)
                    MaterialTheme.colorScheme.onPrimaryContainer
                else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainMenuScreen(
    onNavigateToSetup: () -> Unit,
    onNavigateToRules: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "background")
    val animatedAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.7f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000),
            repeatMode = RepeatMode.Reverse
        ), label = "alpha"
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "TheoryGames",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primaryContainer.copy(alpha = animatedAlpha),
                            MaterialTheme.colorScheme.background
                        ),
                        radius = 1000f
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Spacer(modifier = Modifier.weight(0.5f))

                // Hero section
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(8.dp, RoundedCornerShape(24.dp)),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                    ),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Filled.Psychology,
                            contentDescription = null,
                            modifier = Modifier.size(72.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Welcome to TheoryGames",
                            style = MaterialTheme.typography.headlineSmall,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Compete with friends in nerdy trivia challenges",
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Feature highlights
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    FeatureCard(
                        icon = Icons.Outlined.EmojiEvents,
                        title = "Compete",
                        description = "Multiple game modes"
                    )
                    FeatureCard(
                        icon = Icons.Outlined.Star,
                        title = "Power-ups",
                        description = "Strategic abilities"
                    )
                    FeatureCard(
                        icon = Icons.Outlined.School,
                        title = "Learn",
                        description = "Educational content"
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                // Action buttons
                ExtendedFloatingActionButton(
                    onClick = onNavigateToSetup,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(Icons.Default.PlayArrow, contentDescription = null)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        "Start New Game",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedButton(
                    onClick = onNavigateToRules,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Icon(Icons.Outlined.Help, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("How to Play")
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun FeatureCard(
    icon: ImageVector,
    title: String,
    description: String
) {
    Card(
        modifier = Modifier
            .width(100.dp)
            .height(120.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                icon,
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Text(
                description,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
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
    val isLoading by gameViewModel.isLoading

    var newPlayerName by remember { mutableStateOf("") }
    var selectedAvatar by remember { mutableStateOf(PlayerAvatar.SCIENTIST) }
    var showAvatarPicker by remember { mutableStateOf(false) }

    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Game Setup") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            AnimatedVisibility(
                visible = gameState.players.size >= 2,
                enter = scaleIn() + fadeIn(),
                exit = scaleOut() + fadeOut()
            ) {
                ExtendedFloatingActionButton(
                    onClick = {
                        gameViewModel.startGame()
                        onNavigateToGame()
                    },
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = MaterialTheme.colorScheme.onPrimary,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Icon(Icons.Default.PlayArrow, contentDescription = null)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Start Game")
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item {
                // Game Mode Selection
                ElevatedCard {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Text(
                            "Game Mode",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        GameData.getAllGameModes().forEach { gameMode ->
                            GameModeCard(
                                gameMode = gameMode,
                                isSelected = selectedGameMode == gameMode,
                                onClick = { gameViewModel.selectGameMode(gameMode) }
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }

            item {
                // Category Selection
                ElevatedCard {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Text(
                            "Category",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        // Grid of category cards
                        val categories = GameData.getAllCategories()
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            categories.chunked(2).forEach { rowCategories ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    rowCategories.forEach { category ->
                                        CategoryCard(
                                            category = category,
                                            isSelected = currentCategory == category,
                                            onClick = { gameViewModel.selectCategory(category) },
                                            modifier = Modifier.weight(1f)
                                        )
                                    }
                                    // Fill remaining space if odd number
                                    if (rowCategories.size == 1) {
                                        Spacer(modifier = Modifier.weight(1f))
                                    }
                                }
                            }
                        }
                    }
                }
            }

            item {
                // Game Settings
                ElevatedCard {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Text(
                            "Game Settings",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        // Timer toggle
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.surfaceContainer
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        "Timer",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        if (gameState.timerEnabled) "Questions have time limits"
                                        else "Play at your own pace",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                Switch(
                                    checked = gameState.timerEnabled,
                                    onCheckedChange = { gameViewModel.toggleTimer(it) }
                                )
                            }
                        }
                    }
                }
            }

            item {
                // Player Management
                ElevatedCard {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "Players",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Badge {
                                Text("${gameState.players.size}")
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Add player input
                        OutlinedCard(
                            colors = CardDefaults.outlinedCardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceContainerLow
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Avatar picker
                                Surface(
                                    modifier = Modifier
                                        .size(48.dp)
                                        .clickable { showAvatarPicker = true },
                                    shape = CircleShape,
                                    color = MaterialTheme.colorScheme.primaryContainer
                                ) {
                                    Box(
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            selectedAvatar.emoji,
                                            style = MaterialTheme.typography.headlineSmall
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                OutlinedTextField(
                                    value = newPlayerName,
                                    onValueChange = { newPlayerName = it },
                                    label = { Text("Player Name") },
                                    modifier = Modifier
                                        .weight(1f)
                                        .focusRequester(focusRequester),
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(
                                        imeAction = ImeAction.Done
                                    ),
                                    keyboardActions = KeyboardActions(
                                        onDone = {
                                            if (newPlayerName.isNotBlank()) {
                                                gameViewModel.addPlayer(newPlayerName.trim(), selectedAvatar)
                                                newPlayerName = ""
                                                selectedAvatar = GameData.getRandomAvatar()
                                            }
                                            focusManager.clearFocus()
                                        }
                                    )
                                )

                                Spacer(modifier = Modifier.width(8.dp))

                                FilledIconButton(
                                    onClick = {
                                        if (newPlayerName.isNotBlank()) {
                                            gameViewModel.addPlayer(newPlayerName.trim(), selectedAvatar)
                                            newPlayerName = ""
                                            selectedAvatar = GameData.getRandomAvatar()
                                        }
                                    },
                                    enabled = newPlayerName.isNotBlank()
                                ) {
                                    Icon(Icons.Default.PersonAdd, contentDescription = "Add Player")
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Player list with animations
                        gameState.players.forEach { player ->
                            AnimatedVisibility(
                                visible = true,
                                enter = slideInVertically() + fadeIn(),
                                exit = slideOutVertically() + fadeOut()
                            ) {
                                EnhancedPlayerCard(
                                    player = player,
                                    onRemove = { gameViewModel.removePlayer(player.id) },
                                    showPowerUps = selectedGameMode == GameMode.POWERUP
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                        }

                        if (gameState.players.size < 2) {
                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.errorContainer
                                )
                            ) {
                                Text(
                                    "Add at least 2 players to start",
                                    modifier = Modifier.padding(16.dp),
                                    color = MaterialTheme.colorScheme.onErrorContainer,
                                    style = MaterialTheme.typography.bodyMedium,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }

            // Add space for FAB
            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }

    // Avatar picker dialog
    if (showAvatarPicker) {
        AlertDialog(
            onDismissRequest = { showAvatarPicker = false },
            title = { Text("Choose Avatar") },
            text = {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(PlayerAvatar.values().toList()) { avatar ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedAvatar = avatar
                                    showAvatarPicker = false
                                },
                            colors = CardDefaults.cardColors(
                                containerColor = if (selectedAvatar == avatar)
                                    MaterialTheme.colorScheme.primaryContainer
                                else MaterialTheme.colorScheme.surface
                            )
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(avatar.emoji, style = MaterialTheme.typography.headlineMedium)
                                Spacer(modifier = Modifier.width(16.dp))
                                Text(
                                    avatar.name,
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showAvatarPicker = false }) {
                    Text("Done")
                }
            }
        )
    }
}

@Composable
fun GameModeCard(
    gameMode: GameMode,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected)
                MaterialTheme.colorScheme.primaryContainer
            else MaterialTheme.colorScheme.surfaceContainer
        ),
        border = if (isSelected) {
            BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
        } else null
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    gameMode.displayName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    gameMode.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (isSelected) {
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            } else {
                Icon(
                    Icons.Outlined.RadioButtonUnchecked,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.outline
                )
            }
        }
    }
}

@Composable
fun EnhancedPlayerCard(
    player: Player,
    onRemove: () -> Unit,
    showPowerUps: Boolean = false
) {
    ElevatedCard(
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    modifier = Modifier.size(48.dp),
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            player.avatar.emoji,
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
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
                            "Power-ups: ${player.powerUps.size}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            IconButton(
                onClick = onRemove,
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                )
            ) {
                Icon(Icons.Default.DeleteOutline, contentDescription = "Remove Player")
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

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Round ${gameState.currentRound} of ${gameState.maxRounds}")
                        Text(
                            gameState.gameMode.displayName,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                actions = {
                    if (gameState.timerEnabled) {
                        IconButton(
                            onClick = { gameViewModel.pauseGame() },
                            enabled = gameState.isGameActive && !gameState.isPaused
                        ) {
                            Icon(Icons.Default.Pause, contentDescription = "Pause")
                        }
                    }
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
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        // Enhanced Timer Card - now shows current player's turn with progress bar
                        TimerCard(
                            timeRemaining = gameState.timeRemaining,
                            totalTime = when (gameState.gameMode) {
                                GameMode.SPEED -> 15
                                else -> 30
                            },
                            difficulty = question.difficulty,
                            isPaused = gameState.isPaused,
                            isTimerEnabled = gameState.timerEnabled,
                            currentPlayer = gameViewModel.getCurrentPlayer(),
                            isWaitingForNextPlayer = gameState.isWaitingForNextPlayer,
                            onResume = { gameViewModel.resumeGame() }
                        )
                    }

                    item {
                        // Question Card with GPU chart support
                        QuestionCard(
                            question = question,
                            gameViewModel = gameViewModel
                        )
                    }

                    item {
                        // Answer visualization (when showing results)
                        AnimatedVisibility(
                            visible = showAnswerVisualization,
                            enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                            exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
                        ) {
                            AnswerVisualizationCard(gameViewModel.getAnswerVisualization())
                        }
                    }

                    // Player answer cards - turn-based system
                    if (!showAnswerVisualization) {
                        items(gameState.players) { player ->
                            EnhancedPlayerAnswerCard(
                                player = player,
                                hasAnswered = gameState.playerAnswers.any { it.playerId == player.id },
                                isFrozen = gameState.frozenPlayers.contains(player.id),
                                isCurrentPlayer = gameViewModel.isPlayersTurn(player.id),
                                isWaitingForTurn = !gameViewModel.isPlayersTurn(player.id) &&
                                        !gameState.playerAnswers.any { it.playerId == player.id } &&
                                        !gameState.isWaitingForNextPlayer,
                                onSubmitAnswer = { answer, powerUp ->
                                    gameViewModel.submitAnswer(player.id, answer, powerUp)
                                },
                                onSubmitTextAnswer = { textAnswer, powerUp ->
                                    gameViewModel.submitTextAnswer(player.id, textAnswer, powerUp)
                                },
                                gameViewModel = gameViewModel
                            )
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
fun QuestionCard(
    question: GameQuestion,
    gameViewModel: GameViewModel? = null
) {
    ElevatedCard(
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.secondaryContainer
                    ) {
                        Text(
                            question.category.icon,
                            modifier = Modifier.padding(8.dp),
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        question.category.displayName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                // Show country flag and name if available
                if (question.countryFlag.isNotEmpty()) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            question.countryFlag,
                            style = MaterialTheme.typography.headlineMedium
                        )
                        if (question.countryName.isNotEmpty()) {
                            Spacer(modifier = Modifier.width(8.dp))
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = MaterialTheme.colorScheme.tertiaryContainer
                            ) {
                                Text(
                                    question.countryName,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    style = MaterialTheme.typography.labelLarge,
                                    color = MaterialTheme.colorScheme.onTertiaryContainer
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                question.question,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                lineHeight = MaterialTheme.typography.headlineSmall.lineHeight
            )

            // Show GPU performance charts if this is a GPU question
            if (question.category == GameCategory.GPU) {
                Spacer(modifier = Modifier.height(16.dp))

                GameData.getGPUChartData(question.id)?.let { chartData ->
                    GPUPerformanceChart(
                        chartData = chartData,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            if (question.hint.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.surfaceContainer
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Lightbulb,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "Hint: ${question.hint}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

// [Rest of the existing components remain the same: TimerCard, EnhancedPlayerAnswerCard, PowerUpSelector, AnswerVisualizationCard, AchievementNotification, ResultsScreen, WinnerCard, FinalStandingsCard, PlayerResultCard, AchievementsCard, RulesScreen]

@Composable
fun TimerCard(
    timeRemaining: Int,
    totalTime: Int,
    difficulty: QuestionDifficulty,
    isPaused: Boolean,
    isTimerEnabled: Boolean,
    currentPlayer: Player?,
    isWaitingForNextPlayer: Boolean,
    onResume: () -> Unit
) {
    val progress = if (isTimerEnabled) timeRemaining.toFloat() / totalTime.toFloat() else 1f
    val isLowTime = timeRemaining <= 5 && isTimerEnabled

    ElevatedCard(
        colors = CardDefaults.elevatedCardColors(
            containerColor = when {
                !isTimerEnabled -> MaterialTheme.colorScheme.surfaceContainer
                isPaused -> MaterialTheme.colorScheme.surfaceContainer
                isWaitingForNextPlayer -> MaterialTheme.colorScheme.tertiaryContainer
                isLowTime -> MaterialTheme.colorScheme.errorContainer
                timeRemaining <= 10 -> MaterialTheme.colorScheme.tertiaryContainer
                else -> MaterialTheme.colorScheme.primaryContainer
            }
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        when {
                            !isTimerEnabled -> "No Time Limit"
                            isPaused -> "Game Paused"
                            isWaitingForNextPlayer -> "Next Player..."
                            else -> "${currentPlayer?.name}'s Turn"
                        },
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    if (isTimerEnabled && !isWaitingForNextPlayer) {
                        Text(
                            "${timeRemaining}s",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                    } else if (!isTimerEnabled) {
                        Text(
                            "∞",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                    } else {
                        Text(
                            "Getting ready...",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Show current player avatar
                    if (currentPlayer != null && !isWaitingForNextPlayer) {
                        Surface(
                            modifier = Modifier.size(40.dp),
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.primary
                        ) {
                            Box(
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    currentPlayer.avatar.emoji,
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                    }

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = difficulty.color.copy(alpha = 0.2f)
                    ) {
                        Text(
                            difficulty.displayName,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            style = MaterialTheme.typography.labelLarge,
                            color = difficulty.color,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            if (isTimerEnabled && !isWaitingForNextPlayer) {
                Spacer(modifier = Modifier.height(12.dp))

                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = when {
                        isLowTime -> MaterialTheme.colorScheme.error
                        timeRemaining <= 10 -> MaterialTheme.colorScheme.tertiary
                        else -> MaterialTheme.colorScheme.primary
                    }
                )
            }

            if (isPaused && isTimerEnabled) {
                Spacer(modifier = Modifier.height(12.dp))
                FilledTonalButton(
                    onClick = onResume,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.PlayArrow, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Resume")
                }
            }
        }
    }
}

@Composable
fun EnhancedPlayerAnswerCard(
    player: Player,
    hasAnswered: Boolean,
    isFrozen: Boolean,
    isCurrentPlayer: Boolean,
    isWaitingForTurn: Boolean,
    onSubmitAnswer: (Double, PowerUpType?) -> Unit,
    onSubmitTextAnswer: (String, PowerUpType?) -> Unit,
    gameViewModel: GameViewModel
) {
    var answer by remember(hasAnswered) { mutableStateOf("") }
    var showPowerUps by remember { mutableStateOf(false) }
    var selectedPowerUp by remember { mutableStateOf<PowerUpType?>(null) }

    val focusManager = LocalFocusManager.current
    val currentQuestion = gameViewModel.gameState.value.currentQuestion
    val isGPUQuestion = currentQuestion?.category == GameCategory.GPU

    ElevatedCard(
        colors = CardDefaults.elevatedCardColors(
            containerColor = when {
                isFrozen -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
                hasAnswered -> MaterialTheme.colorScheme.primaryContainer
                isCurrentPlayer -> MaterialTheme.colorScheme.secondaryContainer
                else -> MaterialTheme.colorScheme.surface
            }
        ),
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Player header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        modifier = Modifier.size(40.dp),
                        shape = CircleShape,
                        color = when {
                            isCurrentPlayer && !hasAnswered -> MaterialTheme.colorScheme.primary
                            else -> MaterialTheme.colorScheme.primaryContainer
                        }
                    ) {
                        Box(
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                player.avatar.emoji,
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                player.name,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            if (isCurrentPlayer && !hasAnswered) {
                                Spacer(modifier = Modifier.width(8.dp))
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = MaterialTheme.colorScheme.primary
                                ) {
                                    Text(
                                        "YOUR TURN",
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onPrimary,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "Score: ${player.score}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                            if (player.currentStreak > 0) {
                                Spacer(modifier = Modifier.width(8.dp))
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = MaterialTheme.colorScheme.tertiaryContainer
                                ) {
                                    Text(
                                        "${player.currentStreak} streak",
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onTertiaryContainer
                                    )
                                }
                            }
                        }
                    }
                }

                // Power-ups button
                if (player.powerUps.isNotEmpty() && !hasAnswered && isCurrentPlayer) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Badge(
                            containerColor = MaterialTheme.colorScheme.primary
                        ) {
                            Text("${player.powerUps.size}")
                        }
                        IconButton(
                            onClick = { showPowerUps = !showPowerUps }
                        ) {
                            Icon(
                                if (showPowerUps) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                contentDescription = "Power-ups"
                            )
                        }
                    }
                }
            }

            // Content based on state
            if (isFrozen) {
                Spacer(modifier = Modifier.height(12.dp))
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.surfaceContainer
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.AcUnit,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "Frozen for 5 seconds",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            } else if (hasAnswered) {
                Spacer(modifier = Modifier.height(12.dp))
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "Answer submitted",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            } else if (isWaitingForTurn) {
                Spacer(modifier = Modifier.height(12.dp))
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.surfaceContainer
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.AccessTime,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "Waiting for your turn...",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else if (isCurrentPlayer) {
                // Answer input section for current player
                Spacer(modifier = Modifier.height(12.dp))

                // Power-ups selection
                AnimatedVisibility(
                    visible = showPowerUps,
                    enter = expandVertically() + fadeIn(),
                    exit = shrinkVertically() + fadeOut()
                ) {
                    PowerUpSelector(
                        powerUps = player.powerUps,
                        selectedPowerUp = selectedPowerUp,
                        onPowerUpSelected = { selectedPowerUp = it }
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = answer,
                        onValueChange = { answer = it },
                        label = {
                            Text(
                                if (isGPUQuestion) "GPU Name (e.g., 'RTX 4070' or '4070')"
                                else "Your Answer"
                            )
                        },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = if (isGPUQuestion) KeyboardType.Text else KeyboardType.Decimal,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                if (answer.isNotBlank()) {
                                    if (isGPUQuestion) {
                                        onSubmitTextAnswer(answer.trim(), selectedPowerUp)
                                    } else {
                                        answer.toDoubleOrNull()?.let { answerValue ->
                                            onSubmitAnswer(answerValue, selectedPowerUp)
                                        }
                                    }
                                    answer = ""
                                    selectedPowerUp = null
                                    showPowerUps = false
                                }
                                focusManager.clearFocus()
                            }
                        ),
                        suffix = {
                            if (!isGPUQuestion) {
                                currentQuestion?.unit?.let { unit ->
                                    if (unit.isNotEmpty()) {
                                        Text(
                                            unit,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                        }
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    FilledIconButton(
                        onClick = {
                            if (answer.isNotBlank()) {
                                if (isGPUQuestion) {
                                    onSubmitTextAnswer(answer.trim(), selectedPowerUp)
                                } else {
                                    answer.toDoubleOrNull()?.let { answerValue ->
                                        onSubmitAnswer(answerValue, selectedPowerUp)
                                    }
                                }
                                answer = ""
                                selectedPowerUp = null
                                showPowerUps = false
                            }
                        },
                        enabled = if (isGPUQuestion) answer.isNotBlank() else answer.toDoubleOrNull() != null
                    ) {
                        Icon(Icons.Default.Send, contentDescription = "Submit")
                    }
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
    Column(
        modifier = Modifier.padding(bottom = 12.dp)
    ) {
        Text(
            "Select Power-up:",
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
                        Text("${powerUp.type.displayName} (${powerUp.usesRemaining})")
                    },
                    selected = selectedPowerUp == powerUp.type,
                    leadingIcon = {
                        Text(powerUp.type.icon)
                    }
                )
            }
        }
    }
}

@Composable
fun AnswerVisualizationCard(visualizations: List<AnswerVisualization>) {
    ElevatedCard(
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                "Round Results",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))

            visualizations.forEachIndexed { index, vis ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (vis.isWinner) {
                            Icon(
                                Icons.Default.EmojiEvents,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        } else {
                            Text(
                                "${index + 1}",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                vis.playerName,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = if (vis.isWinner) FontWeight.Bold else FontWeight.Normal
                            )
                            Text(
                                "Answer: ${vis.answer}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (vis.isWinner)
                            MaterialTheme.colorScheme.primaryContainer
                        else MaterialTheme.colorScheme.surfaceContainer
                    ) {
                        Text(
                            "${vis.percentageError}% error",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelMedium,
                            color = if (vis.isWinner)
                                MaterialTheme.colorScheme.onPrimaryContainer
                            else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Divider()
            Spacer(modifier = Modifier.height(12.dp))

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Text(
                    "Correct Answer: ${visualizations.firstOrNull()?.correctAnswer ?: "N/A"}",
                    modifier = Modifier.padding(12.dp),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }
}

@Composable
fun AchievementNotification(
    achievement: AchievementType,
    modifier: Modifier = Modifier
) {
    var visible by remember { mutableStateOf(true) }

    LaunchedEffect(achievement) {
        delay(4000)
        visible = false
    }

    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(initialOffsetY = { -it }) + scaleIn() + fadeIn(),
        exit = slideOutVertically(targetOffsetY = { -it }) + scaleOut() + fadeOut(),
        modifier = modifier
    ) {
        ElevatedCard(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 12.dp),
            colors = CardDefaults.elevatedCardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Row(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primary
                ) {
                    Text(
                        achievement.icon,
                        modifier = Modifier.padding(12.dp),
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        "Achievement Unlocked!",
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
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
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
            CenterAlignedTopAppBar(
                title = { Text("Game Results") }
            )
        },
        bottomBar = {
            Surface(
                tonalElevation = 8.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
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
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item {
                // Winner celebration
                if (topPlayers.isNotEmpty()) {
                    WinnerCard(
                        winner = topPlayers.first(),
                        totalRounds = gameResults.size
                    )
                }
            }

            item {
                // Final standings
                FinalStandingsCard(
                    players = topPlayers,
                    gameViewModel = gameViewModel
                )
            }

            item {
                // Achievements section
                val allAchievements = topPlayers.flatMap { it.achievements }
                if (allAchievements.isNotEmpty()) {
                    AchievementsCard(achievements = allAchievements)
                }
            }
        }
    }
}

@Composable
fun WinnerCard(
    winner: Player,
    totalRounds: Int
) {
    ElevatedCard(
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 12.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                Icons.Default.EmojiEvents,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                winner.name,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Text(
                "Wins the Game!",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))

            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.primary
            ) {
                Text(
                    "${winner.score} of $totalRounds rounds won",
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.Bold
                )
            }

            if (winner.longestStreak > 1) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Best streak: ${winner.longestStreak}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.tertiary
                )
            }
        }
    }
}

@Composable
fun FinalStandingsCard(
    players: List<Player>,
    gameViewModel: GameViewModel
) {
    ElevatedCard {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                "Final Standings",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))

            players.forEachIndexed { index, player ->
                PlayerResultCard(
                    player = player,
                    position = index + 1,
                    accuracy = gameViewModel.calculateAccuracy(player.id)
                )
                if (index < players.size - 1) {
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

@Composable
fun PlayerResultCard(
    player: Player,
    position: Int,
    accuracy: Double
) {
    val positionIcon = when (position) {
        1 -> Icons.Default.Filter1
        2 -> Icons.Default.Filter2
        3 -> Icons.Default.Filter3
        else -> null
    }

    Surface(
        shape = RoundedCornerShape(12.dp),
        color = when (position) {
            1 -> MaterialTheme.colorScheme.primaryContainer
            2 -> MaterialTheme.colorScheme.secondaryContainer
            3 -> MaterialTheme.colorScheme.tertiaryContainer
            else -> MaterialTheme.colorScheme.surfaceContainer
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                positionIcon?.let { icon ->
                    Icon(
                        icon,
                        contentDescription = null,
                        modifier = Modifier.size(32.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                } ?: Text(
                    "$position",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    player.avatar.emoji,
                    style = MaterialTheme.typography.headlineSmall
                )

                Spacer(modifier = Modifier.width(12.dp))

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
                }
            }
        }
    }
}

@Composable
fun AchievementsCard(achievements: List<Achievement>) {
    ElevatedCard {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                "Achievements Unlocked",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))

            achievements.forEach { achievement ->
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.surfaceContainer
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            achievement.type.icon,
                            style = MaterialTheme.typography.headlineSmall
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                achievement.type.displayName,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                achievement.type.description,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
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
                title = { Text("How to Play") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                ElevatedCard {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Text(
                            "Basic Rules",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        val rules = listOf(
                            "Choose a game mode and category",
                            "Add at least 2 players with avatars",
                            "Answer trivia questions from your chosen category",
                            "The player with the closest answer wins the round",
                            "Players have 15-30 seconds depending on game mode",
                            "Player with the most round wins is the champion",
                            "Build streaks for bonus points",
                            "Use power-ups strategically in Power-Up mode"
                        )

                        rules.forEach { rule ->
                            Text(
                                "• $rule",
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                        }
                    }
                }
            }

            item {
                ElevatedCard {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Text(
                            "Game Modes",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        GameData.getAllGameModes().forEach { mode ->
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = MaterialTheme.colorScheme.surfaceContainer
                            ) {
                                Column(
                                    modifier = Modifier.padding(12.dp)
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
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }

            item {
                ElevatedCard {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Text(
                            "Power-ups",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        GameData.getAvailablePowerUps().forEach { powerUp ->
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = MaterialTheme.colorScheme.surfaceContainer
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        powerUp.icon,
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
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
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }
        }
    }
}