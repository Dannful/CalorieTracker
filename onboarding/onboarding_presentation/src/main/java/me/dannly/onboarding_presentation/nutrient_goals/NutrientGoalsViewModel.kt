package me.dannly.onboarding_presentation.nutrient_goals

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import me.dannly.core.domain.preferences.Preferences
import me.dannly.core.util.UiEvent
import me.dannly.onboarding_domain.use_case.FilterOutDecimalDigits
import me.dannly.onboarding_domain.use_case.ValidateNutrients
import javax.inject.Inject

@HiltViewModel
class NutrientGoalsViewModel @Inject constructor(
    private val preferences: Preferences,
    private val filterOutDecimalDigits: FilterOutDecimalDigits,
    private val validateNutrients: ValidateNutrients
) : ViewModel() {

    var state by mutableStateOf(NutrientGoalsState())
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private fun validateNutrientsAndUpdateState() {
        val result =
            validateNutrients(state.carbsRatio, state.proteinRatio, state.fatRatio)
        state = when(result) {
            is ValidateNutrients.Result.Error -> state.copy(errorMessage = result.uiText)
            is ValidateNutrients.Result.Success -> state.copy(errorMessage = null)
        }
    }

    fun onEvent(event: NutrientGoalsEvent) {
        when(event) {
            NutrientGoalsEvent.OnNextClick -> {
                val result =
                    validateNutrients(state.carbsRatio, state.proteinRatio, state.fatRatio)
                when(result) {
                    is ValidateNutrients.Result.Success -> {
                        preferences.saveCarbsRatio(result.carbsRatio / 100f)
                        preferences.saveProteinRatio(result.proteinRatio / 100f)
                        preferences.saveFatRatio(result.fatRatio / 100f)
                        viewModelScope.launch {
                            _uiEvent.send(UiEvent.Success)
                        }
                    }
                    else -> Unit
                }
            }
            is NutrientGoalsEvent.OnCarbsRatioEnter -> {
                state = state.copy(carbsRatio = filterOutDecimalDigits(event.ratio))
                validateNutrientsAndUpdateState()
            }
            is NutrientGoalsEvent.OnFatRatioEnter -> {
                state = state.copy(fatRatio = filterOutDecimalDigits(event.ratio))
                validateNutrientsAndUpdateState()
            }
            is NutrientGoalsEvent.OnProteinRatioEnter -> {
                state = state.copy(proteinRatio = filterOutDecimalDigits(event.ratio))
                validateNutrientsAndUpdateState()
            }
        }
    }
}