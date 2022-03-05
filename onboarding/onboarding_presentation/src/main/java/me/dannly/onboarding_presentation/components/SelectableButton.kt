package me.dannly.onboarding_presentation.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import me.dannly.core_ui.LocalSpacing

@Composable
fun SelectableButton(
    modifier: Modifier = Modifier,
    text: String,
    textStyle: TextStyle = MaterialTheme.typography.button,
    isSelected: Boolean = true,
    color: Color,
    selectedColor: Color,
    onClick: () -> Unit
) {
    val backgroundColor by animateColorAsState(targetValue = if (isSelected) color else Color.Transparent)
    val shape = RoundedCornerShape(100.dp)
    Box(
        modifier = modifier
            .clip(shape)
            .border(width = 2.dp, color = color, shape = shape)
            .background(color = backgroundColor, shape = shape)
            .clickable(onClick = onClick)
            .padding(LocalSpacing.current.spaceMedium),
        contentAlignment = Alignment.Center
    ) {
        val textColor by animateColorAsState(targetValue = if (isSelected) selectedColor else color)
        Text(
            text = text,
            style = textStyle,
            color = textColor
        )
    }
}