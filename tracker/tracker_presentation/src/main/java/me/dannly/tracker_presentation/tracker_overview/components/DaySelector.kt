package me.dannly.tracker_presentation.tracker_overview.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import me.dannly.tracker_presentation.R
import java.time.LocalDate

@Composable
fun DaySelector(
    modifier: Modifier = Modifier,
    date: LocalDate,
    onPreviousDayClick: () -> Unit,
    onNextDayClick: () -> Unit
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onPreviousDayClick
        ) {
            Icon(
                contentDescription = stringResource(id = R.string.previous_day),
                imageVector = Icons.Default.ArrowBack
            )
        }
        Text(
            text = parseDateText(date = date),
            style = MaterialTheme.typography.h2
        )
        IconButton(
            onClick = onNextDayClick
        ) {
            Icon(
                contentDescription = stringResource(id = R.string.next_day),
                imageVector = Icons.Default.ArrowForward
            )
        }
    }
}