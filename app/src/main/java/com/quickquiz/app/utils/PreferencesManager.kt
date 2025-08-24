package com.quickquiz.app.utils

import android.content.Context
import android.content.SharedPreferences

class PreferencesManager(context: Context) {
    
    private val sharedPreferences: SharedPreferences = 
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    
    companion object {
        private const val PREFS_NAME = "quick_quiz_prefs"
        private const val KEY_BEST_SCORE = "best_score"
        private const val KEY_TOTAL_QUIZZES_COMPLETED = "total_quizzes_completed"
        private const val KEY_TOTAL_QUESTIONS_ANSWERED = "total_questions_answered"
        private const val KEY_TOTAL_CORRECT_ANSWERS = "total_correct_answers"
        private const val KEY_SOUND_ENABLED = "sound_enabled"
        private const val KEY_VIBRATION_ENABLED = "vibration_enabled"
    }
    
    // Score management
    fun getBestScore(): Int {
        return sharedPreferences.getInt(KEY_BEST_SCORE, 0)
    }
    
    fun setBestScore(score: Int) {
        if (score > getBestScore()) {
            sharedPreferences.edit().putInt(KEY_BEST_SCORE, score).apply()
        }
    }
    
    // Statistics
    fun getTotalQuizzesCompleted(): Int {
        return sharedPreferences.getInt(KEY_TOTAL_QUIZZES_COMPLETED, 0)
    }
    
    fun incrementQuizzesCompleted() {
        val current = getTotalQuizzesCompleted()
        sharedPreferences.edit().putInt(KEY_TOTAL_QUIZZES_COMPLETED, current + 1).apply()
    }
    
    fun getTotalQuestionsAnswered(): Int {
        return sharedPreferences.getInt(KEY_TOTAL_QUESTIONS_ANSWERED, 0)
    }
    
    fun incrementQuestionsAnswered(count: Int) {
        val current = getTotalQuestionsAnswered()
        sharedPreferences.edit().putInt(KEY_TOTAL_QUESTIONS_ANSWERED, current + count).apply()
    }
    
    fun getTotalCorrectAnswers(): Int {
        return sharedPreferences.getInt(KEY_TOTAL_CORRECT_ANSWERS, 0)
    }
    
    fun incrementCorrectAnswers(count: Int) {
        val current = getTotalCorrectAnswers()
        sharedPreferences.edit().putInt(KEY_TOTAL_CORRECT_ANSWERS, current + count).apply()
    }
    
    fun getOverallAccuracy(): Float {
        val total = getTotalQuestionsAnswered()
        val correct = getTotalCorrectAnswers()
        return if (total > 0) (correct.toFloat() / total.toFloat()) * 100f else 0f
    }
    
    // Settings
    fun isSoundEnabled(): Boolean {
        return sharedPreferences.getBoolean(KEY_SOUND_ENABLED, true)
    }
    
    fun setSoundEnabled(enabled: Boolean) {
        sharedPreferences.edit().putBoolean(KEY_SOUND_ENABLED, enabled).apply()
    }
    
    fun isVibrationEnabled(): Boolean {
        return sharedPreferences.getBoolean(KEY_VIBRATION_ENABLED, true)
    }
    
    fun setVibrationEnabled(enabled: Boolean) {
        sharedPreferences.edit().putBoolean(KEY_VIBRATION_ENABLED, enabled).apply()
    }
}