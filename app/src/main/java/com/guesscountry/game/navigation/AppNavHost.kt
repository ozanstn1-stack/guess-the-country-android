package com.guesscountry.game.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.guesscountry.game.data.local.PlayerRepository
import com.guesscountry.game.data.model.Difficulty
import com.guesscountry.game.data.model.QuestionType
import com.guesscountry.game.data.repository.CountryRepository
import com.guesscountry.game.domain.model.GameSessionConfig
import com.guesscountry.game.ui.PlayerViewModel
import com.guesscountry.game.ui.about.AboutScreen
import com.guesscountry.game.ui.achievements.AchievementsScreen
import com.guesscountry.game.ui.difficulty.DifficultySelectionScreen
import com.guesscountry.game.ui.game.GameScreen
import com.guesscountry.game.ui.game.GameViewModel
import com.guesscountry.game.ui.home.HomeScreen
import com.guesscountry.game.ui.mode.ModeSelectionScreen
import com.guesscountry.game.ui.result.ResultScreen
import com.guesscountry.game.ui.settings.SettingsScreen
import com.guesscountry.game.ui.statistics.StatisticsScreen
import com.guesscountry.game.ui.splash.SplashScreen
import java.time.LocalDate

object Routes {
    const val SPLASH = "splash"
    const val HOME = "home"
    const val MODES = "modes"
    const val STATISTICS = "statistics"
    const val ACHIEVEMENTS = "achievements"
    const val SETTINGS = "settings"
    const val ABOUT = "about"
    const val RESULT = "result"
    const val DIFFICULTY = "difficulty/{mode}"
    const val GAME = "game/{mode}/{difficulty}"
    const val DAILY = "daily/{seed}"

    fun difficulty(mode: QuestionType): String = "difficulty/${mode.key}"
    fun game(mode: QuestionType, difficulty: Difficulty): String = "game/${mode.key}/${difficulty.key}"
    fun daily(seed: Long): String = "daily/$seed"
}

@Composable
fun GuessCountryApp(
    countryRepository: CountryRepository,
    playerRepository: PlayerRepository,
) {
    val navController = rememberNavController()
    val playerViewModel: PlayerViewModel = viewModel(
        factory = PlayerViewModel.Factory(playerRepository),
    )
    val gameViewModel: GameViewModel = viewModel(
        factory = GameViewModel.Factory(countryRepository, playerRepository),
    )
    val playerProgress = playerViewModel.progress.collectAsStateWithLifecycle().value

    NavHost(
        navController = navController,
        startDestination = Routes.SPLASH,
        modifier = Modifier,
    ) {
        composable(Routes.SPLASH) {
            SplashScreen {
                navController.navigate(Routes.HOME) {
                    popUpTo(Routes.SPLASH) { inclusive = true }
                }
            }
        }
        composable(Routes.HOME) {
            HomeScreen(
                onPlay = { navController.navigate(Routes.MODES) },
                onDaily = { navController.navigate(Routes.daily(LocalDate.now().toEpochDay())) },
                onMode = { mode -> navController.navigate(Routes.difficulty(mode)) },
                onStatistics = { navController.navigate(Routes.STATISTICS) },
                onAchievements = { navController.navigate(Routes.ACHIEVEMENTS) },
                onSettings = { navController.navigate(Routes.SETTINGS) },
                onAbout = { navController.navigate(Routes.ABOUT) },
            )
        }
        composable(Routes.MODES) {
            ModeSelectionScreen(
                onBack = { navController.popBackStack() },
                onModeSelected = { mode -> navController.navigate(Routes.difficulty(mode)) },
            )
        }
        composable(
            route = Routes.DIFFICULTY,
            arguments = listOf(navArgument("mode") { type = NavType.StringType }),
        ) { entry ->
            val mode = QuestionType.fromKey(entry.arguments?.getString("mode").orEmpty())
            DifficultySelectionScreen(
                mode = mode,
                progress = playerProgress,
                onBack = { navController.popBackStack() },
                onDifficultySelected = { difficulty ->
                    navController.navigate(Routes.game(mode, difficulty))
                },
            )
        }
        composable(
            route = Routes.GAME,
            arguments = listOf(
                navArgument("mode") { type = NavType.StringType },
                navArgument("difficulty") { type = NavType.StringType },
            ),
        ) { entry ->
            val mode = QuestionType.fromKey(entry.arguments?.getString("mode").orEmpty())
            val difficulty = Difficulty.fromKey(entry.arguments?.getString("difficulty").orEmpty())
            LaunchedEffect(mode, difficulty) {
                gameViewModel.configure(GameSessionConfig(mode, difficulty))
            }
            val state by gameViewModel.uiState.collectAsStateWithLifecycle()
            LaunchedEffect(state.result) {
                if (state.result != null) {
                    navController.navigate(Routes.RESULT) {
                        popUpTo(Routes.GAME) { inclusive = true }
                    }
                }
            }
            GameScreen(
                state = state,
                onAnswer = gameViewModel::submitAnswer,
                onBack = { navController.popBackStack() },
                onRetry = gameViewModel::restart,
            )
        }
        composable(
            route = Routes.DAILY,
            arguments = listOf(navArgument("seed") { type = NavType.LongType }),
        ) { entry ->
            val seed = entry.arguments?.getLong("seed") ?: LocalDate.now().toEpochDay()
            LaunchedEffect(seed) {
                gameViewModel.configure(
                    GameSessionConfig(
                        mode = QuestionType.FLAG,
                        difficulty = Difficulty.MEDIUM,
                        dailySeed = seed,
                    ),
                )
            }
            val state by gameViewModel.uiState.collectAsStateWithLifecycle()
            LaunchedEffect(state.result) {
                if (state.result != null) {
                    navController.navigate(Routes.RESULT) {
                        popUpTo(Routes.DAILY) { inclusive = true }
                    }
                }
            }
            GameScreen(
                state = state,
                onAnswer = gameViewModel::submitAnswer,
                onBack = { navController.popBackStack() },
                onRetry = gameViewModel::restart,
            )
        }
        composable(Routes.RESULT) {
            val state by gameViewModel.uiState.collectAsStateWithLifecycle()
            ResultScreen(
                result = state.result,
                onPlayAgain = {
                    val config = state.config
                    gameViewModel.prepareReplay()
                    val destination = if (config.isDaily && config.dailySeed != null) {
                        Routes.daily(config.dailySeed)
                    } else {
                        Routes.game(config.mode, config.difficulty)
                    }
                    navController.navigate(destination) {
                        popUpTo(Routes.RESULT) { inclusive = true }
                    }
                },
                onHome = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.HOME) { inclusive = false }
                        launchSingleTop = true
                    }
                },
            )
        }
        composable(Routes.STATISTICS) {
            StatisticsScreen(progress = playerProgress, onBack = { navController.popBackStack() })
        }
        composable(Routes.ACHIEVEMENTS) {
            AchievementsScreen(progress = playerProgress, onBack = { navController.popBackStack() })
        }
        composable(Routes.SETTINGS) {
            SettingsScreen(
                progress = playerProgress,
                onSoundChanged = playerViewModel::setSoundEnabled,
                onVibrationChanged = playerViewModel::setVibrationEnabled,
                onBack = { navController.popBackStack() },
            )
        }
        composable(Routes.ABOUT) {
            AboutScreen(onBack = { navController.popBackStack() })
        }
    }
}
