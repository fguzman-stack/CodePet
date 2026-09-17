package com.tamagotchi.code.ui.components

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.tamagotchi.code.R

@StringRes
fun achievementNameRes(id: String): Int? = when (id) {
    "primer_build" -> R.string.ach_primer_build_name
    "nivel_experto" -> R.string.ach_nivel_experto_name
    "cazador_de_bugs" -> R.string.ach_cazador_de_bugs_name
    "git_sin_panico" -> R.string.ach_git_sin_panico_name
    "racha_7" -> R.string.ach_racha_7_name
    "racha_30" -> R.string.ach_racha_30_name
    "coleccionista" -> R.string.ach_coleccionista_name
    "completista" -> R.string.ach_completista_name
    "ahorrador" -> R.string.ach_ahorrador_name
    "primer_acaricie" -> R.string.ach_primer_acaricie_name
    "duermevela" -> R.string.ach_duermevela_name
    "limpiador" -> R.string.ach_limpiador_name
    else -> null
}

@StringRes
fun achievementDescRes(id: String): Int? = when (id) {
    "primer_build" -> R.string.ach_primer_build_desc
    "nivel_experto" -> R.string.ach_nivel_experto_desc
    "cazador_de_bugs" -> R.string.ach_cazador_de_bugs_desc
    "git_sin_panico" -> R.string.ach_git_sin_panico_desc
    "racha_7" -> R.string.ach_racha_7_desc
    "racha_30" -> R.string.ach_racha_30_desc
    "coleccionista" -> R.string.ach_coleccionista_desc
    "completista" -> R.string.ach_completista_desc
    "ahorrador" -> R.string.ach_ahorrador_desc
    "primer_acaricie" -> R.string.ach_primer_acaricie_desc
    "duermevela" -> R.string.ach_duermevela_desc
    "limpiador" -> R.string.ach_limpiador_desc
    else -> null
}

@Composable
fun achievementName(id: String, fallback: String): String {
    val res = achievementNameRes(id) ?: return fallback
    return stringResource(res)
}

@Composable
fun achievementDescription(id: String, fallback: String): String {
    val res = achievementDescRes(id) ?: return fallback
    return stringResource(res)
}

@StringRes
fun skillNameRes(id: String): Int? = when (id) {
    "double_xp" -> R.string.skill_double_xp_name
    "slow_decay" -> R.string.skill_slow_decay_name
    "shop_discount" -> R.string.skill_shop_discount_name
    "minigame_bonus" -> R.string.skill_minigame_bonus_name
    "offline_xp" -> R.string.skill_offline_xp_name
    "extra_heart" -> R.string.skill_extra_heart_name
    else -> null
}

@StringRes
fun skillDescRes(id: String): Int? = when (id) {
    "double_xp" -> R.string.skill_double_xp_desc
    "slow_decay" -> R.string.skill_slow_decay_desc
    "shop_discount" -> R.string.skill_shop_discount_desc
    "minigame_bonus" -> R.string.skill_minigame_bonus_desc
    "offline_xp" -> R.string.skill_offline_xp_desc
    "extra_heart" -> R.string.skill_extra_heart_desc
    else -> null
}

@Composable
fun skillName(id: String, fallback: String): String {
    val res = skillNameRes(id) ?: return fallback
    return stringResource(res)
}

@Composable
fun skillDescription(id: String, fallback: String): String {
    val res = skillDescRes(id) ?: return fallback
    return stringResource(res)
}

@StringRes
fun weeklyMissionTitleRes(id: String): Int? = when (id) {
    "wm1" -> R.string.wm_wm1_title
    "wm2" -> R.string.wm_wm2_title
    "wm3" -> R.string.wm_wm3_title
    else -> null
}

@StringRes
fun weeklyMissionDescRes(id: String): Int? = when (id) {
    "wm1" -> R.string.wm_wm1_desc
    "wm2" -> R.string.wm_wm2_desc
    "wm3" -> R.string.wm_wm3_desc
    else -> null
}

@Composable
fun weeklyMissionTitle(id: String, fallback: String): String {
    val res = weeklyMissionTitleRes(id) ?: return fallback
    return stringResource(res)
}

@Composable
fun weeklyMissionDescription(id: String, fallback: String): String {
    val res = weeklyMissionDescRes(id) ?: return fallback
    return stringResource(res)
}

@StringRes
fun moodletLabelRes(type: String): Int? = when (type) {
    "bug_prod" -> R.string.moodlet_bug_prod
    "tabs" -> R.string.moodlet_tabs
    "spaces" -> R.string.moodlet_spaces
    "bad_commit" -> R.string.moodlet_bad_commit
    "clean_code" -> R.string.moodlet_clean_code
    "spilled_coffee" -> R.string.moodlet_spilled_coffee
    else -> null
}

@Composable
fun moodletLabel(type: String): String {
    val res = moodletLabelRes(type) ?: return type
    return stringResource(res)
}
