package com.example.tasktracker

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tasktracker.ui.base.BaseLocaleActivity
import com.example.tasktracker.ui.settings.SettingsActivity
import com.example.tasktracker.ui.task.TaskActivity
import com.example.tasktracker.ui.tasks.TaskListScreen
import com.example.tasktracker.ui.tasks.TaskListViewModel
import com.example.tasktracker.ui.theme.TaskTrackerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseLocaleActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ThemedContent {
                val viewModel: TaskListViewModel = hiltViewModel()
                val selectedDate by viewModel.selectedDate.collectAsState()

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            title = { Text(stringResource(R.string.task_tracker_title)) },
                            actions = {
                                IconButton(onClick = {
                                    val intent = Intent(this@MainActivity, SettingsActivity::class.java)
                                    startActivity(intent)
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Settings,
                                        contentDescription = stringResource(R.string.settings_title)
                                    )
                                }
                            },
                            colors = TopAppBarDefaults.topAppBarColors()
                        )
                    },
                    floatingActionButton = {
                        FloatingActionButton(
                            onClick = {
                                val intent = Intent(this@MainActivity, TaskActivity::class.java).apply {
                                    putExtra(TaskActivity.EXTRA_TASK_ID, -1L)
                                    putExtra(TaskActivity.EXTRA_SELECTED_DATE, selectedDate)
                                }
                                startActivity(intent)
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = stringResource(R.string.add_task)
                            )
                        }
                    }
                ) { innerPadding ->
                    TaskListScreen(
                        modifier = Modifier.padding(innerPadding),
                        viewModel = viewModel,
                        onTaskClick = { task ->
                            val intent = Intent(this@MainActivity, TaskActivity::class.java).apply {
                                putExtra(TaskActivity.EXTRA_TASK_ID, task.id)
                                putExtra(TaskActivity.EXTRA_SELECTED_DATE, task.dateEpochMillis)
                            }
                            startActivity(intent)
                        }
                    )
                }
            }
        }
    }
}