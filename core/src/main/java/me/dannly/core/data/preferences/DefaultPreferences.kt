package me.dannly.core.data.preferences

import android.content.SharedPreferences
import me.dannly.core.domain.model.ActivityLevel
import me.dannly.core.domain.model.Gender
import me.dannly.core.domain.model.GoalType
import me.dannly.core.domain.model.UserInfo
import me.dannly.core.domain.preferences.Preferences

class DefaultPreferences(
    private val sharedPreferences: SharedPreferences
) : Preferences {

    override fun saveGender(gender: Gender) {
        sharedPreferences.edit().putString(Preferences.KEY_GENDER, gender.name)
            .apply()
    }

    override fun saveAge(age: Int) {
        sharedPreferences.edit().putInt(Preferences.KEY_AGE, age)
            .apply()
    }

    override fun saveWeight(weight: Float) {
        sharedPreferences.edit().putFloat(Preferences.KEY_WEIGHT, weight)
            .apply()
    }

    override fun saveHeight(height: Int) {
        sharedPreferences.edit().putInt(Preferences.KEY_HEIGHT, height)
            .apply()
    }

    override fun saveActivityLevel(activityLevel: ActivityLevel) {
        sharedPreferences.edit().putString(Preferences.KEY_ACTIVITY_LEVEL, activityLevel.name)
            .apply()
    }

    override fun saveGoalType(goalType: GoalType) {
        sharedPreferences.edit().putString(Preferences.KEY_GOAL_TYPE, goalType.name)
            .apply()
    }

    override fun saveCarbsRatio(ratio: Float) {
        sharedPreferences.edit().putFloat(Preferences.KEY_CARB_RATIO, ratio)
            .apply()
    }

    override fun saveProteinRatio(ratio: Float) {
        sharedPreferences.edit().putFloat(Preferences.KEY_PROTEIN_RATIO, ratio)
            .apply()
    }

    override fun saveFatRatio(ratio: Float) {
        sharedPreferences.edit().putFloat(Preferences.KEY_FAT_RATIO, ratio)
            .apply()
    }

    override fun loadUserInfo(): UserInfo {
        val gender = Gender.valueOf(sharedPreferences.getString(Preferences.KEY_GENDER, "")!!)
        val age = sharedPreferences.getInt(Preferences.KEY_AGE, -1)
        val weight = sharedPreferences.getFloat(Preferences.KEY_WEIGHT, -1f)
        val height = sharedPreferences.getInt(Preferences.KEY_HEIGHT, -1)
        val activityLevel =
            ActivityLevel.valueOf(sharedPreferences.getString(Preferences.KEY_ACTIVITY_LEVEL, "")!!)
        val goalType =
            GoalType.valueOf(sharedPreferences.getString(Preferences.KEY_GOAL_TYPE, "")!!)
        val carbRatio = sharedPreferences.getFloat(Preferences.KEY_CARB_RATIO, -1f)
        val proteinRatio = sharedPreferences.getFloat(Preferences.KEY_PROTEIN_RATIO, -1f)
        val fatRatio = sharedPreferences.getFloat(Preferences.KEY_FAT_RATIO, -1f)
        return UserInfo(
            gender,
            age,
            weight,
            height,
            activityLevel,
            goalType,
            carbRatio,
            proteinRatio,
            fatRatio
        )
    }

    override fun saveShouldShowOnboarding(shouldShow: Boolean) {
        sharedPreferences.edit().putBoolean(Preferences.KEY_SHOULD_SHOW_ONBOARDING, shouldShow).apply()
    }

    override fun loadShouldShowOnboarding(): Boolean {
        return sharedPreferences.getBoolean(Preferences.KEY_SHOULD_SHOW_ONBOARDING, true)
    }
}