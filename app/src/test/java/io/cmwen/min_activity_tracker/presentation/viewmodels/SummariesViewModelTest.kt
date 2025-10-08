package io.cmwen.min_activity_tracker.presentation.viewmodels

import app.cash.turbine.test
import io.cmwen.min_activity_tracker.data.database.AppSessionEntity
import io.cmwen.min_activity_tracker.data.repository.SessionRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class SummariesViewModelTest {
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var sessionRepository: SessionRepository
    private lateinit var viewModel: SummariesViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        sessionRepository = mockk(relaxed = true)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `summaries flow should emit summaries of sessions`() =
        runTest {
            val sessions =
                listOf(
                    AppSessionEntity("1", "app1", "App 1", 0, 100, 100),
                    AppSessionEntity("2", "app2", "App 2", 0, 200, 200),
                    AppSessionEntity("3", "app1", "App 1", 200, 350, 150),
                )
            every { sessionRepository.observeSessions() } returns flowOf(sessions)

            viewModel = SummariesViewModel(sessionRepository)

            viewModel.summaries.test {
                awaitItem() // initial empty value
                val summaries = awaitItem()
                assertEquals(2, summaries.size)
                assertEquals(250L, summaries["app1"])
                assertEquals(200L, summaries["app2"])
                cancelAndIgnoreRemainingEvents()
            }
        }
}
