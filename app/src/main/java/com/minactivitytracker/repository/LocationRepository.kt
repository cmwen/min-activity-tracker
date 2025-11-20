package com.minactivitytracker.repository

import com.minactivitytracker.data.dao.LocationSampleDao
import com.minactivitytracker.data.entity.LocationSample
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationRepository @Inject constructor(
    private val locationSampleDao: LocationSampleDao
) {
    fun getRecentLocations(limit: Int = 100): Flow<List<LocationSample>> =
        locationSampleDao.getRecentLocations(limit)

    fun getLocationsInRange(startTime: Long, endTime: Long): Flow<List<LocationSample>> =
        locationSampleDao.getLocationsInRange(startTime, endTime)

    suspend fun insertLocation(location: LocationSample) =
        locationSampleDao.insert(location)

    suspend fun deleteOlderThan(before: Long) =
        locationSampleDao.deleteOlderThan(before)

    suspend fun getCount(): Int =
        locationSampleDao.getCount()
}
