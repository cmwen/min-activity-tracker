package io.cmwen.min_activity_tracker.data.preferences

import io.cmwen.min_activity_tracker.features.export.ExportFormat
import io.cmwen.min_activity_tracker.features.export.ExportTimeRange

data class UserPreferences(
    val collectAppUsage: Boolean = true,
    val collectBattery: Boolean = true,
    val collectLocation: Boolean = false,
    val collectActivityRecognition: Boolean = false,
    val anonymizeLocationData: Boolean = false,
    val autoExportEnabled: Boolean = false,
    val autoExportFormat: ExportFormat = ExportFormat.JSON,
    val autoExportRange: ExportTimeRange = ExportTimeRange.LAST_24_HOURS,
    val autoExportAnonymize: Boolean = false,
) {
    val hasAnyCollectionEnabled: Boolean
        get() = collectAppUsage || collectBattery
}
