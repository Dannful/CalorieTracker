package me.dannly.tracker_presentation.tracker_overview

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import me.dannly.core.domain.preferences.Preferences
import me.dannly.tracker_domain.use_case.TrackerUseCases
import javax.inject.Inject

@HiltViewModel
class TrackerOverviewViewModel @Inject constructor(
    preferences: Preferences,
    private val trackerUseCases: TrackerUseCases
) : ViewModel() {

    var state by mutableStateOf(TrackerOverviewState())
        private set

    private var getFoodsForDateJob: Job? = null

    init {
        refreshFoods()
        preferences.saveShouldShowOnboarding(false)
    }

    fun onEvent(event: TrackerOverviewEvent) {
        when (event) {
            is TrackerOverviewEvent.OnDeleteTrackedFoodClick -> viewModelScope.launch {
                trackerUseCases.deleteTrackedFood(event.trackedFood)
                refreshFoods()
            }
            TrackerOverviewEvent.OnNextDayClick -> {
                state = state.copy(
                    date = state.date.plusDays(1)
                )
                refreshFoods()
            }
            TrackerOverviewEvent.OnPreviousDayClick -> {
                state = state.copy(
                    date = state.date.minusDays(1)
                )
                refreshFoods()
            }
            is TrackerOverviewEvent.OnToggleMealClick -> state = state.copy(
                meals = state.meals.map {
                    if (it.name == event.meal.name) {
                        it.copy(isExpanded = !it.isExpanded)
                    } else {
                        it
                    }
                }
            )
        }
    }

    private fun refreshFoods() {
        getFoodsForDateJob?.cancel()
        getFoodsForDateJob = trackerUseCases.getFoodsForDate(state.date)
            .onEach { foods ->
                val nutrients = trackerUseCases.calculateMealNutrients(foods)
                state = state.copy(
                    trackedFoods = foods,
                    totalCarbs = nutrients.totalCarbs,
                    totalFat = nutrients.totalFat,
                    totalCalories = nutrients.totalCalories,
                    totalProtein = nutrients.totalProtein,
                    carbsGoal = nutrients.carbsGoal,
                    fatGoal = nutrients.fatGoal,
                    proteinGoal = nutrients.proteinGoal,
                    caloriesGoal = nutrients.caloriesGoal,
                    meals = state.meals.map {
                        val nutrientsForMeal =
                            nutrients.mealNutrients[it.mealType] ?: return@map it.copy(
                                carbs = 0,
                                protein = 0,
                                fat = 0,
                                calories = 0
                            )
                        it.copy(
                            carbs = nutrientsForMeal.carbs,
                            fat = nutrientsForMeal.fat,
                            protein = nutrientsForMeal.protein,
                            calories = nutrientsForMeal.calories
                        )
                    }
                )
            }.launchIn(viewModelScope)
    }
}