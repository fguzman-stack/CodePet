package com.tamagotchi.code.feature.games

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tamagotchi.code.R
import com.tamagotchi.code.ui.viewmodel.PetViewModel
import kotlinx.coroutines.delay

private data class GitScenario(
    val situation: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String,
    val humor: String
)

private val scenarioPool = listOf(
    GitScenario(
        situation = "Acabas de clonar el repo y vas a trabajar en un nuevo feature. ¿Qué haces primero?",
        options = listOf(
            "git checkout -b feature/nuevo",
            "git commit -m \"inicio\"",
            "git push --force"
        ),
        correctIndex = 0,
        explanation = "Crear una rama nueva desde main es la forma correcta de empezar un feature.",
        humor = "Codey: ¡Nunca pushes a main sin antes crear una rama! Eres un cowboy."
    ),
    GitScenario(
        situation = "Has hecho cambios locales y te das cuenta de que rompiste todo. Quieres volver al último commit limpio.",
        options = listOf(
            "git reset --hard HEAD",
            "git revert HEAD",
            "git rm -rf ."
        ),
        correctIndex = 1,
        explanation = "revert crea un nuevo commit que deshace los cambios, preservando la historia.",
        humor = "Codey: reset --hard es como una máquina del tiempo sin frenos. revert es más seguro."
    ),
    GitScenario(
        situation = "Tienes un conflicto en un merge. ¿Cuál es el siguiente paso?",
        options = listOf(
            "Resolver el conflicto en el editor y luego git add",
            "git commit --amend",
            "git push --force origin main"
        ),
        correctIndex = 0,
        explanation = "Los conflictos se resuelven editando los archivos, luego git add y git commit.",
        humor = "Codey: push --force no resuelve conflictos, los empeora. Es como echar gasolina al fuego."
    ),
    GitScenario(
        situation = "Trabajas en equipo y necesitas traerte los cambios más recientes de la rama main a tu rama feature.",
        options = listOf(
            "git pull origin main",
            "git merge feature main",
            "git branch -d main"
        ),
        correctIndex = 0,
        explanation = "git pull trae los cambios de main a tu rama actual. merge lo haría al revés.",
        humor = "Codey: No borres main. Nunca borres main. Es como borrar el diccionario."
    ),
    GitScenario(
        situation = "Hiciste un commit pero olvidaste incluir un archivo. ¿Cómo lo arreglas?",
        options = listOf(
            "git add archivo && git commit --amend",
            "git reset --hard HEAD~1",
            "git commit --allow-empty"
        ),
        correctIndex = 0,
        explanation = "Con amend puedes agregar archivos al commit anterior sin crear uno nuevo.",
        humor = "Codey: --allow-empty no arregla nada. Es como poner un post-it en una puerta cerrada."
    ),
    GitScenario(
        situation = "Quieres ver qué archivos modificaste antes de hacer commit. ¿Qué comando usas?",
        options = listOf(
            "git diff",
            "git status",
            "git show"
        ),
        correctIndex = 1,
        explanation = "git status muestra el estado actual: archivos modificados, nuevos y eliminados.",
        humor = "Codey: diff es para ver el contenido exacto, status te da el resumen. Dos herramientas diferentes."
    ),
    GitScenario(
        situation = "El historial de commits está lleno de mensajes como 'fix' y 'update'. Quieres limpiarlo antes de hacer merge.",
        options = listOf(
            "git rebase -i HEAD~5",
            "git reset --hard origin/main",
            "git commit --fixup"
        ),
        correctIndex = 0,
        explanation = "rebase interactivo te permite squash, reordenar y renombrar commits.",
        humor = "Codey: Los mensajes 'fix' y 'update' son como decir 'cosa' en un examen. Sé descriptivo."
    ),
    GitScenario(
        situation = "Tu rama feature quedó atrás de main y necesitas actualizarla sin crear commits de merge.",
        options = listOf(
            "git rebase main",
            "git merge main",
            "git pull --rebase"
        ),
        correctIndex = 2,
        explanation = "pull --rebase trae cambios y los aplica encima de tus commits locales.",
        humor = "Codey: El histórico lineal es como una carretera recta. Los merges son rotondas."
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GitRescueScreen(
    viewModel: PetViewModel,
    onNavigateBack: () -> Unit
) {
    val scenarios = remember { scenarioPool.shuffled().take(5) }

    var currentStep by remember { mutableStateOf(0) }
    var score by remember { mutableStateOf(0) }
    var isGameOver by remember { mutableStateOf(false) }
    var codeyReaction by remember { mutableStateOf("¡Rápido, el repo está en llamas!") }
    var lastExplanation by remember { mutableStateOf<String?>(null) }
    var answeredStep by remember { mutableStateOf(false) }
    var branchPosition by remember { mutableStateOf(0f) }
    var maxBranchSteps by remember { mutableStateOf(scenarios.size.toFloat()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.git_rescue_title)) },
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
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (isGameOver) {
                Text(stringResource(R.string.game_result_title), style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(16.dp))
                val bytesEarned = score * 15
                Text(stringResource(R.string.game_result_reward, bytesEarned))
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Decisiones correctas: $score de ${scenarios.size}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(24.dp))
                Button(onClick = {
                    viewModel.recordGamePlay("git_rescue")
                    viewModel.completeMinigame(bytesEarned, 10f, -5f)
                    onNavigateBack()
                }) {
                    Text(stringResource(R.string.game_result_finish))
                }
            } else {
                // Branch visual progress
                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("Progreso de la rama:", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            scenarios.forEachIndexed { index, _ ->
                                val isDone = index < currentStep
                                val isCurrent = index == currentStep
                                Surface(
                                    shape = RoundedCornerShape(50),
                                    color = when {
                                        isDone -> MaterialTheme.colorScheme.primary
                                        isCurrent -> MaterialTheme.colorScheme.secondary
                                        else -> MaterialTheme.colorScheme.surfaceVariant
                                    },
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(
                                            text = "${index + 1}",
                                            fontSize = MaterialTheme.typography.labelSmall.fontSize,
                                            color = if (isDone || isCurrent) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                                if (index < scenarios.size - 1) {
                                    Surface(
                                        modifier = Modifier
                                            .height(4.dp)
                                            .weight(1f),
                                        color = if (isDone) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
                                    ) {}
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Codey: $codeyReaction", modifier = Modifier.padding(16.dp), color = MaterialTheme.colorScheme.onPrimaryContainer)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text("Paso ${currentStep + 1} de ${scenarios.size}", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Text(scenarios[currentStep].situation, style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(16.dp))

                scenarios[currentStep].options.forEachIndexed { index, option ->
                    val isCorrect = answeredStep && index == scenarios[currentStep].correctIndex
                    val isWrong = answeredStep && index != scenarios[currentStep].correctIndex

                    Surface(
                        color = when {
                            isCorrect -> Color(0xFF1B5E20).copy(alpha = 0.3f)
                            isWrong -> Color(0xFFB71C1C).copy(alpha = 0.1f)
                            else -> MaterialTheme.colorScheme.surfaceVariant
                        },
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable(enabled = !answeredStep) {
                                if (index == scenarios[currentStep].correctIndex) {
                                    score++
                                    codeyReaction = scenarios[currentStep].humor
                                } else {
                                    codeyReaction = "¡Esa no era la mejor opción!"
                                }
                                lastExplanation = scenarios[currentStep].explanation
                                answeredStep = true
                            }
                    ) {
                        Text(
                            text = "> $option",
                            fontFamily = FontFamily.Monospace,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }

                if (answeredStep && lastExplanation != null) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Surface(
                        color = Color(0xFF1B5E20).copy(alpha = 0.1f),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = lastExplanation!!,
                            modifier = Modifier.padding(12.dp),
                            fontFamily = FontFamily.Monospace,
                            fontSize = MaterialTheme.typography.bodySmall.fontSize,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = {
                        if (currentStep < scenarios.size - 1) {
                            currentStep++
                            answeredStep = false
                            lastExplanation = null
                            codeyReaction = "¡Siguiente decisión! El repo te necesita."
                        } else {
                            isGameOver = true
                        }
                    }) {
                        Text(if (currentStep < scenarios.size - 1) "Siguiente decisión" else "Ver resultados")
                    }
                }
            }
        }
    }
}
