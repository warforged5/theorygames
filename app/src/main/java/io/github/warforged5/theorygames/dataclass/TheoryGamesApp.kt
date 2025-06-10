package io.github.warforged5.theorygames.dataclass

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import io.github.warforged5.theorygames.ui.theme.TheorygamesTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TheoryGamesApp() {
    val navController = rememberNavController()
    val gameViewModel: GameViewModel = viewModel()
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            TheoryGamesNavigation(
                navController = navController,
                gameViewModel = gameViewModel
            )
        }
}

@Composable
fun TheoryGamesNavigation(
    navController: NavHostController,
    gameViewModel: GameViewModel
) {
    NavHost(
        navController = navController,
        startDestination = "main_menu"
    ) {
        composable("main_menu") {
            MainMenuScreen(
                onNavigateToSetup = { navController.navigate("game_setup") },
                onNavigateToRules = { navController.navigate("rules") }
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
            ResultsScreen(
                gameViewModel = gameViewModel,
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
    }
}