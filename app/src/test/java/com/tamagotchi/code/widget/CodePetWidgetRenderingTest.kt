package com.tamagotchi.code.widget

import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.test.core.app.ApplicationProvider
import com.tamagotchi.code.R
import com.tamagotchi.code.TestApplication
import com.tamagotchi.code.data.database.PetStateEntity
import com.tamagotchi.code.ui.components.renderCodeyBitmap
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(application = TestApplication::class, sdk = [35])
class CodePetWidgetRenderingTest {
    @Test
    fun remoteViewsUseProceduralBitmapForInitialAliveAndDeadStates() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        for (pet in listOf(null, PetStateEntity(level = 25, currentStatus = "STUDYING"),
            PetStateEntity(level = 50, currentStatus = "HAPPY", isDead = true))) {
            val views = CodePetWidgetProvider.createRemoteViews(context, pet)
            val root = views.apply(context, FrameLayout(context))
            val image = root.findViewById<ImageView>(R.id.widget_pet_image)
            val bitmap = (image.drawable as BitmapDrawable).bitmap
            assertTrue(bitmap.sameAs(renderCodeyBitmap(pet?.level ?: 1,
                if (pet?.isDead == true) "DEAD" else pet?.currentStatus ?: "HAPPY",
                pet?.isDead ?: false)))
        }
    }

    @Test
    fun metersExposeHealthEnergyAndHungerWithPercentages() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val pet = PetStateEntity(level = 3, currentStatus = "HAPPY", health = 80f, energy = 55f, hunger = 30f)
        val root = CodePetWidgetProvider.createRemoteViews(context, pet)
            .apply(context, FrameLayout(context))

        assertEquals(80, root.findViewById<ProgressBar>(R.id.health_bar).progress)
        assertEquals(55, root.findViewById<ProgressBar>(R.id.energy_bar).progress)
        assertEquals(30, root.findViewById<ProgressBar>(R.id.hunger_bar).progress)
        assertEquals("80%", (root.findViewById<TextView>(R.id.health_value)).text.toString())
        assertEquals("55%", (root.findViewById<TextView>(R.id.energy_value)).text.toString())
        assertEquals("30%", (root.findViewById<TextView>(R.id.hunger_value)).text.toString())
    }

    @Test
    fun missingPetStateFallsBackToEmptyStatsAndStartPrompt() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val root = CodePetWidgetProvider.createRemoteViews(context, null)
            .apply(context, FrameLayout(context))

        assertEquals(0, root.findViewById<ProgressBar>(R.id.health_bar).progress)
        assertEquals(context.getString(R.string.widget_start_app),
            (root.findViewById<TextView>(R.id.widget_subtitle)).text.toString())
    }
}
