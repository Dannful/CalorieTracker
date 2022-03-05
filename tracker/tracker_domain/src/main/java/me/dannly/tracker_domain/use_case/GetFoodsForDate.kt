package me.dannly.tracker_domain.use_case

import me.dannly.tracker_domain.repository.TrackerRepository
import java.time.LocalDate

class GetFoodsForDate(
    private val trackerRepository: TrackerRepository
) {

    operator fun invoke(date: LocalDate) = trackerRepository.getFoodsForDate(date)
}