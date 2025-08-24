package com.quickquiz.app.ui

import android.animation.ObjectAnimator
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.View
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.quickquiz.app.R
import com.quickquiz.app.data.QuizDataProvider
import com.quickquiz.app.databinding.ActivityQuizBinding
import com.quickquiz.app.model.Question
import com.quickquiz.app.model.Quiz
import com.quickquiz.app.utils.PreferencesManager

class QuizActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityQuizBinding
    private lateinit var quiz: Quiz
    private lateinit var vibrator: Vibrator
    private lateinit var preferencesManager: PreferencesManager
    private var currentQuestionIndex = 0
    private var selectedAnswerIndex: Int? = null
    private val userAnswers = mutableMapOf<Int, Int>()
    private val radioButtons = mutableListOf<RadioButton>()
    private var isAnswerSubmitted = false
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupSystemUI()
        preferencesManager = PreferencesManager(this)
        initializeQuiz()
        setupClickListeners()
        displayCurrentQuestion()
    }
    
    private fun setupSystemUI() {
        // Ensure status bar is always visible
        window.apply {
            statusBarColor = ContextCompat.getColor(this@QuizActivity, R.color.glass_background)
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
    
    private fun initializeQuiz() {
        quiz = QuizDataProvider.getSampleQuiz()
        
        // Initialize vibrator for haptic feedback
        vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
    }
    
    private fun provideHapticFeedback(type: FeedbackType = FeedbackType.LIGHT) {
        if (preferencesManager.isVibrationEnabled() && vibrator.hasVibrator()) {
            val effect = when (type) {
                FeedbackType.LIGHT -> VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE)
                FeedbackType.MEDIUM -> VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE)
                FeedbackType.SUCCESS -> VibrationEffect.createWaveform(longArrayOf(0, 50, 50, 50), -1)
                FeedbackType.ERROR -> VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE)
            }
            vibrator.vibrate(effect)
        }
    }
    
    enum class FeedbackType {
        LIGHT, MEDIUM, SUCCESS, ERROR
    }
    
    private fun setupClickListeners() {
        binding.btnSubmit.setOnClickListener {
            provideHapticFeedback(FeedbackType.MEDIUM)
            submitAnswer()
        }
        
        binding.btnNext.setOnClickListener {
            provideHapticFeedback(FeedbackType.LIGHT)
            handleNextQuestion()
        }
    }
    
    private fun displayCurrentQuestion() {
        if (currentQuestionIndex >= quiz.questions.size) {
            finishQuiz()
            return
        }
        
        // Reset state for new question
        isAnswerSubmitted = false
        selectedAnswerIndex = null
        binding.llResultContainer.visibility = View.GONE
        
        // Reset button states with smooth transition
        if (binding.btnNext.visibility == View.VISIBLE) {
            // Animate next button out if it's visible
            val fadeOutAnimation = AnimationUtils.loadAnimation(this, R.anim.button_fade_out)
            fadeOutAnimation.setAnimationListener(object : android.view.animation.Animation.AnimationListener {
                override fun onAnimationStart(animation: android.view.animation.Animation?) {}
                
                override fun onAnimationEnd(animation: android.view.animation.Animation?) {
                    binding.btnNext.visibility = View.GONE
                    binding.btnSubmit.visibility = View.VISIBLE
                    binding.btnSubmit.isEnabled = false
                    
                    // Animate submit button in
                    val fadeInAnimation = AnimationUtils.loadAnimation(this@QuizActivity, R.anim.button_fade_in)
                    binding.btnSubmit.startAnimation(fadeInAnimation)
                }
                
                override fun onAnimationRepeat(animation: android.view.animation.Animation?) {}
            })
            binding.btnNext.startAnimation(fadeOutAnimation)
        } else {
            // Just ensure submit button is visible and disabled
            binding.btnSubmit.visibility = View.VISIBLE
            binding.btnSubmit.isEnabled = false
        }
        
        val question = quiz.questions[currentQuestionIndex]
        
        // Animate question entry
        val slideAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_in_right)
        binding.tvQuestion.text = question.question
        binding.tvQuestion.startAnimation(slideAnimation)
        
        // Update progress bar with animation
        val newProgress = ((currentQuestionIndex + 1) * 100) / quiz.questions.size
        val progressAnimator = ObjectAnimator.ofInt(binding.progressBar, "progress", binding.progressBar.progress, newProgress)
        progressAnimator.duration = 500
        progressAnimator.start()
        
        // Create radio button options
        createRadioButtonOptions(question)
    }
    
    private fun createRadioButtonOptions(question: Question) {
        // Clear previous options
        binding.radioGroupOptions.removeAllViews()
        radioButtons.clear()
        
        // Create radio button for each option
        question.options.forEachIndexed { index, option ->
            val radioButton = RadioButton(this).apply {
                text = option
                id = View.generateViewId()
                setTextColor(ContextCompat.getColor(this@QuizActivity, R.color.text_primary))
                
                // Use only default Android radio button styling - no custom background
                buttonTintList = ContextCompat.getColorStateList(this@QuizActivity, R.color.info_color)
                
                setPadding(
                    resources.getDimensionPixelSize(R.dimen.spacing_medium),
                    resources.getDimensionPixelSize(R.dimen.spacing_medium),
                    resources.getDimensionPixelSize(R.dimen.spacing_medium),
                    resources.getDimensionPixelSize(R.dimen.spacing_medium)
                )
                textSize = 16f
                
                // Set layout params
                layoutParams = android.widget.RadioGroup.LayoutParams(
                    android.widget.RadioGroup.LayoutParams.MATCH_PARENT,
                    android.widget.RadioGroup.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(
                        0,
                        resources.getDimensionPixelSize(R.dimen.spacing_small),
                        0,
                        resources.getDimensionPixelSize(R.dimen.spacing_small)
                    )
                }
                
                setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked && !isAnswerSubmitted) {
                        provideHapticFeedback(FeedbackType.LIGHT)
                        selectOption(index)
                    }
                }
            }
            
            radioButtons.add(radioButton)
            binding.radioGroupOptions.addView(radioButton)
        }
        
        // Add slide-in animation for options
        val slideAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_in_right)
        slideAnimation.startOffset = 100 // Small delay after question
        binding.radioGroupOptions.startAnimation(slideAnimation)
    }
    
    private fun selectOption(index: Int) {
        if (isAnswerSubmitted) return
        
        // Update selected answer
        selectedAnswerIndex = index
        
        // Enable submit button
        updateSubmitButton()
    }
    
    private fun updateSubmitButton() {
        binding.btnSubmit.isEnabled = selectedAnswerIndex != null && !isAnswerSubmitted
    }
    
    private fun submitAnswer() {
        selectedAnswerIndex?.let { answerIndex ->
            isAnswerSubmitted = true
            val currentQuestion = quiz.questions[currentQuestionIndex]
            
            // Disable all radio buttons
            radioButtons.forEach { radioButton ->
                radioButton.isEnabled = false
            }
            
            // Save user's answer
            userAnswers[currentQuestion.id] = answerIndex
            
            // Show result
            displayResult(currentQuestion, answerIndex)
            
            // Animate button transformation from submit to next
            transformSubmitToNext()
        }
    }
    
    private fun transformSubmitToNext() {
        // Fade out submit button
        val fadeOutAnimation = AnimationUtils.loadAnimation(this, R.anim.button_fade_out)
        fadeOutAnimation.setAnimationListener(object : android.view.animation.Animation.AnimationListener {
            override fun onAnimationStart(animation: android.view.animation.Animation?) {}
            
            override fun onAnimationEnd(animation: android.view.animation.Animation?) {
                // Hide submit button and prepare next button
                binding.btnSubmit.visibility = View.GONE
                
                // Update next button text
                if (currentQuestionIndex == quiz.questions.size - 1) {
                    binding.btnNext.text = getString(R.string.finish_quiz)
                } else {
                    binding.btnNext.text = getString(R.string.next_question)
                }
                
                // Show and animate in next button
                binding.btnNext.visibility = View.VISIBLE
                val fadeInAnimation = AnimationUtils.loadAnimation(this@QuizActivity, R.anim.button_fade_in)
                binding.btnNext.startAnimation(fadeInAnimation)
            }
            
            override fun onAnimationRepeat(animation: android.view.animation.Animation?) {}
        })
        
        binding.btnSubmit.startAnimation(fadeOutAnimation)
    }
    
    private fun displayResult(question: Question, userAnswerIndex: Int) {
        val isCorrect = question.isCorrectAnswer(userAnswerIndex)
        
        // Provide haptic feedback based on correctness
        if (isCorrect) {
            provideHapticFeedback(FeedbackType.SUCCESS)
        } else {
            provideHapticFeedback(FeedbackType.ERROR)
        }
        
        // Show result container with animation
        binding.llResultContainer.visibility = View.VISIBLE
        val fadeScaleAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_scale_in)
        binding.llResultContainer.startAnimation(fadeScaleAnimation)
        
        // Set result text and color
        if (isCorrect) {
            binding.tvResult.text = getString(R.string.correct_answer)
            binding.tvResult.setTextColor(ContextCompat.getColor(this, R.color.success_color))
        } else {
            binding.tvResult.text = getString(R.string.incorrect_answer)
            binding.tvResult.setTextColor(ContextCompat.getColor(this, R.color.error_color))
        }
        
        // Show explanation if available
        question.explanation?.let { explanation ->
            binding.tvExplanation.text = explanation
            binding.tvExplanation.visibility = View.VISIBLE
        } ?: run {
            binding.tvExplanation.visibility = View.GONE
        }
        
        // Highlight correct answer
        highlightCorrectAnswer(question.correctAnswerIndex, userAnswerIndex)
    }
    
    private fun highlightCorrectAnswer(correctIndex: Int, userAnswerIndex: Int) {
        radioButtons.forEachIndexed { index, radioButton ->
            when {
                index == correctIndex -> {
                    // Highlight correct answer in green
                    radioButton.setTextColor(ContextCompat.getColor(this, R.color.success_color))
                }
                index == userAnswerIndex && index != correctIndex -> {
                    // Highlight incorrect user answer in red
                    radioButton.setTextColor(ContextCompat.getColor(this, R.color.error_color))
                }
                else -> {
                    // Keep other options in default color but dimmed
                    radioButton.setTextColor(ContextCompat.getColor(this, R.color.text_tertiary))
                }
            }
        }
    }
    
    private fun handleNextQuestion() {
        // Move to next question
        currentQuestionIndex++
        
        if (currentQuestionIndex >= quiz.questions.size) {
            finishQuiz()
        } else {
            // Re-enable radio buttons for next question
            radioButtons.forEach { radioButton ->
                radioButton.isEnabled = true
                radioButton.setTextColor(ContextCompat.getColor(this, R.color.text_primary))
            }
            displayCurrentQuestion()
        }
    }
    
    private fun finishQuiz() {
        val quizResult = quiz.calculateScore(userAnswers)
        
        val intent = Intent(this, ResultActivity::class.java).apply {
            putExtra("quiz_result", quizResult)
        }
        startActivity(intent)
        finish()
    }
    
    override fun onBackPressed() {
        // Show confirmation dialog to prevent accidental exit
        AlertDialog.Builder(this)
            .setTitle(R.string.exit_quiz_title)
            .setMessage(R.string.exit_quiz_message)
            .setPositiveButton(R.string.exit_quiz_confirm) { _, _ ->
                super.onBackPressed()
            }
            .setNegativeButton(R.string.continue_quiz, null)
            .show()
    }    }