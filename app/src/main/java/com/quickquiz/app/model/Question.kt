package com.quickquiz.app.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Question(
    val id: Int,
    val question: String,
    val options: List<String>,
    val correctAnswerIndex: Int,
    val explanation: String? = null
) : Parcelable {
    fun isCorrectAnswer(selectedIndex: Int): Boolean {
        return selectedIndex == correctAnswerIndex
    }
    
    fun getCorrectAnswer(): String {
        return if (correctAnswerIndex in options.indices) {
            options[correctAnswerIndex]
        } else {
            ""
        }
    }
}