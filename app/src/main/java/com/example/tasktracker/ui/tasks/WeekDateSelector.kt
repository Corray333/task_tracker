package com.example.tasktracker.ui.tasks

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.distinctUntilChanged
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WeekDateSelector(
    selectedDate: Long,
    onDateSelected: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val initialPage = Int.MAX_VALUE / 2
    val pagerState = rememberPagerState(
        initialPage = initialPage,
        pageCount = { Int.MAX_VALUE }
    )

    // Calculate week offset from current week
    val currentWeekOffset by remember {
        derivedStateOf {
            pagerState.currentPage - initialPage
        }
    }

    // Get list of dates for current week
    val datesInWeek by remember(currentWeekOffset) {
        derivedStateOf {
            getWeekDates(currentWeekOffset)
        }
    }

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }
            .distinctUntilChanged()
            .collect { page ->
                // When page changes, select first day of new week if selected date is not in the week
                val weekDates = getWeekDates(page - initialPage)
                val selectedDateCalendar = Calendar.getInstance().apply {
                    timeInMillis = selectedDate
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }
                val selectedDateMillis = selectedDateCalendar.timeInMillis

                if (!weekDates.any { it.timeInMillis == selectedDateMillis }) {
                    onDateSelected(weekDates[0].timeInMillis)
                }
            }
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth()
        ) { page ->
            val weekOffset = page - initialPage
            val dates = getWeekDates(weekOffset)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                dates.forEach { date ->
                    DateItem(
                        date = date,
                        isSelected = isSameDay(date.timeInMillis, selectedDate),
                        onClick = { onDateSelected(date.timeInMillis) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun DateItem(
    date: Calendar,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dayOfWeek = SimpleDateFormat("EEE", Locale.getDefault()).format(date.time).take(1)
    val dayOfMonth = date.get(Calendar.DAY_OF_MONTH)

    Column(
        modifier = modifier
            .clip(CircleShape)
            .clickable(onClick = onClick)
            .padding(vertical = 4.dp, horizontal = 2.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = dayOfWeek,
            style = MaterialTheme.typography.labelSmall,
            color = if (isSelected)
                MaterialTheme.colorScheme.primary
            else
                MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        Box(
            modifier = Modifier
                .padding(top = 2.dp)
                .size(36.dp)
                .clip(CircleShape)
                .background(
                    if (isSelected)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.surfaceVariant
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = dayOfMonth.toString(),
                style = MaterialTheme.typography.bodySmall,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = if (isSelected)
                    MaterialTheme.colorScheme.onPrimary
                else
                    MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

private fun getWeekDates(weekOffset: Int): List<Calendar> {
    val today = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }

    // Find Monday of current week
    val currentDayOfWeek = today.get(Calendar.DAY_OF_WEEK)
    val daysFromMonday = when (currentDayOfWeek) {
        Calendar.SUNDAY -> 6
        else -> currentDayOfWeek - Calendar.MONDAY
    }

    val monday = Calendar.getInstance().apply {
        timeInMillis = today.timeInMillis
        add(Calendar.DAY_OF_YEAR, -daysFromMonday)
        add(Calendar.WEEK_OF_YEAR, weekOffset)
    }

    return (0..6).map { dayOffset ->
        Calendar.getInstance().apply {
            timeInMillis = monday.timeInMillis
            add(Calendar.DAY_OF_YEAR, dayOffset)
        }
    }
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
