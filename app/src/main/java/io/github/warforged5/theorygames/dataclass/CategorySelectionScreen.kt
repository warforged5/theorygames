// CategorySelectionScreen.kt
package io.github.warforged5.theorygames.dataclass

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategorySelectionScreen(
    userProfileManager: UserProfileManager,
    onCategorySelected: (GameCategory) -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateToProfile: () -> Unit
) {
    val currentProfile = remember { userProfileManager.getCurrentProfile() }
    val categories = remember { GameData.getAllCategories() }

    val infiniteTransition = rememberInfiniteTransition(label = "background")
    val animatedOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(10000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = "rotation"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Choose Your Challenge") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    // Profile button
                    currentProfile?.let { profile ->
                        IconButton(onClick = onNavigateToProfile) {
                            Surface(
                                modifier = Modifier.size(32.dp),
                                shape = RoundedCornerShape(8.dp),
                                color = MaterialTheme.colorScheme.primaryContainer
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        profile.preferredAvatar.emoji,
                                        fontSize = 16.sp
                                    )
                                }
                            }
                        }
                    } ?: IconButton(onClick = onNavigateToProfile) {
                        Icon(Icons.Default.Person, contentDescription = "Profile")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.sweepGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
                            MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.2f),
                            MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.3f),
                            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Welcome header
                currentProfile?.let { profile ->
                    WelcomeHeader(profile = profile)
                    Spacer(modifier = Modifier.height(32.dp))
                } ?: run {
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // Main title
                Text(
                    "What would you like to explore?",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Text(
                    "Choose a category to test your knowledge",
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 8.dp)
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Category grid
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(categories) { category ->
                        AnimatedCategoryCard(
                            category = category,
                            userStats = currentProfile?.categoryStats?.get(category),
                            isRecommended = currentProfile?.favoriteCategory == category,
                            onClick = { onCategorySelected(category) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Quick stats if user has played before
                currentProfile?.let { profile ->
                    if (profile.totalGamesPlayed > 0) {
                        QuickProgressIndicator(profile = profile)
                    }
                }
            }
        }
    }
}

@Composable
private fun WelcomeHeader(profile: UserProfile) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f)
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(48.dp),
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.primary
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        profile.preferredAvatar.emoji,
                        fontSize = 24.sp
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    "Welcome back, ${profile.name}!",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    "${profile.getLevelTitle()} • Level ${profile.getLevel()}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
private fun AnimatedCategoryCard(
    category: GameCategory,
    userStats: CategoryStats?,
    isRecommended: Boolean,
    onClick: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(dampingRatio = 0.8f),
        label = "scale"
    )

    Card(
        modifier = Modifier
            .aspectRatio(1f)
            .scale(scale)
            .clickable {
                isPressed = true
                onClick()
            },
        colors = CardDefaults.cardColors(
            containerColor = if (isRecommended)
                MaterialTheme.colorScheme.primaryContainer
            else MaterialTheme.colorScheme.surfaceContainerHigh
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isRecommended) 8.dp else 4.dp
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            // Recommended badge
            if (isRecommended) {
                Surface(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.primary
                ) {
                    Text(
                        "★",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontSize = 12.sp
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Category icon
                Text(
                    category.icon,
                    fontSize = 48.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // Category name
                Text(
                    category.displayName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = if (isRecommended)
                        MaterialTheme.colorScheme.onPrimaryContainer
                    else MaterialTheme.colorScheme.onSurface,
                    lineHeight = 18.sp
                )

                // User stats if available
                userStats?.let { stats ->
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "${stats.gamesPlayed} games",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        "${stats.getWinRate().toInt()}% win rate",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }

    LaunchedEffect(isPressed) {
        if (isPressed) {
            kotlinx.coroutines.delay(100)
            isPressed = false
        }
    }
}

@Composable
private fun QuickProgressIndicator(profile: UserProfile) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            QuickStat(
                icon = "🎮",
                value = "${profile.totalGamesPlayed}",
                label = "Games"
            )
            QuickStat(
                icon = "🏆",
                value = "${profile.getWinRate().toInt()}%",
                label = "Win Rate"
            )
            QuickStat(
                icon = "🔥",
                value = "${profile.bestStreak}",
                label = "Best Streak"
            )
            QuickStat(
                icon = "⭐",
                value = "Lv.${profile.getLevel()}",
                label = "Level"
            )
        }
    }
}

@Composable
private fun QuickStat(
    icon: String,
    value: String,
    label: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(icon, fontSize = 20.sp)
        Text(
            value,
            style = MaterialTheme.typography.titleMedium,
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

// PlayerImportSetupScreen.kt
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerImportSetupScreen(
    gameViewModel: GameViewModel,
    userProfileManager: UserProfileManager,
    selectedCategory: GameCategory,
    onNavigateToGame: () -> Unit,
    onNavigateBack: () -> Unit
) {
    val gameState by gameViewModel.gameState
    val selectedGameMode by gameViewModel.selectedGameMode
    val isLoading by gameViewModel.isLoading
    val allProfiles = remember { userProfileManager.getAllProfiles() }
    val currentProfile = remember { userProfileManager.getCurrentProfile() }

    // Set the selected category
    LaunchedEffect(selectedCategory) {
        gameViewModel.selectCategory(selectedCategory)
    }

    var showTempPlayerDialog by remember { mutableStateOf(false) }
    var tempPlayerName by remember { mutableStateOf("") }
    var tempPlayerAvatar by remember { mutableStateOf(PlayerAvatar.SCIENTIST) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Game Setup")
                        Text(
                            selectedCategory.displayName,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Selected category display
            CategoryDisplayCard(category = selectedCategory)

            // Import saved profiles section
            if (allProfiles.isNotEmpty()) {
                SavedProfilesSection(
                    profiles = allProfiles,
                    currentProfile = currentProfile,
                    selectedPlayers = gameState.players,
                    onProfileToggle = { profile, isSelected ->
                        if (isSelected) {
                            gameViewModel.addPlayer(profile.name, profile.preferredAvatar)
                        } else {
                            val player = gameState.players.find { it.name == profile.name }
                            player?.let { gameViewModel.removePlayer(it.id) }
                        }
                    }
                )
            }

            // Add temporary players section
            TempPlayersSection(
                onAddTempPlayer = { showTempPlayerDialog = true },
                tempPlayers = gameState.players.filter { player ->
                    allProfiles.none { profile -> profile.name == player.name }
                },
                onRemoveTempPlayer = { playerId ->
                    gameViewModel.removePlayer(playerId)
                }
            )

            // Game settings
            GameSettingsSection(
                gameViewModel = gameViewModel,
                selectedGameMode = selectedGameMode
            )

            // Player count indicator
            PlayerCountIndicator(playerCount = gameState.players.size)

            Spacer(modifier = Modifier.height(80.dp)) // Space for FAB
        }
    }

    // Temp player dialog
    if (showTempPlayerDialog) {
        TempPlayerDialog(
            playerName = tempPlayerName,
            selectedAvatar = tempPlayerAvatar,
            onPlayerNameChange = { tempPlayerName = it },
            onAvatarChange = { tempPlayerAvatar = it },
            onConfirm = {
                if (tempPlayerName.isNotBlank()) {
                    gameViewModel.addPlayer(tempPlayerName.trim(), tempPlayerAvatar)
                    tempPlayerName = ""
                    tempPlayerAvatar = PlayerAvatar.SCIENTIST
                }
                showTempPlayerDialog = false
            },
            onDismiss = { showTempPlayerDialog = false }
        )
    }
}

@Composable
private fun CategoryDisplayCard(category: GameCategory) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(64.dp),
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.primary
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        category.icon,
                        fontSize = 32.sp
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    "Playing: ${category.displayName}",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    "Test your knowledge in this category",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                )
            }
        }
    }
}

