package com.minactivitytracker.util

/**
 * Utility functions for formatting data for display
 */
object FormatUtils {
    /**
     * Format duration in milliseconds to human-readable string
     * Examples: "2h 30m", "45m", "30s"
     */
    fun formatDuration(millis: Long): String {
        val seconds = millis / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        return when {
            hours > 0 -> "${hours}h ${minutes % 60}m"
            minutes > 0 -> "${minutes}m"
            else -> "${seconds}s"
        }
    }

    /**
     * Format duration to decimal hours
     * Example: 90 minutes -> "1.5h"
     */
    fun formatDurationToHours(millis: Long): String {
        val hours = millis / 3600000.0
        return String.format("%.1fh", hours)
    }

    /**
     * Format duration to minutes
     * Example: 90000ms -> "1.5m"
     */
    fun formatDurationToMinutes(millis: Long): String {
        val minutes = millis / 60000.0
        return String.format("%.1fm", minutes)
    }

    /**
     * Extract app name from package name
     * Example: "com.example.myapp" -> "myapp"
     */
    fun getAppNameFromPackage(packageName: String): String {
        return packageName.split(".").lastOrNull() ?: packageName
    }

    /**
     * Format location coordinates for display
     * Example: (37.7749, -122.4194) -> "37.77°N, 122.42°W"
     */
    fun formatLocation(latitude: Double, longitude: Double): String {
        val latDir = if (latitude >= 0) "N" else "S"
        val lonDir = if (longitude >= 0) "E" else "W"
        return String.format("%.2f°%s, %.2f°%s", 
            kotlin.math.abs(latitude), latDir,
            kotlin.math.abs(longitude), lonDir)
    }
}
