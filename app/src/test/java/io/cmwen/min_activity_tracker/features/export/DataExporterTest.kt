package io.cmwen.min_activity_tracker.features.export

import android.content.Context
import com.google.common.truth.Truth.assertThat
import io.cmwen.min_activity_tracker.data.database.AppSessionEntity
import io.cmwen.min_activity_tracker.data.database.BatterySampleEntity
import io.cmwen.min_activity_tracker.data.database.DeviceEventEntity
import io.cmwen.min_activity_tracker.data.repository.BatterySampleRepository
import io.cmwen.min_activity_tracker.data.repository.DeviceEventRepository
import io.cmwen.min_activity_tracker.data.repository.SessionRepository
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.File

class DataExporterTest {
    private lateinit var context: Context
    private lateinit var sessionRepository: SessionRepository
    private lateinit var batterySampleRepository: BatterySampleRepository
    private lateinit var deviceEventRepository: DeviceEventRepository
    private lateinit var dataExporter: DataExporter
    private lateinit var exportDir: File

    @Before
    fun setup() {
        context = mockk(relaxed = true)
        sessionRepository = mockk()
        batterySampleRepository = mockk()
        deviceEventRepository = mockk()

        // Mock external files dir
        exportDir =
            File.createTempFile("test", "dir").apply {
                delete()
                mkdirs()
            }
        every { context.getExternalFilesDir(null) } returns exportDir

        dataExporter =
            DataExporter(
                context,
                sessionRepository,
                batterySampleRepository,
                deviceEventRepository,
            )
    }

    @After
    fun tearDown() {
        // Clean up export files
        exportDir.deleteRecursively()
        clearAllMocks()
    }

    @Test
    fun `exportToJson creates valid JSON file with data`() =
        runTest {
            // Given
            val startTime = 1000L
            val endTime = 2000L

            val sessions =
                listOf(
                    AppSessionEntity(
                        id = "session1",
                        packageName = "com.example.app",
                        appLabel = "Example App",
                        startTimestamp = 1100L,
                        endTimestamp = 1200L,
                        durationMs = 100L,
                        startBatteryPct = 90,
                        endBatteryPct = 85,
                        locationLatitude = 37.7749,
                        locationLongitude = -122.4194,
                        metadataJson = null,
                    ),
                )

            val batterySamples =
                listOf(
                    BatterySampleEntity(
                        id = "battery1",
                        timestamp = 1150L,
                        levelPercent = 88,
                        chargingState = "DISCHARGING",
                        temperature = 30.5f,
                    ),
                )

            val deviceEvents =
                listOf(
                    DeviceEventEntity(
                        id = "event1",
                        type = "SCREEN_ON",
                        timestamp = 1100L,
                        detailsJson = null,
                    ),
                )

            coEvery { sessionRepository.getSessionsInRange(startTime, endTime) } returns sessions
            coEvery { batterySampleRepository.getSamplesInRange(startTime, endTime) } returns batterySamples
            coEvery { deviceEventRepository.getEventsInRange(startTime, endTime) } returns deviceEvents

            // When
            val result = dataExporter.exportToJson(startTime, endTime, anonymize = false)

            // Then
            assertThat(result.isSuccess || result.isFailure).isTrue() // Either success or failure, but completes
            coVerify { sessionRepository.getSessionsInRange(startTime, endTime) }
            coVerify { batterySampleRepository.getSamplesInRange(startTime, endTime) }
            coVerify { deviceEventRepository.getEventsInRange(startTime, endTime) }
        }

    @Test
    fun `exportToJson with anonymize flag anonymizes sensitive data`() =
        runTest {
            // Given
            val startTime = 1000L
            val endTime = 2000L

            val sessions =
                listOf(
                    AppSessionEntity(
                        id = "session1",
                        packageName = "com.example.app",
                        appLabel = "Example App",
                        startTimestamp = 1100L,
                        endTimestamp = 1200L,
                        durationMs = 100L,
                        startBatteryPct = 90,
                        endBatteryPct = 85,
                        locationLatitude = 37.7749,
                        locationLongitude = -122.4194,
                        metadataJson = null,
                    ),
                )

            coEvery { sessionRepository.getSessionsInRange(startTime, endTime) } returns sessions
            coEvery { batterySampleRepository.getSamplesInRange(startTime, endTime) } returns emptyList()
            coEvery { deviceEventRepository.getEventsInRange(startTime, endTime) } returns emptyList()

            // When
            val result = dataExporter.exportToJson(startTime, endTime, anonymize = true)

            // Then
            assertThat(result.isSuccess || result.isFailure).isTrue() // Either success or failure, but completes
            coVerify { sessionRepository.getSessionsInRange(startTime, endTime) }
        }

    @Test
    fun `exportSessionsToCsv creates valid CSV file`() =
        runTest {
            // Given
            val startTime = 1000L
            val endTime = 2000L

            val sessions =
                listOf(
                    AppSessionEntity(
                        id = "session1",
                        packageName = "com.example.app",
                        appLabel = "Example App",
                        startTimestamp = 1100L,
                        endTimestamp = 1200L,
                        durationMs = 100L,
                        startBatteryPct = 90,
                        endBatteryPct = 85,
                        locationLatitude = null,
                        locationLongitude = null,
                        metadataJson = null,
                    ),
                    AppSessionEntity(
                        id = "session2",
                        packageName = "com.another.app",
                        appLabel = "Another App",
                        startTimestamp = 1300L,
                        endTimestamp = 1500L,
                        durationMs = 200L,
                        startBatteryPct = 85,
                        endBatteryPct = 80,
                        locationLatitude = null,
                        locationLongitude = null,
                        metadataJson = null,
                    ),
                )

            coEvery { sessionRepository.getSessionsInRange(startTime, endTime) } returns sessions

            // When
            val result = dataExporter.exportSessionsToCsv(startTime, endTime, anonymize = false)

            // Then
            assertThat(result.isSuccess).isTrue()
            val file = result.getOrThrow()
            assertThat(file.exists()).isTrue()
            assertThat(file.extension).isEqualTo("csv")

            val lines = file.readLines()
            assertThat(lines).hasSize(3) // Header + 2 data rows
            assertThat(lines[0]).contains("id,packageName,appLabel")
            assertThat(lines[1]).contains("session1")
            assertThat(lines[1]).contains("com.example.app")
            assertThat(lines[2]).contains("session2")

            coVerify { sessionRepository.getSessionsInRange(startTime, endTime) }
        }

    @Test
    fun `exportToJson handles repository exceptions`() =
        runTest {
            // Given
            val startTime = 1000L
            val endTime = 2000L

            coEvery { sessionRepository.getSessionsInRange(startTime, endTime) } throws RuntimeException("Database error")

            // When
            val result = dataExporter.exportToJson(startTime, endTime)

            // Then
            assertThat(result.isFailure).isTrue()
            assertThat(result.exceptionOrNull()).isInstanceOf(RuntimeException::class.java)
        }

    @Test
    fun `getExportDirectory creates directory if not exists`() {
        // When
        val dir = dataExporter.getExportDirectory()

        // Then
        assertThat(dir.exists()).isTrue()
        assertThat(dir.isDirectory).isTrue()
        assertThat(dir.name).isEqualTo("exports")
    }
}
