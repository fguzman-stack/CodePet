package com.tamagotchi.code.feature.shop

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tamagotchi.code.data.database.PetStateEntity
import com.tamagotchi.code.ui.viewmodel.PetViewModel

@Composable
fun ShopScreen(
    viewModel: PetViewModel,
    state: PetStateEntity
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = ">>> TIENDA",
            fontSize = 14.sp,
            fontFamily = FontFamily.Monospace,
            color = Color(0xFF81C784),
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
    val shopItems = listOf(
        ShopItemData("Café Negro (CPU Booster)", 10, "Restaura +20 Energía mental", 0f, 0f, 20f, Icons.Default.Coffee),
        ShopItemData("Pizza de Código (Bytes Snack)", 15, "Restaura +35 Alimento", 35f, 0f, 0f, Icons.Default.LocalPizza),
        ShopItemData("Píldora Desbugueadora", 25, "Cura de infecciones y sana +30 Salud", 0f, 30f, 0f, Icons.Default.Medication),
        ShopItemData("Vacuna Super Compiler", 55, "Restaura +75 Salud, +40 Alimento, +40 Energía", 40f, 75f, 40f, Icons.Default.Shield)
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Compra raciones o medicinas con tus Bytes de estudio acumulados.",
            fontSize = 11.sp,
            fontFamily = FontFamily.Monospace,
            color = Color.Gray,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(12.dp))

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            shopItems.forEach { item ->
                val canAfford = state.bytes >= item.cost
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFF151D16),
                    border = BorderStroke(1.dp, Color(0xFF2E7D32).copy(alpha = 0.4f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.name,
                            tint = if (canAfford) Color(0xFFFFD54F) else Color.Gray,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = item.name,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                fontSize = 12.sp,
                                color = Color.White
                            )
                            Text(
                                text = item.effect,
                                fontFamily = FontFamily.Monospace,
                                fontSize = 10.sp,
                                color = Color.LightGray
                            )
                        }

                        Button(
                            onClick = {
                                viewModel.buyShopItem(
                                    itemName = item.name,
                                    cost = item.cost,
                                    hungerRestore = item.hungerRestore,
                                    healthRestore = item.healthRestore,
                                    energyRestore = item.energyRestore
                                )
                            },
                            enabled = canAfford,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFFFD54F),
                                disabledContainerColor = Color.DarkGray
                            ),
                            shape = RoundedCornerShape(6.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                            modifier = Modifier
                                .height(32.dp)
                                .testTag("shop_buy_${item.name.lowercase().replace(" ", "_")}")
                        ) {
                            Text(
                                text = "${item.cost} B",
                                color = Color.Black,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
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
    val name: String,
    val cost: Int,
    val effect: String,
    val hungerRestore: Float,
    val healthRestore: Float,
    val energyRestore: Float,
    val icon: ImageVector
)
