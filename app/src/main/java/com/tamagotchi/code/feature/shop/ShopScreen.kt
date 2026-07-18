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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
            text = ">>> TIENDA",
            fontSize = 14.sp,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(12.dp))

        ShopPanel(viewModel = viewModel, state = state)
        Spacer(modifier = Modifier.height(16.dp))
        SkinShopPanel(viewModel = viewModel, state = state)
        Spacer(modifier = Modifier.height(16.dp))
        PetEditorPanel(viewModel = viewModel, state = state)
    }
}

@Composable
fun ShopPanel(
    viewModel: PetViewModel,
    state: PetStateEntity
) {
    val appTheme = LocalAppTheme.current
    
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
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(12.dp))

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            shopItems.forEach { item ->
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
                            contentDescription = item.name,
                            tint = if (canAfford) appTheme.accent else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = item.name,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.bodyMedium,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = item.effect,
                                style = MaterialTheme.typography.bodySmall,
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
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
                                containerColor = appTheme.accent,
                                disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                            ),
                            shape = RoundedCornerShape(appTheme.cornerRadius.coerceAtMost(6.dp)),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                            modifier = Modifier
                                .height(32.dp)
                                .testTag("shop_buy_${item.name.lowercase().replace(" ", "_")}")
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

@Composable
fun SkinShopPanel(
    viewModel: PetViewModel,
    state: PetStateEntity
) {
    val appTheme = LocalAppTheme.current
    val ownedItems by viewModel.ownedItems.collectAsStateWithLifecycle()
    val equippedSkin by viewModel.equippedSkin.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = ">>> SKINS (MODO RETRO)",
            fontSize = 13.sp,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Activa la opción nano nativa (Pixel Art Canvas).",
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        data class ShopSkinEntry(val id: String, val name: String, val cost: Int, val icon: ImageVector, val description: String)

        val skins = listOf(
            ShopSkinEntry("skin_alien", "Alien Verde (8-bits)", 300, Icons.Default.SmartToy, "Pixel Art clásico"),
            ShopSkinEntry("skin_robot", "Robot Monocromo", 400, Icons.Default.Computer, "Escala de grises")
        )

        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            
            if (equippedSkin != null) {
                Button(
                    onClick = { viewModel.equipSkin(null) },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                    shape = RoundedCornerShape(6.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                    modifier = Modifier.fillMaxWidth().height(36.dp)
                ) {
                    Text(
                        text = "DESACTIVAR MODO RETRO",
                        color = MaterialTheme.colorScheme.onError,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
            }

            skins.forEach { skin ->
                val owns = ownedItems.any { it.itemId == skin.id }
                val isEquipped = equippedSkin == skin.id

                Surface(
                    shape = RoundedCornerShape(appTheme.cornerRadius.coerceAtMost(8.dp)),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    border = BorderStroke(
                        appTheme.borderWidth.coerceAtMost(1.dp),
                        if (isEquipped) appTheme.accent else MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = skin.icon,
                            contentDescription = skin.name,
                            tint = if (owns) appTheme.accent else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = skin.name,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = skin.description,
                                fontSize = 9.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        if (!owns) {
                            Button(
                                onClick = { viewModel.buySkin(skin.id, skin.cost, skin.name) },
                                enabled = state.bytes >= skin.cost,
                                colors = ButtonDefaults.buttonColors(containerColor = appTheme.accent),
                                shape = RoundedCornerShape(6.dp),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                modifier = Modifier.height(30.dp)
                            ) {
                                Text(
                                    text = "${skin.cost} B",
                                    color = appTheme.onPrimary,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp
                                )
                            }
                        } else if (isEquipped) {
                            Text(
                                text = "ACTIVO",
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp,
                                color = appTheme.accent,
                                modifier = Modifier.padding(horizontal = 8.dp)
                            )
                        } else {
                            Button(
                                onClick = { viewModel.equipSkin(skin.id) },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
                                shape = RoundedCornerShape(6.dp),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                modifier = Modifier.height(30.dp)
                            ) {
                                Text(
                                    text = "ACTIVAR",
                                    color = Color.Black,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PetEditorPanel(
    viewModel: PetViewModel,
    state: PetStateEntity
) {
    val appTheme = LocalAppTheme.current
    val accentColor by viewModel.petAccentColor.collectAsStateWithLifecycle()
    var sliderRed by remember { mutableFloatStateOf(accentColor.red) }
    var sliderGreen by remember { mutableFloatStateOf(accentColor.green) }
    var sliderBlue by remember { mutableFloatStateOf(accentColor.blue) }
    var previewColor by remember { mutableStateOf(accentColor) }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = ">>> EDITOR DE CODEY",
            fontSize = 13.sp,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Cambia el color de acento de tu mascota (30 B por cambio).",
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(12.dp))

        Surface(
            shape = RoundedCornerShape(appTheme.cornerRadius.coerceAtMost(8.dp)),
            color = MaterialTheme.colorScheme.surfaceVariant,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("R", fontSize = 11.sp, color = Color(0xFFEF5350), fontWeight = FontWeight.Bold)
                Slider(
                    value = sliderRed,
                    onValueChange = {
                        sliderRed = it
                        previewColor = Color(it, sliderGreen, sliderBlue)
                    },
                    colors = SliderDefaults.colors(thumbColor = Color(0xFFEF5350), activeTrackColor = Color(0xFFEF5350)),
                    modifier = Modifier.fillMaxWidth()
                )
                Text("G", fontSize = 11.sp, color = Color(0xFF4CAF50), fontWeight = FontWeight.Bold)
                Slider(
                    value = sliderGreen,
                    onValueChange = {
                        sliderGreen = it
                        previewColor = Color(sliderRed, it, sliderBlue)
                    },
                    colors = SliderDefaults.colors(thumbColor = Color(0xFF4CAF50), activeTrackColor = Color(0xFF4CAF50)),
                    modifier = Modifier.fillMaxWidth()
                )
                Text("B", fontSize = 11.sp, color = Color(0xFF42A5F5), fontWeight = FontWeight.Bold)
                Slider(
                    value = sliderBlue,
                    onValueChange = {
                        sliderBlue = it
                        previewColor = Color(sliderRed, sliderGreen, it)
                    },
                    colors = SliderDefaults.colors(thumbColor = Color(0xFF42A5F5), activeTrackColor = Color(0xFF42A5F5)),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(4.dp))

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = previewColor,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                ) {}

                Spacer(modifier = Modifier.height(4.dp))

                Button(
                    onClick = { viewModel.setPetAccentColor(previewColor) },
                    enabled = state.bytes >= 30 && previewColor != accentColor,
                    colors = ButtonDefaults.buttonColors(containerColor = appTheme.accent),
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier.fillMaxWidth().height(36.dp)
                ) {
                    Text(
                        text = "APLICAR COLOR (30 B)",
                        color = appTheme.onPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Paletas rápidas:",
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Bold
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    listOf(
                        Color(0xFF81C784) to "Verde",
                        Color(0xFF64B5F6) to "Azul",
                        Color(0xFFFFB74D) to "Naranja",
                        Color(0xFFE57373) to "Rojo",
                        Color(0xFFCE93D8) to "Púrpura"
                    ).forEach { (color, name) ->
                        Surface(
                            onClick = {
                                sliderRed = color.red
                                sliderGreen = color.green
                                sliderBlue = color.blue
                                previewColor = color
                                viewModel.setPetAccentColor(color)
                            },
                            shape = RoundedCornerShape(6.dp),
                            color = color,
                            modifier = Modifier
                                .size(36.dp)
                                .testTag("palette_$name")
                        ) {}
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
