package me.dannly.tracker_presentation.tracker_overview.components

import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import me.dannly.core_ui.LocalSpacing
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.sp
import me.dannly.core_ui.CarbColor
import me.dannly.core_ui.FatColor
import me.dannly.core_ui.ProteinColor
import me.dannly.tracker_presentation.R
import me.dannly.tracker_presentation.components.UnitDisplay
import me.dannly.tracker_presentation.tracker_overview.TrackerOverviewState

@Composable
fun NutrientsHeader(
    modifier: Modifier = Modifier,
    state: TrackerOverviewState
) {
    val spacing = LocalSpacing.current
    val animatedCalories by animateIntAsState(targetValue = state.totalCalories)
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(
                RoundedCornerShape(
                    bottomStart = 50.dp,
                    bottomEnd = 50.dp
                )
            )
            .background(MaterialTheme.colors.primary)
            .padding(horizontal = spacing.spaceLarge, vertical = spacing.spaceExtraLarge)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            UnitDisplay(
                amount = animatedCalories,
                unit = stringResource(id = R.string.kcal),
                amountTextColor = MaterialTheme.colors.onPrimary,
                amountTextSize = 40.sp,
                unitTextColor = MaterialTheme.colors.onPrimary,
                modifier = Modifier.align(Alignment.Bottom)
            )
            Column {
                Text(
                    text = stringResource(id = R.string.your_goal),
                    style = MaterialTheme.typography.body2,
                    color = MaterialTheme.colors.onPrimary
                )
                UnitDisplay(
                    amount = state.caloriesGoal,
                    unit = stringResource(id = R.string.kcal),
                    amountTextColor = MaterialTheme.colors.onPrimary,
                    amountTextSize = 40.sp,
                    unitTextColor = MaterialTheme.colors.onPrimary
                )
            }
        }
        Spacer(modifier = Modifier.height(spacing.spaceSmall))
        NutrientsBar(
            carbs = state.totalCarbs,
            protein = state.totalProtein,
            fat = state.totalFat,
            calories = state.totalCalories,
            calorieGoal = state.caloriesGoal,
            modifier = Modifier
                .fillMaxWidth()
                .height(30.dp)
        )
        Spacer(modifier = Modifier.height(spacing.spaceLarge))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            NutrientBarInfo(
                value = state.totalCarbs, goal = state.carbsGoal, name = stringResource(
                    id = R.string.carbs
                ), color = CarbColor, modifier = Modifier.size(90.dp)
            )
            NutrientBarInfo(
                value = state.totalProtein, goal = state.proteinGoal, name = stringResource(
                    id = R.string.protein
                ), color = ProteinColor, modifier = Modifier.size(90.dp)
            )
            NutrientBarInfo(
                value = state.totalFat, goal = state.fatGoal, name = stringResource(
                    id = R.string.fat
                ), color = FatColor, modifier = Modifier.size(90.dp)
            )
        }
    }
}