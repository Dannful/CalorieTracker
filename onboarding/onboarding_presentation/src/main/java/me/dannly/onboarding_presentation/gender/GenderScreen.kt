package me.dannly.onboarding_presentation.gender

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
import me.dannly.core.domain.model.Gender
import me.dannly.core.util.UiEvent
import me.dannly.core_ui.LocalSpacing
import me.dannly.onboarding_presentation.R
import me.dannly.onboarding_presentation.components.ActionButton
import me.dannly.onboarding_presentation.components.SelectableButton

@Composable
fun GenderScreen(
    genderViewModel: GenderViewModel = hiltViewModel(),
    onNextClick: () -> Unit
) {
    val spacing = LocalSpacing.current
    LaunchedEffect(key1 = true) {
        genderViewModel.uiEvent.collect { event ->
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
                text = stringResource(id = R.string.whats_your_gender),
                style = MaterialTheme.typography.h3
            )
            Spacer(modifier = Modifier.height(spacing.spaceMedium))
            Row(horizontalArrangement = Arrangement.spacedBy(spacing.spaceMedium)) {
                SelectableButton(
                    text = stringResource(id = R.string.male),
                    color = MaterialTheme.colors.primaryVariant,
                    selectedColor = Color.White,
                    textStyle = MaterialTheme.typography.button.copy(fontWeight = FontWeight.Normal),
                    isSelected = genderViewModel.selectedGender == Gender.MALE
                ) {
                    genderViewModel.onGenderClick(Gender.MALE)
                }
                SelectableButton(
                    text = stringResource(id = R.string.female),
                    color = MaterialTheme.colors.primaryVariant,
                    selectedColor = Color.White,
                    textStyle = MaterialTheme.typography.button.copy(fontWeight = FontWeight.Normal),
                    isSelected = genderViewModel.selectedGender == Gender.FEMALE
                ) {
                    genderViewModel.onGenderClick(Gender.FEMALE)
                }
            }
        }
        ActionButton(
            text = stringResource(id = R.string.next),
            onClick = genderViewModel::onNextClick,
            modifier = Modifier.align(Alignment.BottomEnd)
        )
    }
}