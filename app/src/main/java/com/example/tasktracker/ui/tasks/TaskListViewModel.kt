package com.example.tasktracker.ui.tasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasktracker.domain.model.Task
import com.example.tasktracker.domain.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class TaskListViewModel @Inject constructor(
    private val taskRepository: TaskRepository
) : ViewModel() {

    private val _selectedDate = MutableStateFlow(getCurrentDayStart())
    val selectedDate: StateFlow<Long> = _selectedDate.asStateFlow()

    val tasks: StateFlow<List<Task>> = combine(
        taskRepository.getTasks(),
        _selectedDate
    ) { allTasks, selectedDate ->
        allTasks.filter { task ->
            isSameDay(task.dateEpochMillis, selectedDate)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun selectDate(dateMillis: Long) {
        _selectedDate.value = dateMillis
    }

    private fun getCurrentDayStart(): Long {
        return Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
    }

    private fun isSameDay(date1: Long, date2: Long): Boolean {
        val cal1 = Calendar.getInstance().apply {
            timeInMillis = date1
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        val cal2 = Calendar.getInstance().apply {
            timeInMillis = date2
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        return cal1.timeInMillis == cal2.timeInMillis
    }
}
