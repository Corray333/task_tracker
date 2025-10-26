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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TaskUiState(
    val isLoading: Boolean = true,
    val isSaving: Boolean = false,
    val title: String = "",
    val description: String = "",
    val tags: String = "",
    val priority: Int = 1,
    val icon: String = "Task",
    val colorHex: String = "#9C27B0",
    val taskSaved: Boolean = false
)

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val taskId: Long = savedStateHandle.get<Long>("extra_task_id") ?: -1L
    private val selectedDate: Long = savedStateHandle.get<Long>("extra_selected_date") ?: System.currentTimeMillis()

    private val _uiState = MutableStateFlow(TaskUiState())
    val uiState: StateFlow<TaskUiState> = _uiState.asStateFlow()

    init {
        loadTask()
    }

    private fun loadTask() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            if (taskId == -1L) {
                // Creating new task
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        title = "",
                        description = "",
                        tags = "",
                        priority = 1,
                        icon = "Task",
                        colorHex = "#9C27B0"
                    )
                }
            } else {
                // Loading existing task
                val task = taskRepository.getTaskById(taskId)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        title = task?.title ?: "",
                        description = task?.description ?: "",
                        tags = task?.tags?.joinToString(", ") ?: "",
                        priority = task?.priority ?: 1,
                        icon = task?.icon ?: "Task",
                        colorHex = task?.colorHex ?: "#9C27B0"
                    )
                }
            }
        }
    }


    fun onTitleChange(title: String) {
        _uiState.update { it.copy(title = title) }
    }

    fun onDescriptionChange(description: String) {
        _uiState.update { it.copy(description = description) }
    }

    fun onTagsChange(tags: String) {
        _uiState.update { it.copy(tags = tags) }
    }

    fun onPriorityChange(priority: Int) {
        _uiState.update { it.copy(priority = priority) }
    }

    fun onIconChange(icon: String) {
        _uiState.update { it.copy(icon = icon) }
    }

    fun onColorChange(colorHex: String) {
        _uiState.update { it.copy(colorHex = colorHex) }
    }

    fun saveTask() {
        val currentState = _uiState.value

        if (currentState.title.isBlank()) {
            return // Don't save if title is empty
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }

            val tagsList = currentState.tags
                .split(",")
                .map { it.trim() }
                .filter { it.isNotEmpty() }

            val task = Task(
                id = if (taskId == -1L) 0 else taskId,
                title = currentState.title,
                description = currentState.description.ifBlank { null },
                tags = tagsList,
                dateEpochMillis = selectedDate,
                icon = currentState.icon.ifBlank { "Task" },
                colorHex = currentState.colorHex.ifBlank { "#9C27B0" },
                priority = currentState.priority
            )

            if (taskId == -1L) {
                // Create new task
                taskRepository.insertTask(task)
            } else {
                // Update existing task
                taskRepository.updateTask(task)
            }

            _uiState.update {
                it.copy(
                    isSaving = false,
                    taskSaved = true
                )
            }
        }
    }

    fun getSelectedDate(): Long = selectedDate

    fun isNewTask(): Boolean = taskId == -1L
}
