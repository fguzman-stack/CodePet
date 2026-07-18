package com.tamagotchi.code.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tamagotchi.code.data.ChallengesData
import com.tamagotchi.code.data.CodeCard
import com.tamagotchi.code.data.CodingChallenge
import com.tamagotchi.code.data.QuestType
import com.tamagotchi.code.data.codeCards
import com.tamagotchi.code.data.getTargetForQuest
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
import android.content.Context
import android.app.NotificationManager

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

    val ownedItems = MutableStateFlow<List<com.tamagotchi.code.data.database.OwnedItemEntity>>(emptyList())

    val petAccentColor = MutableStateFlow(androidx.compose.ui.graphics.Color(0xFF81C784))

    val moodletState = MutableStateFlow<MoodletState?>(null)
    val pairBuddyState = MutableStateFlow<PairBuddyState?>(null)
    val seasonPassState = MutableStateFlow<SeasonPassData?>(null)
    val weeklyMissionsState = MutableStateFlow<List<WeeklyMissionData>>(emptyList())
    val hackathonState = MutableStateFlow<HackathonData?>(null)
    val skillTreeState = MutableStateFlow<List<SkillNodeData>>(emptyList())
    val githubStats = MutableStateFlow<GitHubStatsData?>(null)
    val isDndActive = MutableStateFlow(false)
    val githubSyncLoading = MutableStateFlow(false)

    val languageProgressList = MutableStateFlow<List<com.tamagotchi.code.data.database.LanguageProgressEntity>>(emptyList())

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
        loadOwnedItems()
        loadPetAccentColor()
        loadMoodlet()
        loadSkillTree()
        loadSeasonPass()
        loadWeeklyMissions()
        loadHackathon()
        loadLanguageProgress()
        checkAndTriggerMoodlet()
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

                val pairBuddyBonus = if (pairBuddyState.value != null) 1.5f else 1.0f
                val reward = RewardCalculator.calculateChallengeReward(challenge.type)
                val adjustedXp = (reward.xp * pairBuddyBonus).toInt()
                val updatedXp = current.xp + adjustedXp
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
                addSeasonPassXp(reward.xp / 2)

                if (pairBuddyState.value != null) {
                    consumePairBuddyChallenge()
                }
                
                if (updated.level > current.level) {
                    achievementsRepository.unlockAchievement("nivel_experto")
                }
                if (updated.bytes >= 1000) {
                    achievementsRepository.unlockAchievement("ahorrador")
                }
                userPreferences.addDailyActivity("challenge:${challenge.language}")
                checkAndActivatePairBuddy()
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

            val discountTier = skillTreeState.value.find { it.id == "shop_discount" }?.currentTier ?: 0
            val discount = when (discountTier) { 1 -> 0.05f; 2 -> 0.10f; 3 -> 0.15f; else -> 0f }
            val finalCost = (cost * (1f - discount)).toInt().coerceAtLeast(1)

            if (current.bytes < finalCost) {
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
                bytes = current.bytes - finalCost,
                hunger = updatedHunger,
                health = updatedHealth,
                energy = updatedEnergy,
                currentStatus = newStatus
            )
            repository.savePetState(updated)
            addSeasonPassXp(5)
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

        val dndMultiplier = if (isDndActive.value) 1.5f else 1.0f
        val adjustedXp = (reward.xp * dndMultiplier).toInt()

        val now = System.currentTimeMillis()
        val updatedEnergy = (current.energy - reward.energyCost).coerceIn(0f, 100f)
        val newStatus = StatusCalculator.determineStatus(current.health, current.hunger, updatedEnergy, false, false)

        val updated = current.copy(
            xp = current.xp + adjustedXp,
            level = com.tamagotchi.code.util.LevelCalculator.calculateLevel(current.xp + adjustedXp),
            bytes = current.bytes + reward.bytes,
            energy = updatedEnergy,
            streak = reward.streak,
            lastStudyDate = now,
            lastUpdated = now,
            currentStatus = newStatus
        )
        repository.savePetState(updated)
        addSeasonPassXp(adjustedXp / 2)
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
            val bonusTier = skillTreeState.value.find { it.id == "minigame_bonus" }?.currentTier ?: 0
            val bonusMultiplier = when (bonusTier) { 1 -> 1.1f; 2 -> 1.2f; 3 -> 1.3f; else -> 1.0f }
            val adjustedBytes = (bytesEarned * bonusMultiplier).toInt()

            val pairBuddyBonus = if (pairBuddyState.value != null) 1.5f else 1.0f
            val finalBytes = (adjustedBytes * pairBuddyBonus).toInt()

            val updated = pet.copy(
                bytes = pet.bytes + finalBytes,
                health = (pet.health + healthEarned).coerceIn(0f, 100f),
                energy = (pet.energy + energyCost).coerceIn(0f, 100f)
            )
            repository.savePetState(updated)
            addSeasonPassXp(finalBytes / 4)
            soundManager.playSuccess()
            triggerCelebration()

            if (pairBuddyState.value != null) {
                consumePairBuddyChallenge()
            }
            
            if (bytesEarned == 100) {
                achievementsRepository.unlockAchievement("cazador_de_bugs")
            } else if (bytesEarned == 45) {
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

    var activeQuestType = mutableStateOf<QuestType?>(null)
        private set
    var activeQuestProgress = mutableStateOf(0)
        private set
    var activeQuestTarget = mutableStateOf(0)
        private set
    var showQuestCompletedDialog = mutableStateOf(false)
        private set
    var questCompletedMessage = mutableStateOf("")
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

    fun loadActiveQuest() {
        viewModelScope.launch {
            val typeName = userPreferences.getActiveQuestType() ?: return@launch
            val type = try { QuestType.valueOf(typeName) } catch (_: Exception) { return@launch }
            val expires = userPreferences.getActiveQuestExpires()
            if (expires > 0L && System.currentTimeMillis() > expires) {
                userPreferences.clearActiveQuest()
                activeQuestType.value = null
                activeQuestProgress.value = 0
                activeQuestTarget.value = 0
                return@launch
            }
            activeQuestType.value = type
            activeQuestProgress.value = userPreferences.getActiveQuestProgress()
            activeQuestTarget.value = getTargetForQuest(type)
        }
    }

    fun assignNewQuest() {
        viewModelScope.launch {
            val allQuests = QuestType.entries
            val chosen = allQuests.random()
            val target = getTargetForQuest(chosen)
            val expiresAt = System.currentTimeMillis() + 4 * 60 * 60 * 1000L
            userPreferences.setActiveQuest(chosen.name, 0, expiresAt, System.currentTimeMillis())
            activeQuestType.value = chosen
            activeQuestProgress.value = 0
            activeQuestTarget.value = target
        }
    }

    private suspend fun checkQuestProgress(type: QuestType) {
        val currentType = activeQuestType.value ?: return
        if (currentType != type) return
        userPreferences.incrementQuestProgress()
        val progress = userPreferences.getActiveQuestProgress()
        activeQuestProgress.value = progress
        if (progress >= activeQuestTarget.value) {
            val rewardXp = currentType.rewardXp
            val rewardBytes = currentType.rewardBytes
            val pet = repository.petState.firstOrNull() ?: return
            val updatedLevel = LevelCalculator.calculateLevel(pet.xp + rewardXp)
            val updated = pet.copy(
                xp = pet.xp + rewardXp,
                level = updatedLevel,
                bytes = pet.bytes + rewardBytes,
                lastUpdated = System.currentTimeMillis()
            )
            repository.savePetState(updated)
            questCompletedMessage.value = "Mision completada!\n\n${currentType.displayName}\n\n+$rewardXp XP\n+$rewardBytes Bytes"
            showQuestCompletedDialog.value = true
            soundManager.playLevelUp()
            triggerCelebration()
            userPreferences.clearActiveQuest()
            activeQuestType.value = null
            activeQuestProgress.value = 0
            activeQuestTarget.value = 0
        }
    }

    fun dismissQuestCompletedDialog() {
        showQuestCompletedDialog.value = false
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

    // === SOMBREROS (Shop) ===
    fun loadOwnedItems() {
        viewModelScope.launch {
            repository.petDao.getOwnedItems().collect { items ->
                ownedItems.value = items
            }
        }
    }

    fun buyHat(hatId: String, cost: Int, hatName: String) {
        viewModelScope.launch {
            val pet = petState.value ?: return@launch
            if (pet.bytes < cost) return@launch
            
            // Auto-equip the new hat immediately
            val updated = pet.copy(
                bytes = pet.bytes - cost,
                equippedHat = hatId
            )
            repository.savePetState(updated)
            
            repository.petDao.insertOwnedItem(
                com.tamagotchi.code.data.database.OwnedItemEntity(
                    itemId = hatId,
                    type = "HAT",
                    isEquipped = true
                )
            )
            repository.petDao.unequipAllOfType("HAT") // Unequip everything else
            repository.petDao.equipItem(hatId) // Equip the new one in DB
            
            soundManager.playBuy()
            userPreferences.addDailyActivity("shop:hat:$hatName")
        }
    }

    fun equipHat(hatId: String) {
        viewModelScope.launch {
            val pet = petState.value ?: return@launch
            repository.petDao.unequipAllOfType("HAT")
            repository.petDao.equipItem(hatId)
            repository.savePetState(pet.copy(equippedHat = hatId))
            soundManager.playClick()
        }
    }

    // === EDITOR DE MASCOTA ===
    fun setPetAccentColor(color: androidx.compose.ui.graphics.Color) {
        viewModelScope.launch {
            val pet = petState.value ?: return@launch
            if (pet.bytes < 30) return@launch
            repository.savePetState(pet.copy(bytes = pet.bytes - 30))
            userPreferences.setPetAccentColor(color.hashCode())
            petAccentColor.value = color
            soundManager.playBuy()
        }
    }

    fun loadPetAccentColor() {
        viewModelScope.launch {
            val colorInt = userPreferences.getPetAccentColor()
            if (colorInt != 0) {
                petAccentColor.value = androidx.compose.ui.graphics.Color(colorInt)
            }
        }
    }

    // === MOODLET SYSTEM ===
    fun loadMoodlet() {
        viewModelScope.launch {
            val data = userPreferences.getMoodletData()
            if (data != null && data.expiry > System.currentTimeMillis()) {
                moodletState.value = MoodletState(data.moodletType, data.expiry)
            } else if (data != null) {
                userPreferences.clearMoodlet()
                moodletState.value = null
            }
        }
    }

    fun triggerRandomMoodlet() {
        viewModelScope.launch {
            val events = listOf(
                "bug_prod" to "Encontró un bug en producción 😱",
                "tabs" to "Te vio usar tabs 😊",
                "spaces" to "Te vio usar espacios 🤔",
                "bad_commit" to "Commit sin descripción 😞",
                "clean_code" to "Código limpio detectado 🎉",
                "spilled_coffee" to "Café derramado 😰"
            )
            val (type, message) = events.random()
            val duration = when (type) {
                "bug_prod" -> 2L
                "spilled_coffee" -> 1L
                "bad_commit" -> 2L
                "clean_code" -> 3L
                else -> 1L
            }
            val expiry = System.currentTimeMillis() + duration * 60 * 60 * 1000
            userPreferences.saveMoodlet(type, expiry)
            moodletState.value = MoodletState(message, expiry)
        }
    }

    // === PAIR BUDDY (Buggy) ===
    fun activatePairBuddy() {
        if (pairBuddyState.value != null) return
        pairBuddyState.value = PairBuddyState(
            name = "Buggy",
            remainingChallenges = 3,
            xpMultiplier = 1.5f
        )
    }

    fun consumePairBuddyChallenge() {
        val current = pairBuddyState.value ?: return
        if (current.remainingChallenges <= 1) {
            pairBuddyState.value = null
        } else {
            pairBuddyState.value = current.copy(remainingChallenges = current.remainingChallenges - 1)
        }
    }

    // === SKILL TREE ===
    fun loadSkillTree() {
        viewModelScope.launch {
            val skills = userPreferences.getSkillTreeData()
            skillTreeState.value = skills.ifEmpty {
                SkillTreeData.DEFAULT_SKILLS
            }
        }
    }

    fun unlockSkillNode(skillId: String) {
        viewModelScope.launch {
            val current = skillTreeState.value.toMutableList()
            val idx = current.indexOfFirst { it.id == skillId }
            if (idx == -1) return@launch
            val node = current[idx]
            if (node.currentTier >= node.maxTier) return@launch

            val pet = petState.value ?: return@launch
            val cost = (node.currentTier + 1) * 100
            if (pet.xp < cost) return@launch

            val updated = pet.copy(xp = pet.xp - cost)
            repository.savePetState(updated)

            current[idx] = node.copy(currentTier = node.currentTier + 1)
            skillTreeState.value = current
            userPreferences.saveSkillTreeData(current)
            soundManager.playLevelUp()
        }
    }

    // === SEASON PASS ===
    fun loadSeasonPass() {
        viewModelScope.launch {
            val data = userPreferences.getSeasonPassData()
            seasonPassState.value = data ?: SeasonPassData()
        }
    }

    fun addSeasonPassXp(xp: Int) {
        viewModelScope.launch {
            val current = seasonPassState.value ?: SeasonPassData()
            val newXp = current.xp + xp
            val newLevel = current.level + (newXp / 100)
            val remainingXp = newXp % 100
            val updated = current.copy(
                xp = remainingXp,
                level = newLevel.coerceAtMost(20)
            )
            seasonPassState.value = updated
            userPreferences.saveSeasonPassData(updated)
            if (newLevel > current.level) {
                triggerCelebration()
            }
        }
    }

    // === WEEKLY MISSIONS ===
    fun loadWeeklyMissions() {
        viewModelScope.launch {
            val missions = userPreferences.getWeeklyMissions()
            weeklyMissionsState.value = missions.ifEmpty {
                WeeklyMissionData.generateWeeklyMissions()
            }
        }
    }

    fun completeWeeklyMission(missionId: String) {
        viewModelScope.launch {
            val current = weeklyMissionsState.value.toMutableList()
            val idx = current.indexOfFirst { it.id == missionId }
            if (idx == -1 || current[idx].completed) return@launch
            current[idx] = current[idx].copy(completed = true)
            val mission = current[idx]

            val pet = petState.value ?: return@launch
            val updatedLevel = com.tamagotchi.code.util.LevelCalculator.calculateLevel(pet.xp + mission.rewardXp)
            repository.savePetState(pet.copy(
                xp = pet.xp + mission.rewardXp,
                level = updatedLevel,
                bytes = pet.bytes + mission.rewardBytes
            ))

            val completedCount = current.count { it.completed }
            if (completedCount == 3) {
                repository.savePetState(pet.copy(
                    bytes = pet.bytes + 500
                ))
            }

            weeklyMissionsState.value = current
            userPreferences.saveWeeklyMissions(current)
            soundManager.playLevelUp()
            triggerCelebration()
        }
    }

    // === HACKATHON ===
    fun loadHackathon() {
        viewModelScope.launch {
            val data = userPreferences.getHackathonData()
            hackathonState.value = data
        }
    }

    fun submitHackathonSolution(attempts: Int, timeMs: Long) {
        viewModelScope.launch {
            val current = hackathonState.value ?: return@launch
            if (current.attempts >= 3) return@launch

            val updated = current.copy(
                attempts = current.attempts + 1,
                bestTimeMs = minOf(current.bestTimeMs, timeMs)
            )
            hackathonState.value = updated
            userPreferences.saveHackathonData(updated)

            val pet = petState.value ?: return@launch
            val newLevel = com.tamagotchi.code.util.LevelCalculator.calculateLevel(pet.xp + 200)
            repository.savePetState(pet.copy(
                xp = pet.xp + 200,
                level = newLevel,
                bytes = pet.bytes + 500
            ))
            soundManager.playLevelUp()
            triggerCelebration()
        }
    }

    // === GITHUB STATS ===
    fun fetchGitHubStats(token: String) {
        githubSyncLoading.value = true
        viewModelScope.launch {
            try {
                val url = java.net.URL("https://api.github.com/user")
                val conn = url.openConnection() as java.net.HttpURLConnection
                conn.setRequestProperty("Authorization", "token $token")
                conn.setRequestProperty("Accept", "application/vnd.github.v3+json")
                conn.connectTimeout = 5000
                conn.readTimeout = 5000

                if (conn.responseCode == 200) {
                    val body = conn.inputStream.bufferedReader().readText()
                    val json = org.json.JSONObject(body)
                    val login = json.optString("login", "unknown")
                    val publicRepos = json.optInt("public_repos", 0)

                    val eventsUrl = java.net.URL("https://api.github.com/users/$login/events?per_page=30")
                    val eventsConn = eventsUrl.openConnection() as java.net.HttpURLConnection
                    eventsConn.setRequestProperty("Authorization", "token $token")
                    eventsConn.setRequestProperty("Accept", "application/vnd.github.v3+json")
                    eventsConn.connectTimeout = 5000
                    eventsConn.readTimeout = 5000

                    var commits = 0
                    var prs = 0
                    var issues = 0
                    if (eventsConn.responseCode == 200) {
                        val eventsBody = eventsConn.inputStream.bufferedReader().readText()
                        val events = org.json.JSONArray(eventsBody)
                        val today = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.US).format(java.util.Date())
                        for (i in 0 until events.length()) {
                            val event = events.getJSONObject(i)
                            val createdAt = event.optString("created_at", "")
                            if (createdAt.startsWith(today)) {
                                when (event.optString("type", "")) {
                                    "PushEvent" -> {
                                        val size = event.optJSONObject("payload")?.optInt("size", 1) ?: 1
                                        commits += size
                                    }
                                    "PullRequestEvent" -> prs++
                                    "IssuesEvent" -> issues++
                                }
                            }
                        }
                    }

                    val xpBonus = commits * 5 + prs * 10 + issues * 3
                    githubStats.value = GitHubStatsData(
                        username = login,
                        publicRepos = publicRepos,
                        todayCommits = commits,
                        todayPRs = prs,
                        todayIssues = issues,
                        xpBonus = xpBonus
                    )
                    userPreferences.saveGitHubToken(token)

                    if (xpBonus > 0) {
                        val pet = petState.value ?: return@launch
                        val newLevel = com.tamagotchi.code.util.LevelCalculator.calculateLevel(pet.xp + xpBonus)
                        repository.savePetState(pet.copy(
                            xp = pet.xp + xpBonus,
                            level = newLevel,
                            lastUpdated = System.currentTimeMillis()
                        ))
                        triggerCelebration()
                    }
                    githubSyncLoading.value = false
                }
            } catch (_: Exception) {
                githubStats.value = null
                githubSyncLoading.value = false
            }
        }
    }

    // === DND MODE ===
    fun checkDndMode(context: android.content.Context) {
        viewModelScope.launch {
            try {
                val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                val filter = notificationManager.currentInterruptionFilter
                isDndActive.value = filter == NotificationManager.INTERRUPTION_FILTER_PRIORITY ||
                        filter == NotificationManager.INTERRUPTION_FILTER_NONE ||
                        filter == NotificationManager.INTERRUPTION_FILTER_ALARMS
            } catch (_: Exception) {
                isDndActive.value = false
            }
        }
    }

    // === LANGUAGE BADGES ===
    fun loadLanguageProgress() {
        viewModelScope.launch {
            repository.petDao.getAllLanguageProgress().collect { progress ->
                languageProgressList.value = progress
            }
        }
    }

    fun updateLanguageProgress(languageId: String, completed: Int, total: Int) {
        viewModelScope.launch {
            val badgeLevel = when {
                total > 0 && completed >= total -> 3
                total > 0 && completed >= (total * 0.75f) -> 2
                total > 0 && completed >= (total * 0.5f) -> 1
                else -> 0
            }
            repository.petDao.updateLanguageProgress(
                com.tamagotchi.code.data.database.LanguageProgressEntity(
                    languageId = languageId,
                    challengesCompleted = completed,
                    totalChallenges = total,
                    badgeLevel = badgeLevel
                )
            )
        }
    }

    // === PAIR BUDDY RANDOM ACTIVATION ===
    fun checkAndActivatePairBuddy() {
        if (pairBuddyState.value != null) return
        if (kotlin.random.Random.nextFloat() < 0.2f) {
            activatePairBuddy()
        }
    }

    // === DAILY MOODLET CHECK ===
    fun checkAndTriggerMoodlet() {
        viewModelScope.launch {
            val lastCheck = userPreferences.getLastMoodletCheck()
            val now = System.currentTimeMillis()
            if (now - lastCheck > 30 * 60 * 1000) { // cada 30 min
                if (kotlin.random.Random.nextFloat() < 0.05f) { // 5% probabilidad
                    triggerRandomMoodlet()
                }
                userPreferences.setLastMoodletCheck(now)
            }
        }
    }
}

