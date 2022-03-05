package me.dannly.onboarding_presentation.nutrient_goals

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.flow.collect
import me.dannly.core.util.UiEvent
import me.dannly.core_ui.LocalSpacing
import me.dannly.onboarding_presentation.R
import me.dannly.onboarding_presentation.components.ActionButton
import me.dannly.onboarding_presentation.components.UnitTextField

@Composable
fun NutrientGoalsScreen(
    nutrientGoalsViewModel: NutrientGoalsViewModel = hiltViewModel(),
    onNextClick: () -> Unit
) {
    val spacing = LocalSpacing.current
    LaunchedEffect(key1 = true) {
        nutrientGoalsViewModel.uiEvent.collect { event ->
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
                text = stringResource(id = R.string.what_are_your_nutrient_goals),
                style = MaterialTheme.typography.h3
            )
            nutrientGoalsViewModel.state.errorMessage?.let {
                Spacer(modifier = Modifier.height(spacing.spaceMedium))
                Text(
                    text = it.asString(LocalContext.current),
                    style = MaterialTheme.typography.caption,
                    color = MaterialTheme.colors.error
                )
            }
            Spacer(modifier = Modifier.height(spacing.spaceMedium))
            UnitTextField(
                value = nutrientGoalsViewModel.state.carbsRatio,
                unit = stringResource(id = R.string.percent_carbs),
                onValueChange = {
                    nutrientGoalsViewModel.onEvent(
                        NutrientGoalsEvent.OnCarbsRatioEnter(
                            it
                        )
                    )
                }
            )
            Spacer(modifier = Modifier.height(spacing.spaceMedium))
            UnitTextField(
                value = nutrientGoalsViewModel.state.proteinRatio,
                unit = stringResource(id = R.string.percent_proteins),
                onValueChange = {
                    nutrientGoalsViewModel.onEvent(
                        NutrientGoalsEvent.OnProteinRatioEnter(
                            it
                        )
                    )
                }
            )
            Spacer(modifier = Modifier.height(spacing.spaceMedium))
            UnitTextField(
                value = nutrientGoalsViewModel.state.fatRatio,
                unit = stringResource(id = R.string.percent_fats),
                onValueChange = {
                    nutrientGoalsViewModel.onEvent(
                        NutrientGoalsEvent.OnFatRatioEnter(
                            it
                        )
                    )
                }
            )
        }
        ActionButton(
            text = stringResource(id = R.string.next),
            onClick = {
                nutrientGoalsViewModel.onEvent(NutrientGoalsEvent.OnNextClick)
            },
            modifier = Modifier.align(Alignment.BottomEnd),
            isEnabled = nutrientGoalsViewModel.state.errorMessage == null
        )
    }
}