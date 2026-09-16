package com.tamagotchi.code.feature.shop

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tamagotchi.code.R
import com.tamagotchi.code.data.database.PetStateEntity
import com.tamagotchi.code.ui.theme.LocalAppTheme
import com.tamagotchi.code.ui.viewmodel.PetViewModel

@Composable
fun ShopScreen(
    viewModel: PetViewModel,
    state: PetStateEntity
) {
    val appTheme = LocalAppTheme.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = stringResource(R.string.shop_header),
            fontSize = 14.sp,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(12.dp))

        ShopPanel(viewModel = viewModel, state = state)
    }
}

@Composable
fun ShopPanel(
    viewModel: PetViewModel,
    state: PetStateEntity
) {
    val appTheme = LocalAppTheme.current
    
    val shopItems = listOf(
        ShopItemData(R.string.shop_item_cafe, R.string.shop_item_cafe_effect, 10, 0f, 0f, 20f, Icons.Default.Coffee, "cafe"),
        ShopItemData(R.string.shop_item_pizza, R.string.shop_item_pizza_effect, 15, 35f, 0f, 0f, Icons.Default.LocalPizza, "pizza"),
        ShopItemData(R.string.shop_item_pill, R.string.shop_item_pill_effect, 25, 0f, 30f, 0f, Icons.Default.Medication, "pill"),
        ShopItemData(R.string.shop_item_vaccine, R.string.shop_item_vaccine_effect, 55, 40f, 75f, 40f, Icons.Default.Shield, "vaccine")
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = stringResource(R.string.shop_desc),
            fontSize = 11.sp,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(12.dp))

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            shopItems.forEach { item ->
                val itemName = stringResource(item.nameRes)
                val canAfford = state.bytes >= item.cost
                Surface(
                    shape = RoundedCornerShape(appTheme.cornerRadius.coerceAtMost(8.dp)),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    border = BorderStroke(appTheme.borderWidth.coerceAtMost(1.dp), MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = itemName,
                            tint = if (canAfford) appTheme.accent else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = itemName,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.bodyMedium,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = stringResource(item.effectRes),
                                style = MaterialTheme.typography.bodySmall,
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Button(
                            onClick = {
                                viewModel.buyShopItem(
                                    itemName = itemName,
                                    cost = item.cost,
                                    hungerRestore = item.hungerRestore,
                                    healthRestore = item.healthRestore,
                                    energyRestore = item.energyRestore
                                )
                            },
                            enabled = canAfford,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = appTheme.accent,
                                disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                            ),
                            shape = RoundedCornerShape(appTheme.cornerRadius.coerceAtMost(6.dp)),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                            modifier = Modifier
                                .height(32.dp)
                                .testTag("shop_buy_${item.id}")
                        ) {
                            Text(
                                text = "${item.cost} B",
                                color = if (canAfford) appTheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.labelSmall,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

data class ShopItemData(
    val nameRes: Int,
    val effectRes: Int,
    val cost: Int,
    val hungerRestore: Float,
    val healthRestore: Float,
    val energyRestore: Float,
    val icon: ImageVector,
    val id: String
)
