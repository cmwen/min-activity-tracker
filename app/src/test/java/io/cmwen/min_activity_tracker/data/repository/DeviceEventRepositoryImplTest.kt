package io.cmwen.min_activity_tracker.data.repository

import com.google.common.truth.Truth.assertThat
import io.cmwen.min_activity_tracker.data.database.DeviceEventDao
import io.cmwen.min_activity_tracker.data.database.DeviceEventEntity
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class DeviceEventRepositoryImplTest {
    private lateinit var repository: DeviceEventRepository
    private lateinit var mockDao: DeviceEventDao

    @Before
    fun setup() {
        mockDao = mockk()
        repository = DeviceEventRepositoryImpl(mockDao)
    }

    @Test
    fun `observeAllEvents returns data from DAO`() =
        runTest {
            // Given
            val events =
                listOf(
                    DeviceEventEntity(
                        id = "1",
                        type = "SCREEN_ON",
                        timestamp = 1000L,
                        detailsJson = null,
                    ),
                )
            every { mockDao.getAllEvents() } returns flowOf(events)

            // When
            val result = repository.observeAllEvents().first()

            // Then
            assertThat(result).hasSize(1)
            assertThat(result[0].type).isEqualTo("SCREEN_ON")
        }

    @Test
    fun `observeEventsByType returns filtered events`() =
        runTest {
            // Given
            val type = "SCREEN_ON"
            val events =
                listOf(
                    DeviceEventEntity("1", type, 1000L, null),
                )
            every { mockDao.getEventsByType(type) } returns flowOf(events)

            // When
            val result = repository.observeEventsByType(type).first()

            // Then
            assertThat(result).hasSize(1)
            assertThat(result[0].type).isEqualTo(type)
        }

    @Test
    fun `observeEventsByTimeRange returns filtered events`() =
        runTest {
            // Given
            val startTime = 1000L
            val endTime = 2000L
            val events =
                listOf(
                    DeviceEventEntity("1", "SCREEN_ON", 1500L, null),
                )
            every { mockDao.getEventsByTimeRange(startTime, endTime) } returns flowOf(events)

            // When
            val result = repository.observeEventsByTimeRange(startTime, endTime).first()

            // Then
            assertThat(result).hasSize(1)
            assertThat(result[0].timestamp).isEqualTo(1500L)
        }

    @Test
    fun `getEventsInRange returns correct events`() =
        runTest {
            // Given
            val startTime = 1000L
            val endTime = 2000L
            val events =
                listOf(
                    DeviceEventEntity("1", "SCREEN_ON", 1500L, null),
                )
            coEvery { mockDao.getEventsInRange(startTime, endTime) } returns events

            // When
            val result = repository.getEventsInRange(startTime, endTime)

            // Then
            assertThat(result).hasSize(1)
            assertThat(result[0].timestamp).isEqualTo(1500L)
        }

    @Test
    fun `insert calls DAO insert`() =
        runTest {
            // Given
            val event = DeviceEventEntity("1", "SCREEN_ON", 1000L, null)
            coEvery { mockDao.insert(event) } returns Unit

            // When
            repository.insert(event)

            // Then
            coVerify { mockDao.insert(event) }
        }

    @Test
    fun `insertAll calls DAO insertAll`() =
        runTest {
            // Given
            val events =
                listOf(
                    DeviceEventEntity("1", "SCREEN_ON", 1000L, null),
                    DeviceEventEntity("2", "SCREEN_OFF", 2000L, null),
                )
            coEvery { mockDao.insertAll(events) } returns Unit

            // When
            repository.insertAll(events)

            // Then
            coVerify { mockDao.insertAll(events) }
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
    fun `deleteOldEvents calls DAO deleteOldEvents`() =
        runTest {
            // Given
            val timestamp = 1000L
            coEvery { mockDao.deleteOldEvents(timestamp) } returns Unit

            // When
            repository.deleteOldEvents(timestamp)

            // Then
            coVerify { mockDao.deleteOldEvents(timestamp) }
        }
}
