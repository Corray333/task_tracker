package com.example.tasktracker.ui.task

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Task
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tasktracker.R
import com.example.tasktracker.ui.tasks.parseColor

@Composable
fun TaskDetailScreen(
    modifier: Modifier = Modifier,
    onTaskSaved: () -> Unit = {},
    viewModel: TaskViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    // Navigate back when task is saved
    LaunchedEffect(uiState.taskSaved) {
        if (uiState.taskSaved) {
            onTaskSaved()
        }
    }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            else -> {
                TaskEditContent(
                    uiState = uiState,
                    onTitleChange = viewModel::onTitleChange,
                    onDescriptionChange = viewModel::onDescriptionChange,
                    onTagsChange = viewModel::onTagsChange,
                    onPriorityChange = viewModel::onPriorityChange,
                    onIconChange = viewModel::onIconChange,
                    onColorChange = viewModel::onColorChange,
                    onSave = viewModel::saveTask,
                    isNewTask = viewModel.isNewTask()
                )
            }
        }
    }
}

@Composable
fun TaskEditContent(
    uiState: TaskUiState,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onTagsChange: (String) -> Unit,
    onPriorityChange: (Int) -> Unit,
    onIconChange: (String) -> Unit,
    onColorChange: (String) -> Unit,
    onSave: () -> Unit,
    isNewTask: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = if (isNewTask) stringResource(R.string.new_task) else stringResource(R.string.edit_task),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        OutlinedTextField(
            value = uiState.title,
            onValueChange = onTitleChange,
            label = { Text(stringResource(R.string.task_title)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = uiState.description,
            onValueChange = onDescriptionChange,
            label = { Text(stringResource(R.string.task_description)) },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3,
            maxLines = 6
        )

        OutlinedTextField(
            value = uiState.tags,
            onValueChange = onTagsChange,
            label = { Text(stringResource(R.string.task_tags)) },
            placeholder = { Text(stringResource(R.string.task_tags_placeholder)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        // Icon selector
        IconSelector(
            selectedIcon = uiState.icon,
            onIconSelected = onIconChange
        )

        // Color selector
        ColorSelector(
            selectedColor = uiState.colorHex,
            onColorSelected = onColorChange
        )

        // Priority selector
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = stringResource(R.string.task_priority),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                PriorityButton(
                    text = stringResource(R.string.priority_high),
                    isSelected = uiState.priority == 1,
                    onClick = { onPriorityChange(1) },
                    modifier = Modifier.weight(1f)
                )
                PriorityButton(
                    text = stringResource(R.string.priority_medium),
                    isSelected = uiState.priority == 2,
                    onClick = { onPriorityChange(2) },
                    modifier = Modifier.weight(1f)
                )
                PriorityButton(
                    text = stringResource(R.string.priority_low),
                    isSelected = uiState.priority == 3,
                    onClick = { onPriorityChange(3) },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Save button
        Button(
            onClick = onSave,
            modifier = Modifier.fillMaxWidth(),
            enabled = uiState.title.isNotBlank() && !uiState.isSaving
        ) {
            if (uiState.isSaving) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text(stringResource(R.string.save))
            }
        }
    }
}

@Composable
fun PriorityButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier,
        colors = if (isSelected) {
            androidx.compose.material3.ButtonDefaults.outlinedButtonColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        } else {
            androidx.compose.material3.ButtonDefaults.outlinedButtonColors()
        }
    ) {
        Text(text)
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun IconSelector(
    selectedIcon: String,
    onIconSelected: (String) -> Unit
) {
    val icons = listOf(
        "Task" to Icons.Filled.Task,
        "PhoneAndroid" to Icons.Filled.PhoneAndroid,
        "ShoppingCart" to Icons.Filled.ShoppingCart,
        "FitnessCenter" to Icons.Filled.FitnessCenter,
        "MenuBook" to Icons.Filled.MenuBook,
        "MedicalServices" to Icons.Filled.MedicalServices,
        "BarChart" to Icons.Filled.BarChart
    )

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = stringResource(R.string.icon_label),
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
        )
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            icons.forEach { (iconName, icon) ->
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(
                            if (selectedIcon == iconName)
                                MaterialTheme.colorScheme.primaryContainer
                            else
                                MaterialTheme.colorScheme.surfaceVariant
                        )
                        .clickable { onIconSelected(iconName) },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = iconName,
                        tint = if (selectedIcon == iconName)
                            MaterialTheme.colorScheme.onPrimaryContainer
                        else
                            MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ColorSelector(
    selectedColor: String,
    onColorSelected: (String) -> Unit
) {
    val colors = listOf(
        "#F44336" to "Red",
        "#E91E63" to "Pink",
        "#9C27B0" to "Purple",
        "#673AB7" to "Deep Purple",
        "#3F51B5" to "Indigo",
        "#2196F3" to "Blue",
        "#03A9F4" to "Light Blue",
        "#00BCD4" to "Cyan",
        "#009688" to "Teal",
        "#4CAF50" to "Green",
        "#8BC34A" to "Light Green",
        "#CDDC39" to "Lime",
        "#FFEB3B" to "Yellow",
        "#FFC107" to "Amber",
        "#FF9800" to "Orange",
        "#FF5722" to "Deep Orange"
    )

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = stringResource(R.string.color_label),
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
        )
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            colors.forEach { (colorHex, colorName) ->
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(parseColor(colorHex))
                        .then(
                            if (selectedColor == colorHex) {
                                Modifier.padding(4.dp)
                            } else {
                                Modifier
                            }
                        )
                        .clip(CircleShape)
                        .background(
                            if (selectedColor == colorHex)
                                MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)
                            else
                                Color.Transparent
                        )
                        .clickable { onColorSelected(colorHex) }
                )
            }
        }
    }
}

