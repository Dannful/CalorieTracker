package me.dannly.calorietracker

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.InternalPlatformDsl.toStr
import io.mockk.every
import io.mockk.mockk
import me.dannly.calorietracker.navigation.Route
import me.dannly.calorietracker.repository.TrackerRepositoryFake
import me.dannly.calorietracker.ui.theme.CalorieTrackerTheme
import me.dannly.core.domain.model.ActivityLevel
import me.dannly.core.domain.model.Gender
import me.dannly.core.domain.model.GoalType
import me.dannly.core.domain.model.UserInfo
import me.dannly.core.domain.preferences.Preferences
import me.dannly.core.domain.use_case.FilterOutDigits
import me.dannly.tracker_domain.model.TrackableFood
import me.dannly.tracker_domain.use_case.*
import me.dannly.tracker_presentation.search.SearchScreen
import me.dannly.tracker_presentation.search.SearchViewModel
import me.dannly.tracker_presentation.tracker_overview.TrackerOverviewScreen
import me.dannly.tracker_presentation.tracker_overview.TrackerOverviewViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.math.roundToInt

@HiltAndroidTest
class TrackerOverviewE2E {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    private lateinit var repositoryFake: TrackerRepositoryFake
    private lateinit var trackerUseCases: TrackerUseCases
    private lateinit var preferences: Preferences
    private lateinit var trackerOverviewViewModel: TrackerOverviewViewModel
    private lateinit var searchViewModel: SearchViewModel
    private lateinit var navController: NavHostController

    @Before
    fun setUp() {
        preferences = mockk(relaxed = true)
        every { preferences.loadUserInfo() } returns UserInfo(
            gender = Gender.MALE,
            age = 19,
            weight = 58f,
            height = 175,
            activityLevel = ActivityLevel.MEDIUM,
            goalType = GoalType.GAIN_WEIGHT,
            carbRatio = 0.4f,
            proteinRatio = 0.3f,
            fatRatio = 0.3f
        )
        repositoryFake = TrackerRepositoryFake()
        trackerUseCases = TrackerUseCases(
            trackFood = TrackFood(repositoryFake),
            calculateMealNutrients = CalculateMealNutrients(preferences),
            deleteTrackedFood = DeleteTrackedFood(repositoryFake),
            getFoodsForDate = GetFoodsForDate(repositoryFake),
            searchFood = SearchFood(repositoryFake)
        )
        trackerOverviewViewModel = TrackerOverviewViewModel(preferences, trackerUseCases)
        searchViewModel = SearchViewModel(trackerUseCases, FilterOutDigits())
        composeRule.setContent {
            CalorieTrackerTheme {
                navController = rememberNavController()
                val scaffoldState = rememberScaffoldState()
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    scaffoldState = scaffoldState
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = Route.TRACKER_OVERVIEW
                    ) {
                        composable(Route.TRACKER_OVERVIEW) {
                            TrackerOverviewScreen(trackerOverviewViewModel = trackerOverviewViewModel) { mealType, dayOfMonth, month, year ->
                                navController.navigate("${Route.SEARCH}/$mealType/$dayOfMonth/$month/$year")
                            }
                        }
                        composable(route = "${Route.SEARCH}/{mealName}/{dayOfMonth}/{month}/{year}",
                            arguments = listOf(
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
                                searchViewModel = searchViewModel,
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

    @Test
    fun addBreakfast_appearsUnderBreakfast_nutrientsProperlyCalculated() {
        repositoryFake.searchResults = listOf(
            TrackableFood(
                name = "Apple",
                imageUrl = null,
                caloriesPer100g = 150,
                proteinsPer100g = 5,
                carbsPer100g = 50,
                fatPer100g = 1
            )
        )
        val addedAmount = 150
        val expectedCalories = (1.5f * 150).roundToInt()
        val expectedCarbs = (1.5f * 50).roundToInt()
        val expectedProtein = (1.5f * 5).roundToInt()
        val expectedFat = (1.5f * 1).roundToInt()

        composeRule.onNodeWithText("Add breakfast", ignoreCase = true)
            .assertDoesNotExist()
        composeRule.onNodeWithContentDescription("Breakfast")
            .performClick()
        composeRule.onNodeWithText("Add breakfast", ignoreCase = true)
            .assertIsDisplayed()
        composeRule.onNodeWithText("Add breakfast", ignoreCase = true)
            .performClick()

        assertThat(navController.currentDestination?.route).startsWith(Route.SEARCH)

        composeRule.onNodeWithTag("search_textField")
            .performTextInput("Apple")
        composeRule.onNodeWithContentDescription("Search…")
            .performClick()
        composeRule.onNodeWithText("Carbs")
            .performClick()
        composeRule.onNodeWithContentDescription("Amount")
            .performTextInput(addedAmount.toStr())
        composeRule.onNodeWithContentDescription("Track")
            .performClick()

        assertThat(navController.currentDestination?.route).isEqualTo(Route.TRACKER_OVERVIEW)

        composeRule.onAllNodesWithText(expectedCarbs.toStr())
            .onFirst()
            .assertIsDisplayed()
        composeRule.onAllNodesWithText(expectedCalories.toStr())
            .onFirst()
            .assertIsDisplayed()
        composeRule.onAllNodesWithText(expectedProtein.toStr())
            .onFirst()
            .assertIsDisplayed()
        composeRule.onAllNodesWithText(expectedFat.toStr())
            .onFirst()
            .assertIsDisplayed()
    }
}