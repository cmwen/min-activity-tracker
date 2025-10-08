package io.cmwen.min_activity_tracker.data.repository

import com.google.common.truth.Truth.assertThat
import io.cmwen.min_activity_tracker.data.database.BatterySampleDao
import io.cmwen.min_activity_tracker.data.database.BatterySampleEntity
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class BatterySampleRepositoryImplTest {
    private lateinit var repository: BatterySampleRepository
    private lateinit var mockDao: BatterySampleDao

    @Before
    fun setup() {
        mockDao = mockk()
        repository = BatterySampleRepositoryImpl(mockDao)
    }

    @Test
    fun `observeAllSamples returns data from DAO`() =
        runTest {
            // Given
            val samples =
                listOf(
                    BatterySampleEntity(
                        id = "1",
                        timestamp = 1000L,
                        levelPercent = 80,
                        chargingState = "DISCHARGING",
                        temperature = 30f,
                    ),
                )
            every { mockDao.getAllSamples() } returns flowOf(samples)

            // When
            val result = repository.observeAllSamples().first()

            // Then
            assertThat(result).hasSize(1)
            assertThat(result[0].levelPercent).isEqualTo(80)
        }

    @Test
    fun `observeSamplesByTimeRange returns filtered data`() =
        runTest {
            // Given
            val startTime = 1000L
            val endTime = 2000L
            val samples =
                listOf(
                    BatterySampleEntity("1", 1500L, 75, "DISCHARGING", 30f),
                )
            every { mockDao.getSamplesByTimeRange(startTime, endTime) } returns flowOf(samples)

            // When
            val result = repository.observeSamplesByTimeRange(startTime, endTime).first()

            // Then
            assertThat(result).hasSize(1)
            assertThat(result[0].timestamp).isEqualTo(1500L)
        }

    @Test
    fun `insert calls DAO insert`() =
        runTest {
            // Given
            val sample = BatterySampleEntity("1", 1000L, 80, "DISCHARGING", 30f)
            coEvery { mockDao.insert(sample) } returns Unit

            // When
            repository.insert(sample)

            // Then
            coVerify { mockDao.insert(sample) }
        }

    @Test
    fun `insertAll calls DAO insertAll`() =
        runTest {
            // Given
            val samples =
                listOf(
                    BatterySampleEntity("1", 1000L, 80, "DISCHARGING", 30f),
                    BatterySampleEntity("2", 2000L, 70, "DISCHARGING", 31f),
                )
            coEvery { mockDao.insertAll(samples) } returns Unit

            // When
            repository.insertAll(samples)

            // Then
            coVerify { mockDao.insertAll(samples) }
        }

    @Test
    fun `deleteById calls DAO deleteById`() =
        runTest {
            // Given
            val id = "test-id"
            coEvery { mockDao.deleteById(id) } returns Unit

            // When
            repository.deleteById(id)

            // Then
            coVerify { mockDao.deleteById(id) }
        }

    @Test
    fun `deleteOldSamples calls DAO deleteOldSamples`() =
        runTest {
            // Given
            val timestamp = 1000L
            coEvery { mockDao.deleteOldSamples(timestamp) } returns Unit

            // When
            repository.deleteOldSamples(timestamp)

            // Then
            coVerify { mockDao.deleteOldSamples(timestamp) }
        }

    @Test
    fun `getAverageBatteryLevel returns correct average`() =
        runTest {
            // Given
            val startTime = 1000L
            val endTime = 2000L
            val average = 75.5
            coEvery { mockDao.getAverageBatteryLevel(startTime, endTime) } returns average

            // When
            val result = repository.getAverageBatteryLevel(startTime, endTime)

            // Then
            assertThat(result).isEqualTo(average)
        }

    @Test
    fun `getSamplesInRange returns correct samples`() =
        runTest {
            // Given
            val startTime = 1000L
            val endTime = 2000L
            val samples =
                listOf(
                    BatterySampleEntity("1", 1500L, 75, "DISCHARGING", 30f),
                )
            coEvery { mockDao.getSamplesInRange(startTime, endTime) } returns samples

            // When
            val result = repository.getSamplesInRange(startTime, endTime)

            // Then
            assertThat(result).hasSize(1)
            assertThat(result[0].timestamp).isEqualTo(1500L)
        }
}
