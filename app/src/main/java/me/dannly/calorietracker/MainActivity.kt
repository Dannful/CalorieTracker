package me.dannly.calorietracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import me.dannly.calorietracker.ui.theme.CalorieTrackerTheme
import me.dannly.core.domain.preferences.Preferences
import me.dannly.calorietracker.navigation.Route
import me.dannly.onboarding_presentation.activity.ActivityScreen
import me.dannly.onboarding_presentation.age.AgeScreen
import me.dannly.onboarding_presentation.gender.GenderScreen
import me.dannly.onboarding_presentation.goal.GoalScreen
import me.dannly.onboarding_presentation.height.HeightScreen
import me.dannly.onboarding_presentation.nutrient_goals.NutrientGoalsScreen
import me.dannly.onboarding_presentation.weight.WeightScreen
import me.dannly.onboarding_presentation.welcome.WelcomeScreen
import me.dannly.tracker_presentation.search.SearchScreen
import me.dannly.tracker_presentation.tracker_overview.TrackerOverviewScreen
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var preferences: Preferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val shouldShowOnboarding = preferences.loadShouldShowOnboarding()
        setContent {
            CalorieTrackerTheme {
                val navController = rememberNavController()
                val scaffoldState = rememberScaffoldState()
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    scaffoldState = scaffoldState
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = if(shouldShowOnboarding) Route.WELCOME else Route.TRACKER_OVERVIEW
                    ) {
                        composable(Route.WELCOME) {
                            WelcomeScreen {
                                navController.navigate(Route.GENDER)
                            }
                        }
                        composable(Route.GENDER) {
                            GenderScreen {
                                navController.navigate(Route.AGE)
                            }
                        }
                        composable(Route.AGE) {
                            AgeScreen {
                                navController.navigate(Route.HEIGHT)
                            }
                        }
                        composable(Route.HEIGHT) {
                            HeightScreen {
                                navController.navigate(Route.WEIGHT)
                            }
                        }
                        composable(Route.WEIGHT) {
                            WeightScreen {
                                navController.navigate(Route.ACTIVITY)
                            }
                        }
                        composable(Route.ACTIVITY) {
                            ActivityScreen {
                                navController.navigate(Route.GOAL)
                            }
                        }
                        composable(Route.GOAL) {
                            GoalScreen {
                                navController.navigate(Route.NUTRIENT_GOAL)
                            }
                        }
                        composable(Route.NUTRIENT_GOAL) {
                            NutrientGoalsScreen {
                                navController.navigate(Route.TRACKER_OVERVIEW)
                            }
                        }
                        composable(Route.TRACKER_OVERVIEW) {
                            TrackerOverviewScreen { mealType, dayOfMonth, month, year ->
                                navController.navigate("${Route.SEARCH}/$mealType/$dayOfMonth/$month/$year")
                            }
                        }
                        composable(route = "${Route.SEARCH}/{mealName}/{dayOfMonth}/{month}/{year}", arguments = listOf(
                            navArgument("mealName") {
                                type = NavType.StringType
                            },
                            navArgument("dayOfMonth") {
                                type = NavType.IntType
                            },
                            navArgument("month") {
                                type = NavType.IntType
                            },
                            navArgument("year") {
                                type = NavType.IntType
                            }
                        )) {
                            SearchScreen(
                                scaffoldState = scaffoldState,
                                mealName = it.arguments?.getString("mealName")!!,
                                dayOfMonth = it.arguments?.getInt("dayOfMonth")!!,
                                month = it.arguments?.getInt("month")!!,
                                year = it.arguments?.getInt("year")!!
                            ) {
                                navController.navigateUp()
                            }
                        }
                    }
                }
            }
        }
    }
}