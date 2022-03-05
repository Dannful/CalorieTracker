package me.dannly.tracker_presentation.search.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import me.dannly.core_ui.LocalSpacing
import me.dannly.tracker_presentation.R
import me.dannly.tracker_presentation.search.TrackableFoodUiState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.LastBaseline
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import me.dannly.tracker_presentation.components.NutrientInfo

@OptIn(ExperimentalCoilApi::class)
@Composable
fun TrackableFoodItem(
    modifier: Modifier = Modifier,
    trackableFoodUiState: TrackableFoodUiState,
    onAmountChange: (String) -> Unit,
    onTrack: () -> Unit,
    onClick: () -> Unit
) {
    val trackableFood = trackableFoodUiState.food
    val spacing = LocalSpacing.current
    val amountStringResource = stringResource(id = R.string.amount)
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(5.dp))
            .padding(spacing.spaceExtraSmall)
            .shadow(
                elevation = 1.dp,
                shape = RoundedCornerShape(5.dp)
            )
            .background(MaterialTheme.colors.surface)
            .clickable(onClick = onClick)
            .padding(end = spacing.spaceMedium)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(spacing.spaceMedium)
            ) {
                Image(
                    painter = rememberImagePainter(data = trackableFood.imageUrl, builder = {
                        crossfade(true)
                        error(R.drawable.ic_burger)
                        fallback(R.drawable.ic_burger)
                    }), contentDescription = trackableFood.name, contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(topStart = 5.dp))
                )
                Column(
                    modifier = Modifier.align(Alignment.CenterVertically),
                    verticalArrangement = Arrangement.spacedBy(spacing.spaceSmall)
                ) {
                    Text(
                        text = trackableFood.name,
                        style = MaterialTheme.typography.body1,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = stringResource(
                            id = R.string.kcal_per_100g,
                            trackableFood.caloriesPer100g
                        ),
                        style = MaterialTheme.typography.body2
                    )
                }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(spacing.spaceSmall)) {
                NutrientInfo(
                    name = stringResource(id = R.string.carbs),
                    amount = trackableFood.carbsPer100g,
                    unit = stringResource(
                        id = R.string.grams
                    ),
                    amountTextSize = 16.sp,
                    unitTextSize = 12.sp,
                    nameTextStyle = MaterialTheme.typography.body2
                )
                NutrientInfo(
                    name = stringResource(id = R.string.protein),
                    amount = trackableFood.proteinsPer100g,
                    unit = stringResource(
                        id = R.string.grams
                    ),
                    amountTextSize = 16.sp,
                    unitTextSize = 12.sp,
                    nameTextStyle = MaterialTheme.typography.body2
                )
                NutrientInfo(
                    name = stringResource(id = R.string.fat),
                    amount = trackableFood.fatPer100g,
                    unit = stringResource(
                        id = R.string.grams
                    ),
                    amountTextSize = 16.sp,
                    unitTextSize = 12.sp,
                    nameTextStyle = MaterialTheme.typography.body2
                )
            }
        }
        AnimatedVisibility(visible = trackableFoodUiState.isExpanded) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(spacing.spaceMedium),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(spacing.spaceExtraSmall)) {
                    BasicTextField(
                        value = trackableFoodUiState.amount, onValueChange = onAmountChange,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = if (trackableFoodUiState.amount.isNotBlank())
                                ImeAction.Done else ImeAction.Default
                        ), keyboardActions = KeyboardActions(onDone = {
                            onTrack()
                            defaultKeyboardAction(ImeAction.Done)
                        }), singleLine = true, modifier = Modifier
                            .border(
                                shape = RoundedCornerShape(5.dp),
                                width = 0.5.dp, color = MaterialTheme.colors.onSurface
                            )
                            .alignBy(LastBaseline)
                            .padding(spacing.spaceMedium)
                            .semantics {
                                contentDescription = amountStringResource
                            }
                    )
                    Text(
                        text = stringResource(id = R.string.grams),
                        style = MaterialTheme.typography.body1,
                        modifier = Modifier.alignBy(LastBaseline)
                    )
                }
                IconButton(
                    onClick = onTrack,
                    enabled = trackableFoodUiState.amount.isNotBlank()
                ) {
                    Icon(
                        contentDescription = stringResource(id = R.string.track),
                        imageVector = Icons.Default.Check
                    )
                }
            }
        }
    }
}