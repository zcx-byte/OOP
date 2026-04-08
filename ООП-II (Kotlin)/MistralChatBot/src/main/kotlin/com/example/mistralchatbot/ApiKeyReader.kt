package com.example.mistralchatbot

import java.io.File

// Объект - это как статический класс в Java
// Мы используем его, потому что нам нужен только один экземпляр
object ApiKeyReader {

    // Функция читает ключ из файла
    fun readApiKey(): String {

        // File("api_key.txt") - создаём объект файла
        // readText() - читаем всё содержимое как текст
        // trim() - убираем пробелы и переносы строк в начале и конце
        return File("api_key.txt").readText().trim()
    }
}