package com.example.tasktracker.ui.task

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasktracker.domain.model.Task
import com.example.tasktracker.domain.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val taskId: Long = savedStateHandle.get<Long>("extra_task_id") ?: -1L
    private val selectedDate: Long = savedStateHandle.get<Long>("extra_selected_date") ?: System.currentTimeMillis()

    private val _task = MutableStateFlow<Task?>(null)
    val task: StateFlow<Task?> = _task.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadTask()
    }

    private fun loadTask() {
        viewModelScope.launch {
            _isLoading.value = true
            _task.value = taskRepository.getTaskById(taskId)
            _isLoading.value = false
        }
    }

    fun getSelectedDate(): Long = selectedDate
}
