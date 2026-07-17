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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HourglassBottom
import com.tamagotchi.code.R
import com.tamagotchi.code.ui.viewmodel.PetViewModel
import kotlinx.coroutines.delay

private data class BugSnippet(
    val lines: List<String>,
    val bugIndex: Int,
    val explanation: String,
    val humor: String
)

private val snippetPool = listOf(
    BugSnippet(
        listOf("val name = \"Codey\"", "println(name", "age++", "fun greet() { }"),
        bugIndex = 1,
        explanation = "Falta el paréntesis de cierre en println.",
        humor = "¡Era un paréntesis fugitivo! Se escapó sin pagar el alquiler."
    ),
    BugSnippet(
        listOf("if (x = 5) {", "print(\"Cinco\")", "} else {", "print(\"Otro\")", "}"),
        bugIndex = 0,
        explanation = "En Kotlin la comparación es == no =. = es asignación.",
        humor = "Codey: ¡Un solo = es asignación, doble == es comparación! No confundas al compilador."
    ),
    BugSnippet(
        listOf("fun add(a: Int, b: Int): Int {", "return a + b", "}", "add(2, 3)"),
        bugIndex = 1,
        explanation = "Falta el tipo de retorno explícito, debería ser ': Int'.",
        humor = "Codey: Hasta una calculadora de bolsillo sabe qué va a devolver."
    ),
    BugSnippet(
        listOf("val nums = listOf(1, 2, 3)", "for i in nums {", "print(i)", "}"),
        bugIndex = 1,
        explanation = "En Kotlin el for usa paréntesis: for (i in nums).",
        humor = "Codey: ¡Los paréntesis no son decoración! Son parte de la sintaxis."
    ),
    BugSnippet(
        listOf("fun main() {", "val msg = \"Hola\"", "println(msg)", "}//fin"),
        bugIndex = 0,
        explanation = "main no necesita paréntesis vacíos si es la entrada.",
        humor = "Codey: En realidad main() está bien, ¡pero este bug es tramposo!"
    ),
    BugSnippet(
        listOf("val count = 0", "while (count < 5) {", "println(count)", "count--", "}"),
        bugIndex = 3,
        explanation = "Está decrementando count en vez de incrementarlo. Bucle infinito.",
        humor = "Codey: ¡Así nunca llegarás a 5! Es como correr hacia atrás."
    ),
    BugSnippet(
        listOf("val data = \"123\"", "val number: Int = data", "println(number + 1)"),
        bugIndex = 1,
        explanation = "No se puede asignar un String directamente a Int. Usa toInt().",
        humor = "Codey: ¡No puedes convertir strings a Int por ósmosis! Usa .toInt()."
    ),
    BugSnippet(
        listOf("fun isEven(n: Int) {", "return n % 2 == 0", "}", "val r = isEven(4)"),
        bugIndex = 0,
        explanation = "La función debe declarar tipo de retorno: fun isEven(n: Int): Boolean.",
        humor = "Codey: El compilador no es adivino. Dile qué vas a devolver."
    ),
    BugSnippet(
        listOf("val items = listOf(1, 2, 3)", "items.add(4)", "println(items)"),
        bugIndex = 1,
        explanation = "listOf crea una lista inmutable. Usa mutableListOf.",
        humor = "Codey: ¡No puedes modificar una lista inmutable! Es como intentar cambiar el pasado."
    ),
    BugSnippet(
        listOf("fun greet() {", "println(\"Hola\")", "", "", "", "}"),
        bugIndex = 2,
        explanation = "Líneas vacías innecesarias. El código debe ser limpio.",
        humor = "Codey: ¿Estás escribiendo código o una novela? Muy poético pero poco práctico."
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BugHuntScreen(
    viewModel: PetViewModel,
    onNavigateBack: () -> Unit
) {
    val snippets = remember { snippetPool.shuffled().take(5) }

    var currentRound by remember { mutableStateOf(1) }
    val totalRounds = snippets.size
    var timeRemaining by remember { mutableStateOf(60) }
    var score by remember { mutableStateOf(0) }
    var isGameOver by remember { mutableStateOf(false) }
    var codeyReaction by remember { mutableStateOf("¡Encuentra el bug antes de que el compilador explote!") }
    var lastExplanation by remember { mutableStateOf<String?>(null) }
    var lastHumor by remember { mutableStateOf<String?>(null) }
    var answeredRound by remember { mutableStateOf(false) }

    val currentSnippet = snippets.getOrNull(currentRound - 1)

    LaunchedEffect(isGameOver) {
        if (!isGameOver) {
            while (timeRemaining > 0) {
                delay(1000)
                timeRemaining--
            }
            isGameOver = true
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.bug_hunt_title)) },
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
                val bytesEarned = score * 10
                Text(stringResource(R.string.game_result_reward, bytesEarned))
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Acertaste $score de $totalRounds",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(24.dp))
                Button(onClick = {
                    viewModel.recordGamePlay("bug_hunt")
                    viewModel.completeMinigame(bytesEarned, 10f, -5f)
                    onNavigateBack()
                }) {
                    Text(stringResource(R.string.game_result_finish))
                }
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(stringResource(R.string.bug_hunt_round, currentRound, totalRounds), fontWeight = FontWeight.Bold)
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Icon(
                            Icons.Filled.HourglassBottom,
                            contentDescription = null,
                            tint = if (timeRemaining < 10) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(16.dp)
                        )
                        Text("${timeRemaining}s", color = if (timeRemaining < 10) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface)
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

                Text(stringResource(R.string.bug_hunt_instruction), fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(12.dp))

                if (currentSnippet != null) {
                    currentSnippet.lines.forEachIndexed { index, line ->
                        val isCorrectLine = index == currentSnippet.bugIndex && answeredRound && lastExplanation != null
                        val isWrongPick = answeredRound && lastExplanation != null && index != currentSnippet.bugIndex

                        Surface(
                            color = when {
                                isCorrectLine -> Color(0xFF1B5E20).copy(alpha = 0.3f)
                                isWrongPick -> Color(0xFFB71C1C).copy(alpha = 0.1f)
                                else -> MaterialTheme.colorScheme.surfaceVariant
                            },
                            shape = RoundedCornerShape(4.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable(enabled = !answeredRound) {
                                    if (index == currentSnippet.bugIndex) {
                                        score++
                                        codeyReaction = currentSnippet.humor
                                    } else {
                                        codeyReaction = "¡Esa línea está bien! Sigue buscando..."
                                    }
                                    lastExplanation = currentSnippet.explanation
                                    lastHumor = currentSnippet.humor
                                    answeredRound = true
                                }
                        ) {
                            Text(
                                text = "${index + 1}. $line",
                                fontFamily = FontFamily.Monospace,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }

                    if (answeredRound && lastExplanation != null) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Surface(
                            color = if (lastHumor != null && score > 0) Color(0xFF1B5E20).copy(alpha = 0.15f) else Color(0xFFB71C1C).copy(alpha = 0.1f),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Explicación: ${lastExplanation}",
                                modifier = Modifier.padding(12.dp),
                                fontFamily = FontFamily.Monospace,
                                fontSize = MaterialTheme.typography.bodySmall.fontSize,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        Button(onClick = {
                            if (currentRound < totalRounds) {
                                currentRound++
                                answeredRound = false
                                lastExplanation = null
                                lastHumor = null
                                codeyReaction = "¡Siguiente ronda! ¿Dónde se esconde el bug?"
                            } else {
                                isGameOver = true
                            }
                        }) {
                            Text(if (currentRound < totalRounds) "Siguiente ronda" else "Ver resultados")
                        }
                    }
                }
            }
        }
    }
}
