package com.tamagotchi.code.feature.focus

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tamagotchi.code.R
import com.tamagotchi.code.data.database.PetStateEntity
import com.tamagotchi.code.data.database.StudySessionEntity
import com.tamagotchi.code.ui.theme.readableOnBackground
import com.tamagotchi.code.ui.theme.bestContentOn
import com.tamagotchi.code.ui.viewmodel.PetViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun FocusScreen(
    viewModel: PetViewModel,
    state: PetStateEntity,
    studySessions: List<StudySessionEntity>
) {
    var activeTab by rememberSaveable { mutableIntStateOf(0) }
    val tabs = listOf(stringResource(R.string.focus_tab_timer), stringResource(R.string.focus_tab_logs))

    viewModel.focusCompletion.value?.let { completion ->
        AlertDialog(
            onDismissRequest = viewModel::dismissFocusCompletion,
            title = { Text(stringResource(R.string.focus_completed_title)) },
            text = {
                Text(stringResource(
                    R.string.focus_completed_body, completion.session.plannedDurationMinutes,
                    completion.xp, completion.bytes
                ))
            },
            confirmButton = {
                TextButton(onClick = viewModel::dismissFocusCompletion) {
                    Text(stringResource(R.string.focus_continue))
                }
            }
        )
    }

    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(bottom = 24.dp)) {
        viewModel.focusError.value?.let { message ->
            Surface(
                color = MaterialTheme.colorScheme.errorContainer,
                contentColor = MaterialTheme.colorScheme.onErrorContainer,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
            ) {
                Column(Modifier.padding(12.dp)) {
                    Text(stringResource(message), style = MaterialTheme.typography.bodyMedium)
                    TextButton(onClick = viewModel::retryFocusSession, enabled = !viewModel.isFocusBusy.value) {
                        Text(stringResource(R.string.focus_retry))
                    }
                }
            }
        }
        Text(
            text = stringResource(R.string.focus_header),
            fontSize = 14.sp,
            fontFamily = FontFamily.Monospace,
            color = readableOnBackground(Color(0xFF81C784)),
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        TabRow(
            selectedTabIndex = activeTab,
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.primary,
            indicator = { tabPositions ->
                if (activeTab < tabPositions.size) {
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[activeTab]),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = activeTab == index,
                    onClick = { activeTab = index },
                    text = { Text(title, fontFamily = FontFamily.Monospace, fontSize = 12.sp) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (activeTab) {
            0 -> TimerPanel(viewModel = viewModel, state = state)
            1 -> LogsPanel(studySessions = studySessions, state = state, viewModel = viewModel)
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TimerPanel(
    viewModel: PetViewModel,
    state: PetStateEntity
) {
    val isRunning = viewModel.isTimerRunning.value
    val secondsRemaining = viewModel.timerSecondsRemaining.value
    val currentTopic = viewModel.currentStudyTopic.value
    val isDndActive by viewModel.isDndActive.collectAsStateWithLifecycle()

    var tempMinutes by rememberSaveable { mutableIntStateOf(viewModel.focusDurationDefault.value) }
    var tempTopic by rememberSaveable { mutableStateOf("Kotlin") }
    var showDropdown by remember { mutableStateOf(false) }

    val topics = com.tamagotchi.code.util.ProgrammingLanguages.ALL + listOf("Clean Code", "Git", stringResource(R.string.topic_data_structures))

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isDndActive) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color(0xFFFFB74D).copy(alpha = 0.1f),
                border = BorderStroke(1.dp, Color(0xFFFFB74D).copy(alpha = 0.5f)),
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
            ) {
                Text(
                    text = stringResource(R.string.focus_dnd_banner),
                    fontFamily = FontFamily.Monospace,
                    fontSize = 10.sp,
                    color = readableOnBackground(Color(0xFFFFB74D)),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
        if (!isRunning) {
            Text(
                text = stringResource(R.string.focus_start_logs),
                fontSize = 13.sp,
                fontFamily = FontFamily.Monospace,
                color = readableOnBackground(Color(0xFF81C784)),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))

            Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = stringResource(R.string.focus_topic_label),
                    fontSize = 12.sp,
                    fontFamily = FontFamily.Monospace,
                    color = readableOnBackground(Color.White),
                )

                Box(modifier = Modifier.fillMaxWidth()) {
                    Surface(
                        onClick = { showDropdown = true },
                        color = Color(0xFF151D16),
                        border = BorderStroke(1.dp, Color(0xFF2E7D32)),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 48.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = tempTopic,
                                color = Color(0xFF81C784),
                                fontSize = 12.sp,
                                fontFamily = FontFamily.Monospace
                            )
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = stringResource(R.string.focus_change),
                                tint = Color(0xFF81C784)
                            )
                        }
                    }

                    DropdownMenu(
                        expanded = showDropdown,
                        onDismissRequest = { showDropdown = false },
                        modifier = Modifier.background(Color(0xFF151D16))
                    ) {
                        topics.forEach { topic ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = topic,
                                        fontFamily = FontFamily.Monospace,
                                        fontSize = 12.sp,
                                        color = Color.White
                                    )
                                },
                                onClick = {
                                    tempTopic = topic
                                    showDropdown = false
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = stringResource(R.string.focus_time_label),
                    fontSize = 12.sp,
                    fontFamily = FontFamily.Monospace,
                    color = readableOnBackground(Color.White),
                )

                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf(15, 25, 50).forEach { mins ->
                        val isSelected = tempMinutes == mins
                        Surface(
                            onClick = { tempMinutes = mins },
                            color = if (isSelected) Color(0xFF2E7D32) else Color(0xFF151D16),
                            border = BorderStroke(1.dp, Color(0xFF2E7D32)),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .widthIn(min = 80.dp)
                                .heightIn(min = 48.dp)
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)
                            ) {
                                Text(
                                    text = stringResource(R.string.focus_min_label, mins),
                                    color = if (isSelected) bestContentOn(Color(0xFF2E7D32)) else Color(0xFF81C784),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.focus_studying_desc),
                fontSize = 11.sp,
                fontFamily = FontFamily.Monospace,
                color = readableOnBackground(Color.Gray),
                lineHeight = 16.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    viewModel.startStudyTimer(tempMinutes, tempTopic)
                },
                enabled = !viewModel.isFocusBusy.value && state.health > 0f && viewModel.focusError.value == null,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 48.dp)
                    .testTag("timer_start_button")
            ) {
                Text(
                    text = stringResource(R.string.focus_start_button),
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
            }
        } else {
            Text(
                text = stringResource(R.string.focus_compiling_header),
                fontSize = 12.sp,
                fontFamily = FontFamily.Monospace,
                color = readableOnBackground(Color(0xFFFFB74D)),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(R.string.focus_enfoque, currentTopic),
                fontSize = 11.sp,
                fontFamily = FontFamily.Monospace,
                color = readableOnBackground(Color.Gray),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            val minText = String.format(Locale.getDefault(), "%02d", secondsRemaining / 60)
            val secText = String.format(Locale.getDefault(), "%02d", secondsRemaining % 60)

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "$minText:$secText",
                    fontSize = 42.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    color = readableOnBackground(Color(0xFF81C784)),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = stringResource(R.string.focus_pet_studying, state.name),
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace,
                    color = readableOnBackground(Color.Gray),
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = { viewModel.cancelStudyTimer() },
                    enabled = !viewModel.isFocusBusy.value,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF5350)),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .weight(1f)
                        .heightIn(min = 48.dp)
                        .testTag("timer_cancel_button")
                ) {
                    Text(
                        text = stringResource(R.string.focus_cancel_button),
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 11.sp
                    )
                }
            }
        }
    }
}

