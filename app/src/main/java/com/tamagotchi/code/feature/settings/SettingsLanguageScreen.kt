package com.tamagotchi.code.feature.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tamagotchi.code.R
import com.tamagotchi.code.ui.components.TopicIcon
import com.tamagotchi.code.ui.viewmodel.PetViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun SettingsLanguageScreen(
    viewModel: PetViewModel,
    onNavigateBack: () -> Unit
) {
    val selectedTopics by viewModel.selectedTopics.collectAsStateWithLifecycle()
    val difficulty by viewModel.difficulty.collectAsStateWithLifecycle()
    val petState by viewModel.petState.collectAsStateWithLifecycle()
    
    val allTopics = listOf(
        stringResource(R.string.topic_kotlin),
        stringResource(R.string.topic_javascript),
        stringResource(R.string.topic_python),
        stringResource(R.string.topic_php),
        stringResource(R.string.topic_sql),
        stringResource(R.string.topic_git),
        stringResource(R.string.topic_clean_code),
        stringResource(R.string.topic_data_structures)
    )
    
    val allLanguages = listOf("Kotlin", "JavaScript", "Python", "PHP")

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.settings_lang_title), fontWeight = FontWeight.Bold) },
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
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            
            // Primary Language
            Text(stringResource(R.string.settings_lang_primary), style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(8.dp))
            
            var expandedLang by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = expandedLang,
                onExpandedChange = { expandedLang = !expandedLang }
            ) {
                OutlinedTextField(
                    value = petState?.language ?: "Kotlin",
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedLang) },
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = expandedLang,
                    onDismissRequest = { expandedLang = false }
                ) {
                    allLanguages.forEach { lang ->
                        DropdownMenuItem(
                            text = { Text(lang) },
                            onClick = {
                                viewModel.selectLanguage(lang)
                                expandedLang = false
                            },
                            leadingIcon = {
                                TopicIcon(topic = lang, modifier = androidx.compose.ui.Modifier.size(18.dp))
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            
            // Difficulty
            Text(stringResource(R.string.settings_lang_difficulty), style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf(
                    stringResource(R.string.settings_lang_diff_beginner),
                    stringResource(R.string.settings_lang_diff_initial),
                    stringResource(R.string.settings_lang_diff_intermediate),
                    stringResource(R.string.settings_lang_diff_mixed)
                ).forEach { diffOption ->
                    FilterChip(
                        selected = difficulty == diffOption,
                        onClick = { viewModel.setDifficulty(diffOption) },
                        label = { Text(diffOption) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Topics
            Text(stringResource(R.string.settings_lang_active_topics), style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(8.dp))
            
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                allTopics.forEach { topic ->
                    FilterChip(
                        selected = selectedTopics.contains(topic),
                        onClick = {
                            val newSelection = selectedTopics.toMutableSet()
                            if (newSelection.contains(topic)) {
                                if (newSelection.size > 1) {
                                    newSelection.remove(topic)
                                }
                            } else {
                                newSelection.add(topic)
                            }
                            viewModel.setTopics(newSelection)
                        },
                        label = { Text(topic) },
                        leadingIcon = {
                            TopicIcon(topic = topic, modifier = androidx.compose.ui.Modifier.size(18.dp))
                        }
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Preview
            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.settings_lang_preview, selectedTopics.size, difficulty),
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
