package com.zabguzxcoop.chatbotllm

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Класс, представляющий одно сообщение в чате.
 * Содержит автора, текст и время отправки.
 */
class Message(
    var author: String = "",           // Кто отправил: имя пользователя или "Bot"
    var content: String = "",          // Текст сообщения
    var timestamp: LocalDateTime = LocalDateTime.now() // Время создания
) {
    /**
     * Форматирует время в строку "ЧЧ:ММ" для отображения в интерфейсе.
     */
    fun formatTime(): String {
        return timestamp.format(DateTimeFormatter.ofPattern("HH:mm"))
    }
}