package com.tamagotchi.code.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tamagotchi.code.data.ChallengesData
import com.tamagotchi.code.data.CodingChallenge
import com.tamagotchi.code.data.database.FocusSessionEntity
import com.tamagotchi.code.data.database.PetStateEntity
import com.tamagotchi.code.data.database.StudySessionEntity
import com.tamagotchi.code.data.repository.PetRepository
import com.tamagotchi.code.data.repository.UserPreferencesRepository
import com.tamagotchi.code.data.repository.AchievementsRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar

class PetViewModel(
    private val repository: PetRepository,
    private val userPreferences: UserPreferencesRepository,
    private val achievementsRepository: AchievementsRepository
) : ViewModel() {
    val soundManager = com.tamagotchi.code.util.SoundManager()

    var hasSeenOnboarding = mutableStateOf(false)
        private set

    var currentTheme = mutableStateOf("Matrix Green")
        private set

    val unlockedThemes = MutableStateFlow<Set<String>>(setOf("Matrix Green"))
    val unlockedAchievements = MutableStateFlow<Set<String>>(emptySet())

    fun unlockTheme(themeName: String) {
        viewModelScope.launch {
            userPreferences.addUnlockedTheme(themeName)
            unlockedThemes.value = unlockedThemes.value + themeName
            soundManager.playLevelUp()
        }
    }

    val selectedTopics = MutableStateFlow<Set<String>>(emptySet())
    val defaultInitialTopics = setOf("Kotlin", "Estructuras de Datos", "Git")

    fun setTopics(topics: Set<String>) {
        viewModelScope.launch {
            userPreferences.setSelectedTopics(topics)
            selectedTopics.value = topics
        }
    }

    fun completeOnboarding(petName: String, topics: Set<String>) {
        viewModelScope.launch {
            userPreferences.setOnboardingCompleted()
            
            val finalTopics = if (topics.isEmpty()) defaultInitialTopics else topics
            userPreferences.setSelectedTopics(finalTopics)
            selectedTopics.value = finalTopics
            
            hasSeenOnboarding.value = true
            soundManager.playLevelUp()
            val current = repository.petState.firstOrNull()
            val updatedName = if (petName.isNotBlank()) petName else "Codey"
            if (current == null) {
                val defaultPet = PetStateEntity(name = updatedName)
                repository.savePetState(defaultPet)
                loadChallengesForLanguage("Kotlin")
            } else {
                repository.savePetState(current.copy(name = updatedName))
            }
        }
    }

    fun skipOnboarding() {
        completeOnboarding("Codey", defaultInitialTopics)
    }

    val difficulty = MutableStateFlow("Inicial")
    val focusDurationDefault = MutableStateFlow(25)
    val soundEnabled = MutableStateFlow(true)
    val vibrationEnabled = MutableStateFlow(true)
    val reduceMotion = MutableStateFlow(false)

    fun changeTheme(theme: String) {
        viewModelScope.launch {
            userPreferences.setTheme(theme)
            currentTheme.value = theme
            soundManager.playClick()
        }
    }

    fun setDifficulty(newDifficulty: String) {
        viewModelScope.launch {
            userPreferences.setDifficulty(newDifficulty)
            difficulty.value = newDifficulty
        }
    }

    fun setFocusDurationDefault(duration: Int) {
        viewModelScope.launch {
            userPreferences.setFocusDurationDefault(duration)
            focusDurationDefault.value = duration
        }
    }

    fun toggleSound(enabled: Boolean) {
        viewModelScope.launch {
            userPreferences.setSoundEnabled(enabled)
            soundEnabled.value = enabled
        }
    }

    fun toggleVibration(enabled: Boolean) {
        viewModelScope.launch {
            userPreferences.setVibrationEnabled(enabled)
            vibrationEnabled.value = enabled
        }
    }

    fun toggleReduceMotion(enabled: Boolean) {
        viewModelScope.launch {
            userPreferences.setReduceMotion(enabled)
            reduceMotion.value = enabled
        }
    }

    fun exportProgressMock() {
        // Mock function for exporting progress
    }

    fun resetProgress() {
        viewModelScope.launch {
            // Note: Needs dao methods or we just save a new initial PetStateEntity
            val defaultPet = PetStateEntity(name = "Codey")
            repository.savePetState(defaultPet)
            userPreferences.setSelectedTopics(defaultInitialTopics)
            hasSeenOnboarding.value = false
            userPreferences.setDifficulty("Inicial")
            userPreferences.setFocusDurationDefault(25)
        }
    }

    override fun onCleared() {
        super.onCleared()
        soundManager.release()
    }

    val petState: StateFlow<PetStateEntity?>
    val studySessions: StateFlow<List<StudySessionEntity>>
    val latestFocusSession: StateFlow<FocusSessionEntity?>

    var isTimerRunning = mutableStateOf(false)
        private set
    var timerSecondsRemaining = mutableStateOf(0)
        private set
    var timerSelectedMinutes = mutableStateOf(25)
        private set
    var currentStudyTopic = mutableStateOf("Kotlin")
        private set

    var showOfflineRewardDialog = mutableStateOf(false)
        private set
    var offlineRewardXp = mutableStateOf(0)
        private set
    var offlineRewardBytes = mutableStateOf(0)
        private set

    fun dismissOfflineRewardDialog() {
        showOfflineRewardDialog.value = false
    }

    private var activeFocusSessionId: Long? = null
    private var timerJob: Job? = null
    private val _activeFocusEntity = MutableStateFlow<FocusSessionEntity?>(null)

    private val _activeChallenges = MutableStateFlow<List<CodingChallenge>>(emptyList())
    val activeChallenges = _activeChallenges.asStateFlow()

    private val _currentChallengeIndex = MutableStateFlow(0)
    val currentChallengeIndex = _currentChallengeIndex.asStateFlow()

    private val _challengeFeedback = MutableStateFlow<String?>(null)
    val challengeFeedback = _challengeFeedback.asStateFlow()

    private val _selectedChallengeLanguage = MutableStateFlow("Kotlin")
    val selectedChallengeLanguage = _selectedChallengeLanguage.asStateFlow()

    init {
        studySessions = repository.studySessions.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        petState = repository.petState.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

        latestFocusSession = repository.latestFocusSession.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

        viewModelScope.launch {
            hasSeenOnboarding.value = userPreferences.getHasSeenOnboarding()
            userPreferences.currentTheme.collect { theme ->
                currentTheme.value = theme
            }
        }

        viewModelScope.launch {
            userPreferences.selectedTopics.collect { topics ->
                selectedTopics.value = topics
            }
        }
        
        viewModelScope.launch { userPreferences.difficulty.collect { difficulty.value = it } }
        viewModelScope.launch { userPreferences.focusDurationDefault.collect { focusDurationDefault.value = it } }
        viewModelScope.launch { userPreferences.soundEnabled.collect { soundEnabled.value = it } }
        viewModelScope.launch { userPreferences.vibrationEnabled.collect { vibrationEnabled.value = it } }
        viewModelScope.launch { userPreferences.reduceMotion.collect { reduceMotion.value = it } }

        viewModelScope.launch {
            userPreferences.unlockedThemes.collect { themes ->
                unlockedThemes.value = themes
            }
        }

        viewModelScope.launch {
            achievementsRepository.unlockedAchievements.collect { achievements ->
                unlockedAchievements.value = achievements
            }
        }

        viewModelScope.launch {
            userPreferences.gameCooldowns.collect { cooldowns ->
                _gameLastPlayed.value = cooldowns
            }
        }

        viewModelScope.launch {
            val current = repository.petState.firstOrNull()
            if (current == null) {
                val defaultPet = PetStateEntity()
                repository.savePetState(defaultPet)
                loadChallengesForLanguage("Kotlin")
            } else {
                val decayed = applyDecay(current)
                repository.savePetState(decayed)
                loadChallengesForLanguage(decayed.language)
            }
        }

        viewModelScope.launch {
            val active = repository.getActiveFocusSession()
            if (active != null) {
                val elapsedSeconds = (System.currentTimeMillis() - active.startedAt) / 1000
                val totalSeconds = active.plannedDurationMinutes * 60L
                val remaining = (totalSeconds - elapsedSeconds).toInt()
                if (remaining > 0) {
                    isTimerRunning.value = true
                    timerSelectedMinutes.value = active.plannedDurationMinutes
                    timerSecondsRemaining.value = remaining
                    currentStudyTopic.value = active.topic
                    activeFocusSessionId = active.id
                    resumeTimer()
                } else {
                    val current = repository.petState.firstOrNull()
                    if (current != null) {
                        val xpReward = active.plannedDurationMinutes * 2
                        val bytesReward = active.plannedDurationMinutes * 1
                        val updatedXp = current.xp + xpReward
                        val updatedLevel = calculateLevel(updatedXp, current.level)
                        val updated = current.copy(
                            xp = updatedXp,
                            level = updatedLevel,
                            bytes = current.bytes + bytesReward
                        )
                        repository.completeOfflineSession(active.id, "COMPLETED", updated)
                        offlineRewardXp.value = xpReward
                        offlineRewardBytes.value = bytesReward
                        showOfflineRewardDialog.value = true
                    } else {
                        repository.updateFocusSessionStatus(active.id, "COMPLETED")
                    }
                }
            }
        }
    }

    private fun resumeTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (timerSecondsRemaining.value > 0 && isTimerRunning.value) {
                delay(1000)
                timerSecondsRemaining.value -= 1
            }
            if (timerSecondsRemaining.value <= 0 && isTimerRunning.value) {
                completeStudySession()
            }
        }
    }

    fun selectLanguage(language: String) {
        viewModelScope.launch {
            val current = petState.value ?: return@launch
            val updated = current.copy(language = language)
            repository.savePetState(updated)
            loadChallengesForLanguage(language)
        }
    }

    fun renamePet(newName: String) {
        if (newName.isBlank()) return
        viewModelScope.launch {
            val current = petState.value ?: return@launch
            val updated = current.copy(name = newName)
            repository.savePetState(updated)
        }
    }

    fun loadChallengesForLanguage(language: String) {
        _selectedChallengeLanguage.value = language
        val filtered = ChallengesData.challenges.filter { it.language.equals(language, ignoreCase = true) }
        _activeChallenges.value = filtered.shuffled().take(3)
        _currentChallengeIndex.value = 0
        _challengeFeedback.value = null
    }

    fun submitAnswer(optionIndex: Int) {
        val challengesList = _activeChallenges.value
        val index = _currentChallengeIndex.value
        if (index >= challengesList.size) return

        val challenge = challengesList[index]
        if (optionIndex == challenge.correctAnswerIndex) {
            _challengeFeedback.value = "CORRECT"
            soundManager.playSuccess()
            viewModelScope.launch {
                val current = petState.value ?: return@launch
                
                val earnedBytes = if (challenge.type == "DEBUG") 30 else 25
                val earnedXp = if (challenge.type == "DEBUG") 25 else 20
                val hungerRestore = 15f
                val healthRestore = 20f

                val updatedXp = current.xp + earnedXp
                val updatedLevel = calculateLevel(updatedXp, current.level)
                
                val updated = current.copy(
                    xp = updatedXp,
                    level = updatedLevel,
                    bytes = current.bytes + earnedBytes,
                    hunger = (current.hunger + hungerRestore).coerceIn(0f, 100f),
                    health = (current.health + healthRestore).coerceIn(0f, 100f),
                    currentStatus = determineStatus(
                        health = (current.health + healthRestore).coerceIn(0f, 100f),
                        hunger = (current.hunger + hungerRestore).coerceIn(0f, 100f),
                        energy = current.energy,
                        isSleeping = current.currentStatus == "SLEEPING",
                        isStudying = current.currentStatus == "STUDYING"
                    )
                )
                repository.savePetState(updated)
                
                if (updated.level > current.level) {
                    achievementsRepository.unlockAchievement("Nivel Experto")
                }
            }
        } else {
            soundManager.playError()
            _challengeFeedback.value = "INCORRECT"
        }
    }

    fun nextChallenge() {
        val size = _activeChallenges.value.size
        val nextIndex = _currentChallengeIndex.value + 1
        if (nextIndex < size) {
            _currentChallengeIndex.value = nextIndex
            _challengeFeedback.value = null
        } else {
            loadChallengesForLanguage(_selectedChallengeLanguage.value)
        }
    }

    fun buyShopItem(itemName: String, cost: Int, hungerRestore: Float, healthRestore: Float, energyRestore: Float) {
        viewModelScope.launch {
            val current = petState.value ?: return@launch
            if (current.bytes < cost) {
                soundManager.playError()
                return@launch
            }

            soundManager.playBuy()

            val updatedHunger = (current.hunger + hungerRestore).coerceIn(0f, 100f)
            val updatedHealth = (current.health + healthRestore).coerceIn(0f, 100f)
            val updatedEnergy = (current.energy + energyRestore).coerceIn(0f, 100f)
            
            var newStatus = current.currentStatus
            if (newStatus != "SLEEPING" && newStatus != "STUDYING") {
                newStatus = determineStatus(updatedHealth, updatedHunger, updatedEnergy, false, false)
            }

            val updated = current.copy(
                bytes = current.bytes - cost,
                hunger = updatedHunger,
                health = updatedHealth,
                energy = updatedEnergy,
                currentStatus = newStatus
            )
            repository.savePetState(updated)
        }
    }

    fun toggleSleep() {
        soundManager.playClick()
        viewModelScope.launch {
            val current = petState.value ?: return@launch
            val isCurrentlySleeping = current.currentStatus == "SLEEPING"
            
            if (!isCurrentlySleeping) {
                soundManager.playSleep()
            }
            
            val newStatus = if (isCurrentlySleeping) {
                determineStatus(current.health, current.hunger, current.energy, false, false)
            } else {
                "SLEEPING"
            }

            val updated = current.copy(
                currentStatus = newStatus,
                lastUpdated = System.currentTimeMillis()
            )
            repository.savePetState(updated)
        }
    }

    fun startStudyTimer(minutes: Int, topic: String) {
        if (isTimerRunning.value) return

        isTimerRunning.value = true
        timerSelectedMinutes.value = minutes
        timerSecondsRemaining.value = minutes * 60
        currentStudyTopic.value = topic

        viewModelScope.launch {
            val current = petState.value ?: return@launch

            val session = FocusSessionEntity(
                topic = topic,
                plannedDurationMinutes = minutes,
                startedAt = System.currentTimeMillis(),
                status = "RUNNING"
            )
            repository.saveFocusSession(session)
            activeFocusSessionId = session.id
            _activeFocusEntity.value = session

            val updated = current.copy(
                currentStatus = "STUDYING",
                lastUpdated = System.currentTimeMillis()
            )
            repository.savePetState(updated)
        }

        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (timerSecondsRemaining.value > 0 && isTimerRunning.value) {
                delay(1000)
                timerSecondsRemaining.value -= 1
            }
            if (timerSecondsRemaining.value <= 0 && isTimerRunning.value) {
                completeStudySession()
            }
        }
    }

    fun completeFocusSession() {
        viewModelScope.launch {
            if (_activeFocusEntity.value != null && _activeFocusEntity.value!!.status == "RUNNING") {
                val pet = repository.petState.firstOrNull() ?: return@launch
                val elapsed = System.currentTimeMillis() - _activeFocusEntity.value!!.startedAt
                val minutes = (elapsed / 60000).toInt()
                
                val xpEarned = minutes * 5
                val healthRecovered = 20
                val energyRecovered = -10
                
                val updated = pet.copy(
                    xp = pet.xp + xpEarned,
                    health = (pet.health + healthRecovered).coerceIn(0f, 100f),
                    energy = (pet.energy + energyRecovered).coerceIn(0f, 100f)
                )

                if (updated.xp >= updated.level * 100) {
                    soundManager.playLevelUp()
                } else {
                    soundManager.playSuccess()
                }
                
                repository.savePetState(updated)
                
                repository.updateFocusSessionStatus(_activeFocusEntity.value!!.id, "COMPLETED")
                
                _activeFocusEntity.value = null
                
                achievementsRepository.unlockAchievement("Primer build")
            }
        }
    }

    fun cancelStudyTimer() {
        if (!isTimerRunning.value) return
        isTimerRunning.value = false
        timerJob?.cancel()

        viewModelScope.launch {
            activeFocusSessionId?.let { id ->
                repository.updateFocusSessionStatus(id, "CANCELLED")
                activeFocusSessionId = null
            }
            val current = petState.value ?: return@launch
            val newStatus = determineStatus(current.health, current.hunger, current.energy, false, false)
            val updated = current.copy(
                currentStatus = newStatus,
                lastUpdated = System.currentTimeMillis()
            )
            repository.savePetState(updated)
        }
    }

    private suspend fun completeStudySession() {
        isTimerRunning.value = false
        timerJob?.cancel()

        activeFocusSessionId?.let { id ->
            repository.updateFocusSessionStatus(id, "COMPLETED")
            activeFocusSessionId = null
        }

        val current = petState.value ?: return
        val minutes = timerSelectedMinutes.value
        val topic = currentStudyTopic.value

        val session = StudySessionEntity(topic = topic, durationMinutes = minutes)
        repository.addStudySession(session)

        val baseBytes = minutes * 2
        val baseXP = minutes * 3
        
        val bonusBytes = if (minutes >= 25) 50 else 0
        val bonusXP = if (minutes >= 25) 75 else 0

        val totalBytesEarned = baseBytes + bonusBytes
        val totalXPEarned = baseXP + bonusXP

        val now = System.currentTimeMillis()
        var newStreak = current.streak
        
        if (current.lastStudyDate == 0L) {
            newStreak = 1
        } else {
            val lastCalendar = Calendar.getInstance().apply { timeInMillis = current.lastStudyDate }
            val nowCalendar = Calendar.getInstance().apply { timeInMillis = now }

            val sameDay = lastCalendar.get(Calendar.YEAR) == nowCalendar.get(Calendar.YEAR) &&
                    lastCalendar.get(Calendar.DAY_OF_YEAR) == nowCalendar.get(Calendar.DAY_OF_YEAR)

            if (!sameDay) {
                val yesterdayCalendar = Calendar.getInstance().apply {
                    timeInMillis = now
                    add(Calendar.DAY_OF_YEAR, -1)
                }
                val studiedYesterday = lastCalendar.get(Calendar.YEAR) == yesterdayCalendar.get(Calendar.YEAR) &&
                        lastCalendar.get(Calendar.DAY_OF_YEAR) == yesterdayCalendar.get(Calendar.DAY_OF_YEAR)

                if (studiedYesterday) {
                    newStreak += 1
                } else {
                    newStreak = 1
                }
            }
        }

        val energyCost = (minutes * 0.5f).coerceAtMost(30f)
        val updatedEnergy = (current.energy - energyCost).coerceIn(0f, 100f)
        val updatedXp = current.xp + totalXPEarned
        val updatedLevel = calculateLevel(updatedXp, current.level)

        val newStatus = determineStatus(current.health, current.hunger, updatedEnergy, false, false)

        val updated = current.copy(
            xp = updatedXp,
            level = updatedLevel,
            bytes = current.bytes + totalBytesEarned,
            energy = updatedEnergy,
            streak = newStreak,
            lastStudyDate = now,
            lastUpdated = now,
            currentStatus = newStatus
        )
        repository.savePetState(updated)
    }

    private fun calculateLevel(xp: Int, currentLevel: Int): Int {
        var level = 1
        var requiredXp = 100
        while (xp >= requiredXp) {
            level++
            requiredXp += level * 100
        }
        return level
    }

    private fun determineStatus(health: Float, hunger: Float, energy: Float, isSleeping: Boolean, isStudying: Boolean, isExcited: Boolean = false): String {
        return when {
            isExcited -> "EXCITED"
            isStudying -> "STUDYING"
            isSleeping -> "SLEEPING"
            health < 20f -> "SICK"
            hunger < 20f -> "HUNGRY"
            energy < 15f -> "SAD"
            else -> "HAPPY"
        }
    }

    private fun applyDecay(state: PetStateEntity): PetStateEntity {
        val now = System.currentTimeMillis()
        val elapsedMs = now - state.lastUpdated
        if (elapsedMs <= 0) return state

        val hours = elapsedMs.toFloat() / (1000f * 60f * 60f)
        if (hours < 0.02f) return state

        var newHunger = state.hunger
        var newEnergy = state.energy
        var newHealth = state.health
        var newStatus = state.currentStatus
        var newStreak = state.streak

        val hoursSinceLastStudy = if (state.lastStudyDate > 0) {
            (now - state.lastStudyDate).toFloat() / (1000f * 60f * 60f)
        } else {
            0f
        }

        if (hoursSinceLastStudy > 48f) {
            newStreak = 0
        }

        if (state.currentStatus == "SLEEPING") {
            newEnergy = (newEnergy + (hours * 12f)).coerceIn(0f, 100f)
            newHunger = (newHunger - (hours * 1f)).coerceIn(0f, 100f)
            if (newEnergy >= 100f) {
                newStatus = "HAPPY"
            }
        } else {
            newHunger = (newHunger - (hours * 2.5f)).coerceIn(0f, 100f)
            newEnergy = (newEnergy - (hours * 2f)).coerceIn(0f, 100f)
        }

        val baseHealthDecay = hours * 1.5f
        newHealth = (newHealth - baseHealthDecay).coerceIn(0f, 100f)

        if (newHunger <= 0f) {
            newHealth = (newHealth - (hours * 3f)).coerceIn(0f, 100f)
        }
        if (newEnergy <= 10f) {
            newHealth = (newHealth - (hours * 1f)).coerceIn(0f, 100f)
        }
        if (hoursSinceLastStudy > 72f) {
            newHealth = (newHealth - (hours * 1.5f)).coerceIn(0f, 100f)
        }

        if (newStatus != "SLEEPING" && newStatus != "STUDYING") {
            newStatus = determineStatus(newHealth, newHunger, newEnergy, false, false)
        }

        return state.copy(
            hunger = newHunger,
            energy = newEnergy,
            health = newHealth,
            streak = newStreak,
            lastUpdated = now,
            currentStatus = newStatus
        )
    }

    fun petThePet() {
        viewModelScope.launch {
            val current = petState.value ?: return@launch
            soundManager.playClick()
            val updatedEnergy = (current.energy + 15f).coerceIn(0f, 100f)
            val updatedHealth = (current.health + 5f).coerceIn(0f, 100f)
            val newStatus = if (current.currentStatus != "SLEEPING" && current.currentStatus != "STUDYING") {
                determineStatus(updatedHealth, current.hunger, updatedEnergy, false, false)
            } else {
                current.currentStatus
            }
            val updated = current.copy(
                energy = updatedEnergy,
                health = updatedHealth,
                currentStatus = newStatus,
                lastUpdated = System.currentTimeMillis()
            )
            repository.savePetState(updated)
        }
    }

    fun cleanThePet() {
        viewModelScope.launch {
            val current = petState.value ?: return@launch
            soundManager.playClick()
            val updatedHealth = (current.health + 12f).coerceIn(0f, 100f)
            val updatedEnergy = (current.energy + 8f).coerceIn(0f, 100f)
            val newStatus = if (current.currentStatus != "SLEEPING" && current.currentStatus != "STUDYING") {
                determineStatus(updatedHealth, current.hunger, updatedEnergy, false, false)
            } else {
                current.currentStatus
            }
            val updated = current.copy(
                health = updatedHealth,
                energy = updatedEnergy,
                currentStatus = newStatus,
                lastUpdated = System.currentTimeMillis()
            )
            repository.savePetState(updated)
        }
    }

    fun canPlayGame(gameId: String, cooldownHours: Long = 24): Boolean {
        val lastPlayed = _gameLastPlayed.value[gameId] ?: 0L
        return System.currentTimeMillis() - lastPlayed >= cooldownHours * 60 * 60 * 1000
    }

    fun recordGamePlay(gameId: String) {
        viewModelScope.launch {
            userPreferences.recordGamePlay(gameId)
            _gameLastPlayed.value = _gameLastPlayed.value + (gameId to System.currentTimeMillis())
        }
    }

    private val _gameLastPlayed = MutableStateFlow<Map<String, Long>>(emptyMap())
    val gameCooldowns: StateFlow<Map<String, Long>> = _gameLastPlayed.asStateFlow()

    fun completeMinigame(bytesEarned: Int, healthEarned: Float, energyCost: Float) {
        viewModelScope.launch {
            val pet = repository.petState.firstOrNull() ?: return@launch
            val updated = pet.copy(
                bytes = pet.bytes + bytesEarned,
                health = (pet.health + healthEarned).coerceIn(0f, 100f),
                energy = (pet.energy + energyCost).coerceIn(0f, 100f)
            )
            repository.savePetState(updated)
            soundManager.playSuccess()
            
            if (bytesEarned == 100) { // Bug Hunt score 10
                achievementsRepository.unlockAchievement("Cazador de bugs")
            } else if (bytesEarned == 45) { // Git Rescue score 3
                achievementsRepository.unlockAchievement("Git sin pánico")
            }
        }
    }
}
