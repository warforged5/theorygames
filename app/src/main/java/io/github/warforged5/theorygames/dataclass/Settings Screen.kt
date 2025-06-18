// SettingsScreen.kt
package io.github.warforged5.theorygames.dataclass

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit,
    currentTheme: AppTheme,
    currentDarkMode: Boolean?,
    onThemeChanged: (AppTheme) -> Unit,
    onDarkModeChanged: (Boolean?) -> Unit
) {
    val context = LocalContext.current
    val settingsManager = remember { SettingsManager(context) }
    val scope = rememberCoroutineScope()

    val settings by settingsManager.settingsFlow.collectAsState(initial = AppSettings())

    var showThemeDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
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
                Text(
                    "Appearance",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            item {
                // Theme Selection Card
                ElevatedCard {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            "Color Theme",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Surface(
                            onClick = { showThemeDialog = true },
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
                                        currentTheme.displayName,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        currentTheme.description,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                Icon(
                                    Icons.Default.ChevronRight,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }

            item {
                // Dark Mode Card
                ElevatedCard {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            "Dark Mode",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Column(
                            modifier = Modifier.selectableGroup()
                        ) {
                            DarkModeOption(
                                label = "Follow System",
                                description = "Use system setting",
                                isSelected = currentDarkMode == null,
                                onClick = { onDarkModeChanged(null) }
                            )

                            DarkModeOption(
                                label = "Light Mode",
                                description = "Always use light theme",
                                isSelected = currentDarkMode == false,
                                onClick = { onDarkModeChanged(false) }
                            )

                            DarkModeOption(
                                label = "Dark Mode",
                                description = "Always use dark theme",
                                isSelected = currentDarkMode == true,
                                onClick = { onDarkModeChanged(true) }
                            )
                        }
                    }
                }
            }

            item {
                Text(
                    "Game Preferences",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            item {
                // Game Settings Card
                ElevatedCard {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Default Game Mode
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    "Default Game Mode",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    settings.defaultGameMode.displayName,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }

                        HorizontalDivider()

                        // Timer Settings
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    "Timer by Default",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    if (settings.defaultTimer) "Questions have time limits"
                                    else "Play at your own pace",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Switch(
                                checked = settings.defaultTimer,
                                onCheckedChange = { newValue ->
                                    scope.launch {
                                        settingsManager.updateSettings(
                                            settings.copy(defaultTimer = newValue)
                                        )
                                    }
                                }
                            )
                        }

                        HorizontalDivider()

                        // Animations
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    "Animations",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    if (settings.enableAnimations) "Enhanced visual effects"
                                    else "Reduced motion",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Switch(
                                checked = settings.enableAnimations,
                                onCheckedChange = { newValue ->
                                    scope.launch {
                                        settingsManager.updateSettings(
                                            settings.copy(enableAnimations = newValue)
                                        )
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    // Theme Selection Dialog
    if (showThemeDialog) {
        ThemeSelectionDialog(
            currentTheme = currentTheme,
            onThemeSelected = { theme ->
                onThemeChanged(theme)
                showThemeDialog = false
            },
            onDismiss = { showThemeDialog = false }
        )
    }
}

@Composable
fun DarkModeOption(
    label: String,
    description: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(8.dp),
        color = if (isSelected) MaterialTheme.colorScheme.primaryContainer
        else MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    label,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            RadioButton(
                selected = isSelected,
                onClick = onClick
            )
        }
    }
}

@Composable
fun ThemeSelectionDialog(
    currentTheme: AppTheme,
    onThemeSelected: (AppTheme) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Choose Theme")
        },
        text = {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(AppTheme.values().toList()) { theme ->
                    ThemeOption(
                        theme = theme,
                        isSelected = currentTheme == theme,
                        onClick = { onThemeSelected(theme) }
                    )
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
fun ThemeOption(
    theme: AppTheme,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        color = if (isSelected) MaterialTheme.colorScheme.primaryContainer
        else MaterialTheme.colorScheme.surfaceContainer
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
                    theme.displayName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    theme.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (isSelected) {
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = "Selected",
                    tint = MaterialTheme.colorScheme.primary
                )
            } else {
                Surface(
                    modifier = Modifier.size(24.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = getThemePreviewColor(theme)
                ) {}
            }
        }
    }
}

@Composable
private fun getThemePreviewColor(theme: AppTheme) = when (theme) {
    AppTheme.CLASSIC, AppTheme.SYSTEM -> androidx.compose.ui.graphics.Color(0xFF6650a4)
    AppTheme.OCEAN -> androidx.compose.ui.graphics.Color(0xFF1976D2)
    AppTheme.FOREST -> androidx.compose.ui.graphics.Color(0xFF2E7D32)
    AppTheme.SUNSET -> androidx.compose.ui.graphics.Color(0xFFE65100)
    AppTheme.MIDNIGHT -> androidx.compose.ui.graphics.Color(0xFF9C27B0)
    AppTheme.DYNAMIC -> MaterialTheme.colorScheme.primary
}