@Composable
fun LogsPanel(
    studySessions: List<StudySessionEntity>,
    state: PetStateEntity,
    viewModel: PetViewModel
) {
    val dateFormat = remember { SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()) }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = stringResource(R.string.focus_logs_header),
            fontSize = 13.sp,
            fontFamily = FontFamily.Monospace,
            color = readableOnBackground(Color(0xFF81C784)),
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(10.dp))

        if (studySessions.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.focus_logs_empty),
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace,
                    color = readableOnBackground(Color.Gray),
                    textAlign = TextAlign.Center,
                    lineHeight = 16.sp
                )
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 220.dp)
            ) {
                items(studySessions) { session ->
                    val dateStr = dateFormat.format(Date(session.timestamp))
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Color(0xFF151D16),
                        border = BorderStroke(1.dp, Color(0xFF2E7D32).copy(alpha = 0.2f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = stringResource(R.string.focus_logs_entry, session.topic),
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 12.sp,
                                    color = Color.White
                                )
                                Text(
                                    text = dateStr,
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 10.sp,
                                    color = Color.LightGray
                                )
                            }

                            Text(
                                text = stringResource(R.string.focus_logs_min, session.durationMinutes),
                                color = Color(0xFF81C784),
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Surface(
            shape = RoundedCornerShape(8.dp),
            color = Color(0xFF1E281F),
            border = BorderStroke(1.dp, Color(0xFF2E7D32).copy(alpha = 0.4f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = stringResource(R.string.focus_note_title),
                    fontSize = 10.sp,
                    fontFamily = FontFamily.Monospace,
                    color = Color(0xFFFFB74D),
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = stringResource(R.string.focus_note_body),
                    fontSize = 10.sp,
                    fontFamily = FontFamily.Monospace,
                    color = Color.LightGray,
                    lineHeight = 14.sp,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }
    }
}

