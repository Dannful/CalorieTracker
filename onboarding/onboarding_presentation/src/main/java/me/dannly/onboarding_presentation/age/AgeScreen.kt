package me.dannly.onboarding_presentation.age

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.flow.collect
import me.dannly.core.util.UiEvent
import me.dannly.core_ui.LocalSpacing
import me.dannly.onboarding_presentation.R
import me.dannly.onboarding_presentation.components.ActionButton
import me.dannly.onboarding_presentation.components.UnitTextField

@Composable
fun AgeScreen(
    ageViewModel: AgeViewModel = hiltViewModel(),
    onNextClick: () -> Unit
) {
    val spacing = LocalSpacing.current
    LaunchedEffect(key1 = true) {
        ageViewModel.uiEvent.collect { event ->
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
                text = stringResource(id = R.string.whats_your_age),
                style = MaterialTheme.typography.h3
            )
            Spacer(modifier = Modifier.height(spacing.spaceMedium))
            UnitTextField(
                value = ageViewModel.age,
                unit = stringResource(id = R.string.years),
                onValueChange = ageViewModel::onAgeEnter
            )
        }
        ActionButton(
            text = stringResource(id = R.string.next),
            onClick = ageViewModel::onNextClick,
            modifier = Modifier.align(Alignment.BottomEnd),
            isEnabled = ageViewModel.validInput
        )
    }
}