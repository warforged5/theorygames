package io.github.warforged5.theorygames.utils

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Error
import androidx.compose.ui.Alignment

// Extension functions for cleaner code
@Composable
fun Modifier.clickableIf(condition: Boolean, onClick: () -> Unit): Modifier {
    return if (condition) {
        this.clickable { onClick() }
    } else {
        this
    }
}

// Custom spacing helpers
object AppSpacing {
    val extraSmall = 4.dp
    val small = 8.dp
    val medium = 16.dp
    val large = 24.dp
    val extraLarge = 32.dp
}

// Profile Status Helpers
@Composable
fun ProfileStatusIndicator(
    isOnline: Boolean = false,
    totalGames: Int = 0,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        color = if (totalGames > 0) MaterialTheme.colorScheme.primaryContainer
        else MaterialTheme.colorScheme.surfaceContainer
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (totalGames > 0) {
                Text(
                    "$totalGames games",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            } else {
                Text(
                    "New Player",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

// Theme Preview Colors Helper
fun getThemePreviewColors(theme: io.github.warforged5.theorygames.dataclass.AppTheme): List<Color> {
    return when (theme) {
        io.github.warforged5.theorygames.dataclass.AppTheme.CLASSIC,
        io.github.warforged5.theorygames.dataclass.AppTheme.SYSTEM -> listOf(
            Color(0xFF6650a4), Color(0xFF625b71), Color(0xFF7D5260)
        )
        io.github.warforged5.theorygames.dataclass.AppTheme.OCEAN -> listOf(
            Color(0xFF1976D2), Color(0xFF0288D1), Color(0xFF00ACC1)
        )
        io.github.warforged5.theorygames.dataclass.AppTheme.FOREST -> listOf(
            Color(0xFF2E7D32), Color(0xFF388E3C), Color(0xFF4CAF50)
        )
        io.github.warforged5.theorygames.dataclass.AppTheme.SUNSET -> listOf(
            Color(0xFFE65100), Color(0xFFFF5722), Color(0xFFFF9800)
        )
        io.github.warforged5.theorygames.dataclass.AppTheme.MIDNIGHT -> listOf(
            Color(0xFF9C27B0), Color(0xFF673AB7), Color(0xFF3F51B5)
        )
        io.github.warforged5.theorygames.dataclass.AppTheme.DYNAMIC -> listOf(
            Color(0xFF6750A4), Color(0xFF958DA5), Color(0xFF735573)
        )
    }
}

// Animation helpers
@Composable
fun rememberBounceAnimation(): androidx.compose.animation.core.InfiniteTransition {
    return androidx.compose.animation.core.rememberInfiniteTransition(label = "bounce")
}

// Statistics helpers
fun calculateWinRate(wins: Int, totalGames: Int): Double {
    return if (totalGames > 0) (wins.toDouble() / totalGames.toDouble()) * 100 else 0.0
}

fun formatWinRate(wins: Int, totalGames: Int): String {
    val rate = calculateWinRate(wins, totalGames)
    return "${String.format("%.1f", rate)}%"
}

// Profile sorting helpers
enum class ProfileSortOrder {
    NAME,
    LAST_PLAYED,
    TOTAL_WINS,
    WIN_RATE,
    GAMES_PLAYED
}

fun List<io.github.warforged5.theorygames.dataclass.SavedProfile>.sortedBy(
    order: ProfileSortOrder
): List<io.github.warforged5.theorygames.dataclass.SavedProfile> {
    return when (order) {
        ProfileSortOrder.NAME -> this.sortedBy { it.name }
        ProfileSortOrder.LAST_PLAYED -> this.sortedByDescending { it.lastPlayed }
        ProfileSortOrder.TOTAL_WINS -> this.sortedByDescending { it.totalWins }
        ProfileSortOrder.WIN_RATE -> this.sortedByDescending {
            calculateWinRate(it.totalWins, it.totalGamesPlayed)
        }
        ProfileSortOrder.GAMES_PLAYED -> this.sortedByDescending { it.totalGamesPlayed }
    }
}

// Enhanced Composable Components
@Composable
fun StatCard(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier,
    subtitle: String? = null
) {
    ElevatedCard(
        modifier = modifier,
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            subtitle?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun LoadingCard(
    message: String = "Loading...",
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                strokeWidth = 2.dp
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = message,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

// Validation helpers
fun validateProfileName(name: String): String? {
    return when {
        name.isBlank() -> "Name cannot be empty"
        name.length < 2 -> "Name must be at least 2 characters"
        name.length > 20 -> "Name must be less than 20 characters"
        !name.matches(Regex("^[a-zA-Z0-9\\s]+$")) -> "Name can only contain letters, numbers, and spaces"
        else -> null
    }
}

// Date formatting helpers
fun formatLastPlayed(timestamp: Long): String {
    if (timestamp == 0L) return "Never"

    val now = System.currentTimeMillis()
    val diff = now - timestamp

    return when {
        diff < 60_000 -> "Just now"
        diff < 3_600_000 -> "${diff / 60_000} minutes ago"
        diff < 86_400_000 -> "${diff / 3_600_000} hours ago"
        diff < 604_800_000 -> "${diff / 86_400_000} days ago"
        else -> {
            val date = java.util.Date(timestamp)
            java.text.SimpleDateFormat("MMM dd, yyyy", java.util.Locale.getDefault()).format(date)
        }
    }
}

// Error handling helpers
@Composable
fun ErrorCard(
    message: String,
    onRetry: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = modifier,
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            androidx.compose.material.icons.Icons.Default.Error.let { icon ->
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onErrorContainer,
                    modifier = Modifier.size(48.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Error",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onErrorContainer
            )

            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onErrorContainer,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            onRetry?.let { retry ->
                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = retry,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.onError
                    )
                ) {
                    Text("Retry")
                }
            }
        }
    }
}