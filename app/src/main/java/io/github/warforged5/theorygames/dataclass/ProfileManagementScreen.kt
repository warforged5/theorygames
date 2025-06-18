package io.github.warforged5.theorygames.dataclass

import androidx.compose.animation.*
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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileManagementScreen(
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val profileManager = remember { ProfileManager(context) }
    val scope = rememberCoroutineScope()

    val profileCollection by profileManager.profilesFlow.collectAsState(initial = ProfileCollection())

    var showCreateDialog by remember { mutableStateOf(false) }
    var editingProfile by remember { mutableStateOf<SavedProfile?>(null) }
    var showDeleteDialog by remember { mutableStateOf<SavedProfile?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Manage Profiles") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showCreateDialog = true },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.PersonAdd, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("New Profile")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (profileCollection.profiles.isEmpty()) {
                item {
                    EmptyProfilesCard(
                        onCreateProfile = { showCreateDialog = true }
                    )
                }
            } else {
                item {
                    Text(
                        "Saved Profiles (${profileCollection.profiles.size})",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }

                items(
                    items = profileCollection.profiles.sortedByDescending { it.lastPlayed },
                    key = { it.id }
                ) { profile ->
                    AnimatedVisibility(
                        visible = true,
                        enter = slideInVertically() + fadeIn(),
                        exit = slideOutVertically() + fadeOut()
                    ) {
                        ProfileCard(
                            profile = profile,
                            onEdit = { editingProfile = profile },
                            onDelete = { showDeleteDialog = profile }
                        )
                    }
                }
            }

            // Add space for FAB
            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }

    // Create/Edit Profile Dialog
    if (showCreateDialog || editingProfile != null) {
        ProfileEditDialog(
            profile = editingProfile,
            onDismiss = {
                showCreateDialog = false
                editingProfile = null
            },
            onSave = { profile ->
                scope.launch {
                    profileManager.saveProfile(profile)
                    showCreateDialog = false
                    editingProfile = null
                }
            }
        )
    }

    // Delete Confirmation Dialog
    showDeleteDialog?.let { profile ->
        AlertDialog(
            onDismissRequest = { showDeleteDialog = null },
            title = { Text("Delete Profile") },
            text = {
                Text("Are you sure you want to delete ${profile.name}? This action cannot be undone.")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        scope.launch {
                            profileManager.deleteProfile(profile.id)
                            showDeleteDialog = null
                        }
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = null }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun EmptyProfilesCard(
    onCreateProfile: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                Icons.Default.PersonAdd,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "No Profiles Yet",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Text(
                "Create your first profile to save your game statistics and achievements",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = onCreateProfile,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Create First Profile")
            }
        }
    }
}

@Composable
fun ProfileCard(
    profile: SavedProfile,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val dateFormat = remember { SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()) }
    val lastPlayedDate = remember(profile.lastPlayed) {
        if (profile.lastPlayed > 0) {
            dateFormat.format(Date(profile.lastPlayed))
        } else {
            "Never"
        }
    }

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
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                // Avatar
                Surface(
                    modifier = Modifier.size(56.dp),
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            profile.avatar.emoji,
                            style = MaterialTheme.typography.headlineMedium
                        )
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        profile.name,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        "${profile.totalWins} wins • ${profile.totalGamesPlayed} games",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )

                    if (profile.longestStreak > 1) {
                        Text(
                            "Best streak: ${profile.longestStreak}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.tertiary
                        )
                    }

                    Text(
                        "Last played: $lastPlayedDate",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    if (profile.achievements.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.secondaryContainer
                        ) {
                            Text(
                                "${profile.achievements.size} achievements",
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    }
                }
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                IconButton(
                    onClick = onEdit,
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit Profile")
                }

                IconButton(
                    onClick = onDelete,
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete Profile")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileEditDialog(
    profile: SavedProfile?,
    onDismiss: () -> Unit,
    onSave: (SavedProfile) -> Unit
) {
    var name by remember { mutableStateOf(profile?.name ?: "") }
    var selectedAvatar by remember { mutableStateOf(profile?.avatar ?: PlayerAvatar.SCIENTIST) }
    var selectedCategory by remember { mutableStateOf(profile?.favoriteCategory ?: GameCategory.HDI) }

    val focusManager = LocalFocusManager.current
    val isEditing = profile != null

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(if (isEditing) "Edit Profile" else "Create Profile")
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Profile Name") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = { focusManager.clearFocus() }
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                // Avatar Selection
                Text(
                    "Choose Avatar",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(PlayerAvatar.values().toList()) { avatar ->
                        FilterChip(
                            onClick = { selectedAvatar = avatar },
                            selected = selectedAvatar == avatar,
                            label = { Text(avatar.names) },
                            leadingIcon = {
                                Text(avatar.emoji)
                            }
                        )
                    }
                }

                // Favorite Category
                Text(
                    "Favorite Category",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(GameCategory.values().toList()) { category ->
                        FilterChip(
                            onClick = { selectedCategory = category },
                            selected = selectedCategory == category,
                            label = { Text(category.displayName) },
                            leadingIcon = {
                                Text(category.icon)
                            }
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotBlank()) {
                        val newProfile = SavedProfile(
                            id = profile?.id ?: "profile_${System.currentTimeMillis()}",
                            name = name.trim(),
                            avatar = selectedAvatar,
                            favoriteCategory = selectedCategory,
                            totalGamesPlayed = profile?.totalGamesPlayed ?: 0,
                            totalWins = profile?.totalWins ?: 0,
                            longestStreak = profile?.longestStreak ?: 0,
                            achievements = profile?.achievements ?: emptyList(),
                            createdAt = profile?.createdAt ?: System.currentTimeMillis(),
                            lastPlayed = profile?.lastPlayed ?: 0
                        )
                        onSave(newProfile)
                    }
                },
                enabled = name.isNotBlank()
            ) {
                Text(if (isEditing) "Save Changes" else "Create Profile")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}