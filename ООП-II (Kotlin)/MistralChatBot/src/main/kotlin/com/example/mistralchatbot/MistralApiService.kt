package com.example.mistralchatbot

import com.google.gson.Gson
import com.google.gson.JsonObject
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

// Класс для отправки запросов к API Mistral
class MistralApiService {

    // URL API Mistral
    private val apiUrl = "https://api.mistral.ai/v1/chat/completions"

    // Создаём HTTP клиент (это как браузер для наших запросов)
    private val client = OkHttpClient()

    // Для работы с JSON
    private val gson = Gson()

    // Функция отправки сообщения и получения ответа
    fun sendMessage(message: String, apiKey: String): String {

        // Создаём JSON для отправки
        val json = JsonObject()
        json.addProperty("model", "mistral-tiny")  // Модель ИИ
        json.addProperty("temperature", 0.7)  // Креативность (0.7 - баланс)

        // Создаём массив сообщений
        val messagesArray = com.google.gson.JsonArray()
        val messageObject = JsonObject()

        messageObject.addProperty("role", "user")
        messageObject.addProperty("content", message)
        messagesArray.add(messageObject)

        json.add("messages", messagesArray)

        // Создаём HTTP запрос
        val body = gson.toJson(json).toRequestBody("application/json".toMediaType())
        val request = Request.Builder()
            .url(apiUrl)
            .addHeader("Authorization", "Bearer $apiKey")  // Наш ключ
            .addHeader("Content-Type", "application/json")
            .post(body)
            .build()

        // Отправляем запрос и получаем ответ
        return try {
            val response = client.newCall(request).execute()
            val responseBody = response.body?.string() ?: "Ошибка: пустой ответ"

            // Разбираем JSON ответ
            val jsonResponse = gson.fromJson(responseBody, JsonObject::class.java)
            val choices = jsonResponse.getAsJsonArray("choices")
            val firstChoice = choices[0].asJsonObject
            val messageObject = firstChoice.getAsJsonObject("message")

            // Возвращаем текст ответа
            messageObject.get("content")?.asString ?: "Нет ответа"
        } catch (e: IOException) {
            "Ошибка сети: ${e.message}"
        } catch (e: Exception) {
            "Ошибка: ${e.message}"
        }
    }
}