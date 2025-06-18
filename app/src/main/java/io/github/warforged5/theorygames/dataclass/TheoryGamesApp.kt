package io.github.warforged5.theorygames.dataclass

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import io.github.warforged5.theorygames.ui.theme.TheoryGamesTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TheoryGamesApp() {
    val context = LocalContext.current
    val navController = rememberNavController()
    val gameViewModel: GameViewModel = viewModel()

    val profileManager = remember { ProfileManager(context) }
    val settingsManager = remember { SettingsManager(context) }

    val settings by settingsManager.settingsFlow.collectAsState(initial = AppSettings())

    LaunchedEffect(Unit) {
        gameViewModel.setProfileManager(profileManager)
    }

    TheoryGamesTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            TheoryGamesNavigation(
                navController = navController,
                gameViewModel = gameViewModel,
                profileManager = profileManager,
                settingsManager = settingsManager,
                currentSettings = settings
            )
        }
    }
}

@Composable
fun TheoryGamesNavigation(
    navController: NavHostController,
    gameViewModel: GameViewModel,
    profileManager: ProfileManager,
    settingsManager: SettingsManager,
    currentSettings: AppSettings
) {
    val scope = rememberCoroutineScope()

    NavHost(
        navController = navController,
        startDestination = "main_menu"
    ) {
        composable("main_menu") {
            MainMenuScreen(
                onNavigateToSetup = { navController.navigate("game_setup") },
                onNavigateToRules = { navController.navigate("rules") },
                onNavigateToProfiles = { navController.navigate("profiles") },
                onNavigateToSettings = { navController.navigate("settings") }
            )
        }

        composable("game_setup") {
            GameSetupScreen(
                gameViewModel = gameViewModel,
                onNavigateToGame = { navController.navigate("gameplay") },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable("gameplay") {
            GameplayScreen(
                gameViewModel = gameViewModel,
                onNavigateToResults = { navController.navigate("results") },
                onNavigateToMenu = {
                    navController.navigate("main_menu") {
                        popUpTo("main_menu") { inclusive = true }
                    }
                }
            )
        }

        composable("results") {
            // FIXED: Added missing profileManager parameter
            ResultsScreen(
                gameViewModel = gameViewModel,
                profileManager = profileManager,
                onNavigateToMenu = {
                    navController.navigate("main_menu") {
                        popUpTo("main_menu") { inclusive = true }
                    }
                },
                onPlayAgain = {
                    gameViewModel.resetGame()
                    navController.navigate("game_setup") {
                        popUpTo("game_setup") { inclusive = true }
                    }
                }
            )
        }

        composable("rules") {
            RulesScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable("profiles") {
            ProfileManagementScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable("settings") {
            SettingsScreen(
                onNavigateBack = { navController.popBackStack() },
                currentTheme = currentSettings.selectedTheme,
                currentDarkMode = currentSettings.isDarkMode,
                onThemeChanged = { theme ->
                    scope.launch {
                        settingsManager.updateTheme(theme)
                    }
                },
                onDarkModeChanged = { isDark ->
                    scope.launch {
                        settingsManager.updateDarkMode(isDark)
                    }
                }
            )
        }
    }
}