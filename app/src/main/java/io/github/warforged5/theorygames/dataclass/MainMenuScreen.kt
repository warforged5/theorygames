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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import androidx.compose.foundation.border
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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
    onNavigateToRules: () -> Unit,
    onNavigateToProfiles: () -> Unit,
    onNavigateToSettings: () -> Unit
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
                actions = {
                    IconButton(onClick = onNavigateToSettings) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
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

                // Hero section (same as before)
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

                // Enhanced feature highlights
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
                        icon = Icons.Outlined.Person,
                        title = "Profiles",
                        description = "Save your progress"
                    )
                    FeatureCard(
                        icon = Icons.Outlined.Palette,
                        title = "Themes",
                        description = "Customize appearance"
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                // Enhanced action buttons
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

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onNavigateToProfiles,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Outlined.Person, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Profiles")
                    }

                    OutlinedButton(
                        onClick = onNavigateToRules,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Outlined.Help, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Rules")
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}
@Composable
private fun StatItem(
    value: String,
    label: String,
    icon: ImageVector
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            value,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun CategoryPreviewCard(category: GameCategory) {
    Card(
        modifier = Modifier
            .width(140.dp)
            .height(120.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
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
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun FeatureCard(
    icon: ImageVector,
    title: String,
    description: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0.7f)
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(48.dp),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        icon,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
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
                            AnswerVisualizationCard(
                                visualizations = gameViewModel.getAnswerVisualization(),
                                gameViewModel = gameViewModel
                            )
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
fun WelcomeBanner(
    currentProfile: UserProfile?,
    onCreateProfile: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        if (currentProfile != null) {
            // Returning user welcome
            Row(
                modifier = Modifier.padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    modifier = Modifier.size(64.dp),
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primary
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            currentProfile.preferredAvatar.emoji,
                            fontSize = 28.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        "Welcome back, ${currentProfile.name}!",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        "${currentProfile.getLevelTitle()} • Level ${currentProfile.getLevel()}",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    if (currentProfile.totalGamesPlayed > 0) {
                        Text(
                            "${currentProfile.totalWins}/${currentProfile.totalGamesPlayed} games won (${currentProfile.getWinRate().toInt()}%)",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                        )
                    }
                }
            }
        } else {
            // New user welcome
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    TheoryGamesIcons.Waving,
                    contentDescription = null,
                    modifier = Modifier.size(48.dp),
                    tint = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    "Welcome to TheoryGames!",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )

                Text(
                    "Create a profile to track your progress and compete with friends!",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onCreateProfile,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Icon(Icons.Default.PersonAdd, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Create Profile")
                }
            }
        }
    }
}
@Composable
fun AppIconDisplay(
    modifier: Modifier = Modifier,
    size: androidx.compose.ui.unit.Dp = 120.dp
) {
    Card(
        modifier = modifier.size(size),
        shape = CircleShape,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Icon(
                Icons.Default.Psychology,
                contentDescription = "TheoryGames Logo",
                modifier = Modifier.size(size * 0.5f),
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Composable
fun RecentActivityCard(
    gameHistory: List<GameHistoryEntry>,
    onViewMore: () -> Unit
) {
    if (gameHistory.isEmpty()) return

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Recent Activity",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                TextButton(onClick = onViewMore) {
                    Text("View All")
                    Icon(
                        Icons.Default.ChevronRight,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            gameHistory.take(3).forEach { entry ->
                RecentActivityItem(entry = entry)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun RecentActivityItem(entry: GameHistoryEntry) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.surfaceContainer
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                entry.category.icon,
                fontSize = 20.sp
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    entry.category.displayName,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "${entry.gameMode.displayName} • ${entry.playerCount} players",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Surface(
                shape = RoundedCornerShape(12.dp),
                color = when (entry.finalPosition) {
                    1 -> MaterialTheme.colorScheme.primaryContainer
                    2 -> MaterialTheme.colorScheme.secondaryContainer
                    3 -> MaterialTheme.colorScheme.tertiaryContainer
                    else -> MaterialTheme.colorScheme.surfaceVariant
                }
            ) {
                Text(
                    "#${entry.finalPosition}",
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold
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
    var gpuSearchText by remember(hasAnswered) { mutableStateOf("") }
    var selectedGPU by remember(hasAnswered) { mutableStateOf<GPUPerformanceData?>(null) }
    var showPowerUps by remember { mutableStateOf(false) }
    var selectedPowerUp by remember { mutableStateOf<PowerUpType?>(null) }

    val focusManager = LocalFocusManager.current
    val currentQuestion = gameViewModel.gameState.value.currentQuestion
    val isGPUQuestion = currentQuestion?.category == GameCategory.GPU

    Box {
        // Turn indicator border
        TurnIndicator(
            isCurrentPlayer = isCurrentPlayer,
            hasAnswered = hasAnswered,
            isWaitingForTurn = isWaitingForTurn
        )

        ElevatedCard(
            // Remove the color-based turn indication, use consistent card color
            colors = CardDefaults.elevatedCardColors(
                containerColor = when {
                    isFrozen -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
                    hasAnswered -> MaterialTheme.colorScheme.surfaceContainerHigh
                    else -> MaterialTheme.colorScheme.surface
                }
            ),
            modifier = Modifier.padding(2.dp) // Small padding for the border effect
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                // Player header with new status badge
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Player avatar with enhanced styling for current player
                        Surface(
                            modifier = Modifier.size(40.dp),
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.primaryContainer,
                            border = if (isCurrentPlayer && !hasAnswered) {
                                BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
                            } else null
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
                                Spacer(modifier = Modifier.width(8.dp))
                                // New status badge
                                PlayerStatusBadge(
                                    isCurrentPlayer = isCurrentPlayer && !hasAnswered,
                                    hasAnswered = hasAnswered,
                                    isWaitingForTurn = isWaitingForTurn,
                                    isFrozen = isFrozen
                                )
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

                // Content based on state - same as before but with cleaner styling
                if (isFrozen) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.errorContainer
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
                                tint = MaterialTheme.colorScheme.onErrorContainer
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "Frozen for 5 seconds",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onErrorContainer
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
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
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

                    if (isGPUQuestion) {
                        // GPU Search Component
                        Column {
                            GPUSearchBox(
                                searchText = gpuSearchText,
                                onSearchTextChanged = { gpuSearchText = it },
                                onGPUSelected = { gpu ->
                                    selectedGPU = gpu
                                    gpuSearchText = ""
                                },
                                onClearSelection = {
                                    selectedGPU = null
                                    gpuSearchText = ""
                                },
                                selectedGPU = selectedGPU,
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            // Submit button for GPU
                            FilledTonalButton(
                                onClick = {
                                    selectedGPU?.let { gpu ->
                                        onSubmitTextAnswer(gpu.fullName, selectedPowerUp)
                                        selectedGPU = null
                                        gpuSearchText = ""
                                        selectedPowerUp = null
                                        showPowerUps = false
                                    }
                                },
                                enabled = selectedGPU != null,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(Icons.Default.Send, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (selectedGPU != null)
                                        "Submit ${selectedGPU!!.fullName}"
                                    else "Select a GPU to submit"
                                )
                            }
                        }
                    } else {
                        // Regular numeric input
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedTextField(
                                value = answer,
                                onValueChange = { answer = it },
                                label = { Text("Your Answer") },
                                modifier = Modifier.weight(1f),
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Decimal,
                                    imeAction = ImeAction.Done
                                ),
                                keyboardActions = KeyboardActions(
                                    onDone = {
                                        answer.toDoubleOrNull()?.let { answerValue ->
                                            onSubmitAnswer(answerValue, selectedPowerUp)
                                            answer = ""
                                            selectedPowerUp = null
                                            showPowerUps = false
                                        }
                                        focusManager.clearFocus()
                                    }
                                ),
                                suffix = {
                                    currentQuestion?.unit?.let { unit ->
                                        if (unit.isNotEmpty()) {
                                            Text(
                                                unit,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    }
                                }
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            FilledIconButton(
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
                                Icon(Icons.Default.Send, contentDescription = "Submit")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TurnIndicator(
    isCurrentPlayer: Boolean,
    hasAnswered: Boolean,
    isWaitingForTurn: Boolean,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "turn_indicator")

    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        ), label = "pulse"
    )

    val borderColor by animateColorAsState(
        targetValue = when {
            hasAnswered -> MaterialTheme.colorScheme.primary
            isCurrentPlayer -> MaterialTheme.colorScheme.secondary
            isWaitingForTurn -> MaterialTheme.colorScheme.outline
            else -> Color.Transparent
        },
        animationSpec = tween(300), label = "border_color"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = if (isCurrentPlayer && !hasAnswered) 3.dp else 1.dp,
                color = if (isCurrentPlayer && !hasAnswered) {
                    borderColor.copy(alpha = pulseAlpha)
                } else {
                    borderColor
                },
                shape = RoundedCornerShape(12.dp)
            )
    )
}

@Composable
fun PlayerStatusBadge(
    isCurrentPlayer: Boolean,
    hasAnswered: Boolean,
    isWaitingForTurn: Boolean,
    isFrozen: Boolean
) {
    AnimatedVisibility(
        visible = isCurrentPlayer || hasAnswered || isWaitingForTurn || isFrozen,
        enter = scaleIn() + fadeIn(),
        exit = scaleOut() + fadeOut()
    ) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = when {
                isFrozen -> MaterialTheme.colorScheme.errorContainer
                hasAnswered -> MaterialTheme.colorScheme.primaryContainer
                isCurrentPlayer -> MaterialTheme.colorScheme.secondary
                isWaitingForTurn -> MaterialTheme.colorScheme.surfaceContainer
                else -> Color.Transparent
            }
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = when {
                        isFrozen -> Icons.Default.AcUnit
                        hasAnswered -> Icons.Default.CheckCircle
                        isCurrentPlayer -> Icons.Default.PlayArrow
                        isWaitingForTurn -> Icons.Default.AccessTime
                        else -> Icons.Default.Circle
                    },
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = when {
                        isFrozen -> MaterialTheme.colorScheme.onErrorContainer
                        hasAnswered -> MaterialTheme.colorScheme.onPrimaryContainer
                        isCurrentPlayer -> MaterialTheme.colorScheme.onSecondary
                        isWaitingForTurn -> MaterialTheme.colorScheme.onSurfaceVariant
                        else -> Color.Transparent
                    }
                )

                Spacer(modifier = Modifier.width(6.dp))

                Text(
                    text = when {
                        isFrozen -> "FROZEN"
                        hasAnswered -> "ANSWERED"
                        isCurrentPlayer -> "YOUR TURN"
                        isWaitingForTurn -> "WAITING"
                        else -> ""
                    },
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = when {
                        isFrozen -> MaterialTheme.colorScheme.onErrorContainer
                        hasAnswered -> MaterialTheme.colorScheme.onPrimaryContainer
                        isCurrentPlayer -> MaterialTheme.colorScheme.onSecondary
                        isWaitingForTurn -> MaterialTheme.colorScheme.onSurfaceVariant
                        else -> Color.Transparent
                    }
                )
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
fun AnswerVisualizationCard(
    visualizations: List<AnswerVisualization>,
    gameViewModel: GameViewModel
) {
    val currentQuestion = gameViewModel.gameState.value.currentQuestion
    val isGPUQuestion = currentQuestion?.category == GameCategory.GPU

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

            if (isGPUQuestion) {
                // Enhanced GPU visualization
                GPUAnswerResults(
                    gameViewModel = gameViewModel,
                    currentQuestion = currentQuestion
                )
            } else {
                // Regular numeric visualization
                RegularAnswerResults(visualizations = visualizations)
            }
        }
    }
}

@Composable
private fun RegularAnswerResults(visualizations: List<AnswerVisualization>) {
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
    HorizontalDivider()
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

@Composable
private fun GPUAnswerResults(
    gameViewModel: GameViewModel,
    currentQuestion: GameQuestion?
) {
    val gameState = gameViewModel.gameState.value
    val chartData = currentQuestion?.let { GameData.getGPUChartData(it.id) }
    val actualGpu = chartData?.mysteryGpu

    // Get the winner from the latest game result
    val latestResult = gameViewModel.gameResults.lastOrNull()
    val winnerPlayerId = latestResult?.winner?.id

    if (actualGpu == null || chartData == null) {
        Text(
            "Unable to load GPU results",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.error
        )
        return
    }

    // Process and sort GPU guesses by performance accuracy
    val gpuGuesses = gameState.playerAnswers.mapNotNull { answer ->
        val player = gameState.players.find { it.id == answer.playerId }
        if (player == null) return@mapNotNull null

        val guessedGpuName = if (answer.textAnswer.isNotEmpty()) {
            answer.textAnswer
        } else {
            answer.answer.toString()
        }

        val isExact = GameData.isExactGPUMatch(guessedGpuName, actualGpu)
        val performanceDistance = if (isExact) {
            0.0
        } else {
            val guessedGpu = GameData.findGPUByName(guessedGpuName)
            guessedGpu?.let {
                GameData.calculateGPUPerformanceDistance(it, actualGpu, chartData.games)
            } ?: Double.MAX_VALUE
        }

        GPUResultData(
            playerName = player.name,
            guessedGpuName = guessedGpuName,
            isExactMatch = isExact,
            performanceDistance = performanceDistance,
            isWinner = answer.playerId == winnerPlayerId,
            timeTaken = answer.timeTaken
        )
    }.sortedBy { it.performanceDistance }

    // Display results
    gpuGuesses.forEachIndexed { index, result ->
        GPUResultRow(
            position = index + 1,
            result = result
        )
        if (index < gpuGuesses.size - 1) {
            Spacer(modifier = Modifier.height(8.dp))
        }
    }

    Spacer(modifier = Modifier.height(12.dp))
    HorizontalDivider()
    Spacer(modifier = Modifier.height(12.dp))

    // Correct answer section with enhanced details
    CorrectGPUAnswer(
        actualGpu = actualGpu,
        chartData = chartData
    )
}

@Composable
private fun GPUResultRow(
    position: Int,
    result: GPUResultData
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            // Position indicator
            if (result.isWinner) {
                Icon(
                    Icons.Default.EmojiEvents,
                    contentDescription = "Winner",
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            } else {
                Surface(
                    modifier = Modifier.size(20.dp),
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.surfaceContainer
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            "$position",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    result.playerName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = if (result.isWinner) FontWeight.Bold else FontWeight.Normal
                )
                Text(
                    "Guess: ${result.guessedGpuName}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (result.timeTaken > 0) {
                    Text(
                        "Time: ${String.format("%.1f", result.timeTaken)}s",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Accuracy indicator
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = when {
                result.isExactMatch -> MaterialTheme.colorScheme.primaryContainer
                result.performanceDistance == Double.MAX_VALUE -> MaterialTheme.colorScheme.errorContainer
                result.performanceDistance < 20.0 -> MaterialTheme.colorScheme.secondaryContainer
                else -> MaterialTheme.colorScheme.surfaceContainer
            }
        ) {
            Text(
                when {
                    result.isExactMatch -> "✓ Perfect"
                    result.performanceDistance == Double.MAX_VALUE -> "Invalid"
                    else -> "${result.performanceDistance.toInt()} FPS off"
                },
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Medium,
                color = when {
                    result.isExactMatch -> MaterialTheme.colorScheme.onPrimaryContainer
                    result.performanceDistance == Double.MAX_VALUE -> MaterialTheme.colorScheme.onErrorContainer
                    result.performanceDistance < 20.0 -> MaterialTheme.colorScheme.onSecondaryContainer
                    else -> MaterialTheme.colorScheme.onSurfaceVariant
                }
            )
        }
    }
}

@Composable
private fun CorrectGPUAnswer(
    actualGpu: GPUPerformanceData,
    chartData: GPUChartData
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.primaryContainer
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
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
                    "Correct Answer",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                actualGpu.fullName,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            Text(
                "Average Performance: ${actualGpu.getAveragePerformance().toInt()} FPS",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Show specific game performances
            Column {
                chartData.games.forEach { game ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            game,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                        )
                        Text(
                            "${actualGpu.getPerformance(game).toInt()} FPS",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                        )
                    }
                }
            }
        }
    }
}

// Data class for cleaner GPU result handling
private data class GPUResultData(
    val playerName: String,
    val guessedGpuName: String,
    val isExactMatch: Boolean,
    val performanceDistance: Double,
    val isWinner: Boolean,
    val timeTaken: Double
)

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
    profileManager: ProfileManager,
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
                if (topPlayers.isNotEmpty()) {
                    WinnerCard(
                        winner = topPlayers.first(),
                        totalRounds = gameResults.size
                    )
                }
            }

            item {
                FinalStandingsCard(
                    players = topPlayers,
                    gameViewModel = gameViewModel
                )
            }

            item {
                val allAchievements = topPlayers.flatMap { it.achievements }
                if (allAchievements.isNotEmpty()) {
                    AchievementsCard(achievements = allAchievements)
                }
            }
        }
    }
}

// Helper function to update profile with game results
private fun updateProfileWithGameResults(
    profile: UserProfile,
    player: Player,
    finalPosition: Int,
    category: GameCategory,
    gameMode: GameMode,
    totalRounds: Int,
    accuracy: Double
): UserProfile {
    val won = finalPosition == 1
    val newTotalGames = profile.totalGamesPlayed + 1
    val newTotalWins = if (won) profile.totalWins + 1 else profile.totalWins
    val newBestStreak = maxOf(profile.bestStreak, player.longestStreak)

    // Update category stats
    val currentCategoryStats = profile.categoryStats[category] ?: CategoryStats()
    val newCategoryStats = currentCategoryStats.copy(
        gamesPlayed = currentCategoryStats.gamesPlayed + 1,
        wins = if (won) currentCategoryStats.wins + 1 else currentCategoryStats.wins,
        bestStreak = maxOf(currentCategoryStats.bestStreak, player.longestStreak),
        averageAccuracy = if (currentCategoryStats.gamesPlayed > 0) {
            ((currentCategoryStats.averageAccuracy * currentCategoryStats.gamesPlayed) + accuracy) / (currentCategoryStats.gamesPlayed + 1)
        } else accuracy,
        lastPlayed = System.currentTimeMillis()
    )

    val updatedCategoryStats = profile.categoryStats.toMutableMap()
    updatedCategoryStats[category] = newCategoryStats

    return profile.copy(
        totalGamesPlayed = newTotalGames,
        totalWins = newTotalWins,
        bestStreak = newBestStreak,
        categoryStats = updatedCategoryStats,
        lastPlayedAt = System.currentTimeMillis(),
        achievements = profile.achievements + player.achievements, // Merge new achievements
        preferredGameMode = gameMode // Update preference based on recent play
    )
}

// New component for showing profile progress
@Composable
private fun ProfileProgressCard(
    profile: UserProfile,
    player: Player,
    finalPosition: Int
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                "Your Progress",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        "Final Position",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.8f)
                    )
                    Text(
                        "#$finalPosition",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }

                Column {
                    Text(
                        "Rounds Won",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.8f)
                    )
                    Text(
                        "${player.score}",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }

                Column {
                    Text(
                        "New Level",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.8f)
                    )
                    Text(
                        "${profile.getLevel()}",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            if (player.achievements.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    "🎉 Unlocked ${player.achievements.size} new achievement${if (player.achievements.size > 1) "s" else ""}!",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                )
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

object TheoryGamesIcons {
    val Psychology = Icons.Default.Psychology
    val Waving = Icons.Default.WavingHand
    val TrendingUp = Icons.Default.TrendingUp
    val BarChart = Icons.Default.BarChart
    val DonutLarge = Icons.Default.DonutLarge
    val Category = Icons.Default.Category
    val Vibration = Icons.Default.Vibration
    val ChevronRight = Icons.Default.ChevronRight
    val SportsEsports = Icons.Default.SportsEsports
    val WavingHand = Icons.Default.WavingHand
}