package com.tamagotchi.code.feature.games

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tamagotchi.code.R
import com.tamagotchi.code.ui.viewmodel.PetViewModel

private data class RefactorPuzzle(
    val title: String,
    val blocks: List<String>
)

private val puzzleBank = listOf(
    RefactorPuzzle(
        "Calcular total",
        listOf(
            "fun calculateTotal(items: List<Int>): Int {",
            "    var total = 0",
            "    for (item in items) {",
            "        total += item",
            "    }",
            "    return total",
            "}"
        )
    ),
    RefactorPuzzle(
        "Filtrar pares",
        listOf(
            "fun filterEven(numbers: List<Int>): List<Int> {",
            "    val result = mutableListOf<Int>()",
            "    for (n in numbers) {",
            "        if (n % 2 == 0) {",
            "            result.add(n)",
            "        }",
            "    }",
            "    return result",
            "}"
        )
    ),
    RefactorPuzzle(
        "Saludo personalizado",
        listOf(
            "fun greet(name: String, age: Int): String {",
            "    val greeting = \"Hola, \$name\"",
            "    val ageMsg = if (age >= 18) \"Eres mayor\" else \"Eres menor\"",
            "    return \"\$greeting. \$ageMsg\"",
            "}"
        )
    ),
    RefactorPuzzle(
        "Buscar máximo",
        listOf(
            "fun findMax(values: List<Int>): Int? {",
            "    if (values.isEmpty()) return null",
            "    var max = values[0]",
            "    for (v in values) {",
            "        if (v > max) max = v",
            "    }",
            "    return max",
            "}"
        )
    ),
    RefactorPuzzle(
        "Contar vocales",
        listOf(
            "fun countVowels(text: String): Int {",
            "    val vowels = setOf('a', 'e', 'i', 'o', 'u')",
            "    var count = 0",
            "    for (ch in text.lowercase()) {",
            "        if (ch in vowels) count++",
            "    }",
            "    return count",
            "}"
        )
    ),
    RefactorPuzzle(
        "Invertir lista",
        listOf(
            "fun reverseList<T>(items: List<T>): List<T> {",
            "    val result = mutableListOf<T>()",
            "    for (i in items.indices.reversed()) {",
            "        result.add(items[i])",
            "    }",
            "    return result",
            "}"
        )
    ),
    RefactorPuzzle(
        "Es palíndromo",
        listOf(
            "fun isPalindrome(word: String): Boolean {",
            "    val cleaned = word.lowercase().filter { it.isLetter() }",
            "    return cleaned == cleaned.reversed()",
            "}"
        )
    ),
    RefactorPuzzle(
        "Promedio de notas",
        listOf(
            "fun average(grades: List<Double>): Double {",
            "    if (grades.isEmpty()) return 0.0",
            "    val sum = grades.sum()",
            "    return sum / grades.size",
            "}"
        )
    ),
    RefactorPuzzle(
        "Generar rango",
        listOf(
            "fun generateRange(start: Int, end: Int): List<Int> {",
            "    val range = mutableListOf<Int>()",
            "    for (i in start..end) {",
            "        range.add(i)",
            "    }",
            "    return range",
            "}"
        )
    ),
    RefactorPuzzle(
        "Validar email",
        listOf(
            "fun isValidEmail(email: String): Boolean {",
            "    if (!email.contains('@')) return false",
            "    val parts = email.split('@')",
            "    if (parts.size != 2) return false",
            "    return parts[1].contains('.')",
            "}"
        )
    ),
    RefactorPuzzle(
        "Fibonacci",
        listOf(
            "fun fibonacci(n: Int): List<Int> {",
            "    if (n <= 0) return emptyList()",
            "    val fib = mutableListOf(0, 1)",
            "    for (i in 2 until n) {",
            "        fib.add(fib[i - 1] + fib[i - 2])",
            "    }",
            "    return fib.take(n)",
            "}"
        )
    ),
    RefactorPuzzle(
        "Capitalizar palabras",
        listOf(
            "fun capitalizeWords(sentence: String): String {",
            "    return sentence.split(\" \")",
            "        .joinToString(\" \") { word ->",
            "            word.replaceFirstChar { it.uppercase() }",
            "        }",
            "}"
        )
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RefactorRushScreen(
    viewModel: PetViewModel,
    onNavigateBack: () -> Unit
) {
    val puzzle = remember { puzzleBank.random() }
    val originalBlocks = remember { puzzle.blocks }

    var isGameOver by remember { mutableStateOf(false) }
    var codeyReaction by remember { mutableStateOf("¡Ordena este desastre!") }
    var currentBlocks by remember { mutableStateOf(originalBlocks.shuffled()) }
    var attempts by remember { mutableStateOf(0) }

    fun moveUp(index: Int) {
        if (index > 0) {
            val newList = currentBlocks.toMutableList()
            val temp = newList[index - 1]
            newList[index - 1] = newList[index]
            newList[index] = temp
            currentBlocks = newList
        }
    }

    fun moveDown(index: Int) {
        if (index < currentBlocks.size - 1) {
            val newList = currentBlocks.toMutableList()
            val temp = newList[index + 1]
            newList[index + 1] = newList[index]
            newList[index] = temp
            currentBlocks = newList
        }
    }

    fun checkResult() {
        attempts++
        if (currentBlocks == originalBlocks) {
            codeyReaction = "¡Excelente! Código limpio y ordenado en $attempts intento(s)."
            isGameOver = true
        } else {
            codeyReaction = "Sigue intentando, todavía no compila (intento $attempts)."
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.refactor_rush_title)) },
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
                Spacer(modifier = Modifier.height(8.dp))
                Text(puzzle.title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(16.dp))
                val bytesEarned = maxOf(50 - (attempts * 5), 10)
                Text(stringResource(R.string.game_result_reward, bytesEarned))
                Spacer(modifier = Modifier.height(24.dp))
                Button(onClick = {
                    viewModel.recordGamePlay("refactor_rush")
                    viewModel.completeMinigame(bytesEarned, 10f, -5f)
                    onNavigateBack()
                }) {
                    Text(stringResource(R.string.game_result_finish))
                }
            } else {
                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Codey: $codeyReaction", modifier = Modifier.padding(16.dp), color = MaterialTheme.colorScheme.onPrimaryContainer)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(puzzle.title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(4.dp))
                Text(stringResource(R.string.refactor_rush_instruction), style = MaterialTheme.typography.bodyMedium)

                Spacer(modifier = Modifier.height(16.dp))

                currentBlocks.forEachIndexed { index, block ->
                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(8.dp)
                        ) {
                            Column {
                                IconButton(onClick = { moveUp(index) }, enabled = index > 0) {
                                    Icon(Icons.Default.KeyboardArrowUp, contentDescription = "Up")
                                }
                                IconButton(onClick = { moveDown(index) }, enabled = index < currentBlocks.size - 1) {
                                    Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Down")
                                }
                            }
                            Text(
                                text = block,
                                fontFamily = FontFamily.Monospace,
                                modifier = Modifier
                                    .padding(8.dp)
                                    .weight(1f)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
                Button(onClick = { checkResult() }) {
                    Text("Verificar")
                }
            }
        }
    }
}
