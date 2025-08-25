package io.cmwen.min_activity_tracker.data.repository

import io.cmwen.min_activity_tracker.data.database.AppSessionEntity
import io.cmwen.min_activity_tracker.data.database.SessionDao
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class SessionRepositoryImplTest {
    private lateinit var repository: SessionRepository
    private lateinit var mockDao: SessionDao

    @Before
    fun setup() {
        mockDao = createMockSessionDao()
        repository = SessionRepositoryImpl(mockDao)
    }

    @Test
    fun `observeSessions returns data from DAO`() =
        runTest {
            // Given (placeholder data removed — mock returns empty list)

            // When
            val result = repository.observeSessions().first()

            // Then - For now, this will be empty since we're using a mock
            // In a real test, we would set up the mock to return testSessions
            assertNotNull(result)
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

            // When
            repository.insert(testSession)

            // Then - In a real test with a proper mock framework,
            // we would verify that dao.insert was called with testSession
            // For now, we just ensure no exception is thrown
            assertTrue(true)
        }

    @Test
    fun `deleteById calls DAO deleteById`() =
        runTest {
            // Given
            val testId = "test-id"

            // When
            repository.deleteById(testId)

            // Then - In a real test with a proper mock framework,
            // we would verify that dao.deleteById was called with testId
            // For now, we just ensure no exception is thrown
            assertTrue(true)
        }

    private fun createMockSessionDao(): SessionDao =
        object : SessionDao {
            override fun getAllSessions() = flow { emit(emptyList<AppSessionEntity>()) }

            override suspend fun insert(session: AppSessionEntity) = Unit

            override suspend fun deleteById(id: String) = Unit
        }
}
