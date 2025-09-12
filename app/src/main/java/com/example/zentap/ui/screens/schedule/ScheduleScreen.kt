package com.example.zentap.ui.screens.schedule

import android.app.AlarmManager
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.zentap.MainViewModel
import com.example.zentap.data.DayOfWeek
import com.example.zentap.data.Schedule
import com.example.zentap.data.ScheduleAction
import com.example.zentap.util.ToastManager
import com.example.zentap.util.safePopBackStack
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleScreen(viewModel: MainViewModel, navController: NavController) {
    val context = LocalContext.current
    val schedules by viewModel.schedules.collectAsState()
    var showEditDialog by remember { mutableStateOf(false) }
    var scheduleToEdit by remember { mutableStateOf<Schedule?>(null) }

    var showPermissionRationale by remember { mutableStateOf(false) }
    var scheduleToSaveAfterPermission by remember { mutableStateOf<Schedule?>(null) }

    val isBlockedMode by viewModel.isOverallToggleOn.collectAsState()

    val settingsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) {
        val alarmManager = ContextCompat.getSystemService(context, AlarmManager::class.java) as AlarmManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && alarmManager.canScheduleExactAlarms()) {
            scheduleToSaveAfterPermission?.let {
                if (schedules.any { s -> s.id == it.id }) {
                    viewModel.updateSchedule(context, it)
                } else {
                    viewModel.addSchedule(context, it)
                }
                scheduleToSaveAfterPermission = null
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.loadSchedules(context)
    }

    LaunchedEffect(isBlockedMode) {
        if (isBlockedMode) {
            ToastManager.showToast(context, "Cannot manage schedules while blocked mode is active")
            navController.safePopBackStack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Auto-Lock Schedules") },
                navigationIcon = {
                    IconButton(onClick = { navController.safePopBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                scheduleToEdit = null
                showEditDialog = true
            }) {
                Icon(Icons.Default.Add, contentDescription = "Add Schedule")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            if (schedules.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No schedules set yet.", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(schedules, key = { it.id }) { schedule ->
                        ScheduleItem(
                            schedule = schedule,
                            onToggle = { isEnabled -> viewModel.toggleSchedule(context, schedule, isEnabled) },
                            onClick = {
                                scheduleToEdit = schedule
                                showEditDialog = true
                            },
                            onDelete = { viewModel.removeSchedule(context, schedule) }
                        )
                    }
                }
            }
        }
    }

    if (showPermissionRationale) {
        PermissionRationaleDialog(
            onDismiss = { showPermissionRationale = false },
            onConfirm = {
                showPermissionRationale = false
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    settingsLauncher.launch(Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM))
                }
            }
        )
    }

    if (showEditDialog) {
        ScheduleEditDialog(
            schedule = scheduleToEdit,
            onDismiss = { showEditDialog = false },
            onSave = { schedule ->
                val alarmManager = ContextCompat.getSystemService(context, AlarmManager::class.java) as AlarmManager
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarmManager.canScheduleExactAlarms()) {
                    scheduleToSaveAfterPermission = schedule
                    showPermissionRationale = true
                } else {
                    if (scheduleToEdit == null) {
                        viewModel.addSchedule(context, schedule)
                    } else {
                        viewModel.updateSchedule(context, schedule)
                    }
                }
                showEditDialog = false
            }
        )
    }
}

@Composable
fun PermissionRationaleDialog(onDismiss: () -> Unit, onConfirm: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Permission Required") },
        text = { Text("To schedule automatic locking, this app needs permission to set precise alarms and reminders. Please enable this in the system settings.") },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text("Go to Settings")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun ScheduleItem(
    schedule: Schedule,
    onToggle: (Boolean) -> Unit,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = String.format("%02d:%02d", schedule.hour, schedule.minute),
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Light
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (schedule.action == ScheduleAction.TURN_ON) Icons.Default.Lock else Icons.Default.LockOpen,
                        contentDescription = "Action",
                        modifier = Modifier.size(14.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = getRepeatDaysString(schedule.repeatDays),
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Switch(
                checked = schedule.isEnabled,
                onCheckedChange = onToggle
            )
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Delete Schedule", tint = MaterialTheme.colorScheme.error)
            }
        }
    }
}

@Composable
fun ScheduleEditDialog(
    schedule: Schedule?,
    onDismiss: () -> Unit,
    onSave: (Schedule) -> Unit
) {
    val context = LocalContext.current
    val initialTime = schedule ?: Calendar.getInstance().let {
        Schedule("", it.get(Calendar.HOUR_OF_DAY), it.get(Calendar.MINUTE), repeatDays = emptySet())
    }

    var hour by remember { mutableStateOf(initialTime.hour) }
    var minute by remember { mutableStateOf(initialTime.minute) }
    var selectedDays by remember { mutableStateOf(initialTime.repeatDays) }
    var action by remember { mutableStateOf(initialTime.action) }

    val timePickerDialog = TimePickerDialog(
        context, { _, selectedHour, selectedMinute ->
            hour = selectedHour
            minute = selectedMinute
        }, hour, minute, true
    )

    Dialog(onDismissRequest = onDismiss) {
        Card {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Set Schedule", style = MaterialTheme.typography.headlineSmall)
                Spacer(Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { timePickerDialog.show() }
                        .padding(vertical = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = String.format("%02d:%02d", hour, minute),
                        style = MaterialTheme.typography.displayMedium
                    )
                }

                Spacer(Modifier.height(16.dp))
                Text("Repeat on", fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                DayOfWeekSelector(selectedDays) { day ->
                    selectedDays = if (selectedDays.contains(day)) {
                        selectedDays - day
                    } else {
                        selectedDays + day
                    }
                }

                Spacer(Modifier.height(24.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = if (action == ScheduleAction.TURN_ON) "Turn Blocked Mode ON" else "Turn Blocked Mode OFF",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                    Switch(
                        checked = action == ScheduleAction.TURN_ON,
                        onCheckedChange = { isChecked ->
                            action = if (isChecked) ScheduleAction.TURN_ON else ScheduleAction.TURN_OFF
                        }
                    )
                }

                Spacer(Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) { Text("Cancel") }
                    Spacer(Modifier.width(8.dp))
                    Button(onClick = {
                        val newSchedule = schedule?.copy(
                            hour = hour,
                            minute = minute,
                            repeatDays = selectedDays,
                            action = action
                        ) ?: Schedule(
                            hour = hour,
                            minute = minute,
                            repeatDays = selectedDays,
                            action = action
                        )
                        onSave(newSchedule)
                    }) {
                        Text("Save")
                    }
                }
            }
        }
    }
}

@Composable
fun DayOfWeekSelector(
    selectedDays: Set<DayOfWeek>,
    onDayClick: (DayOfWeek) -> Unit
) {
    val days = DayOfWeek.values()
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally)
    ) {
        days.forEach { day ->
            val isSelected = day in selectedDays
            val backgroundColor = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent
            val contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
            val border = if (isSelected) null else BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f))

            Box(
                modifier = Modifier
                    .size(35.dp)
                    .clip(CircleShape)
                    .background(backgroundColor)
                    .then(if (border != null) Modifier.border(border, CircleShape) else Modifier)
                    .clickable { onDayClick(day) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = day.name.take(1),
                    color = contentColor,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

private fun getRepeatDaysString(days: Set<DayOfWeek>): String {
    if (days.isEmpty()) return "Once"
    if (days.size == 7) return "Every day"
    if (days == setOf(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY)) return "Weekends"
    if (days == setOf(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY)) return "Weekdays"
    return days.sorted().joinToString(", ") { it.name.take(3) }
}