package io.cmwen.min_activity_tracker.core.error

/**
 * Base sealed class for all application errors
 */
sealed class AppError(
    // Use non-conflicting parameter names to avoid hiding Throwable members
    open val msg: String,
    open val errorCause: Throwable? = null,
) : Exception(msg, errorCause) {
    /**
     * Database related errors
     */
    sealed class DatabaseError(
        override val msg: String,
        override val errorCause: Throwable? = null,
    ) : AppError(msg, errorCause) {
        class ConnectionError(
            cause: Throwable,
        ) : DatabaseError(
                "Database connection failed",
                cause,
            )

        class CorruptionError(
            cause: Throwable,
        ) : DatabaseError(
                "Database corruption detected",
                cause,
            )

        class MigrationError(
            cause: Throwable,
        ) : DatabaseError(
                "Database migration failed",
                cause,
            )
    }

    /**
     * Permission related errors
     */
    sealed class PermissionError(
        override val msg: String,
        override val errorCause: Throwable? = null,
    ) : AppError(msg, errorCause) {
        object UsageStatsNotGranted : PermissionError(
            "Usage Stats permission not granted",
        )

        object LocationPermissionDenied : PermissionError(
            "Location permission denied",
        )

        object NotificationAccessDenied : PermissionError(
            "Notification access permission denied",
        )
    }

    /**
     * Data collection errors
     */
    sealed class DataCollectionError(
        override val msg: String,
        override val errorCause: Throwable? = null,
    ) : AppError(msg, errorCause) {
        class ServiceUnavailable(
            service: String,
        ) : DataCollectionError(
                "Service $service is unavailable",
            )

        class DataCorrupted(
            details: String,
        ) : DataCollectionError(
                "Data corruption detected: $details",
            )
    }

    /**
     * Unknown/unexpected errors
     */
    class UnknownError(
        override val msg: String = "An unexpected error occurred",
        override val errorCause: Throwable? = null,
    ) : AppError(msg, errorCause)
}
