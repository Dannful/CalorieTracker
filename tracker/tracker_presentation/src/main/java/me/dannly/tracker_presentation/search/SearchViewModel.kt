package me.dannly.tracker_presentation.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import javax.inject.Inject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import me.dannly.core.domain.use_case.FilterOutDigits
import me.dannly.core.util.UiEvent
import me.dannly.core.util.UiText
import me.dannly.tracker_domain.use_case.TrackerUseCases
import me.dannly.tracker_presentation.R

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val trackerUseCases: TrackerUseCases,
    private val filterOutDigits: FilterOutDigits
) : ViewModel() {

    var state by mutableStateOf(SearchState())
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: SearchEvent) {
        when (event) {
            is SearchEvent.OnAmountForFoodChange -> state =
                state.copy(trackableFoodUiStateList = state.trackableFoodUiStateList.map {
                    if (it.food == event.trackableFood)
                        it.copy(amount = filterOutDigits(event.amount))
                    else
                        it
                })
            is SearchEvent.OnQueryChange -> state = state.copy(query = event.query)
            SearchEvent.OnSearch -> executeSearch()
            is SearchEvent.OnSearchFocusChange -> state =
                state.copy(isHintVisible = !event.isFocused && state.query.isBlank())
            is SearchEvent.OnToggleTrackableFood -> state =
                state.copy(trackableFoodUiStateList = state.trackableFoodUiStateList.map {
                    if (it.food == event.trackableFood)
                        it.copy(isExpanded = !it.isExpanded)
                    else
                        it
                })
            is SearchEvent.OnTrackedFoodClick -> trackFood(event)
        }
    }

    private fun executeSearch() {
        viewModelScope.launch {
            state = state.copy(
                isSearching = true,
                trackableFoodUiStateList = emptyList()
            )
            trackerUseCases.searchFood(state.query)
                .onSuccess { foods ->
                    state = state.copy(trackableFoodUiStateList = foods.map {
                        TrackableFoodUiState(it)
                    }, isSearching = false, query = "")
                }.onFailure {
                    state = state.copy(isSearching = false)
                    _uiEvent.send(UiEvent.ShowMessage(UiText.StringResource(R.string.error_something_went_wrong)))
                }
        }
    }

    private fun trackFood(event: SearchEvent.OnTrackedFoodClick) {
        viewModelScope.launch {
            val uiState = state.trackableFoodUiStateList.find { it.food == event.trackableFood }
            trackerUseCases.trackFood(
                trackableFood = uiState?.food ?: return@launch,
                amount = uiState.amount.toIntOrNull() ?: return@launch,
                date = event.date,
                mealType = event.mealType
            )
            _uiEvent.send(UiEvent.NavigateUp)
        }
    }
}