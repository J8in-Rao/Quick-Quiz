package com.quickquiz.app.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.quickquiz.app.R
import com.quickquiz.app.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityMainBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupSystemUI()
        setupClickListeners()
    }
    
    private fun setupSystemUI() {
        // Ensure status bar is always visible with light theme
        window.apply {
            statusBarColor = ContextCompat.getColor(this@MainActivity, R.color.glass_background)
            navigationBarColor = android.graphics.Color.TRANSPARENT
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
        
        // Set dark status bar icons for better visibility on light background
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.isAppearanceLightStatusBars = true
        
        // Ensure status bar is never hidden
        windowInsetsController.show(WindowInsetsCompat.Type.statusBars())
    }
    
    private fun setupClickListeners() {
        binding.btnStartQuiz.setOnClickListener {
            startQuiz()
        }
    }
    
    private fun startQuiz() {
        val intent = Intent(this, QuizActivity::class.java)
        startActivity(intent)
    }
}