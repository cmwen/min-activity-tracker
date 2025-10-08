package io.cmwen.min_activity_tracker.data.repository

import com.google.common.truth.Truth.assertThat
import io.cmwen.min_activity_tracker.data.database.AppSessionEntity
import io.cmwen.min_activity_tracker.data.database.SessionDao
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class SessionRepositoryImplTest {
    private lateinit var repository: SessionRepository
    private lateinit var mockDao: SessionDao

    @Before
    fun setup() {
        mockDao = mockk()
        repository = SessionRepositoryImpl(mockDao)
    }

    @Test
    fun `observeSessions returns data from DAO`() =
        runTest {
            // Given
            val sessions =
                listOf(
                    AppSessionEntity(
                        id = "1",
                        packageName = "com.test.app",
                        appLabel = "Test App",
                        startTimestamp = 1000L,
                        endTimestamp = 2000L,
                        durationMs = 1000L,
                    ),
                )
            every { mockDao.getAllSessions() } returns flowOf(sessions)

            // When
            val result = repository.observeSessions().first()

            // Then
            assertThat(result).hasSize(1)
            assertThat(result[0].packageName).isEqualTo("com.test.app")
        }

    @Test
    fun `getAllSessions returns all sessions`() =
        runTest {
            // Given
            val sessions =
                listOf(
                    AppSessionEntity(
                        id = "1",
                        packageName = "com.test.app",
                        appLabel = "Test App",
                        startTimestamp = 1000L,
                        endTimestamp = 2000L,
                        durationMs = 1000L,
                    ),
                    AppSessionEntity(
                        id = "2",
                        packageName = "com.another.app",
                        appLabel = "Another App",
                        startTimestamp = 3000L,
                        endTimestamp = 4000L,
                        durationMs = 1000L,
                    ),
                )
            every { mockDao.getAllSessions() } returns flowOf(sessions)

            // When
            val result = repository.getAllSessions()

            // Then
            assertThat(result).hasSize(2)
        }

    @Test
    fun `getSessionsInRange returns filtered sessions`() =
        runTest {
            // Given
            val startTime = 1000L
            val endTime = 2000L
            val sessions =
                listOf(
                    AppSessionEntity(
                        id = "1",
                        packageName = "com.test.app",
                        appLabel = "Test App",
                        startTimestamp = 1500L,
                        endTimestamp = 1800L,
                        durationMs = 300L,
                    ),
                )
            coEvery { mockDao.getSessionsInRange(startTime, endTime) } returns sessions

            // When
            val result = repository.getSessionsInRange(startTime, endTime)

            // Then
            assertThat(result).hasSize(1)
            assertThat(result[0].startTimestamp).isEqualTo(1500L)
        }

    @Test
    fun `getSessionById returns single session`() =
        runTest {
            // Given
            val session =
                AppSessionEntity(
                    id = "test-id",
                    packageName = "com.test.app",
                    appLabel = "Test App",
                    startTimestamp = 1000L,
                    endTimestamp = 2000L,
                    durationMs = 1000L,
                )
            coEvery { mockDao.getSessionById("test-id") } returns session

            // When
            val result = repository.getSessionById("test-id")

            // Then
            assertThat(result).isNotNull()
            assertThat(result?.id).isEqualTo("test-id")
        }

    @Test
    fun `getSessionById returns null when not found`() =
        runTest {
            // Given
            coEvery { mockDao.getSessionById("nonexistent") } returns null

            // When
            val result = repository.getSessionById("nonexistent")

            // Then
            assertThat(result).isNull()
        }

    @Test
    fun `insert calls DAO insert`() =
        runTest {
            // Given
            val testSession =
                AppSessionEntity(
                    id = "1",
                    packageName = "com.test.app",
                    appLabel = "Test App",
                    startTimestamp = 1000L,
                    endTimestamp = 2000L,
                    durationMs = 1000L,
                )
            coEvery { mockDao.insert(testSession) } returns Unit

            // When
            repository.insert(testSession)

            // Then
            coVerify { mockDao.insert(testSession) }
        }

    @Test
    fun `insertAll calls DAO insertAll`() =
        runTest {
            // Given
            val sessions =
                listOf(
                    AppSessionEntity(
                        id = "1",
                        packageName = "com.test.app",
                        appLabel = "Test App",
                        startTimestamp = 1000L,
                        endTimestamp = 2000L,
                        durationMs = 1000L,
                    ),
                )
            coEvery { mockDao.insertAll(sessions) } returns Unit

            // When
            repository.insertAll(sessions)

            // Then
            coVerify { mockDao.insertAll(sessions) }
        }

    @Test
    fun `deleteById calls DAO deleteById`() =
        runTest {
            // Given
            val testId = "test-id"
            coEvery { mockDao.deleteById(testId) } returns Unit

            // When
            repository.deleteById(testId)

            // Then
            coVerify { mockDao.deleteById(testId) }
        }

    @Test
    fun `deleteSessionsOlderThan calls DAO deleteSessionsOlderThan`() =
        runTest {
            // Given
            val timestamp = 1000L
            coEvery { mockDao.deleteSessionsOlderThan(timestamp) } returns Unit

            // When
            repository.deleteSessionsOlderThan(timestamp)

            // Then
            coVerify { mockDao.deleteSessionsOlderThan(timestamp) }
        }
}
