package me.dannly.onboarding_domain.use_case

import me.dannly.core.util.UiText
import me.dannly.core.R

class ValidateNutrients {

    operator fun invoke(
        carbsRatioText: String,
        proteinRatioText: String,
        fatRatioText: String
    ): Result {
        val carbsRatio =
            carbsRatioText.toFloatOrNull() ?: return Result.Error(UiText.StringResource(R.string.error_invalid_values))
        val proteinRatio =
            proteinRatioText.toFloatOrNull() ?: return Result.Error(UiText.StringResource(R.string.error_invalid_values))
        val fatRatio = fatRatioText.toFloatOrNull() ?: return Result.Error(UiText.StringResource(R.string.error_invalid_values))
        if(carbsRatio + proteinRatio + fatRatio != 100f)
            return Result.Error(UiText.StringResource(R.string.error_not_100_percent))
        return Result.Success(carbsRatio, proteinRatio, fatRatio)
    }

    sealed class Result {

        data class Success(
            val carbsRatio: Float,
            val proteinRatio: Float,
            val fatRatio: Float
        ): Result()

        data class Error(val uiText: UiText): Result()
    }
}