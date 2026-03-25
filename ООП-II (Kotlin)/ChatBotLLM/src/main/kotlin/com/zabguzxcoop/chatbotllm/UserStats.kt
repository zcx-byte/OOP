package com.zabguzxcoop.chatbotllm

import java.time.LocalDateTime

/**
 * Data-класс для хранения статистики пользователя.
 * Параметры по умолчанию упрощают создание объекта.
 */
data class UserStats(
    val username: String,                          // Имя пользователя
    var messagesSent: Int = 0,                     // Сколько сообщений отправил пользователь
    var messagesReceived: Int = 0,                 // Сколько ответов получил от бота
    val firstVisit: String = LocalDateTime.now().toString() // Дата первого входа
)