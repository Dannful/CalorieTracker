package me.dannly.tracker_presentation.tracker_overview

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import me.dannly.core_ui.LocalSpacing
import me.dannly.tracker_presentation.R
import me.dannly.tracker_presentation.tracker_overview.components.*
import androidx.compose.foundation.layout.*

@Composable
fun TrackerOverviewScreen(
    trackerOverviewViewModel: TrackerOverviewViewModel = hiltViewModel(),
    onNavigateToSearch: (String, Int, Int, Int) -> Unit
) {
    val spacing = LocalSpacing.current
    val context = LocalContext.current
    val state = trackerOverviewViewModel.state
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = spacing.spaceMedium)
    ) {
        item {
            NutrientsHeader(state = state)
            Spacer(modifier = Modifier.height(spacing.spaceMedium))
            DaySelector(
                date = state.date,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = spacing.spaceMedium),
                onPreviousDayClick = { trackerOverviewViewModel.onEvent(TrackerOverviewEvent.OnPreviousDayClick) }) {
                trackerOverviewViewModel.onEvent(TrackerOverviewEvent.OnNextDayClick)
            }
            Spacer(modifier = Modifier.height(spacing.spaceMedium))
        }
        items(state.meals) { meal ->
            ExpandableMeal(meal = meal, onClick = {
                trackerOverviewViewModel.onEvent(TrackerOverviewEvent.OnToggleMealClick(meal))
            }, modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = spacing.spaceSmall),
                    verticalArrangement = Arrangement.spacedBy(spacing.spaceMedium)
                ) {
                    state.trackedFoods.filter {
                        it.mealType == meal.mealType
                    }.forEach { food ->
                        TrackedFoodItem(trackedFood = food) {
                            trackerOverviewViewModel.onEvent(
                                TrackerOverviewEvent.OnDeleteTrackedFoodClick(
                                    food
                                )
                            )
                        }
                    }
                    AddButton(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(
                            id = R.string.add_meal,
                            meal.name.asString(context)
                        )
                    ) {
                        onNavigateToSearch(
                            meal.mealType.name,
                            state.date.dayOfMonth,
                            state.date.monthValue,
                            state.date.year
                        )
                    }
                }
            }
        }
    }
}