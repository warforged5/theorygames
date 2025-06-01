package io.github.warforged5.theorygames

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.github.warforged5.theorygames.ui.theme.TheorygamesTheme
import kotlinx.coroutines.delay
import kotlin.math.abs
import androidx.compose.ui.semantics.semantics
import androidx.graphics.shapes.RoundedPolygon

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
           TheorygamesTheme {
                NerdyGamesApp()
            }
        }
    }
}

data class PlayerGuess(
    val player: Player,
    val guess: Double,
    val difference: Double,
    val isClosest: Boolean = false
)

data class UserProfile(
    val id: String = java.util.UUID.randomUUID().toString(),
    val name: String,
    val totalGames: Int = 0,
    val totalWins: Int = 0,
    val hdiGames: Int = 0,
    val hdiWins: Int = 0,
    val gdpGames: Int = 0,
    val gdpWins: Int = 0,
    val atomicGames: Int = 0,
    val atomicWins: Int = 0,
    val bestStreak: Int = 0,
    val totalScore: Int = 0,
    val averageAccuracy: Double = 0.0,
    val achievements: List<String> = emptyList(),
    val createdAt: Long = System.currentTimeMillis()
)

// Persistent User Manager
object UserManager {
    private val users = mutableStateMapOf<String, UserProfile>()

    fun saveUser(user: UserProfile) {
        users[user.id] = user
    }

    fun getUser(id: String): UserProfile? = users[id]

    fun getAllUsers(): List<UserProfile> = users.values.toList()

    fun updateUserStats(
        userId: String,
        gameType: String,
        won: Boolean,
        score: Int,
        streak: Int
    ) {
        val user = users[userId] ?: return
        val updatedUser = when (gameType) {
            "hdi" -> user.copy(
                totalGames = user.totalGames + 1,
                totalWins = if (won) user.totalWins + 1 else user.totalWins,
                hdiGames = user.hdiGames + 1,
                hdiWins = if (won) user.hdiWins + 1 else user.hdiWins,
                bestStreak = maxOf(user.bestStreak, streak),
                totalScore = user.totalScore + score
            )
            "gdp_total", "gdp_per_capita" -> user.copy(
                totalGames = user.totalGames + 1,
                totalWins = if (won) user.totalWins + 1 else user.totalWins,
                gdpGames = user.gdpGames + 1,
                gdpWins = if (won) user.gdpWins + 1 else user.gdpWins,
                bestStreak = maxOf(user.bestStreak, streak),
                totalScore = user.totalScore + score
            )
            "atomic" -> user.copy(
                totalGames = user.totalGames + 1,
                totalWins = if (won) user.totalWins + 1 else user.totalWins,
                atomicGames = user.atomicGames + 1,
                atomicWins = if (won) user.atomicWins + 1 else user.atomicWins,
                bestStreak = maxOf(user.bestStreak, streak),
                totalScore = user.totalScore + score
            )
            else -> user
        }
        users[userId] = updatedUser
    }
}

data class Player(
    val id: String = java.util.UUID.randomUUID().toString(),
    val name: String,
    var score: Int = 0,
    var streak: Int = 0,
    var totalCorrect: Int = 0,
    var averageAccuracy: Double = 0.0,
    val userProfile: UserProfile? = null
)

data class GameSession(
    val players: MutableList<Player> = mutableListOf(),
    val gameType: String = "",
    val questionsAnswered: Int = 0,
    val totalQuestions: Int = 10,
    val difficulty: Difficulty = Difficulty.NORMAL,
    val hasTimer: Boolean = false,
    val timerSeconds: Int = 30,
    val currentPlayerIndex: Int = 0
)

enum class Difficulty(val displayName: String, val emoji: String, val multiplier: Double) {
    EASY("Easy", "😊", 0.8),
    NORMAL("Normal", "🤔", 1.0),
    HARD("Hard", "🧠", 1.5),
    EXTREME("Extreme", "💀", 2.0)
}

data class Country(
    val name: String,
    val hdi: Double,
    val emoji: String = "🌍",
    val region: String = "Unknown",
    val funFact: String = "",
    val gdpTotal: Double = 0.0,
    val gdpPerCapita: Double = 0.0,
    val population: Double = 0.0
)

data class Element(
    val name: String,
    val symbol: String,
    val atomicNumber: Int,
    val category: String = "Unknown",
    val funFact: String = ""
)

