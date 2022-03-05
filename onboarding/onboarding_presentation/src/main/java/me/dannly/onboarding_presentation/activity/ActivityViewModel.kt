package me.dannly.onboarding_presentation.activity

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import me.dannly.core.domain.model.ActivityLevel
import me.dannly.core.domain.preferences.Preferences
import me.dannly.core.util.UiEvent
import javax.inject.Inject

@HiltViewModel
class ActivityViewModel @Inject constructor(
    private val preferences: Preferences
) : ViewModel() {

    var selectedActivity by mutableStateOf(ActivityLevel.MEDIUM)
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onActivityClick(activityLevel: ActivityLevel) {
        selectedActivity = activityLevel
    }

    fun onNextClick() {
        viewModelScope.launch {
            preferences.saveActivityLevel(selectedActivity)
            _uiEvent.send(UiEvent.Success)
        }
    }
}