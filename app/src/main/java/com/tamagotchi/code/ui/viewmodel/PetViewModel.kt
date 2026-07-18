package com.tamagotchi.code.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tamagotchi.code.data.ChallengesData
import com.tamagotchi.code.data.CodeCard
import com.tamagotchi.code.data.CodingChallenge
import com.tamagotchi.code.data.codeCards
import com.tamagotchi.code.data.database.FocusSessionEntity
import com.tamagotchi.code.data.database.PetStateEntity
import com.tamagotchi.code.data.database.StudySessionEntity
import com.tamagotchi.code.data.repository.PetRepository
import com.tamagotchi.code.data.repository.UserPreferencesRepository
import com.tamagotchi.code.data.repository.AchievementsRepository
import com.tamagotchi.code.ui.theme.ThemeRegistry
import com.tamagotchi.code.util.DecayCalculator
import com.tamagotchi.code.util.LevelCalculator
import com.tamagotchi.code.util.RewardCalculator
import com.tamagotchi.code.util.StatusCalculator
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
class PetViewModel(
    private val repository: PetRepository,
    private val userPreferences: UserPreferencesRepository,
    private val achievementsRepository: AchievementsRepository
) : ViewModel() {
    val soundManager = com.tamagotchi.code.util.SoundManager()

    var hasSeenOnboarding = mutableStateOf(false)
        private set

    var currentTheme = mutableStateOf("Default")
        private set

    var defaultThemeMode = mutableStateOf("SYSTEM")
        private set

    val unlockedThemes = MutableStateFlow<Set<String>>(ThemeRegistry.allThemes.map { it.name }.toSet() + "Default")
    val unlockedAchievements = MutableStateFlow<Set<String>>(emptySet())

    fun unlockTheme(themeName: String) {
        viewModelScope.launch {
            userPreferences.addUnlockedTheme(themeName)
            val newSet = unlockedThemes.value + themeName
            unlockedThemes.value = newSet
            soundManager.playLevelUp()
            if (newSet.size >= 3) achievementsRepository.unlockAchievement("coleccionista")
            if (newSet.size >= 12) achievementsRepository.unlockAchievement("completista")
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
            
            // Lógica para dificultad principiante
            if (newDifficulty.contains("Principiante", ignoreCase = true) || 
                newDifficulty.contains("experiencia", ignoreCase = true)) {
                // Podríamos cargar un set de retos ultra-básicos aquí
            }
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

    fun setDefaultThemeMode(mode: String) {
        viewModelScope.launch {
            userPreferences.setDefaultThemeMode(mode)
            defaultThemeMode.value = mode
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

    var showDailyRewardDialog = mutableStateOf(false)
        private set
    var nextClaimableDay = mutableStateOf(1)
        private set
    var isDailyRewardClaimedToday = mutableStateOf(false)
        private set

    fun dismissDailyRewardDialog() {
        showDailyRewardDialog.value = false
    }

    fun openDailyRewardDialog() {
        showDailyRewardDialog.value = true
    }

    fun checkDailyRewardEligibility() {
        viewModelScope.launch {
            delay(1000)
            val lastClaimTime = userPreferences.lastDailyRewardClaimTime.firstOrNull() ?: 0L
            val currentSavedDay = userPreferences.dailyRewardDay.firstOrNull() ?: 0
            val now = System.currentTimeMillis()
            
            if (lastClaimTime == 0L) {
                nextClaimableDay.value = 1
                isDailyRewardClaimedToday.value = false
                showDailyRewardDialog.value = true
            } else {
                val lastCal = java.util.Calendar.getInstance().apply { timeInMillis = lastClaimTime }
                val nowCal = java.util.Calendar.getInstance().apply { timeInMillis = now }
                
                val sameDay = lastCal.get(java.util.Calendar.YEAR) == nowCal.get(java.util.Calendar.YEAR) &&
                        lastCal.get(java.util.Calendar.DAY_OF_YEAR) == nowCal.get(java.util.Calendar.DAY_OF_YEAR)
                
                if (sameDay) {
                    nextClaimableDay.value = if (currentSavedDay >= 7) 1 else currentSavedDay
                    isDailyRewardClaimedToday.value = true
                    showDailyRewardDialog.value = false
                } else {
                    val yesterdayCal = java.util.Calendar.getInstance().apply {
                        timeInMillis = now
                        add(java.util.Calendar.DAY_OF_YEAR, -1)
                    }
                    val claimedYesterday = lastCal.get(java.util.Calendar.YEAR) == yesterdayCal.get(java.util.Calendar.YEAR) &&
                            lastCal.get(java.util.Calendar.DAY_OF_YEAR) == yesterdayCal.get(java.util.Calendar.DAY_OF_YEAR)
                    
                    if (claimedYesterday) {
                        nextClaimableDay.value = if (currentSavedDay >= 7) 1 else currentSavedDay + 1
                    } else {
                        nextClaimableDay.value = 1
                    }
                    isDailyRewardClaimedToday.value = false
                    showDailyRewardDialog.value = true
                }
            }
        }
    }

    fun claimDailyReward() {
        viewModelScope.launch {
            val pet = repository.petState.firstOrNull() ?: return@launch
            val targetDay = nextClaimableDay.value
            if (targetDay < 1 || targetDay > 7) return@launch
            
            val reward = dailyRewardsList[targetDay - 1]
            
            val updatedXp = pet.xp + reward.xp
            val updatedLevel = LevelCalculator.calculateLevel(updatedXp)
            val updatedBytes = pet.bytes + reward.bytes
            val updatedHealth = (pet.health + reward.healthRestore).coerceIn(0f, 100f)
            val updatedEnergy = (pet.energy + reward.energyRestore).coerceIn(0f, 100f)
            
            var newStatus = pet.currentStatus
            if (newStatus != "SLEEPING" && newStatus != "STUDYING") {
                newStatus = StatusCalculator.determineStatus(updatedHealth, pet.hunger, updatedEnergy, false, false)
            }
            
            val updatedPet = pet.copy(
                xp = updatedXp,
                level = updatedLevel,
                bytes = updatedBytes,
                health = updatedHealth,
                energy = updatedEnergy,
                currentStatus = newStatus,
                lastUpdated = System.currentTimeMillis()
            )
            repository.savePetState(updatedPet)
            
            userPreferences.setDailyRewardClaim(targetDay, System.currentTimeMillis())
            isDailyRewardClaimedToday.value = true
            showDailyRewardDialog.value = false
            
            soundManager.playSuccess()
            triggerCelebration()
        }
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
        viewModelScope.launch { userPreferences.defaultThemeMode.collect { defaultThemeMode.value = it } }

        viewModelScope.launch {
            userPreferences.unlockedThemes.collect { themes ->
                val allThemes = ThemeRegistry.allThemes.map { it.name }.toSet()
                if (themes != allThemes) {
                    userPreferences.setUnlockedThemes(allThemes)
                    unlockedThemes.value = allThemes
                } else {
                    unlockedThemes.value = themes
                }
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
                val decayed = DecayCalculator.applyDecay(current)
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
                        val updatedLevel = LevelCalculator.calculateLevel(updatedXp)
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
        checkDailyRewardEligibility()
        checkDeathState()
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
            
            val updated = current.copy(
                name = newName,
                hasRenamed = true
            )
            repository.savePetState(updated)
            soundManager.playSuccess()
            triggerCelebration()
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
            triggerCelebration()
            triggerLearningEvent("SUCCESS")
            viewModelScope.launch {
                val current = petState.value ?: return@launch

                val reward = RewardCalculator.calculateChallengeReward(challenge.type)
                val updatedXp = current.xp + reward.xp
                val updatedLevel = LevelCalculator.calculateLevel(updatedXp)
                val newHealth = (current.health + reward.healthRestore).coerceIn(0f, 100f)
                val newHunger = (current.hunger + reward.hungerRestore).coerceIn(0f, 100f)

                val updated = current.copy(
                    xp = updatedXp,
                    level = updatedLevel,
                    bytes = current.bytes + reward.bytes,
                    hunger = newHunger,
                    health = newHealth,
                    currentStatus = StatusCalculator.determineStatus(
                        health = newHealth,
                        hunger = newHunger,
                        energy = current.energy,
                        isSleeping = current.currentStatus == "SLEEPING",
                        isStudying = current.currentStatus == "STUDYING"
                    )
                )
                repository.savePetState(updated)
                
                if (updated.level > current.level) {
                    achievementsRepository.unlockAchievement("nivel_experto")
                }
                if (updated.bytes >= 1000) {
                    achievementsRepository.unlockAchievement("ahorrador")
                }
                userPreferences.addDailyActivity("challenge:${challenge.language}")
            }
        } else {
            soundManager.playError()
            _challengeFeedback.value = "INCORRECT"
            triggerLearningEvent("FAILURE")
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
                newStatus = StatusCalculator.determineStatus(updatedHealth, updatedHunger, updatedEnergy, false, false)
            }

            val updated = current.copy(
                bytes = current.bytes - cost,
                hunger = updatedHunger,
                health = updatedHealth,
                energy = updatedEnergy,
                currentStatus = newStatus
            )
            repository.savePetState(updated)
            userPreferences.addDailyActivity("shop:${itemName}")
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
                StatusCalculator.determineStatus(current.health, current.hunger, current.energy, false, false)
            } else {
                "SLEEPING"
            }

            if (!isCurrentlySleeping) {
                achievementsRepository.unlockAchievement("duermevela")
                userPreferences.addDailyActivity("sleep")
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
                
                triggerCelebration()
                repository.savePetState(updated)
                
                repository.updateFocusSessionStatus(_activeFocusEntity.value!!.id, "COMPLETED")
                
                _activeFocusEntity.value = null
                
                achievementsRepository.unlockAchievement("primer_build")
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
            val newStatus = StatusCalculator.determineStatus(current.health, current.hunger, current.energy, false, false)
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

        val reward = RewardCalculator.calculateStudyReward(
            minutes = minutes, currentXp = current.xp,
            currentLevel = current.level, currentStreak = current.streak,
            lastStudyDate = current.lastStudyDate
        )

        val now = System.currentTimeMillis()
        val updatedEnergy = (current.energy - reward.energyCost).coerceIn(0f, 100f)
        val newStatus = StatusCalculator.determineStatus(current.health, current.hunger, updatedEnergy, false, false)

        val updated = current.copy(
            xp = current.xp + reward.xp,
            level = reward.newLevel,
            bytes = current.bytes + reward.bytes,
            energy = updatedEnergy,
            streak = reward.streak,
            lastStudyDate = now,
            lastUpdated = now,
            currentStatus = newStatus
        )
        repository.savePetState(updated)
        if (reward.streak >= 7) achievementsRepository.unlockAchievement("racha_7")
        if (reward.streak >= 30) achievementsRepository.unlockAchievement("racha_30")
        userPreferences.addDailyActivity("study:${topic}")
    }

    fun petThePet() {
        viewModelScope.launch {
            val current = petState.value ?: return@launch
            soundManager.playClick()
            triggerCelebration()
            val updatedEnergy = (current.energy + 15f).coerceIn(0f, 100f)
            val updatedHealth = (current.health + 5f).coerceIn(0f, 100f)
            val newStatus = if (current.currentStatus != "SLEEPING" && current.currentStatus != "STUDYING") {
                StatusCalculator.determineStatus(updatedHealth, current.hunger, updatedEnergy, false, false)
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
            achievementsRepository.unlockAchievement("primer_acaricie")
            userPreferences.addDailyActivity("pet")
        }
    }

    fun cleanThePet() {
        viewModelScope.launch {
            val current = petState.value ?: return@launch
            soundManager.playClick()
            triggerCelebration()
            val updatedHealth = (current.health + 12f).coerceIn(0f, 100f)
            val updatedEnergy = (current.energy + 8f).coerceIn(0f, 100f)
            val newStatus = if (current.currentStatus != "SLEEPING" && current.currentStatus != "STUDYING") {
                StatusCalculator.determineStatus(updatedHealth, current.hunger, updatedEnergy, false, false)
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
            userPreferences.addDailyActivity("clean")
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

    private val _celebrationTrigger = MutableSharedFlow<Unit>(replay = 0)
    val celebrationTrigger: SharedFlow<Unit> = _celebrationTrigger.asSharedFlow()

    private val _learningEventTrigger = MutableSharedFlow<String>(replay = 0)
    val learningEventTrigger: SharedFlow<String> = _learningEventTrigger.asSharedFlow()

    fun triggerLearningEvent(type: String) {
        viewModelScope.launch {
            _learningEventTrigger.emit(type)
        }
    }

    fun triggerCelebration() {
        viewModelScope.launch {
            _celebrationTrigger.emit(Unit)
        }
    }

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
            triggerCelebration()
            
            if (bytesEarned == 100) { // Bug Hunt score 10
                achievementsRepository.unlockAchievement("cazador_de_bugs")
            } else if (bytesEarned == 45) { // Git Rescue score 3
                achievementsRepository.unlockAchievement("git_sin_panico")
            }
            userPreferences.addDailyActivity("game:${bytesEarned}")
        }
    }

    var showDeathDialog = mutableStateOf(false)
        private set
    var deathReviveCost = mutableStateOf(500)
        private set

    var showCommitDialog = mutableStateOf(false)
        private set
    var pendingCommitMessage = mutableStateOf("")
        private set

    var showCodeCardDialog = mutableStateOf(false)
        private set
    var currentCodeCard = mutableStateOf<CodeCard?>(null)
        private set

    fun dismissDeathDialog() {
        showDeathDialog.value = false
    }

    fun dismissCommitDialog() {
        showCommitDialog.value = false
        viewModelScope.launch { userPreferences.clearPendingCommit() }
    }

    fun checkPendingCommit() {
        viewModelScope.launch {
            val commit = userPreferences.getPendingCommit()
            if (commit != null) {
                pendingCommitMessage.value = commit
                showCommitDialog.value = true
            }
        }
    }

    fun dismissCodeCardDialog() {
        showCodeCardDialog.value = false
        currentCodeCard.value = null
    }

    fun showRandomCodeCard() {
        viewModelScope.launch {
            val shownIds = userPreferences.getShownCards().mapNotNull { it.toIntOrNull() }.toSet()
            val unseen = codeCards.filter { it.id !in shownIds }
            val pool = if (unseen.isEmpty()) {
                userPreferences.resetShownCards()
                codeCards
            } else {
                unseen
            }
            val card = pool.random()
            userPreferences.markCardShown(card.id)
            currentCodeCard.value = card
            showCodeCardDialog.value = true
        }
    }

    fun checkDeathState() {
        viewModelScope.launch {
            val pet = repository.petState.firstOrNull() ?: return@launch
            if (pet.isDead) {
                showDeathDialog.value = true
            }
        }
    }

    fun reviveWithBytes() {
        viewModelScope.launch {
            val pet = repository.petState.firstOrNull() ?: return@launch
            if (!pet.isDead) return@launch
            val cost = deathReviveCost.value
            if (pet.bytes < cost) return@launch

            val revived = pet.copy(
                isDead = false,
                health = 50f,
                energy = 50f,
                hunger = 50f,
                currentStatus = "HAPPY",
                bytes = pet.bytes - cost,
                lastUpdated = System.currentTimeMillis()
            )
            repository.savePetState(revived)
            showDeathDialog.value = false
            soundManager.playLevelUp()
            triggerCelebration()
        }
    }

    fun reviveWithAd() {
        viewModelScope.launch {
            val pet = repository.petState.firstOrNull() ?: return@launch
            if (!pet.isDead) return@launch

            val revived = pet.copy(
                isDead = false,
                health = 40f,
                energy = 40f,
                hunger = 40f,
                currentStatus = "HAPPY",
                lastUpdated = System.currentTimeMillis()
            )
            repository.savePetState(revived)
            showDeathDialog.value = false
            soundManager.playLevelUp()
            triggerCelebration()
        }
    }
}

data class DailyReward(
    val day: Int,
    val bytes: Int,
    val xp: Int,
    val healthRestore: Float = 0f,
    val energyRestore: Float = 0f,
    val title: String
)

val dailyRewardsList = listOf(
    DailyReward(1, 50, 15, title = "Hola, Mundo!"),
    DailyReward(2, 100, 25, title = "Variables Inicializadas"),
    DailyReward(3, 150, 35, energyRestore = 15f, title = "Café Double Shot"),
    DailyReward(4, 200, 45, title = "Bucle Optimizado"),
    DailyReward(5, 250, 55, healthRestore = 15f, title = "Bug Solucionado"),
    DailyReward(6, 350, 70, title = "Compilación Limpia"),
    DailyReward(7, 500, 100, healthRestore = 25f, energyRestore = 25f, title = "Despliegue Exitoso (PROD)")
)
