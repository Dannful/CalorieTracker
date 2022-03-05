package me.dannly.tracker_domain.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import me.dannly.core.domain.preferences.Preferences
import me.dannly.tracker_domain.repository.TrackerRepository
import me.dannly.tracker_domain.use_case.*

@Module
@InstallIn(ViewModelComponent::class)
object TrackerDomainModule {

    @ViewModelScoped
    @Provides
    fun provideTrackerUseCases(
        preferences: Preferences,
        trackerRepository: TrackerRepository
    ) = TrackerUseCases(
        calculateMealNutrients = CalculateMealNutrients(preferences),
        deleteTrackedFood = DeleteTrackedFood(trackerRepository),
        getFoodsForDate = GetFoodsForDate(trackerRepository),
        searchFood = SearchFood(trackerRepository),
        trackFood = TrackFood(trackerRepository)
    )
}