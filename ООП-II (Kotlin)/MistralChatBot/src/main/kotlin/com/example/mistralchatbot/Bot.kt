// ссылка на задание -> https://github.com/VetrovSV/OOP/blob/master/%D0%97%D0%B0%D0%B4%D0%B0%D0%BD%D0%B8%D1%8F-II.md#%D0%B7%D0%B0%D0%B4%D0%B0%D0%BD%D0%B8%D0%B5-2-%D1%87%D0%B0%D1%82-%D0%B1%D0%BE%D1%82

package com.example.mistralchatbot

import okhttp3.OkHttpClient
import okhttp3.Request
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

// Класс бота — здесь вся логика: команды, математика, простые ответы и ИИ
// Реализует интерфейс IBot, чтобы контроллер мог работать с ним "вслепую"
class Bot : IBot {

    // HTTP-клиент для запросов (нужен для получения курса валют)
    private val client = OkHttpClient()

    // Обрабатываем команды типа /привет, /время, /курс
    // Если команда распознана — возвращаем ответ, если нет — null (пусть пробует дальше)
    override fun handleCommand(command: String, userName: String): String? {

        // Приводим к нижнему регистру и убираем лишние пробелы — так проще сравнивать
        val lower = command.lowercase().trim()

        return when {

            // Простые приветствия — сразу отвечаем, не грузим ИИ
            lower in listOf("привет", "хай", "hello", "здравствуйте") ->
                "Привет! Чем могу помочь?"

            // Помощь — показываем, что вообще умеет бот
            lower in listOf("помощь", "help", "/помощь") -> """
                Доступные команды:
                • привет - поздороваться
                • помощь - список команд
                • время - текущее время
                • курс - примерный текущий курс популярных валют
                Математика: 2+2 или "умножь 5 на 3"
            """.trimIndent()

            // Время — берём системное, форматируем под человека
            lower in listOf("время", "time", "/время") -> {
                val now = LocalDateTime.now()
                "Сейчас: ${now.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"))}"
            }

            // Курс валют — делегируем отдельному методу, чтобы не загромождать код
            lower in listOf("курс", "валюта", "/курс") -> getCurrency()

            // Если не наша команда — возвращаем null, пусть пробует другие варианты
            else -> null
        }
    }

    // Пытаемся посчитать выражение: ищем числа и математические слова/символы
    // Если получилось — возвращаем результат, если нет — null
    override fun calculate(expression: String): String? {
        val lower = expression.lowercase()

        // Находим все числа в тексте (целые и дробные), берём первые два
            val numbers = Regex("\\d+\\.?\\d*").findAll(expression)
            .map { it.value.toDouble() }.toList()

        // Если чисел меньше двух — нечего считать
        if (numbers.size < 2) return null

        val a = numbers[0]
        val b = numbers[1]
        var result = 0.0
        var symbol = ""

        // Определяем операцию по ключевым словам или символам
        when {
            lower.contains("умножь") || lower.contains("multiply") || lower.contains("x") -> {
                result = a * b; symbol = "*"
            }
            lower.contains("раздели") || lower.contains("divide") || lower.contains("/") -> {
                if (b == 0.0) return "На ноль делить нельзя!"
                result = a / b; symbol = "/"
            }
            lower.contains("прибавь") || lower.contains("сложи") || lower.contains("add") ||
                    lower.contains("+") || lower.contains("плюс") -> {
                result = a + b; symbol = "+"
            }
            lower.contains("вычти") || lower.contains("отними") || lower.contains("subtract") ||
                    lower.contains("-") || lower.contains("минус") -> {
                result = a - b; symbol = "-"
            }

            // Если не нашли ни одного известного оператора — не наша математика
            else -> return null
        }

        // Форматируем результат: если число целое — показываем без .0, если дробное — округляем до 2 знаков
        val resStr = if (result == result.toInt().toDouble()) {
            result.toInt().toString()
        } else {
            String.format("%.2f", result)
        }
        return "Считаем: $a $symbol $b = $resStr"
    }

    // Простые ответы на частые фразы — чтобы не дёргать ИИ по пустякам
    override fun getSimpleResponse(message: String): String? {
        val lower = message.lowercase()
        return when {
            lower.contains("как дела") -> "У меня всё отлично!"
            lower.contains("кто ты") -> "Я AI чат-бот."
            lower.contains("спасибо") -> "Пожалуйста!"
            lower.contains("пока") -> "До свидания!"
            // Если фраза не из списка — пусть пробует ИИ
            else -> null
        }
    }

    //TODO: в UML дать пояснение от какого класса к какому рисовать

    // Если ничего не подошло — спрашиваем у нейросети (через сервис)
    // TODO: создать обьъек класс местрал и уже с ним работать
    override fun getAIResponse(message: String, apiKey: String): String {
        return MistralApiService().sendMessage(message, apiKey)
    }

    // Внутренний метод: получает курсы валют с публичного API
    // Парсим ответ "в лоб" строкой, без сложных JSON-библиотек — для простоты
    private fun getCurrency(): String {
        return try {

            // Запрос к бесплатному API с курсами
            val request = Request.Builder()
                .url("https://open.er-api.com/v6/latest/USD")
                .build()
            val response = client.newCall(request).execute()
            val body = response.body?.string() ?: return "Ошибка сети"

            // Ищем блок с курсами в JSON-ответе
            val ratesStart = body.indexOf("\"rates\":{")
            if (ratesStart < 0) return "Не удалось найти курсы"

            // Вырезаем только часть с курсами, чтобы не парсить весь гигантский ответ
            val ratesEnd = body.indexOf('}', ratesStart)
            val ratesBlock = body.substring(ratesStart, ratesEnd + 1)

            // Список валют, которые нам интересны (код → название для человека)
            val currencies = mapOf("RUB" to "Рубль", "EUR" to "Евро", "CNY" to "Юань", "KZT" to "Тенге")
            val result = StringBuilder("Курс к 1 USD:\n\n")

            // Для каждой валюты ищем её значение в строке и вырезаем число
            for ((code, name) in currencies) {
                val pattern = "\"$code\":"
                val index = ratesBlock.indexOf(pattern)
                if (index >= 0) {
                    val start = index + pattern.length
                    val end = ratesBlock.indexOfAny(charArrayOf(',', '}'), start)
                    val value = ratesBlock.substring(start, end).trim()
                    result.append("$name ($code): $value\n")
                }
            }

            result.append("\n Курсы приблизительные").toString()
        } catch (e: Exception) {
            
            // Если API упал или нет интернета — не ломаем приложение, просто сообщаем
            "Не удалось загрузить курс."
        }
    }
}