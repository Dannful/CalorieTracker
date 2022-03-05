package me.dannly.tracker_domain.use_case

import me.dannly.tracker_domain.model.TrackableFood
import me.dannly.tracker_domain.repository.TrackerRepository

class SearchFood(
    private val trackerRepository: TrackerRepository
) {

    suspend operator fun invoke(query: String, page: Int = 1, pageSize: Int = 40): Result<List<TrackableFood>> {
        if(query.isBlank())
            return Result.success(emptyList())
        return trackerRepository.searchFood(query.trim(), page, pageSize)
    }
}