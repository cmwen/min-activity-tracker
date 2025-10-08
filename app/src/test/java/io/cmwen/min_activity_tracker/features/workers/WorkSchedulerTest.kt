package io.cmwen.min_activity_tracker.features.workers

import android.content.Context
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.testing.WorkManagerTestInitHelper
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [33])
class WorkSchedulerTest {
    private lateinit var context: Context
    private lateinit var workScheduler: WorkScheduler
    private lateinit var workManager: WorkManager

    @Before
    fun setup() {
        context = RuntimeEnvironment.getApplication()
        WorkManagerTestInitHelper.initializeTestWorkManager(context)
        workManager = WorkManager.getInstance(context)
        workScheduler = WorkScheduler(context)
    }

    @Test
    fun `scheduleDataCollection enqueues periodic work`() {
        // When
        workScheduler.scheduleDataCollection()

        // Then
        val workInfos = workManager.getWorkInfosForUniqueWork(DataCollectionWorker.WORK_NAME).get()
        assertThat(workInfos).isNotEmpty()
        assertThat(workInfos[0].state).isIn(listOf(WorkInfo.State.ENQUEUED, WorkInfo.State.RUNNING))
    }

    @Test
    fun `scheduleDailyAnalysis enqueues periodic work`() {
        // When
        workScheduler.scheduleDailyAnalysis()

        // Then
        val workInfos = workManager.getWorkInfosForUniqueWork("${AnalysisWorker.WORK_NAME}_daily").get()
        assertThat(workInfos).isNotEmpty()
    }

    @Test
    fun `scheduleWeeklyAnalysis enqueues periodic work`() {
        // When
        workScheduler.scheduleWeeklyAnalysis()

        // Then
        val workInfos = workManager.getWorkInfosForUniqueWork("${AnalysisWorker.WORK_NAME}_weekly").get()
        assertThat(workInfos).isNotEmpty()
    }

    @Test
    fun `cancelAllWork cancels all scheduled work`() {
        // Given
        workScheduler.scheduleDataCollection()
        workScheduler.scheduleDailyAnalysis()
        workScheduler.scheduleWeeklyAnalysis()

        // When
        workScheduler.cancelAllWork()

        // Then
        val dataCollectionWork = workManager.getWorkInfosForUniqueWork(DataCollectionWorker.WORK_NAME).get()
        val dailyWork = workManager.getWorkInfosForUniqueWork("${AnalysisWorker.WORK_NAME}_daily").get()
        val weeklyWork = workManager.getWorkInfosForUniqueWork("${AnalysisWorker.WORK_NAME}_weekly").get()

        // Work should be cancelled
        dataCollectionWork.forEach { assertThat(it.state).isEqualTo(WorkInfo.State.CANCELLED) }
        dailyWork.forEach { assertThat(it.state).isEqualTo(WorkInfo.State.CANCELLED) }
        weeklyWork.forEach { assertThat(it.state).isEqualTo(WorkInfo.State.CANCELLED) }
    }

    @Test
    fun `isDataCollectionScheduled returns true when work is scheduled`() {
        // Given
        workScheduler.scheduleDataCollection()

        // When
        val isScheduled = workScheduler.isDataCollectionScheduled()

        // Then
        assertThat(isScheduled).isTrue()
    }

    @Test
    fun `isDataCollectionScheduled returns false when work is not scheduled`() {
        // When
        val isScheduled = workScheduler.isDataCollectionScheduled()

        // Then
        assertThat(isScheduled).isFalse()
    }

    @Test
    fun `multiple calls to scheduleDataCollection keeps existing work`() {
        // Given
        workScheduler.scheduleDataCollection()
        val firstWorkInfos = workManager.getWorkInfosForUniqueWork(DataCollectionWorker.WORK_NAME).get()
        val firstWorkId = firstWorkInfos[0].id

        // When
        workScheduler.scheduleDataCollection()
        val secondWorkInfos = workManager.getWorkInfosForUniqueWork(DataCollectionWorker.WORK_NAME).get()

        // Then - should keep the existing work (KEEP policy)
        assertThat(secondWorkInfos).hasSize(1)
        assertThat(secondWorkInfos[0].id).isEqualTo(firstWorkId)
    }
}
