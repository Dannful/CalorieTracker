package me.dannly.tracker_presentation.search

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import me.dannly.core.util.UiEvent
import me.dannly.core_ui.LocalSpacing
import me.dannly.tracker_domain.model.MealType
import me.dannly.tracker_presentation.R
import me.dannly.tracker_presentation.search.components.SearchTextField
import me.dannly.tracker_presentation.search.components.TrackableFoodItem
import java.time.LocalDate
import java.util.*

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchScreen(
    scaffoldState: ScaffoldState,
    mealName: String,
    dayOfMonth: Int,
    month: Int,
    year: Int,
    searchViewModel: SearchViewModel = hiltViewModel(),
    onNavigateUp: () -> Unit
) {
    val spacing = LocalSpacing.current
    val state = searchViewModel.state
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    LaunchedEffect(key1 = keyboardController) {
        searchViewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.Success -> Unit
                UiEvent.NavigateUp -> onNavigateUp()
                is UiEvent.ShowMessage -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        event.uiText.asString(
                            context
                        )
                    )
                    keyboardController?.hide()
                }
            }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(spacing.spaceMedium),
        verticalArrangement = Arrangement.spacedBy(spacing.spaceMedium)
    ) {
        Text(
            text = stringResource(id = R.string.add_meal, mealName.lowercase(Locale.getDefault())),
            style = MaterialTheme.typography.h1
        )
        SearchTextField(text = state.query, onValueChange = {
            searchViewModel.onEvent(SearchEvent.OnQueryChange(it))
        }, onFocusChange = {
            searchViewModel.onEvent(SearchEvent.OnSearchFocusChange(it.isFocused))
        }, shouldShowHint = state.isHintVisible) {
            keyboardController?.hide()
            searchViewModel.onEvent(SearchEvent.OnSearch)
        }
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(state.trackableFoodUiStateList) { food ->
                TrackableFoodItem(
                    trackableFoodUiState = food,
                    onAmountChange = {
                        searchViewModel.onEvent(
                            SearchEvent.OnAmountForFoodChange(
                                trackableFood = food.food,
                                amount = it
                            )
                        )
                    },
                    onTrack = {
                        keyboardController?.hide()
                        searchViewModel.onEvent(
                            SearchEvent.OnTrackedFoodClick(
                                trackableFood = food.food,
                                mealType = MealType.valueOf(mealName),
                                date = LocalDate.of(year, month, dayOfMonth)
                            )
                        )
                    }, modifier = Modifier.fillMaxWidth()
                ) {
                    searchViewModel.onEvent(SearchEvent.OnToggleTrackableFood(food.food))
                }
            }
        }
    }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when {
            state.isSearching -> CircularProgressIndicator()
            state.trackableFoodUiStateList.isEmpty() -> Text(
                text = stringResource(id = R.string.no_results),
                style = MaterialTheme.typography.body1,
                textAlign = TextAlign.Center
            )
        }
    }
}