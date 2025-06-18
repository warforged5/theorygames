// SettingsScreen.kt
package io.github.warforged5.theorygames.dataclass

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
    var showSuccessMessage by remember { mutableStateOf(false) }

    // Show success message when theme changes
    LaunchedEffect(currentTheme) {
        showSuccessMessage = true
        kotlinx.coroutines.delay(2000)
        showSuccessMessage = false
    }

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
        Box(modifier = Modifier.fillMaxSize()) {
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
                    // Enhanced Theme Selection Card
                    ElevatedCard(
                        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 6.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Default.Palette,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    "Color Theme",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Surface(
                                onClick = { showThemeDialog = true },
                                shape = RoundedCornerShape(16.dp),
                                color = MaterialTheme.colorScheme.primaryContainer,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(20.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            currentTheme.displayName,
                                            style = MaterialTheme.typography.titleLarge,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onPrimaryContainer
                                        )
                                        Text(
                                            currentTheme.description,
                                            style = MaterialTheme.typography.bodyLarge,
                                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                                        )
                                    }

                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        // Theme preview
                                        ThemePreviewDots(theme = currentTheme)
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Icon(
                                            Icons.Default.ChevronRight,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                item {
                    // Enhanced Dark Mode Card
                    ElevatedCard(
                        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 6.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    if (currentDarkMode == true) Icons.Default.DarkMode
                                    else if (currentDarkMode == false) Icons.Default.LightMode
                                    else Icons.Default.Brightness4,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    "Dark Mode",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Column(
                                modifier = Modifier.selectableGroup(),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                DarkModeOption(
                                    label = "Follow System",
                                    description = "Use system setting",
                                    icon = Icons.Default.Brightness4,
                                    isSelected = currentDarkMode == null,
                                    onClick = { onDarkModeChanged(null) }
                                )

                                DarkModeOption(
                                    label = "Light Mode",
                                    description = "Always use light theme",
                                    icon = Icons.Default.LightMode,
                                    isSelected = currentDarkMode == false,
                                    onClick = { onDarkModeChanged(false) }
                                )

                                DarkModeOption(
                                    label = "Dark Mode",
                                    description = "Always use dark theme",
                                    icon = Icons.Default.DarkMode,
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
                            modifier = Modifier.padding(20.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
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

            // Success message
            AnimatedVisibility(
                visible = showSuccessMessage,
                enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut(),
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 16.dp)
            ) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "Theme applied successfully!",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
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
fun ThemePreviewDots(theme: AppTheme) {
    val colors = getThemePreviewColors(theme)

    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        colors.forEach { color ->
            Surface(
                modifier = Modifier.size(12.dp),
                shape = androidx.compose.foundation.shape.CircleShape,
                color = color
            ) {}
        }
    }
}

@Composable
fun DarkModeOption(
    label: String,
    description: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
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
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = if (isSelected) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
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
                ThemePreviewDots(theme = theme)
            }
        }
    }
}

private fun getThemePreviewColors(theme: AppTheme): List<androidx.compose.ui.graphics.Color> {
    return when (theme) {
        AppTheme.CLASSIC, AppTheme.SYSTEM -> listOf(
            androidx.compose.ui.graphics.Color(0xFF6650a4),
            androidx.compose.ui.graphics.Color(0xFF625b71),
            androidx.compose.ui.graphics.Color(0xFF7D5260)
        )
        AppTheme.OCEAN -> listOf(
            androidx.compose.ui.graphics.Color(0xFF1976D2),
            androidx.compose.ui.graphics.Color(0xFF0288D1),
            androidx.compose.ui.graphics.Color(0xFF00ACC1)
        )
        AppTheme.FOREST -> listOf(
            androidx.compose.ui.graphics.Color(0xFF2E7D32),
            androidx.compose.ui.graphics.Color(0xFF388E3C),
            androidx.compose.ui.graphics.Color(0xFF4CAF50)
        )
        AppTheme.SUNSET -> listOf(
            androidx.compose.ui.graphics.Color(0xFFE65100),
            androidx.compose.ui.graphics.Color(0xFFFF5722),
            androidx.compose.ui.graphics.Color(0xFFFF9800)
        )
        AppTheme.MIDNIGHT -> listOf(
            androidx.compose.ui.graphics.Color(0xFF9C27B0),
            androidx.compose.ui.graphics.Color(0xFF673AB7),
            androidx.compose.ui.graphics.Color(0xFF3F51B5)
        )
        AppTheme.DYNAMIC -> listOf(
            androidx.compose.ui.graphics.Color(0xFF6750A4),
            androidx.compose.ui.graphics.Color(0xFF958DA5),
            androidx.compose.ui.graphics.Color(0xFF735573)
        )
    }
}