// Game Manager
class GameManager {
    companion object {
        val countries = listOf(
            Country("Iceland", 0.972, "🇮🇸", "Europe", "Land of fire and ice with geothermal energy", 25.6, 75420.0, 0.34),
            Country("Norway", 0.970, "🇳🇴", "Europe", "Home to the midnight sun and Northern Lights", 482.2, 89154.0, 5.4),
            Country("Switzerland", 0.965, "🇨🇭", "Europe", "Famous for chocolate, watches, and neutrality", 812.9, 93457.0, 8.7),
            Country("Denmark", 0.962, "🇩🇰", "Europe", "Consistently ranked as the happiest country", 390.7, 67218.0, 5.8),
            Country("Germany", 0.959, "🇩🇪", "Europe", "Europe's economic powerhouse", 4259.9, 51203.0, 83.2),
            Country("Sweden", 0.957, "🇸🇪", "Europe", "Pioneer in sustainability and innovation", 635.7, 60816.0, 10.5),
            Country("Australia", 0.958, "🇦🇺", "Oceania", "Home to unique wildlife and the Great Barrier Reef", 1552.7, 59934.0, 25.9),
            Country("Netherlands", 0.955, "🇳🇱", "Europe", "Famous for tulips, windmills, and cycling", 1012.8, 57101.0, 17.4),
            Country("United States", 0.937, "🇺🇸", "North America", "Land of the free, home of the brave", 25462.7, 76398.0, 333.3),
            Country("Japan", 0.925, "🇯🇵", "Asia", "Land of the rising sun and cutting-edge technology", 4940.9, 39340.0, 125.7),
            Country("China", 0.797, "🇨🇳", "Asia", "Most populous country and ancient civilization", 17734.1, 12556.0, 1412.2),
            Country("Brazil", 0.786, "🇧🇷", "South America", "Samba, soccer, and the Amazon rainforest", 2056.3, 9638.0, 214.3),
            Country("India", 0.684, "🇮🇳", "Asia", "Incredible diversity and Bollywood", 3385.0, 2389.0, 1417.2),
            Country("Nigeria", 0.560, "🇳🇬", "Africa", "Most populous African nation", 440.8, 2085.0, 218.5),
            Country("Ethiopia", 0.497, "🇪🇹", "Africa", "Birthplace of coffee and Lucy fossil", 111.3, 944.0, 117.9),
            Country("Chad", 0.416, "🇹🇩", "Africa", "Landlocked nation in the heart of Africa", 11.3, 665.0, 17.0)
        )

        val elements = listOf(
            Element("Hydrogen", "H", 1, "Non-metal", "Most abundant element in the universe"),
            Element("Helium", "He", 2, "Noble Gas", "Makes your voice sound funny when inhaled"),
            Element("Lithium", "Li", 3, "Alkali Metal", "Used in batteries and mood stabilizers"),
            Element("Carbon", "C", 6, "Non-metal", "The basis of all organic life"),
            Element("Oxygen", "O", 8, "Non-metal", "Essential for breathing and combustion"),
            Element("Iron", "Fe", 26, "Transition Metal", "Essential for blood and very magnetic"),
            Element("Gold", "Au", 79, "Transition Metal", "Precious metal that never tarnishes"),
            Element("Silver", "Ag", 47, "Transition Metal", "Best electrical conductor"),
            Element("Uranium", "U", 92, "Actinide", "Used in nuclear power and weapons")
        )

        fun getRandomCountries(count: Int, difficulty: Difficulty): List<Country> {
            return when (difficulty) {
                Difficulty.EASY -> countries.filter { it.hdi >= 0.800 }.shuffled().take(count)
                Difficulty.NORMAL -> countries.filter { it.hdi >= 0.700 }.shuffled().take(count)
                Difficulty.HARD -> countries.filter { it.hdi >= 0.550 }.shuffled().take(count)
                Difficulty.EXTREME -> countries.shuffled().take(count)
            }
        }

        fun getRandomElements(count: Int, difficulty: Difficulty): List<Element> {
            return when (difficulty) {
                Difficulty.EASY -> elements.filter { it.atomicNumber <= 20 }.shuffled().take(count)
                Difficulty.NORMAL -> elements.filter { it.atomicNumber <= 50 }.shuffled().take(count)
                Difficulty.HARD -> elements.filter { it.atomicNumber <= 86 }.shuffled().take(count)
                Difficulty.EXTREME -> elements.shuffled().take(count)
            }
        }
    }
}

// Expressive Shape Helpers with Cookie7Sided for profiles
object ExpressiveShapes {
    val HeartShape = RoundedCornerShape(
        topStart = 50.dp,
        topEnd = 50.dp,
        bottomStart = 8.dp,
        bottomEnd = 8.dp
    )

    val DiamondShape = RoundedCornerShape(
        topStart = 4.dp,
        topEnd = 32.dp,
        bottomStart = 32.dp,
        bottomEnd = 4.dp
    )

    val CloudShape = RoundedCornerShape(
        topStart = 24.dp,
        topEnd = 16.dp,
        bottomStart = 8.dp,
        bottomEnd = 20.dp
    )

    val StarShape = RoundedCornerShape(
        topStart = 2.dp,
        topEnd = 24.dp,
        bottomStart = 24.dp,
        bottomEnd = 2.dp
    )

    val BubbleShape = RoundedCornerShape(
        topStart = 20.dp,
        topEnd = 20.dp,
        bottomStart = 20.dp,
        bottomEnd = 4.dp
    )

    // Cookie7Sided shape for profile pictures
    val Cookie7Sided = RoundedCornerShape(
        topStart = 16.dp,
        topEnd = 8.dp,
        bottomStart = 12.dp,
        bottomEnd = 20.dp
    )
}

