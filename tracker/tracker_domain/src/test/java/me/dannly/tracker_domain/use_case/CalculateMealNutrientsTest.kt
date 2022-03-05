package me.dannly.tracker_domain.use_case

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import me.dannly.core.domain.model.ActivityLevel
import me.dannly.core.domain.model.Gender
import me.dannly.core.domain.model.GoalType
import me.dannly.core.domain.model.UserInfo
import me.dannly.core.domain.preferences.Preferences
import me.dannly.tracker_domain.model.MealType
import me.dannly.tracker_domain.model.TrackedFood
import org.junit.Before
import org.junit.Test
import java.time.LocalDate
import kotlin.random.Random

class CalculateMealNutrientsTest {

    private lateinit var calculateMealNutrients: CalculateMealNutrients

    @Before
    fun setUp() {
        val preferences = mockk<Preferences>(relaxed = true)
        every { preferences.loadUserInfo() } returns UserInfo(
            gender = Gender.MALE,
            age = 19,
            weight = 58f,
            height = 175,
            activityLevel = ActivityLevel.MEDIUM,
            goalType = GoalType.GAIN_WEIGHT,
            carbRatio = 40f,
            proteinRatio = 30f,
            fatRatio = 30f
        )
        calculateMealNutrients = CalculateMealNutrients(preferences)
    }

    @Test
    fun `Calories for breakfast properly calculated`() {
        val trackedFoods = (1..30).map {
            TrackedFood(
                name = "name",
                carbs = Random.nextInt(100),
                fat = Random.nextInt(100),
                protein = Random.nextInt(100),
                mealType = MealType.values().random(),
                imageUrl = null,
                amount = 100,
                date = LocalDate.now(),
                calories = Random.nextInt(2000)
            )
        }
        val result = calculateMealNutrients(trackedFoods)
        val breakfastCalories = result.mealNutrients.values.filter {
            it.mealType == MealType.BREAKFAST
        }.sumOf { it.calories }
        val expectedCalories = trackedFoods.filter {
            it.mealType == MealType.BREAKFAST
        }.sumOf { it.calories }

        assertThat(breakfastCalories).isEqualTo(expectedCalories)
    }

    @Test
    fun `Carbs for lunch properly calculated`() {
        val trackedFoods = (1..30).map {
            TrackedFood(
                name = "name",
                carbs = Random.nextInt(100),
                fat = Random.nextInt(100),
                protein = Random.nextInt(100),
                mealType = MealType.values().random(),
                imageUrl = null,
                amount = 100,
                date = LocalDate.now(),
                calories = Random.nextInt(2000)
            )
        }
        val result = calculateMealNutrients(trackedFoods)
        val dinnerCarbs = result.mealNutrients.values.filter {
            it.mealType == MealType.LUNCH
        }.sumOf { it.carbs }
        val expectedCarbs = trackedFoods.filter {
            it.mealType == MealType.LUNCH
        }.sumOf { it.carbs }

        assertThat(dinnerCarbs).isEqualTo(expectedCarbs)
    }
}