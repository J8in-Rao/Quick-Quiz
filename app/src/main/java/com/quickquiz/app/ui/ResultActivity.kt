package com.quickquiz.app.ui

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.quickquiz.app.R
import com.quickquiz.app.databinding.ActivityResultBinding
import com.quickquiz.app.model.QuizResult
import com.quickquiz.app.utils.PreferencesManager

class ResultActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityResultBinding
    private lateinit var quizResult: QuizResult
    private lateinit var preferencesManager: PreferencesManager
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        preferencesManager = PreferencesManager(this)
        
        setupSystemUI()
        getQuizResult()
        saveQuizResults()
        displayResults()
        setupClickListeners()
    }
    
    private fun saveQuizResults() {
        // Update statistics
        preferencesManager.incrementQuizzesCompleted()
        preferencesManager.incrementQuestionsAnswered(quizResult.totalQuestions)
        preferencesManager.incrementCorrectAnswers(quizResult.correctAnswers)
        preferencesManager.setBestScore(quizResult.percentage)
    }
    
    private fun setupSystemUI() {
        // Ensure status bar is always visible with light theme
        window.apply {
            statusBarColor = ContextCompat.getColor(this@ResultActivity, R.color.glass_background)
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
    
    private fun getQuizResult() {
        quizResult = intent.getParcelableExtra("quiz_result") ?: run {
            // Handle error case - no result passed
            finish()
            return
        }
    }
    
    private fun displayResults() {
        with(binding) {
            // Display grade
            tvGrade.text = getGradeWithEmoji(quizResult.getGrade())
            
            // Display score
            tvScore.text = getString(
                R.string.score_format,
                quizResult.correctAnswers,
                quizResult.totalQuestions
            )
            
            // Display percentage
            tvPercentage.text = getString(
                R.string.percentage_format,
                quizResult.percentage
            )
            
            // Set grade color based on performance
            val gradeColor = getGradeColor(quizResult.percentage)
            tvGrade.setTextColor(gradeColor)
            tvPercentage.setTextColor(gradeColor)
        }
    }
    
    private fun getGradeWithEmoji(grade: String): String {
        return when (grade) {
            "Excellent" -> "🎉 $grade!"
            "Very Good" -> "👏 $grade!"
            "Good" -> "👍 $grade!"
            "Average" -> "👌 $grade"
            "Needs Improvement" -> "📚 $grade"
            else -> grade
        }
    }
    
    private fun getGradeColor(percentage: Int): Int {
        return when {
            percentage >= 80 -> getColor(R.color.success_color)
            percentage >= 60 -> getColor(R.color.warning_color)
            else -> getColor(R.color.error_color)
        }
    }
    
    private fun setupClickListeners() {
        binding.btnRetakeQuiz.setOnClickListener {
            retakeQuiz()
        }
        
        binding.btnBackToHome.setOnClickListener {
            backToHome()
        }
    }
    
    private fun retakeQuiz() {
        val intent = Intent(this, QuizActivity::class.java)
        startActivity(intent)
        finish()
    }
    
    private fun backToHome() {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        }
        startActivity(intent)
        finish()
    }
    
    override fun onBackPressed() {
        backToHome()
    }
}