package me.dannly.onboarding_presentation.nutrient_goals

import me.dannly.core.util.UiText

data class NutrientGoalsState(
    val errorMessage: UiText? = null,
    val carbsRatio: String = "40.0",
    val proteinRatio: String = "30.0",
    val fatRatio: String = "30.0"
)
