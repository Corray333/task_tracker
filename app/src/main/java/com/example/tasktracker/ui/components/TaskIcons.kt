package com.example.tasktracker.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import com.example.tasktracker.R

data class TaskIcon(
    val name: String,
    val resourceId: Int
)

object TaskIcons {
    val icons = listOf(
        TaskIcon("work", R.drawable.ic_task_work),
        TaskIcon("shopping", R.drawable.ic_task_shopping),
        TaskIcon("health", R.drawable.ic_task_health),
        TaskIcon("fitness", R.drawable.ic_task_fitness),
        TaskIcon("study", R.drawable.ic_task_study),
        TaskIcon("food", R.drawable.ic_task_food),
        TaskIcon("travel", R.drawable.ic_task_travel),
        TaskIcon("home", R.drawable.ic_task_home),
        TaskIcon("phone", R.drawable.ic_task_phone),
        TaskIcon("calendar", R.drawable.ic_task_calendar),
        TaskIcon("music", R.drawable.ic_task_music),
        TaskIcon("game", R.drawable.ic_task_game),
        TaskIcon("car", R.drawable.ic_task_car),
        TaskIcon("gift", R.drawable.ic_task_gift),
        TaskIcon("star", R.drawable.ic_task_star),
        TaskIcon("heart", R.drawable.ic_task_heart),
        TaskIcon("email", R.drawable.ic_task_email),
        TaskIcon("camera", R.drawable.ic_task_camera),
        TaskIcon("task", R.drawable.ic_task_task),
    )

    fun getIconResource(iconName: String?): Int {
        return icons.find { it.name == iconName }?.resourceId ?: R.drawable.ic_task_work
    }
}

@Composable
fun getIconPainter(iconName: String?): Painter {
    return painterResource(id = TaskIcons.getIconResource(iconName))
}
