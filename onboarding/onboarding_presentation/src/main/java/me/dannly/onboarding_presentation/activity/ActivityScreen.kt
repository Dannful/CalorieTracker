package me.dannly.onboarding_presentation.activity

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.flow.collect
import me.dannly.core.domain.model.ActivityLevel
import me.dannly.core.util.UiEvent
import me.dannly.core_ui.LocalSpacing
import me.dannly.onboarding_presentation.R
import me.dannly.onboarding_presentation.components.ActionButton
import me.dannly.onboarding_presentation.components.SelectableButton

@Composable
fun ActivityScreen(
    activityViewModel: ActivityViewModel = hiltViewModel(),
    onNextClick: () -> Unit
) {
    val spacing = LocalSpacing.current
    LaunchedEffect(key1 = true) {
        activityViewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.Success -> onNextClick()
                else -> Unit
            }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(spacing.spaceLarge)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(id = R.string.whats_your_activity_level),
                style = MaterialTheme.typography.h3
            )
            Spacer(modifier = Modifier.height(spacing.spaceMedium))
            Row(horizontalArrangement = Arrangement.spacedBy(spacing.spaceMedium)) {
                SelectableButton(
                    text = stringResource(id = R.string.low),
                    color = MaterialTheme.colors.primaryVariant,
                    selectedColor = Color.White,
                    textStyle = MaterialTheme.typography.button.copy(fontWeight = FontWeight.Normal),
                    isSelected = activityViewModel.selectedActivity == ActivityLevel.LOW
                ) {
                    activityViewModel.onActivityClick(ActivityLevel.LOW)
                }
                SelectableButton(
                    text = stringResource(id = R.string.medium),
                    color = MaterialTheme.colors.primaryVariant,
                    selectedColor = Color.White,
                    textStyle = MaterialTheme.typography.button.copy(fontWeight = FontWeight.Normal),
                    isSelected = activityViewModel.selectedActivity == ActivityLevel.MEDIUM
                ) {
                    activityViewModel.onActivityClick(ActivityLevel.MEDIUM)
                }
                SelectableButton(
                    text = stringResource(id = R.string.high),
                    color = MaterialTheme.colors.primaryVariant,
                    selectedColor = Color.White,
                    textStyle = MaterialTheme.typography.button.copy(fontWeight = FontWeight.Normal),
                    isSelected = activityViewModel.selectedActivity == ActivityLevel.HIGH
                ) {
                    activityViewModel.onActivityClick(ActivityLevel.HIGH)
                }
            }
        }
        ActionButton(
            text = stringResource(id = R.string.next),
            onClick = activityViewModel::onNextClick,
            modifier = Modifier.align(Alignment.BottomEnd)
        )
    }
}