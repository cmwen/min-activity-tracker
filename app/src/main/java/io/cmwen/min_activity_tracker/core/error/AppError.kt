package io.cmwen.min_activity_tracker.core.error

/**
 * Base sealed class for all application errors
 */
sealed class AppError(
    open val message: String,
    open val cause: Throwable? = null
) : Exception(message, cause) {
    
    /**
     * Database related errors
     */
    sealed class DatabaseError(
        override val message: String,
        override val cause: Throwable? = null
    ) : AppError(message, cause) {
        
        class ConnectionError(cause: Throwable) : DatabaseError(
            "Database connection failed",
            cause
        )
        
        class CorruptionError(cause: Throwable) : DatabaseError(
            "Database corruption detected",
            cause
        )
        
        class MigrationError(cause: Throwable) : DatabaseError(
            "Database migration failed",
            cause
        )
    }
    
    /**
     * Permission related errors
     */
    sealed class PermissionError(
        override val message: String,
        override val cause: Throwable? = null
    ) : AppError(message, cause) {
        
        object UsageStatsNotGranted : PermissionError(
            "Usage Stats permission not granted"
        )
        
        object LocationPermissionDenied : PermissionError(
            "Location permission denied"
        )
        
        object NotificationAccessDenied : PermissionError(
            "Notification access permission denied"
        )
    }
    
    /**
     * Data collection errors
     */
    sealed class DataCollectionError(
        override val message: String,
        override val cause: Throwable? = null
    ) : AppError(message, cause) {
        
        class ServiceUnavailable(service: String) : DataCollectionError(
            "Service $service is unavailable"
        )
        
        class DataCorrupted(details: String) : DataCollectionError(
            "Data corruption detected: $details"
        )
    }
    
    /**
     * Unknown/unexpected errors
     */
    class UnknownError(
        override val message: String = "An unexpected error occurred",
        override val cause: Throwable? = null
    ) : AppError(message, cause)
}