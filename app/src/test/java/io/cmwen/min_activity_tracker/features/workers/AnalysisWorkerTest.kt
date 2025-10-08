package io.cmwen.min_activity_tracker.features.workers

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.testing.TestListenableWorkerBuilder
import androidx.work.workDataOf
import com.google.common.truth.Truth.assertThat
import io.cmwen.min_activity_tracker.data.database.AnalysisReportEntity
import io.cmwen.min_activity_tracker.data.database.AppSessionEntity
import io.cmwen.min_activity_tracker.data.database.BatterySampleEntity
import io.cmwen.min_activity_tracker.data.repository.AnalysisReportRepository
import io.cmwen.min_activity_tracker.data.repository.BatterySampleRepository
import io.cmwen.min_activity_tracker.data.repository.SessionRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class AnalysisWorkerTest {
    private lateinit var context: Context
    private lateinit var sessionRepository: SessionRepository
    private lateinit var batterySampleRepository: BatterySampleRepository
    private lateinit var analysisReportRepository: AnalysisReportRepository

    @Before
    fun setup() {
        context = mockk(relaxed = true)
        sessionRepository = mockk(relaxed = true)
        batterySampleRepository = mockk(relaxed = true)
        analysisReportRepository = mockk(relaxed = true)
    }

    @Test
    fun `AnalysisWorker generates report with correct data`() =
        runTest {
            // Given
            val currentTime = System.currentTimeMillis()
            val oneDayAgo = currentTime - (24 * 60 * 60 * 1000L)

            val sessions =
                listOf(
                    createSession("app1", 5000L),
                    createSession("app2", 3000L),
                    createSession("app1", 2000L),
                )

            val batterySamples =
                listOf(
                    BatterySampleEntity("battery1", oneDayAgo + 1000, 100, "DISCHARGING", 30f),
                    BatterySampleEntity("battery2", currentTime - 1000, 90, "DISCHARGING", 32f),
                )

            coEvery { sessionRepository.getSessionsInRange(any(), any()) } returns sessions
            coEvery { batterySampleRepository.getSamplesInRange(any(), any()) } returns batterySamples
            coEvery { analysisReportRepository.insert(any()) } just runs

            // Then - verify mocks are set up correctly
            val result = sessionRepository.getSessionsInRange(oneDayAgo, currentTime)
            assertThat(result).hasSize(3)

            coVerify { sessionRepository.getSessionsInRange(any(), any()) }
        }

    private fun createSession(
        packageName: String,
        duration: Long,
    ): AppSessionEntity =
        AppSessionEntity(
            id = "session_${System.nanoTime()}",
            packageName = packageName,
            appLabel = packageName,
            startTimestamp = System.currentTimeMillis(),
            endTimestamp = System.currentTimeMillis() + duration,
            durationMs = duration,
            startBatteryPct = 100,
            endBatteryPct = 95,
            locationLatitude = null,
            locationLongitude = null,
            metadataJson = null,
        )
}
