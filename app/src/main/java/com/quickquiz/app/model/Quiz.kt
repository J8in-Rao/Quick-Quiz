package com.quickquiz.app.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Quiz(
    val title: String,
    val description: String,
    val questions: List<Question>,
    val timeLimit: Int? = null // in minutes
) : Parcelable {
    fun getTotalQuestions(): Int = questions.size
    
    fun calculateScore(userAnswers: Map<Int, Int>): QuizResult {
        var correctAnswers = 0
        val questionResults = mutableListOf<QuestionResult>()
        
        questions.forEachIndexed { index, question ->
            val userAnswer = userAnswers[question.id]
            val isCorrect = userAnswer?.let { question.isCorrectAnswer(it) } ?: false
            
            if (isCorrect) correctAnswers++
            
            questionResults.add(
                QuestionResult(
                    question = question,
                    userAnswerIndex = userAnswer,
                    isCorrect = isCorrect
                )
            )
        }
        
        val percentage = if (questions.isNotEmpty()) {
            (correctAnswers * 100) / questions.size
        } else 0
        
        return QuizResult(
            quiz = this,
            correctAnswers = correctAnswers,
            totalQuestions = questions.size,
            percentage = percentage,
            questionResults = questionResults
        )
    }
}

@Parcelize
data class QuestionResult(
    val question: Question,
    val userAnswerIndex: Int?,
    val isCorrect: Boolean
) : Parcelable

@Parcelize
data class QuizResult(
    val quiz: Quiz,
    val correctAnswers: Int,
    val totalQuestions: Int,
    val percentage: Int,
    val questionResults: List<QuestionResult>
) : Parcelable {
    fun getGrade(): String {
        return when {
            percentage >= 90 -> "Excellent"
            percentage >= 80 -> "Very Good"
            percentage >= 70 -> "Good"
            percentage >= 60 -> "Average"
            else -> "Needs Improvement"
        }
    }
}