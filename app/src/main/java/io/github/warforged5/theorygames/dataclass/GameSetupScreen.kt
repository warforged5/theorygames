// Enhanced GameSetupScreen.kt
package io.github.warforged5.theorygames.dataclass

import androidx.compose.animation.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameSetupScreen(
    gameViewModel: GameViewModel,
    onNavigateToGame: () -> Unit,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val profileManager = remember { ProfileManager(context) }
    val scope = rememberCoroutineScope()

    val gameState by gameViewModel.gameState
    val currentCategory by gameViewModel.currentCategory
    val selectedGameMode by gameViewModel.selectedGameMode
    val isLoading by gameViewModel.isLoading

    val profileCollection by profileManager.profilesFlow.collectAsState(initial = ProfileCollection())

    var newPlayerName by remember { mutableStateOf("") }
    var selectedAvatar by remember { mutableStateOf(PlayerAvatar.SCIENTIST) }
    var showAvatarPicker by remember { mutableStateOf(false) }
    var showProfileSelector by remember { mutableStateOf(false) }
    var showQuickAddProfiles by remember { mutableStateOf(false) }

    val focusRequester = remember { androidx.compose.ui.focus.FocusRequester() }
    val focusManager = LocalFocusManager.current

    // Auto-populate with last selected profiles when screen loads
    LaunchedEffect(profileCollection) {
        if (gameState.players.isEmpty() && profileCollection.lastSelectedProfiles.isNotEmpty()) {
            val lastProfiles = profileCollection.profiles.filter {
                it.id in profileCollection.lastSelectedProfiles
            }
            lastProfiles.forEach { profile ->
                gameViewModel.addPlayer(profile.name, profile.avatar, profile.id)
            }
        }
    }

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
                        // Save current player selection as last selected
                        scope.launch {
                            profileManager.updateLastSelectedProfiles(
                                gameState.players.map { it.id }
                            )
                            gameViewModel.startGame()
                            onNavigateToGame()
                        }
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
                // Enhanced Player Management with Profile Support
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
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Badge {
                                    Text("${gameState.players.size}")
                                }

                                // Quick add profiles button
                                if (profileCollection.profiles.isNotEmpty()) {
                                    IconButton(
                                        onClick = { showQuickAddProfiles = true }
                                    ) {
                                        Icon(
                                            Icons.Default.GroupAdd,
                                            contentDescription = "Add saved profiles"
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Quick add saved profiles
                        if (profileCollection.profiles.isNotEmpty()) {
                            QuickAddProfilesSection(
                                profiles = profileCollection.profiles,
                                currentPlayers = gameState.players,
                                onAddProfile = { profile ->
                                    gameViewModel.addPlayer(profile.name, profile.avatar, profile.id)
                                }
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            HorizontalDivider()

                            Spacer(modifier = Modifier.height(16.dp))
                        }

                        // Manual player addition
                        OutlinedCard(
                            colors = CardDefaults.outlinedCardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceContainerLow
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text(
                                    "Add New Player",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
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
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Current players list
                        gameState.players.forEach { player ->
                            AnimatedVisibility(
                                visible = true,
                                enter = slideInVertically() + fadeIn(),
                                exit = slideOutVertically() + fadeOut()
                            ) {
                                EnhancedPlayerCard(
                                    player = player,
                                    onRemove = { gameViewModel.removePlayer(player.id) },
                                    showPowerUps = selectedGameMode == GameMode.POWERUP,
                                    isFromSavedProfile = profileCollection.profiles.any { it.id == player.id }
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
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
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
                                    avatar.names,
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

    // Quick add profiles dialog
    if (showQuickAddProfiles) {
        QuickAddProfilesDialog(
            profiles = profileCollection.profiles,
            currentPlayers = gameState.players,
            onAddProfile = { profile ->
                gameViewModel.addPlayer(profile.name, profile.avatar, profile.id)
            },
            onDismiss = { showQuickAddProfiles = false }
        )
    }
}

@Composable
fun QuickAddProfilesSection(
    profiles: List<SavedProfile>,
    currentPlayers: List<Player>,
    onAddProfile: (SavedProfile) -> Unit
) {
    val availableProfiles = profiles.filter { profile ->
        currentPlayers.none { it.id == profile.id }
    }.take(6) // Show max 6 for space

    if (availableProfiles.isNotEmpty()) {
        Column {
            Text(
                "Quick Add Profiles",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(availableProfiles) { profile ->
                    QuickAddProfileChip(
                        profile = profile,
                        onAdd = { onAddProfile(profile) }
                    )
                }

                if (profiles.size > currentPlayers.size + 6) {
                    item {
                        AssistChip(
                            onClick = { /* Open full dialog */ },
                            label = { Text("+${profiles.size - currentPlayers.size - 6} more") }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun QuickAddProfileChip(
    profile: SavedProfile,
    onAdd: () -> Unit
) {
    AssistChip(
        onClick = onAdd,
        label = { Text(profile.name) },
        leadingIcon = {
            Text(
                profile.avatar.emoji,
                style = MaterialTheme.typography.titleSmall
            )
        },
        trailingIcon = {
            Icon(
                Icons.Default.Add,
                contentDescription = "Add",
                modifier = Modifier.size(16.dp)
            )
        }
    )
}

@Composable
fun QuickAddProfilesDialog(
    profiles: List<SavedProfile>,
    currentPlayers: List<Player>,
    onAddProfile: (SavedProfile) -> Unit,
    onDismiss: () -> Unit
) {
    val availableProfiles = profiles.filter { profile ->
        currentPlayers.none { it.id == profile.id }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Saved Profiles") },
        text = {
            if (availableProfiles.isEmpty()) {
                Text("All saved profiles are already in the game.")
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(availableProfiles) { profile ->
                        QuickAddProfileItem(
                            profile = profile,
                            onAdd = {
                                onAddProfile(profile)
                                if (availableProfiles.size == 1) {
                                    onDismiss()
                                }
                            }
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Done")
            }
        }
    )
}

@Composable
fun QuickAddProfileItem(
    profile: SavedProfile,
    onAdd: () -> Unit
) {
    Surface(
        onClick = onAdd,
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceContainer
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(40.dp),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        profile.avatar.emoji,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    profile.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    "${profile.totalWins} wins • ${profile.totalGamesPlayed} games",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Icon(
                Icons.Default.Add,
                contentDescription = "Add",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun EnhancedPlayerCard(
    player: Player,
    onRemove: () -> Unit,
    showPowerUps: Boolean = false,
    isFromSavedProfile: Boolean = false
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
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            player.name,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        if (isFromSavedProfile) {
                            Spacer(modifier = Modifier.width(8.dp))
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = MaterialTheme.colorScheme.secondaryContainer
                            ) {
                                Text(
                                    "Saved",
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                            }
                        }
                    }

                    if (showPowerUps && player.powerUps.isNotEmpty()) {
                        Text(
                            "Power-ups: ${player.powerUps.size}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    if (isFromSavedProfile) {
                        Text(
                            "${player.totalWins} career wins",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.tertiary
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