package me.dannly.tracker_domain.use_case

import me.dannly.tracker_domain.model.MealType
import me.dannly.tracker_domain.model.TrackableFood
import me.dannly.tracker_domain.model.TrackedFood
import me.dannly.tracker_domain.repository.TrackerRepository
import java.time.LocalDate
import kotlin.math.roundToInt

class TrackFood(
    private val trackerRepository: TrackerRepository
) {

    suspend operator fun invoke(
        trackableFood: TrackableFood,
        amount: Int,
        mealType: MealType,
        date: LocalDate
    ) {
        trackerRepository.insertTrackedFood(
            TrackedFood(
                name = trackableFood.name,
                carbs = ((trackableFood.carbsPer100g / 100f) * amount).roundToInt(),
                protein = ((trackableFood.proteinsPer100g / 100f) * amount).roundToInt(),
                calories = ((trackableFood.caloriesPer100g / 100f) * amount).roundToInt(),
                fat = ((trackableFood.fatPer100g / 100f) * amount).roundToInt(),
                imageUrl = trackableFood.imageUrl,
                amount = amount,
                date = date,
                mealType = mealType
            )
        )
    }
}