// === NEW DATA CLASSES ===

data class MoodletState(
    val moodletType: String,
    val expiry: Long
)

data class PairBuddyState(
    val name: String,
    val remainingChallenges: Int,
    val xpMultiplier: Float
)

data class SeasonPassData(
    val level: Int = 0,
    val xp: Int = 0,
    val premium: Boolean = false
)

data class WeeklyMissionData(
    val id: String,
    val title: String,
    val description: String,
    val rewardXp: Int,
    val rewardBytes: Int,
    val completed: Boolean = false
) {
    companion object {
        fun generateWeeklyMissions(): List<WeeklyMissionData> = listOf(
            WeeklyMissionData("wm1", "5 retos de código", "Completa 5 retos en Aprender", 100, 50),
            WeeklyMissionData("wm2", "2h modo foco", "Estudia 2 horas en modo foco", 200, 100),
            WeeklyMissionData("wm3", "Gana 3 Bug Hunt", "Gana 3 partidas de Bug Hunt", 150, 75)
        )
    }
}

data class HackathonData(
    val active: Boolean = true,
    val attempts: Int = 0,
    val bestTimeMs: Long = Long.MAX_VALUE,
    val expiresAt: Long = System.currentTimeMillis() + 48 * 60 * 60 * 1000
)

data class SkillNodeData(
    val id: String,
    val name: String,
    val description: String,
    val maxTier: Int = 3,
    val currentTier: Int = 0,
    val icon: String = "Star"
)

object SkillTreeData {
    val DEFAULT_SKILLS = listOf(
        SkillNodeData("double_xp", "Doble XP domingo", "XP x1.5/2/3 los domingos"),
        SkillNodeData("slow_decay", "Decaimiento lento", "Decaimiento -10%/-20%/-30%"),
        SkillNodeData("shop_discount", "Descuento tienda", "5%/10%/15% descuento"),
        SkillNodeData("minigame_bonus", "Bonus minijuegos", "+10%/+20%/+30% recompensa"),
        SkillNodeData("offline_xp", "XP offline", "1h/2h/4h de XP pasivo"),
        SkillNodeData("extra_heart", "Corazón extra", "Máximo 6 corazones")
    )
}

data class GitHubStatsData(
    val username: String,
    val publicRepos: Int,
    val todayCommits: Int,
    val todayPRs: Int,
    val todayIssues: Int,
    val xpBonus: Int
)

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
