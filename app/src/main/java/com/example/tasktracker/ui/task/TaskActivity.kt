package com.example.tasktracker.ui.task

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModelProvider
import com.example.tasktracker.R
import com.example.tasktracker.ui.theme.TaskTrackerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TaskActivity : ComponentActivity() {

    companion object {
        const val EXTRA_TASK_ID = "extra_task_id"
        const val EXTRA_SELECTED_DATE = "extra_selected_date"
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            TaskTrackerTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            title = { Text(stringResource(R.string.task_details_title)) },
                            navigationIcon = {
                                IconButton(onClick = { finish() }) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = stringResource(R.string.back)
                                    )
                                }
                            },
                            colors = TopAppBarDefaults.topAppBarColors()
                        )
                    }
                ) { innerPadding ->
                    TaskDetailScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
