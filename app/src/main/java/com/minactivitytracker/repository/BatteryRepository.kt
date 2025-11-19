package com.minactivitytracker.repository

import com.minactivitytracker.data.dao.BatterySampleDao
import com.minactivitytracker.data.entity.BatterySample
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BatteryRepository @Inject constructor(
    private val batterySampleDao: BatterySampleDao
) {
    fun getAllSamples(): Flow<List<BatterySample>> = batterySampleDao.getAllSamples()

    suspend fun insertSample(sample: BatterySample) = batterySampleDao.insert(sample)
}
