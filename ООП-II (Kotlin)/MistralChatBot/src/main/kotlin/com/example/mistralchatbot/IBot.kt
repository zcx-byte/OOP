package com.example.mistralchatbot

// Интерфейс — контракт, что умеет бот
interface IBot {

    // Обработка команд (/привет, /время, /курс)
    fun handleCommand(command: String, userName: String): String?

    // Математика (2+2, умножь 5 на 3)
    fun calculate(expression: String): String?

    // Простые ответы (привет, как дела, спасибо)
    fun getSimpleResponse(message: String): String?

    // Ответ от нейросети (если ничего не подошло)
    fun getAIResponse(message: String, apiKey: String): String
}