package me.dannly.onboarding_presentation.weight

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import me.dannly.core.R
import me.dannly.core.domain.preferences.Preferences
import me.dannly.core.util.UiEvent
import me.dannly.core.util.UiText
import me.dannly.onboarding_domain.use_case.FilterOutDecimalDigits
import javax.inject.Inject

@HiltViewModel
class WeightViewModel @Inject constructor(
    private val preferences: Preferences,
    private val filterOutDecimalDigits: FilterOutDecimalDigits
) : ViewModel() {

    var weight by mutableStateOf("80.0")
        private set

    var validInput by mutableStateOf(true)
        private set

    private val isWeightValid
        get() = weight.toFloatOrNull() != null

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onWeightEnter(weight: String) {
        if(weight.length < 6)
            this.weight = filterOutDecimalDigits(weight)
        validInput = isWeightValid
    }

    fun onNextClick() {
        viewModelScope.launch {
            val weightNumber = weight.toFloatOrNull() ?: run {
                _uiEvent.send(
                    UiEvent.ShowMessage(UiText.StringResource(R.string.error_weight_cant_be_empty))
                )
                return@launch
            }
            preferences.saveWeight(weightNumber)
            _uiEvent.send(UiEvent.Success)
        }
    }
}