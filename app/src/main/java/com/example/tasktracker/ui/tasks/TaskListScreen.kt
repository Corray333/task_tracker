package com.example.tasktracker.ui.tasks

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Task
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tasktracker.domain.model.Task

@Composable
fun TaskListScreen(
    modifier: Modifier = Modifier,
    viewModel: TaskListViewModel = hiltViewModel(),
    onTaskClick: (Task) -> Unit
) {
    val tasks by viewModel.tasks.collectAsState()

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(tasks) { task ->
            TaskCard(
                task = task,
                onClick = { onTaskClick(task) }
            )
        }
    }
}

@Composable
fun TaskCard(
    task: Task,
    onClick: () -> Unit
) {
    val cardColor = parseColor(task.colorHex).copy(alpha = 0.15f)
    val iconBackgroundColor = parseColor(task.colorHex)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon with colored background
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(iconBackgroundColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = getIconForName(task.icon),
                    contentDescription = task.title,
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))

            // Task details
            Column(
                modifier = Modifier.weight(1f)
            ) {
                // Title
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                // Tags
                if (task.tags.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        task.tags.take(3).forEach { tag ->
                            TagChip(tag = tag)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TagChip(tag: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = tag,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSecondaryContainer
        )
    }
}

fun parseColor(colorHex: String?): Color {
    return try {
        colorHex?.let {
            Color(android.graphics.Color.parseColor(it))
        } ?: Color.Gray
    } catch (_: Exception) {
        Color.Gray
    }
}

fun getIconForName(iconName: String?): ImageVector {
    return when (iconName) {
        "PhoneAndroid" -> Icons.Filled.PhoneAndroid
        "ShoppingCart" -> Icons.Filled.ShoppingCart
        "FitnessCenter" -> Icons.Filled.FitnessCenter
        "MenuBook" -> Icons.Filled.MenuBook
        "MedicalServices" -> Icons.Filled.MedicalServices
        "BarChart" -> Icons.Filled.BarChart
        else -> Icons.Filled.Task
    }
}
