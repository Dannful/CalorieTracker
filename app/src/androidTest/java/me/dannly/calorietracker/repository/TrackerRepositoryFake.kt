package me.dannly.calorietracker.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import me.dannly.tracker_domain.model.TrackableFood
import me.dannly.tracker_domain.model.TrackedFood
import me.dannly.tracker_domain.repository.TrackerRepository
import java.time.LocalDate

class TrackerRepositoryFake : TrackerRepository {

    var shouldReturnError = false

    private val trackedFoodList = mutableListOf<TrackedFood>()
    var searchResults = listOf<TrackableFood>()

    private val getFoodsForDateFlow = MutableSharedFlow<List<TrackedFood>>(replay = 1)

    override suspend fun searchFood(
        query: String,
        page: Int,
        pageSize: Int
    ): Result<List<TrackableFood>> {
        return if (shouldReturnError) {
            Result.failure(Throwable())
        } else {
            Result.success(searchResults)
        }
    }

    override suspend fun insertTrackedFood(trackedFood: TrackedFood) {
        trackedFoodList.add(trackedFood.copy(id = trackedFoodList.size))
        getFoodsForDateFlow.emit(trackedFoodList)
    }

    override suspend fun deleteTrackedFood(trackedFood: TrackedFood) {
        trackedFoodList.remove(trackedFood)
        getFoodsForDateFlow.emit(trackedFoodList)
    }

    override fun getFoodsForDate(localDate: LocalDate): Flow<List<TrackedFood>> {
        return getFoodsForDateFlow
    }
}