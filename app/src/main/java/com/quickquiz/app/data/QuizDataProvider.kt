package com.quickquiz.app.data

import com.quickquiz.app.model.Question
import com.quickquiz.app.model.Quiz

object QuizDataProvider {
    
    fun getSampleQuiz(): Quiz {
        val questions = listOf(
            Question(
                id = 1,
                question = "What is the capital of France?",
                options = listOf("London", "Berlin", "Paris", "Madrid"),
                correctAnswerIndex = 2,
                explanation = "Paris is the capital and largest city of France."
            ),
            Question(
                id = 2,
                question = "Which planet is known as the Red Planet?",
                options = listOf("Venus", "Mars", "Jupiter", "Saturn"),
                correctAnswerIndex = 1,
                explanation = "Mars is called the Red Planet due to its reddish appearance from iron oxide on its surface."
            ),
            Question(
                id = 3,
                question = "What is the largest mammal in the world?",
                options = listOf("African Elephant", "Blue Whale", "Giraffe", "Polar Bear"),
                correctAnswerIndex = 1,
                explanation = "The Blue Whale is the largest mammal and the largest animal ever known to have lived on Earth."
            ),
            Question(
                id = 4,
                question = "Which programming language is Android primarily developed in?",
                options = listOf("Swift", "Kotlin", "Python", "JavaScript"),
                correctAnswerIndex = 1,
                explanation = "Android development is primarily done using Kotlin, which is Google's preferred language for Android."
            ),
            Question(
                id = 5,
                question = "What is the chemical symbol for gold?",
                options = listOf("Go", "Gd", "Au", "Ag"),
                correctAnswerIndex = 2,
                explanation = "Au is the chemical symbol for gold, derived from the Latin word 'aurum'."
            ),
            Question(
                id = 6,
                question = "Which year did the first iPhone launch?",
                options = listOf("2006", "2007", "2008", "2009"),
                correctAnswerIndex = 1,
                explanation = "The first iPhone was launched by Apple in 2007, revolutionizing the smartphone industry."
            ),
            Question(
                id = 7,
                question = "What is the smallest country in the world?",
                options = listOf("Monaco", "San Marino", "Vatican City", "Liechtenstein"),
                correctAnswerIndex = 2,
                explanation = "Vatican City is the smallest country in the world by both area and population."
            ),
            Question(
                id = 8,
                question = "Which element has the atomic number 1?",
                options = listOf("Helium", "Hydrogen", "Lithium", "Carbon"),
                correctAnswerIndex = 1,
                explanation = "Hydrogen has the atomic number 1, making it the lightest and most abundant element in the universe."
            ),
            Question(
                id = 9,
                question = "What is the longest river in the world?",
                options = listOf("Amazon River", "Nile River", "Yangtze River", "Mississippi River"),
                correctAnswerIndex = 1,
                explanation = "The Nile River is generally considered the longest river in the world at approximately 6,650 km."
            ),
            Question(
                id = 10,
                question = "Which company developed the Android operating system?",
                options = listOf("Apple", "Microsoft", "Google", "Samsung"),
                correctAnswerIndex = 2,
                explanation = "Google developed the Android operating system, which is now the most widely used mobile OS globally."
            )
        )
        
        return Quiz(
            title = "General Knowledge Quiz",
            description = "Test your knowledge with these 10 questions covering various topics including geography, science, technology, and more!",
            questions = questions,
            timeLimit = 15 // 15 minutes
        )
    }
    
    fun getTechnologyQuiz(): Quiz {
        val questions = listOf(
            Question(
                id = 11,
                question = "What does API stand for?",
                options = listOf(
                    "Application Programming Interface",
                    "Advanced Programming Integration", 
                    "Automated Program Interaction",
                    "Application Process Integration"
                ),
                correctAnswerIndex = 0,
                explanation = "API stands for Application Programming Interface, which allows different software applications to communicate with each other."
            ),
            Question(
                id = 12,
                question = "Which of these is a NoSQL database?",
                options = listOf("MySQL", "PostgreSQL", "MongoDB", "SQLite"),
                correctAnswerIndex = 2,
                explanation = "MongoDB is a NoSQL database that stores data in flexible, JSON-like documents."
            ),
            Question(
                id = 13,
                question = "What is the latest version of HTTP?",
                options = listOf("HTTP/1.1", "HTTP/2", "HTTP/3", "HTTP/4"),
                correctAnswerIndex = 2,
                explanation = "HTTP/3 is the latest version of the HTTP protocol, built on top of QUIC instead of TCP."
            ),
            Question(
                id = 14,
                question = "Which design pattern is commonly used in Android development?",
                options = listOf("Singleton", "Observer", "MVP/MVVM", "All of the above"),
                correctAnswerIndex = 3,
                explanation = "All of these design patterns are commonly used in Android development, with MVP and MVVM being particularly popular for architecture."
            ),
            Question(
                id = 15,
                question = "What does CSS stand for?",
                options = listOf(
                    "Computer Style Sheets",
                    "Cascading Style Sheets",
                    "Creative Style Sheets",
                    "Colorful Style Sheets"
                ),
                correctAnswerIndex = 1,
                explanation = "CSS stands for Cascading Style Sheets, used to describe the presentation of HTML documents."
            )
        )
        
        return Quiz(
            title = "Technology Quiz",
            description = "Challenge your knowledge of programming, web development, and modern technology concepts.",
            questions = questions,
            timeLimit = 10 // 10 minutes
        )
    }
}