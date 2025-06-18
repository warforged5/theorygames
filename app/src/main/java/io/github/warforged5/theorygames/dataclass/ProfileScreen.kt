// ProfileScreen.kt
package io.github.warforged5.theorygames.dataclass

import androidx.compose.animation.*
import androidx.compose.foundation.background
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    userProfileManager: UserProfileManager,
    onNavigateBack: () -> Unit
) {
    var currentProfile by remember { mutableStateOf(userProfileManager.getCurrentProfile()) }
    var showCreateProfile by remember { mutableStateOf(currentProfile == null) }
    var showProfileSelector by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableStateOf(0) }

    val gameHistory = remember { userProfileManager.getGameHistory() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (currentProfile != null) "Profile" else "Create Profile"
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (currentProfile != null) {
                        IconButton(onClick = { showProfileSelector = true }) {
                            Icon(Icons.Default.SwitchAccount, contentDescription = "Switch Profile")
                        }
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
            if (showCreateProfile) {
                CreateProfileDialog(
                    userProfileManager = userProfileManager,
                    onProfileCreated = { profile ->
                        currentProfile = profile
                        showCreateProfile = false
                    },
                    onDismiss = {
                        if (currentProfile == null) {
                            onNavigateBack()
                        } else {
                            showCreateProfile = false
                        }
                    }
                )
            }

            currentProfile?.let { profile ->
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    // Tab Row
                    TabRow(
                        selectedTabIndex = selectedTab,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Tab(
                            selected = selectedTab == 0,
                            onClick = { selectedTab = 0 },
                            text = { Text("Overview") },
                            icon = { Icon(Icons.Default.Person, contentDescription = null) }
                        )
                        Tab(
                            selected = selectedTab == 1,
                            onClick = { selectedTab = 1 },
                            text = { Text("Statistics") },
                            icon = { Icon(Icons.Default.BarChart, contentDescription = null) }
                        )
                        Tab(
                            selected = selectedTab == 2,
                            onClick = { selectedTab = 2 },
                            text = { Text("History") },
                            icon = { Icon(Icons.Default.History, contentDescription = null) }
                        )
                    }

                    // Content based on selected tab
                    when (selectedTab) {
                        0 -> ProfileOverview(
                            profile = profile,
                            userProfileManager = userProfileManager,
                            onProfileUpdated = { updatedProfile ->
                                currentProfile = updatedProfile
                            }
                        )
                        1 -> StatisticsTab(profile = profile)
                        2 -> HistoryTab(gameHistory = gameHistory)
                    }
                }
            }

            if (showProfileSelector) {
                ProfileSelectorDialog(
                    userProfileManager = userProfileManager,
                    currentProfile = currentProfile,
                    onProfileSelected = { profile ->
                        currentProfile = profile
                        showProfileSelector = false
                    },
                    onCreateNew = {
                        showCreateProfile = true
                        showProfileSelector = false
                    },
                    onDismiss = { showProfileSelector = false }
                )
            }
        }
    }
}

@Composable
private fun CreateProfileDialog(
    userProfileManager: UserProfileManager,
    onProfileCreated: (UserProfile) -> Unit,
    onDismiss: () -> Unit
) {
    var playerName by remember { mutableStateOf("") }
    var selectedAvatar by remember { mutableStateOf(PlayerAvatar.SCIENTIST) }
    val focusManager = LocalFocusManager.current

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Create Your Profile",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Avatar selection
                Text(
                    "Choose Your Avatar",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(PlayerAvatar.values()) { avatar ->
                        Surface(
                            modifier = Modifier
                                .size(60.dp)
                                .clickable { selectedAvatar = avatar },
                            shape = CircleShape,
                            color = if (selectedAvatar == avatar)
                                MaterialTheme.colorScheme.primaryContainer
                            else MaterialTheme.colorScheme.surfaceContainer,
                            border = if (selectedAvatar == avatar)
                                androidx.compose.foundation.BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
                            else null
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    avatar.emoji,
                                    fontSize = 28.sp
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Name input
                OutlinedTextField(
                    value = playerName,
                    onValueChange = { playerName = it },
                    label = { Text("Your Name") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = { focusManager.clearFocus() }
                    )
                )

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
                        onClick = {
                            if (playerName.isNotBlank()) {
                                val profile = userProfileManager.createNewProfile(
                                    name = playerName.trim(),
                                    avatar = selectedAvatar
                                )
                                userProfileManager.saveCurrentProfile(profile)
                                onProfileCreated(profile)
                            }
                        },
                        enabled = playerName.isNotBlank(),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Create")
                    }
                }
            }
        }
    }
}

