package me.dannly.tracker_domain.use_case

import me.dannly.tracker_domain.model.TrackedFood
import me.dannly.tracker_domain.repository.TrackerRepository
import java.time.LocalDate

class DeleteTrackedFood(
    private val trackerRepository: TrackerRepository
) {

    suspend operator fun invoke(trackedFood: TrackedFood) = trackerRepository.deleteTrackedFood(trackedFood)
}