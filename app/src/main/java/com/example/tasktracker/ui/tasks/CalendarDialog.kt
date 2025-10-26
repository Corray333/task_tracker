package com.example.tasktracker.ui.tasks

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.tasktracker.R
import java.text.DateFormatSymbols
import java.util.Calendar
import java.util.Locale

@Composable
fun CalendarDialog(
    selectedDate: Long,
    onDateSelected: (Long) -> Unit,
    onDismiss: () -> Unit
) {
    val calendar = Calendar.getInstance().apply {
        timeInMillis = selectedDate
    }

    var currentYear by remember { mutableIntStateOf(calendar.get(Calendar.YEAR)) }
    var currentMonth by remember { mutableIntStateOf(calendar.get(Calendar.MONTH)) }
    var selectedDay by remember { mutableIntStateOf(calendar.get(Calendar.DAY_OF_MONTH)) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Year and Month selector
                YearMonthSelector(
                    year = currentYear,
                    month = currentMonth,
                    onYearChanged = { currentYear = it },
                    onMonthChanged = { currentMonth = it }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Calendar grid
                CalendarGrid(
                    year = currentYear,
                    month = currentMonth,
                    selectedDay = selectedDay,
                    onDaySelected = { selectedDay = it }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Action buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text(stringResource(R.string.cancel))
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    TextButton(onClick = {
                        val selectedCalendar = Calendar.getInstance().apply {
                            set(Calendar.YEAR, currentYear)
                            set(Calendar.MONTH, currentMonth)
                            set(Calendar.DAY_OF_MONTH, selectedDay)
                            set(Calendar.HOUR_OF_DAY, 0)
                            set(Calendar.MINUTE, 0)
                            set(Calendar.SECOND, 0)
                            set(Calendar.MILLISECOND, 0)
                        }
                        onDateSelected(selectedCalendar.timeInMillis)
                        onDismiss()
                    }) {
                        Text(stringResource(R.string.ok))
                    }
                }
            }
        }
    }
}

@Composable
private fun YearMonthSelector(
    year: Int,
    month: Int,
    onYearChanged: (Int) -> Unit,
    onMonthChanged: (Int) -> Unit
) {
    var showYearMenu by remember { mutableStateOf(false) }
    var showMonthMenu by remember { mutableStateOf(false) }

    val monthNames = DateFormatSymbols(Locale.getDefault()).months
    val currentYear = Calendar.getInstance().get(Calendar.YEAR)
    val years = (currentYear - 10..currentYear + 10).toList()

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Month selector
        Box {
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { showMonthMenu = true }
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = monthNames[month],
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
            }

            DropdownMenu(
                expanded = showMonthMenu,
                onDismissRequest = { showMonthMenu = false }
            ) {
                monthNames.take(12).forEachIndexed { index, monthName ->
                    DropdownMenuItem(
                        text = { Text(monthName) },
                        onClick = {
                            onMonthChanged(index)
                            showMonthMenu = false
                        }
                    )
                }
            }
        }

        // Navigation arrows and year
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    if (month == 0) {
                        onMonthChanged(11)
                        onYearChanged(year - 1)
                    } else {
                        onMonthChanged(month - 1)
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowLeft,
                    contentDescription = stringResource(R.string.previous_month)
                )
            }

            // Year selector
            Box {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { showYearMenu = true }
                        .padding(horizontal = 8.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = year.toString(),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                }

                DropdownMenu(
                    expanded = showYearMenu,
                    onDismissRequest = { showYearMenu = false }
                ) {
                    years.forEach { yearOption ->
                        DropdownMenuItem(
                            text = { Text(yearOption.toString()) },
                            onClick = {
                                onYearChanged(yearOption)
                                showYearMenu = false
                            }
                        )
                    }
                }
            }

            IconButton(
                onClick = {
                    if (month == 11) {
                        onMonthChanged(0)
                        onYearChanged(year + 1)
                    } else {
                        onMonthChanged(month + 1)
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = stringResource(R.string.next_month)
                )
            }
        }
    }
}

@Composable
private fun CalendarGrid(
    year: Int,
    month: Int,
    selectedDay: Int,
    onDaySelected: (Int) -> Unit
) {
    val calendar = Calendar.getInstance().apply {
        set(Calendar.YEAR, year)
        set(Calendar.MONTH, month)
        set(Calendar.DAY_OF_MONTH, 1)
    }

    val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
    val firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)

    // Adjust for Monday as first day of week
    val startOffset = when (firstDayOfWeek) {
        Calendar.SUNDAY -> 6
        else -> firstDayOfWeek - Calendar.MONDAY
    }

    val days = (1..daysInMonth).toList()
    val paddedDays = List(startOffset) { null } + days

    Column {
        // Day names header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            val dayNames = listOf(
                stringResource(R.string.mon),
                stringResource(R.string.tue),
                stringResource(R.string.wed),
                stringResource(R.string.thu),
                stringResource(R.string.fri),
                stringResource(R.string.sat),
                stringResource(R.string.sun)
            )
            dayNames.forEach { dayName ->
                Text(
                    text = dayName,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Calendar grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(paddedDays) { day ->
                if (day != null) {
                    DayItem(
                        day = day,
                        isSelected = day == selectedDay,
                        onClick = { onDaySelected(day) }
                    )
                } else {
                    Spacer(modifier = Modifier.aspectRatio(1f))
                }
            }
        }
    }
}

@Composable
private fun DayItem(
    day: Int,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .clip(CircleShape)
            .background(
                if (isSelected)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.surface
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = day.toString(),
            style = MaterialTheme.typography.bodyMedium,
            color = if (isSelected)
                MaterialTheme.colorScheme.onPrimary
            else
                MaterialTheme.colorScheme.onSurface,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}
