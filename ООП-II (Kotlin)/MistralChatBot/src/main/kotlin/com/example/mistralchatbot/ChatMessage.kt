package com.example.mistralchatbot

// Data class для сообщений
data class ChatMessage(
    val timestamp: String,
    val userMessage: String,
    val botMessage: String
)