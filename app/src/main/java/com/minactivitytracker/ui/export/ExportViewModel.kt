package com.minactivitytracker.ui.export

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minactivitytracker.repository.ActivityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.OutputStreamWriter
import javax.inject.Inject

@HiltViewModel
class ExportViewModel @Inject constructor(
    private val repository: ActivityRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _exportStatus = MutableStateFlow<String?>(null)
    val exportStatus: StateFlow<String?> = _exportStatus

    fun exportData(uri: Uri) {
        viewModelScope.launch {
            _exportStatus.value = "Exporting..."
            try {
                val sessions = repository.getAllSessions().first()
                val json = buildString {
                    append("[")
                    sessions.forEachIndexed { index, session ->
                        append("""{"package":"${session.packageName}","start":${session.startTimestamp},"end":${session.endTimestamp}}""")
                        if (index < sessions.size - 1) append(",")
                    }
                    append("]")
                }

                withContext(Dispatchers.IO) {
                    context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                        OutputStreamWriter(outputStream).use { writer ->
                            writer.write(json)
                        }
                    }
                }
                _exportStatus.value = "Export Successful!"
            } catch (e: Exception) {
                _exportStatus.value = "Export Failed: ${e.message}"
            }
        }
    }
}