// Enhanced Components
@Composable
fun PulsingIcon(
    icon: ImageVector,
    modifier: Modifier = Modifier,
    tint: Color = MaterialTheme.colorScheme.primary
) {
    val scale by animateFloatAsState(
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    Icon(
        icon,
        contentDescription = null,
        modifier = modifier.scale(scale),
        tint = tint
    )
}


// Enhanced Player Guess Card Component
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnhancedPlayerGuessCard(
    player: Player,
    guess: String,
    onGuessChange: (String) -> Unit,
    placeholder: String,
    label: String,
    keyboardType: KeyboardType = KeyboardType.Decimal
) {
    var isFocused by remember { mutableStateOf(false) }
    var hasGuessed = guess.isNotBlank()

    val cardColor by animateColorAsState(
        targetValue = when {
            hasGuessed -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.8f)
            isFocused -> MaterialTheme.colorScheme.secondaryContainer
            else -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        },
        animationSpec = tween(300),
        label = "cardColor"
    )

    val scale by animateFloatAsState(
        targetValue = if (isFocused) 1.02f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "cardScale"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        shape = ExpressiveShapes.BubbleShape,
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isFocused) 8.dp else 4.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Player Avatar
            Surface(
                shape = ExpressiveShapes.Cookie7Sided,
                color = if (hasGuessed)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.outline,
                modifier = Modifier.size(48.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    if (hasGuessed) {
                        Text("✓", style = MaterialTheme.typography.titleLarge)
                    } else {
                        Text(
                            player.name.take(2).uppercase(),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Player Info and Input
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                player.name,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            if (player.userProfile != null) {
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("👤", style = MaterialTheme.typography.bodySmall)
                            }
                        }
                        Text(
                            "Score: ${player.score} | Streak: ${player.streak}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }

                    // Status Indicator
                    if (hasGuessed) {
                        Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                Icons.Default.CheckCircle,
                                contentDescription = "Submitted",
                                modifier = Modifier.padding(4.dp),
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    } else {
                        PulsingIcon(
                            Icons.Default.EditNote,
                            modifier = Modifier.size(24.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Input Field
                OutlinedTextField(
                    value = guess,
                    onValueChange = onGuessChange,
                    label = { Text(label) },
                    placeholder = { Text(placeholder) },
                    keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                    leadingIcon = {
                        Icon(
                            when (keyboardType) {
                                KeyboardType.Decimal -> Icons.Default.Numbers
                                KeyboardType.Number -> Icons.Default.Tag
                                else -> Icons.Default.Edit
                            },
                            contentDescription = null
                        )
                    },
                    trailingIcon = {
                        if (guess.isNotBlank()) {
                            IconButton(onClick = { onGuessChange("") }) {
                                Icon(
                                    Icons.Default.Clear,
                                    contentDescription = "Clear",
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = ExpressiveShapes.CloudShape,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        focusedLabelColor = MaterialTheme.colorScheme.primary
                    ),
                    singleLine = true
                )
            }
        }
    }

    // Track focus state
    LaunchedEffect(guess) {
        isFocused = guess.isNotBlank()
        delay(1000)
        isFocused = false
    }
}

@Composable
fun PlayerResultCard(
    result: PlayerGuess,
    rank: Int,
    isWinner: Boolean
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (isWinner)
            MaterialTheme.colorScheme.primaryContainer
        else
            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        animationSpec = tween(500),
        label = "resultCardColor"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        shape = if (isWinner) ExpressiveShapes.HeartShape else ExpressiveShapes.CloudShape,
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isWinner) 8.dp else 2.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Rank Badge
                Surface(
                    shape = CircleShape,
                    color = when (rank) {
                        1 -> Color(0xFFFFD700) // Gold
                        2 -> Color(0xFFC0C0C0) // Silver
                        3 -> Color(0xFFCD7F32) // Bronze
                        else -> MaterialTheme.colorScheme.outline
                    },
                    modifier = Modifier.size(32.dp)
                ) {
                    Text(
                        if (isWinner) "🏆" else "$rank",
                        modifier = Modifier.wrapContentSize(Alignment.Center),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (rank <= 3) Color.White else MaterialTheme.colorScheme.onSurface
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        result.player.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = if (isWinner) FontWeight.Bold else FontWeight.Medium
                    )
                    Text(
                        "Guess: ${if (result.guess >= 0) String.format("%.3f", result.guess) else "Invalid"}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                if (result.guess >= 0) {
                    Text(
                        if (result.difference == 0.0) "Perfect!"
                        else "±${String.format("%.3f", result.difference)}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (isWinner) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                    )
                    if (isWinner) {
                        Text(
                            "Winner! 🎯",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                } else {
                    Text(
                        "Invalid",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.error,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}


// Score Chip Component for Live Scoreboard
@ExperimentalMaterial3ExpressiveApi
@Composable
fun ScoreChip(
    player: Player,
    rank: Int,
    isLeader: Boolean
) {
    val chipColor by animateColorAsState(
        targetValue = if (isLeader)
            MaterialTheme.colorScheme.primaryContainer
        else
            MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.7f),
        animationSpec = tween(300),
        label = "chipColor"
    )

    val scale by animateFloatAsState(
        targetValue = if (isLeader) 1.1f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "leaderScale"
    )

    // Varied shapes based on rank for visual interest
    val chipShape = when (rank) {
        1 -> RoundedPolygon(MaterialShapes.Heart)
        2 -> RoundedPolygon(MaterialShapes.Gem)
        3 -> RoundedPolygon(MaterialShapes.Diamond)
        4 -> RoundedPolygon(MaterialShapes.Flower)
        5 -> RoundedPolygon(MaterialShapes.Clover4Leaf)
        6 -> RoundedPolygon(MaterialShapes.Burst)
        else -> RoundedPolygon(MaterialShapes.Puffy)
    }

    Surface(
        modifier = Modifier.scale(scale),
        shape = chipShape.toShape(),
        color = chipColor,
        shadowElevation = if (isLeader) 8.dp else 2.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Rank Indicator with expressive shapes
            val rankShape = when (rank) {
                1 -> RoundedPolygon(MaterialShapes.Sunny)
                2 -> RoundedPolygon(MaterialShapes.SemiCircle)
                3 -> RoundedPolygon(MaterialShapes.Triangle)
                else -> CircleShape
            }

            Surface(
                shape = rankShape as Shape,
                color = when (rank) {
                    1 -> Color(0xFFFFD700) // Gold
                    2 -> Color(0xFFC0C0C0) // Silver
                    3 -> Color(0xFFCD7F32) // Bronze
                    else -> MaterialTheme.colorScheme.outline
                },
                modifier = Modifier.size(28.dp)
            ) {
                Text(
                    when (rank) {
                        1 -> "👑"
                        2 -> "🥈"
                        3 -> "🥉"
                        else -> "$rank"
                    },
                    modifier = Modifier.wrapContentSize(Alignment.Center),
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    player.name,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = if (isLeader) FontWeight.Bold else FontWeight.Medium,
                    maxLines = 1
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "${player.score}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (isLeader) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                    )
                    if (player.streak > 1) {
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            "🔥${player.streak}",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    if (player.userProfile != null) {
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("👤", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }

            // Leader Crown Animation
            if (isLeader && rank == 1) {
                Spacer(modifier = Modifier.width(8.dp))
                PulsingIcon(
                    Icons.Default.EmojiEvents,
                    modifier = Modifier.size(20.dp),
                    tint = Color(0xFFFFD700)
                )
            }
        }
    }
}

// User Profile Creation Dialog
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onCreateUser: (String) -> Unit
) {
    if (showDialog) {
        var userName by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = onDismiss,
            title = {
                Text(
                    "Create User Profile 👤",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column {
                    Text("Create a profile to save your stats forever!")
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = userName,
                        onValueChange = { userName = it },
                        label = { Text("Username") },
                        leadingIcon = {
                            Icon(Icons.Default.Person, contentDescription = null)
                        },
                        shape = ExpressiveShapes.CloudShape,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (userName.isNotBlank()) {
                            onCreateUser(userName.trim())
                            onDismiss()
                        }
                    },
                    enabled = userName.isNotBlank(),
                    shape = ExpressiveShapes.BubbleShape
                ) {
                    Text("Create Profile")
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text("Cancel")
                }
            },
            shape = RoundedCornerShape(28.dp)
        )
    }
}

// Main App
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NerdyGamesApp() {
    val navController = rememberNavController()
    var gameSession by remember { mutableStateOf(GameSession()) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        NavHost(
            navController = navController,
            startDestination = "home"
        ) {
            composable("home") {
                HomeScreen(
                    navController = navController,
                    gameSession = gameSession,
                    onGameSessionUpdate = { gameSession = it }
                )
            }
            composable("setup/{gameType}") { backStackEntry ->
                val gameType = backStackEntry.arguments?.getString("gameType") ?: ""
                GameSetupScreen(
                    navController = navController,
                    gameType = gameType,
                    gameSession = gameSession,
                    onGameSessionUpdate = { gameSession = it }
                )
            }
            composable("game/hdi") {
                IndividualHDIGameScreen(
                    navController = navController,
                    gameSession = gameSession,
                    onGameSessionUpdate = { gameSession = it }
                )
            }
            composable("game/gdp_total") {
                IndividualGDPTotalGameScreen(
                    navController = navController,
                    gameSession = gameSession,
                    onGameSessionUpdate = { gameSession = it }
                )
            }
            composable("game/gdp_per_capita") {
                IndividualGDPPerCapitaGameScreen(
                    navController = navController,
                    gameSession = gameSession,
                    onGameSessionUpdate = { gameSession = it }
                )
            }
            composable("game/atomic") {
                IndividualAtomicGameScreen(
                    navController = navController,
                    gameSession = gameSession,
                    onGameSessionUpdate = { gameSession = it }
                )
            }
            composable("results") {
                ResultsScreen(
                    navController = navController,
                    gameSession = gameSession,
                    onNewGame = { gameSession = GameSession() }
                )
            }
            composable("profiles") {
                UserProfilesScreen(navController = navController)
            }
        }
    }
}

// Enhanced Home Screen
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    gameSession: GameSession,
    onGameSessionUpdate: (GameSession) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        PulsingIcon(
                            Icons.Default.Psychology,
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "Nerdy Games",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { navController.navigate("profiles") }) {
                        Icon(Icons.Default.Person, contentDescription = "User Profiles")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            item {
                // Hero Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    shape = ExpressiveShapes.HeartShape,
                    elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("🧠", style = MaterialTheme.typography.displayMedium)
                            Spacer(modifier = Modifier.width(12.dp))
                            Text("🌍", style = MaterialTheme.typography.displayMedium)
                            Spacer(modifier = Modifier.width(12.dp))
                            Text("💰", style = MaterialTheme.typography.displayMedium)
                            Spacer(modifier = Modifier.width(12.dp))
                            Text("⚗️", style = MaterialTheme.typography.displayMedium)
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            "Welcome to Nerdy Games!",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Challenge yourself with knowledge battles! 🎯",
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            item {
                Text(
                    "Choose Your Battle",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            }

            item {
                GameCard(
                    title = "HDI Knowledge",
                    subtitle = "Human Development Index",
                    description = "Turn-based HDI guessing game! 🏆",
                    emoji = "🌍",
                    shape = ExpressiveShapes.CloudShape,
                    onClick = { navController.navigate("setup/hdi") }
                )
            }

            item {
                GameCard(
                    title = "GDP Challenge",
                    subtitle = "Total Economic Output",
                    description = "Guess GDP in billions USD 💵",
                    emoji = "🏭",
                    shape = ExpressiveShapes.DiamondShape,
                    onClick = { navController.navigate("setup/gdp_total") }
                )
            }

            item {
                GameCard(
                    title = "Wealth Per Person",
                    subtitle = "GDP Per Capita",
                    description = "How much does each citizen earn? 💰",
                    emoji = "👤",
                    shape = ExpressiveShapes.StarShape,
                    onClick = { navController.navigate("setup/gdp_per_capita") }
                )
            }

            item {
                GameCard(
                    title = "Atomic Showdown",
                    subtitle = "Element Mastery",
                    description = "Test your periodic table knowledge ⚗️",
                    emoji = "🧪",
                    shape = ExpressiveShapes.BubbleShape,
                    onClick = { navController.navigate("setup/atomic") }
                )
            }

            item {
                // Quick Stats Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                    ),
                    shape = RoundedCornerShape(
                        topStart = 32.dp,
                        topEnd = 8.dp,
                        bottomStart = 8.dp,
                        bottomEnd = 32.dp
                    )
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            "Global Stats 📊",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            StatBubble("${UserManager.getAllUsers().size}", "Players")
                            StatBubble("${UserManager.getAllUsers().sumOf { it.totalGames }}", "Games")
                            StatBubble("${UserManager.getAllUsers().maxOfOrNull { it.bestStreak } ?: 0}", "Best Streak")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun GameCard(
    title: String,
    subtitle: String,
    description: String,
    emoji: String,
    shape: RoundedCornerShape,
    onClick: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "cardPress"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale),
        onClick = {
            isPressed = true
            onClick()
        },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        ),
        shape = shape,
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        emoji,
                        style = MaterialTheme.typography.headlineLarge
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            title,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            subtitle,
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    description,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    Icons.Default.PlayArrow,
                    contentDescription = null,
                    modifier = Modifier.padding(8.dp),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }

    LaunchedEffect(isPressed) {
        if (isPressed) {
            delay(150)
            isPressed = false
        }
    }
}

@Composable
fun StatBubble(value: String, label: String) {
    Surface(
        shape = ExpressiveShapes.BubbleShape,
        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f),
        modifier = Modifier.padding(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                value,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
    }
}

// Enhanced Game Setup Screen with ButtonGroup
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun GameSetupScreen(
    navController: NavController,
    gameType: String,
    gameSession: GameSession,
    onGameSessionUpdate: (GameSession) -> Unit
) {
    var newPlayerName by remember { mutableStateOf("") }
    var selectedDifficulty by remember { mutableStateOf(Difficulty.NORMAL) }
    var hasTimer by remember { mutableStateOf(false) }
    var timerSeconds by remember { mutableStateOf(30) }
    var showUserDialog by remember { mutableStateOf(false) }

    val updatedSession = gameSession.copy(
        gameType = gameType,
        difficulty = selectedDifficulty,
        hasTimer = hasTimer,
        timerSeconds = timerSeconds
    )

    val gameInfo = when (gameType) {
        "hdi" -> Triple("HDI Knowledge Battle 🌍", "🎯 Each player gets a turn\n🏆 Timer resets for each player\n📊 Guess HDI values (0.000-1.000)", "🌍")
        "gdp_total" -> Triple("GDP Total Challenge 🏭", "💰 Guess total GDP in billions USD\n🏆 Each player gets their own timer\n📈 Economic powerhouse knowledge", "🏭")
        "gdp_per_capita" -> Triple("GDP Per Capita Battle 👤", "💸 Guess GDP per capita in USD\n🏆 Turn-based with timer reset\n💰 Individual wealth knowledge", "👤")
        "atomic" -> Triple("Atomic Showdown ⚗️", "🧪 Identify chemical elements\n⚡ Exact atomic numbers required\n🔬 Test your chemistry knowledge", "⚗️")
        else -> Triple("Unknown Game", "Unknown game type", "❓")
    }

    UserProfileDialog(
        showDialog = showUserDialog,
        onDismiss = { showUserDialog = false },
        onCreateUser = { userName ->
            val newUser = UserProfile(name = userName)
            UserManager.saveUser(newUser)
            val newPlayer = Player(name = userName, userProfile = newUser)
            updatedSession.players.add(newPlayer)
            onGameSessionUpdate(updatedSession)
        }
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Game Setup ${gameInfo.third}",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
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
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Game Info Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer
                    ),
                    shape = ExpressiveShapes.HeartShape
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            gameInfo.first,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            gameInfo.second,
                            style = MaterialTheme.typography.bodyLarge,
                            lineHeight = MaterialTheme.typography.bodyLarge.lineHeight * 1.4
                        )
                    }
                }
            }

            // Difficulty Selection with ButtonGroup
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = ExpressiveShapes.CloudShape
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            "Choose Difficulty 🎚️",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(ButtonGroupDefaults.ConnectedSpaceBetween)
                        ) {
                            Difficulty.values().forEachIndexed { index, difficulty ->
                                ToggleButton(
                                    checked = selectedDifficulty == difficulty,
                                    onCheckedChange = { selectedDifficulty = difficulty },
                                    modifier = Modifier
                                        .weight(1f)
                                        .semantics { role = Role.RadioButton },
                                    shapes = when (index) {
                                        0 -> ButtonGroupDefaults.connectedLeadingButtonShapes()
                                        Difficulty.values().lastIndex -> ButtonGroupDefaults.connectedTrailingButtonShapes()
                                        else -> ButtonGroupDefaults.connectedMiddleButtonShapes()
                                    }
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text(
                                            difficulty.emoji,
                                            style = MaterialTheme.typography.titleMedium
                                        )
                                        Spacer(modifier = Modifier.size(ToggleButtonDefaults.IconSpacing))
                                        Text(
                                            difficulty.displayName,
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Game Options
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = ExpressiveShapes.DiamondShape
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            "Game Options ⚙️",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Enable Timer ⏱️")
                            Switch(
                                checked = hasTimer,
                                onCheckedChange = { hasTimer = it }
                            )
                        }

                        if (hasTimer) {
                            Spacer(modifier = Modifier.height(16.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Timer Duration")
                                Text(
                                    "${timerSeconds}s",
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Slider(
                                value = timerSeconds.toFloat(),
                                onValueChange = { timerSeconds = it.toInt() },
                                valueRange = 10f..60f,
                                steps = 9
                            )
                        }
                    }
                }
            }

            // Players Section
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = ExpressiveShapes.StarShape
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            "Add Players 👥",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedTextField(
                                value = newPlayerName,
                                onValueChange = { newPlayerName = it },
                                label = { Text("Player Name") },
                                leadingIcon = {
                                    Icon(Icons.Default.Person, contentDescription = null)
                                },
                                modifier = Modifier.weight(1f),
                                shape = ExpressiveShapes.BubbleShape
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            FilledTonalButton(
                                onClick = {
                                    if (newPlayerName.isNotBlank()) {
                                        val existingUser = UserManager.getAllUsers()
                                            .find { it.name.equals(newPlayerName.trim(), ignoreCase = true) }

                                        val player = if (existingUser != null) {
                                            Player(name = existingUser.name, userProfile = existingUser)
                                        } else {
                                            Player(name = newPlayerName.trim())
                                        }

                                        updatedSession.players.add(player)
                                        onGameSessionUpdate(updatedSession)
                                        newPlayerName = ""
                                    }
                                },
                                enabled = newPlayerName.isNotBlank(),
                                shape = CircleShape
                            ) {
                                Icon(Icons.Default.Add, contentDescription = null)
                            }
                            Spacer(modifier = Modifier.width(4.dp))
                            OutlinedIconButton(
                                onClick = { showUserDialog = true },
                                modifier = Modifier.size(40.dp)
                            ) {
                                Icon(
                                    Icons.Default.PersonAdd,
                                    contentDescription = "Create Profile",
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }

                        if (updatedSession.players.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                "Players (${updatedSession.players.size}) 🎮",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Medium
                            )

                            updatedSession.players.forEachIndexed { index, player ->
                                EnhancedPlayerCard(
                                    player = player,
                                    position = index + 1,
                                    onRemove = {
                                        updatedSession.players.remove(player)
                                        onGameSessionUpdate(updatedSession)
                                    }
                                )
                            }
                        }
                    }
                }
            }

            // Start Game Button
            item {
                Button(
                    onClick = {
                        onGameSessionUpdate(updatedSession)
                        navController.navigate("game/$gameType")
                    },
                    enabled = updatedSession.players.isNotEmpty(),
                    modifier = Modifier.fillMaxWidth(),
                    shape = ExpressiveShapes.BubbleShape
                ) {
                    Text(
                        "Start Battle! 🚀",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                if (updatedSession.players.isEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "⚠️ Add at least 1 player to start",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
fun EnhancedPlayerCard(
    player: Player,
    position: Int,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (player.userProfile != null)
                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f)
            else
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ),
        shape = ExpressiveShapes.BubbleShape
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = ExpressiveShapes.Cookie7Sided,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(32.dp)
            ) {
                Text(
                    "$position",
                    modifier = Modifier.wrapContentSize(Alignment.Center),
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        player.name,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )
                    if (player.userProfile != null) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("👤", style = MaterialTheme.typography.bodyMedium)
                    }
                }
                if (player.userProfile != null) {
                    Text(
                        "Games: ${player.userProfile.totalGames} | Best Streak: ${player.userProfile.bestStreak}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            }
            IconButton(
                onClick = onRemove,
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Remove player",
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

// HDI Game Screen - Back to Simultaneous Competitive Format
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun IndividualHDIGameScreen(
    navController: NavController,
    gameSession: GameSession,
    onGameSessionUpdate: (GameSession) -> Unit
) {
    var currentCountry by remember { mutableStateOf(
        GameManager.getRandomCountries(1, gameSession.difficulty).first()
    ) }
    var playerGuesses by remember { mutableStateOf(mutableMapOf<String, String>()) }
    var showResults by remember { mutableStateOf(false) }
    var questionResults by remember { mutableStateOf<List<PlayerGuess>>(emptyList()) }
    var questionsAnswered by remember { mutableStateOf(0) }
    var timeLeft by remember { mutableStateOf(gameSession.timerSeconds) }
    var isTimerActive by remember { mutableStateOf(false) }

    fun submitAllGuesses() {
        val results = gameSession.players.map { player ->
            val guessText = playerGuesses[player.id] ?: ""
            val guess = guessText.toDoubleOrNull() ?: -1.0
            val difference = if (guess >= 0 && guess <= 1.0) abs(guess - currentCountry.hdi) else Double.MAX_VALUE

            PlayerGuess(player, guess, difference)
        }.sortedBy { it.difference }

        // Mark the closest guess as winner (if valid)
        if (results.isNotEmpty() && results.first().guess >= 0) {
            val winner = results.first()
            winner.player.score++
            winner.player.totalCorrect++
            winner.player.streak++
            questionResults = results.mapIndexed { index, result ->
                result.copy(isClosest = index == 0)
            }
        } else {
            questionResults = results
        }

        showResults = true
        isTimerActive = false
        onGameSessionUpdate(gameSession)
    }

    // Fixed Timer Logic
    LaunchedEffect(isTimerActive, timeLeft) {
        if (gameSession.hasTimer && isTimerActive && timeLeft > 0) {
            delay(1000L)
            timeLeft--
        } else if (timeLeft <= 0 && isTimerActive) {
            isTimerActive = false
            submitAllGuesses()
        }
    }



    fun nextQuestion() {
        questionsAnswered++
        if (questionsAnswered >= gameSession.totalQuestions) {
            // Update user profiles
            gameSession.players.forEach { player ->
                player.userProfile?.let { profile ->
                    UserManager.updateUserStats(
                        userId = profile.id,
                        gameType = "hdi",
                        won = player.score > 0,
                        score = player.score,
                        streak = player.streak
                    )
                }
            }
            navController.navigate("results")
        } else {
            currentCountry = GameManager.getRandomCountries(1, gameSession.difficulty).first()
            playerGuesses = mutableMapOf()
            showResults = false
            timeLeft = gameSession.timerSeconds
            questionResults = emptyList()
            isTimerActive = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("HDI Battle Royale 🌍") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
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
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Progress and Timer
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Card(
                        shape = ExpressiveShapes.CloudShape,
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                "Question ${questionsAnswered + 1}/${gameSession.totalQuestions}",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            LinearProgressIndicator(
                                progress = { questionsAnswered.toFloat() / gameSession.totalQuestions },
                                modifier = Modifier.width(120.dp)
                            )
                        }
                    }

                    if (gameSession.hasTimer && !showResults) {
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = if (timeLeft <= 10)
                                    MaterialTheme.colorScheme.errorContainer
                                else
                                    MaterialTheme.colorScheme.tertiaryContainer
                            ),
                            shape = ExpressiveShapes.StarShape
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Default.Timer,
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    "${timeLeft}s",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            // Country Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    ),
                    shape = ExpressiveShapes.BubbleShape,
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            currentCountry.emoji,
                            style = MaterialTheme.typography.displayLarge
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            currentCountry.name,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "What's the HDI? 📊",
                            style = MaterialTheme.typography.titleLarge,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            "(Range: 0.000 - 1.000)",
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )

                        if (currentCountry.funFact.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(12.dp))
                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f)
                                ),
                                shape = ExpressiveShapes.CloudShape
                            ) {
                                Text(
                                    "💡 ${currentCountry.funFact}",
                                    style = MaterialTheme.typography.bodySmall,
                                    modifier = Modifier.padding(12.dp),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }

            if (showResults) {
                // Results Display
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        ),
                        shape = ExpressiveShapes.HeartShape
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Text(
                                "Round Results! 🎯",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                "Actual HDI: ${String.format("%.3f", currentCountry.hdi)}",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            questionResults.forEachIndexed { index, result ->
                                PlayerResultCard(
                                    result = result,
                                    rank = index + 1,
                                    isWinner = result.isClosest
                                )
                            }
                        }
                    }
                }

                item {
                    Button(
                        onClick = { nextQuestion() },
                        modifier = Modifier.fillMaxWidth(),
                        shape = ExpressiveShapes.BubbleShape
                    ) {
                        Text(
                            if (questionsAnswered >= gameSession.totalQuestions - 1)
                                "View Final Results! 🏆"
                            else
                                "Next Question! ⚡",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            } else {
                // Enhanced Input Section for All Players
                item {
                    Text(
                        "All Players Submit Your Guesses! 🎮",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Quick Value Selector for HDI
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = ExpressiveShapes.CloudShape
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                "Quick Select Common Values:",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(listOf(0.300, 0.500, 0.700, 0.850, 0.950)) { value ->
                                    FilterChip(
                                        onClick = {
                                            // Set this value for players who haven't guessed yet
                                            gameSession.players.forEach { player ->
                                                if (playerGuesses[player.id].isNullOrBlank()) {
                                                    playerGuesses = playerGuesses.toMutableMap().apply {
                                                        put(player.id, String.format("%.3f", value))
                                                    }
                                                }
                                            }
                                        },
                                        label = {
                                            Text(String.format("%.3f", value))
                                        },
                                        selected = false,
                                        colors = FilterChipDefaults.filterChipColors(
                                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                                            selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                                        )
                                    )
                                }
                            }
                        }
                    }
                }

                // All Player Input Cards
                items(gameSession.players) { player ->
                    EnhancedPlayerGuessCard(
                        player = player,
                        guess = playerGuesses[player.id] ?: "",
                        onGuessChange = { guess ->
                            playerGuesses = playerGuesses.toMutableMap().apply {
                                put(player.id, guess)
                            }
                        },
                        placeholder = "e.g., 0.850",
                        label = "HDI Guess",
                        keyboardType = KeyboardType.Decimal
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { submitAllGuesses() },
                        enabled = playerGuesses.size == gameSession.players.size &&
                                playerGuesses.values.all { it.isNotBlank() },
                        modifier = Modifier.fillMaxWidth(),
                        shape = ExpressiveShapes.BubbleShape
                    ) {
                        Text("Submit All Guesses! 🚀", fontWeight = FontWeight.Bold)
                    }

                    // Start timer when input phase begins
                    if (!isTimerActive && gameSession.hasTimer && !showResults) {
                        LaunchedEffect(Unit) {
                            timeLeft = gameSession.timerSeconds
                            isTimerActive = true
                        }
                    }
                }
            }

            // Live Scoreboard
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                    ),
                    shape = ExpressiveShapes.DiamondShape
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            "Live Scoreboard 📊",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            itemsIndexed(gameSession.players.sortedByDescending { it.score }) { index, player ->
                                ScoreChip(
                                    player = player,
                                    rank = index + 1,
                                    isLeader = index == 0
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// Similarly implemented GDP games with individual turns...
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IndividualGDPTotalGameScreen(
    navController: NavController,
    gameSession: GameSession,
    onGameSessionUpdate: (GameSession) -> Unit
) {
    // Similar implementation to HDI game but for GDP Total
    Text("GDP Total Game - Individual turns implementation")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IndividualGDPPerCapitaGameScreen(
    navController: NavController,
    gameSession: GameSession,
    onGameSessionUpdate: (GameSession) -> Unit
) {
    // Similar implementation to HDI game but for GDP Per Capita
    Text("GDP Per Capita Game - Individual turns implementation")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IndividualAtomicGameScreen(
    navController: NavController,
    gameSession: GameSession,
    onGameSessionUpdate: (GameSession) -> Unit
) {
    // Individual atomic number game implementation
    Text("Atomic Number Game - Individual turns implementation")
}

// Results Screen
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultsScreen(
    navController: NavController,
    gameSession: GameSession,
    onNewGame: () -> Unit
) {
    val sortedPlayers = gameSession.players.sortedByDescending { it.score }
    var showConfetti by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        delay(3000)
        showConfetti = false
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Battle Results! 🏆") }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            item {
                // Winner Celebration
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    shape = ExpressiveShapes.HeartShape,
                    elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if (showConfetti) {
                            Text("🎉🏆🎉", style = MaterialTheme.typography.displayLarge)
                        } else {
                            Text("🏆", style = MaterialTheme.typography.displayLarge)
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            "${sortedPlayers.firstOrNull()?.name ?: "Nobody"} Wins!",
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            "🎮 ${gameSession.totalQuestions} questions each • ${gameSession.difficulty.emoji} ${gameSession.difficulty.displayName}",
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            }

            item {
                Text(
                    "Final Rankings 📊",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            }

            items(sortedPlayers) { player ->
                val position = sortedPlayers.indexOf(player) + 1
                FinalResultCard(
                    player = player,
                    position = position,
                    totalQuestions = gameSession.totalQuestions,
                    isWinner = position == 1
                )
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            onNewGame()
                            navController.navigate("home") {
                                popUpTo("home") { inclusive = true }
                            }
                        },
                        modifier = Modifier.weight(1f),
                        shape = ExpressiveShapes.CloudShape
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("New Game")
                    }

                    Button(
                        onClick = {
                            navController.navigate("home") {
                                popUpTo("home") { inclusive = true }
                            }
                        },
                        modifier = Modifier.weight(1f),
                        shape = ExpressiveShapes.BubbleShape
                    ) {
                        Text("Home 🏠")
                    }
                }
            }
        }
    }
}

@Composable
fun FinalResultCard(
    player: Player,
    position: Int,
    totalQuestions: Int,
    isWinner: Boolean
) {
    val scale by animateFloatAsState(
        targetValue = if (isWinner) 1.05f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "winnerScale"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale),
        colors = CardDefaults.cardColors(
            containerColor = when (position) {
                1 -> MaterialTheme.colorScheme.tertiaryContainer
                2 -> MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.7f)
                3 -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                else -> MaterialTheme.colorScheme.surfaceVariant
            }
        ),
        shape = when (position) {
            1 -> ExpressiveShapes.HeartShape
            2 -> ExpressiveShapes.StarShape
            3 -> ExpressiveShapes.DiamondShape
            else -> ExpressiveShapes.CloudShape
        },
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isWinner) 8.dp else 4.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = when (position) {
                        1 -> ExpressiveShapes.HeartShape
                        2 -> ExpressiveShapes.StarShape
                        3 -> ExpressiveShapes.DiamondShape
                        else -> CircleShape
                    },
                    color = when (position) {
                        1 -> Color(0xFFFFD700) // Gold
                        2 -> Color(0xFFC0C0C0) // Silver
                        3 -> Color(0xFFCD7F32) // Bronze
                        else -> MaterialTheme.colorScheme.outline
                    },
                    modifier = Modifier.size(48.dp)
                ) {
                    Text(
                        when (position) {
                            1 -> "👑"
                            2 -> "🥈"
                            3 -> "🥉"
                            else -> "$position"
                        },
                        modifier = Modifier.wrapContentSize(Alignment.Center),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            player.name,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = if (isWinner) FontWeight.Bold else FontWeight.Medium
                        )
                        if (player.userProfile != null) {
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("👤", style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                    Text(
                        "${String.format("%.1f", (player.score.toDouble() / totalQuestions) * 100)}% accuracy",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    "${player.score}",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "/ $totalQuestions",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
        }
    }
}

// User Profiles Screen
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfilesScreen(
    navController: NavController
) {
    val users = UserManager.getAllUsers()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("User Profiles 👥") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
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
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    shape = ExpressiveShapes.HeartShape
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("👥", style = MaterialTheme.typography.displayMedium)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            "Saved Players",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "Your stats are saved forever!",
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            if (users.isEmpty()) {
                item {
                    Text(
                        "No saved players yet. Create a profile when setting up a game!",
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            } else {
                items(users.sortedByDescending { it.totalScore }) { user ->
                    UserProfileCard(user = user)
                }
            }
        }
    }
}

@Composable
fun UserProfileCard(user: UserProfile) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        ),
        shape = ExpressiveShapes.DiamondShape
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = ExpressiveShapes.HeartShape,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(40.dp)
                    ) {
                        Text(
                            user.name.take(2).uppercase(),
                            modifier = Modifier.wrapContentSize(Alignment.Center),
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            user.name,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "Level ${user.totalScore / 10 + 1}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
                Text(
                    "👤",
                    style = MaterialTheme.typography.headlineMedium
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatBubble("${user.totalGames}", "Games")
                StatBubble("${user.totalWins}", "Wins")
                StatBubble("${user.bestStreak}", "Best Streak")
                StatBubble("${user.totalScore}", "Total Score")
            }

            if (user.totalGames > 0) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    "Win Rate: ${String.format("%.1f", (user.totalWins.toDouble() / user.totalGames) * 100)}%",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}