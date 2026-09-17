package com.tamagotchi.code

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.app.Activity
import android.widget.LinearLayout
import android.widget.ImageView
import android.widget.TextView
import android.view.Gravity
import android.view.ViewGroup
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import com.tamagotchi.code.ui.theme.ThemeRegistry
import com.tamagotchi.code.ui.theme.toColorScheme

class SplashActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Set background to match the current theme
        val themeName = intent.getStringExtra("theme") ?: "Default"
        val isDark = ThemeRegistry.getTheme(themeName).isDark
        val theme = ThemeRegistry.getTheme(themeName, isDark)
        val colorScheme = theme.toColorScheme()
        
        // Create a custom view for splash screen
        val splashView = android.widget.LinearLayout(this).apply {
            layoutParams = android.view.ViewGroup.LayoutParams(
                android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                android.view.ViewGroup.LayoutParams.MATCH_PARENT
            )
            orientation = android.widget.LinearLayout.VERTICAL
            gravity = android.view.Gravity.CENTER
            
            // Background gradient
            val gradient = android.graphics.drawable.GradientDrawable(
                android.graphics.drawable.GradientDrawable.Orientation.TOP_BOTTOM,
                intArrayOf(
                    android.graphics.Color.parseColor("#5D67FF"),
                    android.graphics.Color.parseColor("#3A44D9")
                )
            )
            background = gradient
            
            // App logo
            val logoImageView = android.widget.ImageView(this@SplashActivity).apply {
                layoutParams = android.view.ViewGroup.LayoutParams(
                    120, 120
                )
                setImageResource(R.drawable.iconoapp)
                scaleType = android.widget.ImageView.ScaleType.FIT_CENTER
                setPadding(0, 0, 0, 40)
            }
            addView(logoImageView)
            
            // App name
            val appNameTextView = android.widget.TextView(this@SplashActivity).apply {
                text = "CodePet"
                textSize = 24f
                setTextColor(android.graphics.Color.WHITE)
                typeface = android.graphics.Typeface.create("sans-serif-bold", android.graphics.Typeface.BOLD)
                layoutParams = android.view.ViewGroup.LayoutParams(
                    android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
                    android.view.ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
            addView(appNameTextView)
            
            // Tagline
            val taglineTextView = android.widget.TextView(this@SplashActivity).apply {
                text = getString(R.string.splash_tagline)
                textSize = 14f
                setTextColor(android.graphics.Color.parseColor("#E6E9FF"))
                layoutParams = android.view.ViewGroup.LayoutParams(
                    android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
                    android.view.ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
            addView(taglineTextView)
        }
        
        setContentView(splashView)
        
        // Navigate to main activity after 2 seconds
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("theme", themeName)
            startActivity(intent)
            finish()
        }, 2000)
    }
}