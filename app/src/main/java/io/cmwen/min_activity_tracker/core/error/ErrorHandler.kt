package io.cmwen.min_activity_tracker.core.error

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Global error handler for the application.
 * Collects and manages errors from different parts of the app.
 */
class ErrorHandler {
    private val _currentError = MutableStateFlow<AppError?>(null)
    val currentError: StateFlow<AppError?> = _currentError.asStateFlow()

    private val _errorHistory = MutableStateFlow<List<AppError>>(emptyList())
    val errorHistory: StateFlow<List<AppError>> = _errorHistory.asStateFlow()

    /**
     * Handle a new error
     */
    fun handleError(error: AppError) {
        _currentError.value = error

        // Add to history (keep last 10 errors)
        val currentHistory = _errorHistory.value.toMutableList()
        currentHistory.add(0, error)
        if (currentHistory.size > MAX_ERROR_HISTORY) {
            currentHistory.removeAt(currentHistory.lastIndex)
        }
        _errorHistory.value = currentHistory
    }

    /**
     * Handle throwable and convert to AppError
     */
    fun handleThrowable(throwable: Throwable) {
        val error =
            when (throwable) {
                is AppError -> throwable
                is SecurityException -> AppError.PermissionError.UsageStatsNotGranted
                else -> AppError.UnknownError(throwable.message ?: "Unknown error", throwable)
            }
        handleError(error)
    }

    /**
     * Clear current error
     */
    fun clearCurrentError() {
        _currentError.value = null
    }

    /**
     * Clear all errors
     */
    fun clearAllErrors() {
        _currentError.value = null
        _errorHistory.value = emptyList()
    }

    companion object {
        private const val MAX_ERROR_HISTORY = 10

        @Volatile
        private var instance: ErrorHandler? = null

        fun getInstance(): ErrorHandler =
            instance ?: synchronized(this) {
                instance ?: ErrorHandler().also { instance = it }
            }
    }
}
