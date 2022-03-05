package me.dannly.tracker_data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import me.dannly.tracker_data.local.entity.TrackedFoodEntity

@Dao
interface TrackerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrackedFood(trackedFoodEntity: TrackedFoodEntity)

    @Delete
    suspend fun deleteTrackedFood(trackedFoodEntity: TrackedFoodEntity)

    @Query(
        """
            SELECT * FROM TrackedFoodEntity
            WHERE dayOfMonth = :dayOfMonth
            AND month = :month AND year = :year
        """
    )
    fun getFoodsForDate(dayOfMonth: Int, month: Int, year: Int): Flow<List<TrackedFoodEntity>>
}