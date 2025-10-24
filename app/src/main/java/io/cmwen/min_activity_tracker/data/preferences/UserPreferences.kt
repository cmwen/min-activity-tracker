package io.cmwen.min_activity_tracker.data.preferences

data class UserPreferences(
    val collectAppUsage: Boolean = true,
    val collectBattery: Boolean = true,
    val collectLocation: Boolean = false,
    val collectActivityRecognition: Boolean = false,
    val anonymizeLocationData: Boolean = false,
) {
    val hasAnyCollectionEnabled: Boolean
        get() = collectAppUsage || collectBattery
}
