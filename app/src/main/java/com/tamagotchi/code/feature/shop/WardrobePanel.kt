package com.tamagotchi.code.feature.shop

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tamagotchi.code.R
import com.tamagotchi.code.data.PetOutfit
import com.tamagotchi.code.data.PetWardrobe
import com.tamagotchi.code.data.database.PetStateEntity
import com.tamagotchi.code.ui.components.CodeySprite
import com.tamagotchi.code.ui.viewmodel.PetViewModel

@Composable
internal fun WardrobePanel(viewModel: PetViewModel, state: PetStateEntity) {
    val owned by viewModel.ownedItems.collectAsStateWithLifecycle()
    val equipped = PetOutfit.from(owned)
    Column(Modifier.fillMaxWidth().padding(bottom = 24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(stringResource(R.string.wardrobe_title), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Text(stringResource(R.string.wardrobe_desc), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Card(shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
            Column(Modifier.fillMaxWidth().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text(stringResource(R.string.wardrobe_equipped_preview), style = MaterialTheme.typography.labelLarge)
                CodeySprite(status = state.currentStatus, level = state.level, outfit = equipped,
                    modifier = Modifier.size(230.dp).testTag("wardrobe_preview"))
            }
        }
        Text(stringResource(R.string.wardrobe_shop_title), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        PetWardrobe.items.filter { it.language == null }.forEach { item ->
                val ownedItem = owned.firstOrNull { it.itemId == item.id }
                val isEquipped = ownedItem?.isEquipped == true
                Surface(shape = RoundedCornerShape(16.dp), color = MaterialTheme.colorScheme.surface,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)) {
                    Column(Modifier.fillMaxWidth().padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Column(Modifier.weight(1f)) {
                                Text(stringResource(item.nameRes), style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                                Text(stringResource(if (item.slot == "HAT") R.string.wardrobe_hat else R.string.wardrobe_clothing), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            if (isEquipped) Text(stringResource(R.string.wardrobe_equipped), color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.labelMedium)
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                            Button(onClick = {
                                if (ownedItem == null) viewModel.buyCosmetic(item.id) else viewModel.equipCosmetic(item.id, !isEquipped)
                            }, enabled = ownedItem != null || state.bytes >= item.cost,
                                modifier = Modifier.fillMaxWidth().testTag("wardrobe_action_${item.id}"), contentPadding = PaddingValues(horizontal = 8.dp)) {
                                Text(when {
                                    isEquipped -> stringResource(R.string.wardrobe_remove)
                                    ownedItem != null -> stringResource(R.string.wardrobe_equip)
                                    else -> stringResource(R.string.wardrobe_buy, item.cost)
                                })
                            }
                        }
                    }
                }
        }
    }
}