@Composable
private fun SavedProfilesSection(
    profiles: List<UserProfile>,
    currentProfile: UserProfile?,
    selectedPlayers: List<Player>,
    onProfileToggle: (UserProfile, Boolean) -> Unit
) {
    Card {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                "Import Saved Profiles",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                "Select players from your saved profiles",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            profiles.forEach { profile ->
                val isSelected = selectedPlayers.any { it.name == profile.name }
                val isCurrentUser = profile.id == currentProfile?.id

                ProfileImportCard(
                    profile = profile,
                    isSelected = isSelected,
                    isCurrentUser = isCurrentUser,
                    onToggle = { onProfileToggle(profile, !isSelected) }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun ProfileImportCard(
    profile: UserProfile,
    isSelected: Boolean,
    isCurrentUser: Boolean,
    onToggle: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onToggle() },
        colors = CardDefaults.cardColors(
            containerColor = when {
                isSelected -> MaterialTheme.colorScheme.primaryContainer
                isCurrentUser -> MaterialTheme.colorScheme.secondaryContainer
                else -> MaterialTheme.colorScheme.surfaceContainer
            }
        ),
        border = if (isSelected) {
            androidx.compose.foundation.BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
        } else null
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar
            Surface(
                modifier = Modifier.size(48.dp),
                shape = RoundedCornerShape(12.dp),
                color = if (isSelected) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.surfaceVariant
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        profile.preferredAvatar.emoji,
                        fontSize = 24.sp
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Profile info
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        profile.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    if (isCurrentUser) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.primary
                        ) {
                            Text(
                                "You",
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
                Text(
                    "Level ${profile.getLevel()} • ${profile.totalGamesPlayed} games • ${profile.getWinRate().toInt()}% win rate",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Selection indicator
            Checkbox(
                checked = isSelected,
                onCheckedChange = { onToggle() }
            )
        }
    }
}

@Composable
private fun TempPlayersSection(
    onAddTempPlayer: () -> Unit,
    tempPlayers: List<Player>,
    onRemoveTempPlayer: (String) -> Unit
) {
    Card {
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
                        "Temporary Players",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }

                FilledTonalButton(
                    onClick = onAddTempPlayer,
                    modifier = Modifier.height(48.dp)
                ) {
                    Icon(Icons.Default.PersonAdd, contentDescription = null)
                }
            }

            if (tempPlayers.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                tempPlayers.forEach { player ->
                    TempPlayerCard(
                        player = player,
                        onRemove = { onRemoveTempPlayer(player.id) }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
private fun TempPlayerCard(
    player: Player,
    onRemove: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(40.dp),
                shape = RoundedCornerShape(10.dp),
                color = MaterialTheme.colorScheme.surface
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        player.avatar.emoji,
                        fontSize = 20.sp
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    player.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "Temporary player",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            IconButton(
                onClick = onRemove,
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                )
            ) {
                Icon(Icons.Default.Delete, contentDescription = "Remove")
            }
        }
    }
}

@Composable
private fun GameSettingsSection(
    gameViewModel: GameViewModel,
    selectedGameMode: GameMode
) {
    Card {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                "Game Settings",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Game mode selection
            Text(
                "Game Mode",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(8.dp))

            GameData.getAllGameModes().forEach { mode ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { gameViewModel.selectGameMode(mode) }
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = selectedGameMode == mode,
                        onClick = { gameViewModel.selectGameMode(mode) }
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            mode.displayName,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            mode.description,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PlayerCountIndicator(playerCount: Int) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = if (playerCount >= 2)
                MaterialTheme.colorScheme.primaryContainer
            else MaterialTheme.colorScheme.errorContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                if (playerCount >= 2) Icons.Default.Group else Icons.Default.Warning,
                contentDescription = null,
                tint = if (playerCount >= 2)
                    MaterialTheme.colorScheme.onPrimaryContainer
                else MaterialTheme.colorScheme.onErrorContainer
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                if (playerCount >= 2)
                    "$playerCount players ready to play!"
                else "Add at least 2 players to start",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = if (playerCount >= 2)
                    MaterialTheme.colorScheme.onPrimaryContainer
                else MaterialTheme.colorScheme.onErrorContainer
            )
        }
    }
}

@Composable
private fun TempPlayerDialog(
    playerName: String,
    selectedAvatar: PlayerAvatar,
    onPlayerNameChange: (String) -> Unit,
    onAvatarChange: (PlayerAvatar) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    androidx.compose.ui.window.Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Add Temporary Player",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = playerName,
                    onValueChange = onPlayerNameChange,
                    label = { Text("Player Name") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text("Choose Avatar", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))

                val arrayAvatars = PlayerAvatar.values()

                androidx.compose.foundation.lazy.LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(PlayerAvatar.values().size) { avatar ->
                        Surface(
                            modifier = Modifier
                                .size(48.dp)
                                .clickable { onAvatarChange(arrayAvatars.get(avatar)) },
                            shape = RoundedCornerShape(12.dp),
                            color = if (selectedAvatar == arrayAvatars.get(avatar))
                                MaterialTheme.colorScheme.primaryContainer
                            else MaterialTheme.colorScheme.surfaceVariant,
                            border = if (selectedAvatar == arrayAvatars.get(avatar))
                                androidx.compose.foundation.BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
                            else null
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(arrayAvatars.get(avatar).emoji, fontSize = 24.sp)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cancel")
                    }

                    Button(
                        onClick = onConfirm,
                        enabled = playerName.isNotBlank(),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Add Player")
                    }
                }
            }
        }
    }
}

fun processAvatar(avatar: PlayerAvatar) {
    val ordinalValue = avatar.ordinal // Use ordinal to get the integer value of the enum
}