package com.tamagotchi.code.feature.onboarding

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tamagotchi.code.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun OnboardingScreen(
    onComplete: (String, Set<String>) -> Unit,
    onSkip: () -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { 4 })
    val scope = rememberCoroutineScope()
    
    var selectedTopics by remember { mutableStateOf(setOf<String>()) }
    var petName by remember { mutableStateOf("") }
    
    var showSkipDialog by remember { mutableStateOf(false) }

    if (showSkipDialog) {
        AlertDialog(
            onDismissRequest = { showSkipDialog = false },
            title = { Text(stringResource(R.string.dialog_skip_title)) },
            text = { Text(stringResource(R.string.dialog_skip_desc)) },
            confirmButton = {
                TextButton(onClick = { 
                    showSkipDialog = false
                    onSkip() 
                }) {
                    Text(stringResource(R.string.dialog_skip_confirm))
                }
            },
            dismissButton = {
                TextButton(onClick = { showSkipDialog = false }) {
                    Text(stringResource(R.string.dialog_skip_cancel))
                }
            }
        )
    }

    Scaffold(
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (pagerState.currentPage < 3) {
                    TextButton(onClick = { showSkipDialog = true }) {
                        Text(stringResource(R.string.onboarding_skip))
                    }
                    Button(onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    }) {
                        Text(stringResource(R.string.onboarding_next))
                    }
                }
            }
        }
    ) { innerPadding ->
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) { page ->
            when (page) {
                0 -> Step1()
                1 -> Step2()
                2 -> Step3(selectedTopics = selectedTopics, onTopicsChange = { selectedTopics = it })
                3 -> Step4(
                    petName = petName,
                    onNameChange = { petName = it },
                    onComplete = {
                        onComplete(petName, selectedTopics)
                    }
                )
            }
        }
    }
}

@Composable
fun Step1() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        var visible by remember { mutableStateOf(false) }
        LaunchedEffect(Unit) { visible = true }
        
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn() + slideInVertically(initialOffsetY = { 50 })
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "(^‿^)",
                    fontSize = 72.sp,
                    fontFamily = FontFamily.Monospace,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(32.dp))
                
                Text(
                    text = stringResource(R.string.onboarding_step1_title),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(R.string.onboarding_step1_desc),
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Spacer(modifier = Modifier.height(32.dp))
                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.onboarding_step1_terminal),
                        fontFamily = FontFamily.Monospace,
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
fun Step2() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.onboarding_step2_title),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(32.dp))
        
        Card(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
            Text(stringResource(R.string.onboarding_step2_card1), modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.titleMedium)
        }
        Card(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
            Text(stringResource(R.string.onboarding_step2_card2), modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.titleMedium)
        }
        Card(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
            Text(stringResource(R.string.onboarding_step2_card3), modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.titleMedium)
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = stringResource(R.string.onboarding_step2_desc),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun Step3(selectedTopics: Set<String>, onTopicsChange: (Set<String>) -> Unit) {
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
    
    val initialRoute = setOf(
        stringResource(R.string.topic_kotlin),
        stringResource(R.string.topic_git),
        stringResource(R.string.topic_data_structures)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.onboarding_step3_title),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.onboarding_step3_subtitle) + " (${selectedTopics.size}/3)",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(24.dp))
        
        if (selectedTopics == initialRoute) {
            Button(
                onClick = { onTopicsChange(initialRoute) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.onboarding_step3_initial_route))
            }
        } else {
            OutlinedButton(
                onClick = { onTopicsChange(initialRoute) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.onboarding_step3_initial_route))
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
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
                            if (newSelection.size > 1) { // Mínimo un tema
                                newSelection.remove(topic)
                            }
                        } else {
                            if (newSelection.size < 3) {
                                newSelection.add(topic)
                            }
                        }
                        if (newSelection.isNotEmpty()) {
                            onTopicsChange(newSelection)
                        }
                    },
                    label = { Text(topic) }
                )
            }
        }
        
        LaunchedEffect(Unit) {
            if (selectedTopics.isEmpty()) {
                onTopicsChange(initialRoute)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Step4(petName: String, onNameChange: (String) -> Unit, onComplete: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.onboarding_step4_title),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(32.dp))
        
        OutlinedTextField(
            value = petName,
            onValueChange = { if (it.length <= 15) onNameChange(it) },
            label = { Text("Nombre (máx 15)") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        val previewName = petName.ifBlank { stringResource(R.string.default_pet_name) }
        Surface(
            color = MaterialTheme.colorScheme.surfaceVariant,
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = stringResource(R.string.onboarding_step4_preview, previewName),
                fontFamily = FontFamily.Monospace,
                modifier = Modifier.padding(16.dp),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        
        Spacer(modifier = Modifier.height(48.dp))
        
        Button(
            onClick = onComplete,
            modifier = Modifier.fillMaxWidth().height(56.dp)
        ) {
            Text(stringResource(R.string.onboarding_step4_cta, previewName))
        }
    }
}
