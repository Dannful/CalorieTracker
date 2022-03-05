package me.dannly.tracker_presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

@Composable
fun NutrientInfo(
    modifier: Modifier = Modifier,
    name: String,
    amount: Int,
    unit: String,
    unitTextSize: TextUnit = 14.sp,
    unitTextColor: Color = MaterialTheme.colors.onBackground,
    amountTextSize: TextUnit = 20.sp,
    amountTextColor: Color = MaterialTheme.colors.onBackground,
    nameTextStyle: TextStyle = MaterialTheme.typography.body1
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        UnitDisplay(
            amount = amount, unit = unit,
            amountTextSize = amountTextSize, amountTextColor = amountTextColor,
            unitTextColor = unitTextColor, unitTextSize = unitTextSize
        )
        Text(
            text = name, style = nameTextStyle, color = MaterialTheme.colors.onBackground,
            fontWeight = FontWeight.Light
        )
    }
}