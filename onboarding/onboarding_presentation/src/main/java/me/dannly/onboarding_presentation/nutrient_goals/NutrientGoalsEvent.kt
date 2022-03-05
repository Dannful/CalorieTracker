package me.dannly.onboarding_presentation.nutrient_goals

sealed class NutrientGoalsEvent {

    data class OnCarbsRatioEnter(val ratio: String): NutrientGoalsEvent()
    data class OnProteinRatioEnter(val ratio: String): NutrientGoalsEvent()
    data class OnFatRatioEnter(val ratio: String): NutrientGoalsEvent()
    object OnNextClick : NutrientGoalsEvent()
}
