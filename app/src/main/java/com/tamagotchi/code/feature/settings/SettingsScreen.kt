package com.tamagotchi.code.feature.settings

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tamagotchi.code.R
import com.tamagotchi.code.ui.theme.AppTheme
import com.tamagotchi.code.ui.theme.ThemeRegistry
import com.tamagotchi.code.ui.viewmodel.PetViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: PetViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToLanguage: () -> Unit
) {
    val petState by viewModel.petState.collectAsStateWithLifecycle()
    val soundEnabled by viewModel.soundEnabled.collectAsStateWithLifecycle()
    val vibrationEnabled by viewModel.vibrationEnabled.collectAsStateWithLifecycle()
    val reduceMotion by viewModel.reduceMotion.collectAsStateWithLifecycle()
    val currentTheme = viewModel.currentTheme.value
    val unlockedThemes by viewModel.unlockedThemes.collectAsStateWithLifecycle()
    
    var showResetStep1 by remember { mutableStateOf(false) }
    var showResetStep2 by remember { mutableStateOf(false) }
    var showRenameDialog by remember { mutableStateOf(false) }
    var newName by remember { mutableStateOf("") }

    if (showResetStep1) {
        AlertDialog(
            onDismissRequest = { showResetStep1 = false },
            title = { Text(stringResource(R.string.dialog_reset_title)) },
            text = { Text(stringResource(R.string.dialog_reset_desc)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        showResetStep1 = false
                        showResetStep2 = true
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                ) {
                    Text(stringResource(R.string.dialog_reset_confirm))
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetStep1 = false }) {
                    Text(stringResource(R.string.dialog_reset_cancel))
                }
            }
        )
    }

    if (showResetStep2) {
        AlertDialog(
            onDismissRequest = { showResetStep2 = false },
            title = { Text(stringResource(R.string.dialog_reset_confirm2_title)) },
            text = { Text(stringResource(R.string.dialog_reset_confirm2_desc)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.resetProgress()
                        showResetStep2 = false
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                ) {
                    Text(stringResource(R.string.dialog_reset_confirm2_confirm))
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetStep2 = false }) {
                    Text(stringResource(R.string.dialog_reset_cancel))
                }
            }
        )
    }

    if (showRenameDialog) {
        AlertDialog(
            onDismissRequest = { showRenameDialog = false },
            title = { Text(stringResource(R.string.dialog_rename_title)) },
            text = {
                OutlinedTextField(
                    value = newName,
                    onValueChange = { if (it.length <= 15) newName = it },
                    label = { Text(stringResource(R.string.dialog_rename_label)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (newName.isNotBlank()) {
                            viewModel.renamePet(newName)
                        }
                        showRenameDialog = false
                    }
                ) {
                    Text(stringResource(R.string.dialog_rename_confirm))
                }
            },
            dismissButton = {
                TextButton(onClick = { showRenameDialog = false }) {
                    Text(stringResource(R.string.dialog_reset_cancel))
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.settings_title), fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Text("<", style = MaterialTheme.typography.titleLarge)
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            // A. Perfil
            SettingsSectionTitle(stringResource(R.string.settings_section_profile))
            SettingsItemClickable(
                title = stringResource(R.string.settings_profile_name),
                subtitle = petState?.name ?: "",
                onClick = {
                    newName = petState?.name ?: ""
                    showRenameDialog = true
                }
            )
            SettingsItemInfo(
                title = stringResource(R.string.settings_profile_level, petState?.level ?: 1)
            )

            HorizontalDivider()

            // B. Aprendizaje
            SettingsSectionTitle(stringResource(R.string.settings_section_learning))
            SettingsItemClickable(
                title = stringResource(R.string.settings_learning_languages),
                onClick = onNavigateToLanguage
            )
            SettingsItemInfo(
                title = stringResource(R.string.settings_learning_focus_duration),
                subtitle = stringResource(R.string.settings_learning_focus_duration_value)
            )

            HorizontalDivider()

            // C. Experiencia
            SettingsSectionTitle(stringResource(R.string.settings_section_experience))

            // Theme selector — visual card carousel
            Text(
                text = stringResource(R.string.settings_experience_theme),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            ThemeCarousel(
                allThemes = ThemeRegistry.allThemes,
                currentTheme = currentTheme,
                unlockedThemes = unlockedThemes,
                onThemeSelected = { viewModel.changeTheme(it) }
            )

            Spacer(modifier = Modifier.height(8.dp))

            SettingsItemSwitch(
                title = stringResource(R.string.settings_experience_sound),
                checked = soundEnabled,
                onCheckedChange = { viewModel.toggleSound(it) }
            )
            SettingsItemSwitch(
                title = stringResource(R.string.settings_experience_vibration),
                checked = vibrationEnabled,
                onCheckedChange = { viewModel.toggleVibration(it) }
            )
            SettingsItemSwitch(
                title = stringResource(R.string.settings_experience_reduce_motion),
                checked = reduceMotion,
                onCheckedChange = { viewModel.toggleReduceMotion(it) }
            )

            HorizontalDivider()

            // D. Recordatorios (Local pref mock)
            SettingsSectionTitle(stringResource(R.string.settings_section_reminders))
            var remindersEnabled by remember { mutableStateOf(false) }
            SettingsItemSwitch(
                title = stringResource(R.string.settings_reminders_daily),
                checked = remindersEnabled,
                onCheckedChange = { remindersEnabled = it }
            )

            HorizontalDivider()

            // E. Datos y ayuda
            SettingsSectionTitle(stringResource(R.string.settings_section_data))
            SettingsItemClickable(
                title = stringResource(R.string.settings_data_export),
                onClick = { viewModel.exportProgressMock() }
            )
            SettingsItemClickable(
                title = stringResource(R.string.settings_data_reset),
                titleColor = MaterialTheme.colorScheme.error,
                onClick = { showResetStep1 = true }
            )
            SettingsItemClickable(
                title = stringResource(R.string.settings_data_about),
                onClick = { /* Open about */ }
            )
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

// ─────────────────────────────────────────────────────────────────
// Theme Carousel – horizontal scrolling theme cards with previews
// ─────────────────────────────────────────────────────────────────

@Composable
fun ThemeCarousel(
    allThemes: List<AppTheme>,
    currentTheme: String,
    unlockedThemes: Set<String>,
    onThemeSelected: (String) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(allThemes) { theme ->
            val isSelected = theme.name == currentTheme
            val isUnlocked = theme.name in unlockedThemes

            ThemePreviewCard(
                theme = theme,
                isSelected = isSelected,
                isUnlocked = isUnlocked,
                onClick = {
                    if (isUnlocked) {
                        onThemeSelected(theme.name)
                    }
                }
            )
        }
    }
}

@Composable
fun ThemePreviewCard(
    theme: AppTheme,
    isSelected: Boolean,
    isUnlocked: Boolean,
    onClick: () -> Unit
) {
    val borderColor by animateColorAsState(
        targetValue = when {
            isSelected -> theme.primary
            isUnlocked -> theme.primary.copy(alpha = 0.3f)
            else -> Color.Gray.copy(alpha = 0.2f)
        },
        animationSpec = tween(300),
        label = "border_color"
    )

    Card(
        shape = RoundedCornerShape(theme.cornerRadius.coerceIn(4.dp, 16.dp)),
        border = BorderStroke(
            width = if (isSelected) 2.dp else 1.dp,
            color = borderColor
        ),
        colors = CardDefaults.cardColors(
            containerColor = if (isUnlocked) theme.surface else Color(0xFF1A1A1A)
        ),
        modifier = Modifier
            .width(140.dp)
            .clickable(enabled = isUnlocked, onClick = onClick)
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Color palette preview strip
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = if (isUnlocked) {
                                listOf(theme.primary, theme.secondary, theme.tertiary)
                            } else {
                                listOf(Color(0xFF333333), Color(0xFF444444), Color(0xFF555555))
                            }
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (!isUnlocked) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Bloqueado",
                        tint = Color.White.copy(alpha = 0.6f),
                        modifier = Modifier.size(18.dp)
                    )
                } else if (isSelected) {
                    Surface(
                        shape = CircleShape,
                        color = Color.White.copy(alpha = 0.9f),
                        modifier = Modifier.size(22.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Seleccionado",
                                tint = theme.primary,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Emoji
            Text(
                text = theme.emoji,
                fontSize = 20.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Theme name
            Text(
                text = theme.name,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                fontSize = 12.sp,
                color = if (isUnlocked) {
                    if (theme.isDark) Color.White else theme.textPrimary
                } else {
                    Color.Gray
                },
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth()
            )

            // Description
            Text(
                text = if (isUnlocked) theme.description else "Bloqueado",
                fontSize = 9.sp,
                color = if (isUnlocked) {
                    if (theme.isDark) Color.LightGray else theme.textSecondary
                } else {
                    Color.DarkGray
                },
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 12.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 2.dp)
            )

            // Corner radius / style indicator
            if (isUnlocked) {
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    // Small color dots showing the palette
                    listOf(theme.background, theme.surface, theme.primary, theme.accent).forEach { color ->
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(color)
                        )
                    }
                }
            }

            if (isSelected) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stringResource(R.string.settings_experience_theme_active),
                    fontSize = 9.sp,
                    color = theme.primary,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun SettingsSectionTitle(title: String) {
    Text(
        text = title,
        color = MaterialTheme.colorScheme.primary,
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 24.dp, bottom = 8.dp)
    )
}

@Composable
fun SettingsItemClickable(
    title: String,
    subtitle: String? = null,
    titleColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.onSurface,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        Text(text = title, color = titleColor, style = MaterialTheme.typography.bodyLarge)
        if (subtitle != null) {
            Text(text = subtitle, color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun SettingsItemInfo(
    title: String,
    subtitle: String? = null
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        Text(text = title, style = MaterialTheme.typography.bodyLarge)
        if (subtitle != null) {
            Text(text = subtitle, color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun SettingsItemSwitch(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!checked) }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = title, style = MaterialTheme.typography.bodyLarge)
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}