@Composable
private fun ProfileOverview(
    profile: UserProfile,
    userProfileManager: UserProfileManager,
    onProfileUpdated: (UserProfile) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            // Profile Header
            ProfileHeader(profile = profile)
        }

        item {
            // Level Progress
            LevelProgressCard(profile = profile)
        }

        item {
            // Quick Stats
            QuickStatsCard(profile = profile)
        }

        item {
            // Recent Achievements
            RecentAchievementsCard(profile = profile)
        }

        item {
            // Category Preferences
            CategoryStatsCard(profile = profile)
        }
    }
}

@Composable
private fun ProfileHeader(profile: UserProfile) {
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
            Surface(
                modifier = Modifier.size(80.dp),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primary
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        profile.preferredAvatar.emoji,
                        fontSize = 40.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                profile.name,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Text(
                profile.getLevelTitle(),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
            Text(
                "Member since ${dateFormat.format(Date(profile.createdAt))}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun LevelProgressCard(profile: UserProfile) {
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
                    "Level ${profile.getLevel()}",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    "${profile.totalGamesPlayed} games played",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Progress towards next level
            val currentLevelThreshold = when (profile.getLevel()) {
                1 -> 0
                2 -> 5
                3 -> 15
                4 -> 30
                5 -> 50
                else -> 100
            }

            val nextLevelThreshold = when (profile.getLevel()) {
                1 -> 5
                2 -> 15
                3 -> 30
                4 -> 50
                5 -> 100
                else -> 100
            }

            val progress = if (profile.getLevel() >= 6) 1f else {
                (profile.totalGamesPlayed - currentLevelThreshold).toFloat() /
                        (nextLevelThreshold - currentLevelThreshold).toFloat()
            }

            LinearProgressIndicator(
                progress = { progress.coerceIn(0f, 1f) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (profile.getLevel() < 6) {
                Text(
                    "${nextLevelThreshold - profile.totalGamesPlayed} more games to next level",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                Text(
                    "Maximum level reached!",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
private fun StatisticsTab(profile: UserProfile) {
    LazyColumn(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            OverallStatsCard(profile = profile)
        }

        item {
            CategoryBreakdownCard(profile = profile)
        }
    }
}

@Composable
fun QuickStatsCard(profile: UserProfile) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                "Quick Stats",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                QuickStatItem(
                    value = profile.totalGamesPlayed.toString(),
                    label = "Games Played",
                    icon = Icons.Default.SportsEsports,
                    color = MaterialTheme.colorScheme.primary
                )

                QuickStatItem(
                    value = "${profile.getWinRate().roundToInt()}%",
                    label = "Win Rate",
                    icon = Icons.Default.EmojiEvents,
                    color = MaterialTheme.colorScheme.secondary
                )

                QuickStatItem(
                    value = profile.bestStreak.toString(),
                    label = "Best Streak",
                    icon = Icons.Default.Whatshot,
                    color = MaterialTheme.colorScheme.tertiary
                )

                QuickStatItem(
                    value = profile.achievements.size.toString(),
                    label = "Achievements",
                    icon = Icons.Default.Star,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
private fun QuickStatItem(
    value: String,
    label: String,
    icon: ImageVector,
    color: androidx.compose.ui.graphics.Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            modifier = Modifier.size(48.dp),
            shape = CircleShape,
            color = color.copy(alpha = 0.1f)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    icon,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = color
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            value,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = color
        )

        Text(
            label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun RecentAchievementsCard(profile: UserProfile) {
    val recentAchievements = profile.achievements.takeLast(6) // Show last 6 achievements

    if (recentAchievements.isEmpty()) {
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    Icons.Default.EmojiEvents,
                    contentDescription = null,
                    modifier = Modifier.size(48.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "No Achievements Yet",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    "Play games to unlock achievements!",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }
        }
        return
    }

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
                    "Recent Achievements",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                if (profile.achievements.size > 6) {
                    Text(
                        "+${profile.achievements.size - 6} more",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(recentAchievements) { achievement ->
                    AchievementBadge(achievement = achievement)
                }
            }
        }
    }
}

@Composable
private fun AchievementBadge(achievement: Achievement) {
    Card(
        modifier = Modifier.width(120.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                achievement.type.icon,
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                achievement.type.displayName,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Text(
                achievement.type.description,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
            )
        }
    }
}

@Composable
fun CategoryStatsCard(profile: UserProfile) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                "Category Performance",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (profile.categoryStats.isEmpty()) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        Icons.Default.BarChart,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "No category data yet",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        "Play games in different categories to see your performance!",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    profile.categoryStats.entries.sortedByDescending { it.value.gamesPlayed }.forEach { (category, stats) ->
                        CategoryStatItem(
                            category = category,
                            stats = stats
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CategoryStatItem(
    category: GameCategory,
    stats: CategoryStats
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceContainer
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Category icon
            Surface(
                modifier = Modifier.size(48.dp),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        category.icon,
                        fontSize = 20.sp
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    category.displayName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        "${stats.gamesPlayed} games",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        "${stats.getWinRate().roundToInt()}% win rate",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    if (stats.bestStreak > 0) {
                        Text(
                            "${stats.bestStreak} best streak",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            // Win rate indicator
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = when {
                    stats.getWinRate() >= 70 -> MaterialTheme.colorScheme.primaryContainer
                    stats.getWinRate() >= 50 -> MaterialTheme.colorScheme.secondaryContainer
                    stats.getWinRate() >= 30 -> MaterialTheme.colorScheme.tertiaryContainer
                    else -> MaterialTheme.colorScheme.errorContainer
                }
            ) {
                Text(
                    "${stats.wins}/${stats.gamesPlayed}",
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun OverallStatsCard(profile: UserProfile) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                "Overall Statistics",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Main stats grid
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    DetailedStatItem(
                        title = "Total Games",
                        value = profile.totalGamesPlayed.toString(),
                        subtitle = "Since ${java.text.SimpleDateFormat("MMM yyyy", java.util.Locale.getDefault()).format(java.util.Date(profile.createdAt))}",
                        icon = Icons.Default.SportsEsports
                    )

                    DetailedStatItem(
                        title = "Total Wins",
                        value = profile.totalWins.toString(),
                        subtitle = "${profile.getWinRate().roundToInt()}% win rate",
                        icon = Icons.Default.EmojiEvents
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    DetailedStatItem(
                        title = "Best Streak",
                        value = profile.bestStreak.toString(),
                        subtitle = "Consecutive wins",
                        icon = Icons.Default.Whatshot
                    )

                    DetailedStatItem(
                        title = "Level",
                        value = profile.getLevel().toString(),
                        subtitle = profile.getLevelTitle(),
                        icon = Icons.Default.Star
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    DetailedStatItem(
                        title = "Achievements",
                        value = profile.achievements.size.toString(),
                        subtitle = "Unlocked",
                        icon = Icons.Default.EmojiEvents
                    )

                    DetailedStatItem(
                        title = "Categories",
                        value = profile.categoryStats.size.toString(),
                        subtitle = "Played",
                        icon = Icons.Default.Category
                    )
                }
            }
        }
    }
}

@Composable
private fun DetailedStatItem(
    title: String,
    value: String,
    subtitle: String,
    icon: ImageVector
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(140.dp)
    ) {
        Icon(
            icon,
            contentDescription = null,
            modifier = Modifier.size(32.dp),
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            value,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Text(
            title,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
        )

        Text(
            subtitle,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun CategoryBreakdownCard(profile: UserProfile) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                "Category Breakdown",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (profile.categoryStats.isEmpty()) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        Icons.Default.DonutLarge,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "No Category Data",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        "Play games in different categories to see detailed breakdowns!",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    profile.categoryStats.entries.sortedByDescending { it.value.gamesPlayed }.forEach { (category, stats) ->
                        CategoryBreakdownItem(
                            category = category,
                            stats = stats,
                            totalGames = profile.totalGamesPlayed
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CategoryBreakdownItem(
    category: GameCategory,
    stats: CategoryStats,
    totalGames: Int
) {
    val percentage = if (totalGames > 0) (stats.gamesPlayed.toFloat() / totalGames.toFloat()) else 0f

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    category.icon,
                    fontSize = 20.sp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    category.displayName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Text(
                "${stats.gamesPlayed} games (${(percentage * 100).roundToInt()}%)",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Progress bar showing category usage
        LinearProgressIndicator(
            progress = { percentage },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp),
            color = MaterialTheme.colorScheme.primary,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                "Win Rate: ${stats.getWinRate().roundToInt()}%",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            if (stats.bestStreak > 0) {
                Text(
                    "Best Streak: ${stats.bestStreak}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
private fun HistoryTab(gameHistory: List<GameHistoryEntry>) {
    LazyColumn(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (gameHistory.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Default.History,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            "No games played yet",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            "Start playing to see your game history!",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        } else {
            items(gameHistory) { entry ->
                GameHistoryCard(entry = entry)
            }
        }
    }
}

@Composable
private fun GameHistoryCard(entry: GameHistoryEntry) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Category icon
            Surface(
                modifier = Modifier.size(48.dp),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.surfaceContainer
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        entry.category.icon,
                        fontSize = 20.sp
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    entry.category.displayName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "${entry.gameMode.displayName} • ${entry.playerCount} players",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault()).format(Date(entry.date)),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Column(
                horizontalAlignment = Alignment.End
            ) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = when (entry.finalPosition) {
                        1 -> MaterialTheme.colorScheme.primaryContainer
                        2 -> MaterialTheme.colorScheme.secondaryContainer
                        3 -> MaterialTheme.colorScheme.tertiaryContainer
                        else -> MaterialTheme.colorScheme.surfaceContainer
                    }
                ) {
                    Text(
                        "#${entry.finalPosition}",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "${entry.score}/${entry.totalRounds} wins",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    "${entry.accuracy.toInt()}% accuracy",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

// Additional helper composables for the remaining cards would go here...
// (QuickStatsCard, RecentAchievementsCard, CategoryStatsCard, etc.)

@Composable
private fun ProfileSelectorDialog(
    userProfileManager: UserProfileManager,
    currentProfile: UserProfile?,
    onProfileSelected: (UserProfile) -> Unit,
    onCreateNew: () -> Unit,
    onDismiss: () -> Unit
) {
    val allProfiles = remember { userProfileManager.getAllProfiles() }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                Text(
                    "Select Profile",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(allProfiles) { profile ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onProfileSelected(profile) },
                            colors = CardDefaults.cardColors(
                                containerColor = if (profile.id == currentProfile?.id)
                                    MaterialTheme.colorScheme.primaryContainer
                                else MaterialTheme.colorScheme.surfaceContainer
                            )
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    modifier = Modifier.size(40.dp),
                                    shape = CircleShape,
                                    color = MaterialTheme.colorScheme.primary
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(
                                            profile.preferredAvatar.emoji,
                                            fontSize = 20.sp
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.width(16.dp))

                                Column(
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        profile.name,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        "Level ${profile.getLevel()} • ${profile.totalGamesPlayed} games",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }

                                if (profile.id == currentProfile?.id) {
                                    Icon(
                                        Icons.Default.CheckCircle,
                                        contentDescription = "Current",
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }
                    }

                    item {
                        OutlinedCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onCreateNew() }
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Default.PersonAdd,
                                    contentDescription = null,
                                    modifier = Modifier.size(40.dp),
                                    tint = MaterialTheme.colorScheme.primary
                                )

                                Spacer(modifier = Modifier.width(16.dp))

                                Text(
                                    "Create New Profile",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Cancel")
                }
            }
        }
    }
}