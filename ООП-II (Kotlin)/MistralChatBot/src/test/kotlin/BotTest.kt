package com.example.mistralchatbot

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class BotTest {

    private val bot = Bot()

    // ================== ТЕСТЫ ДЛЯ КОМАНД ==================

    @Test
    fun `handleCommand возвращает приветствие на русском`() {
        val result = bot.handleCommand("привет", "User")
        assertNotNull(result)
        assertTrue(result!!.contains("Привет"))
    }

    @Test
    fun `handleCommand возвращает приветствие - провальный`() {
        // Ошибка: ожидаем "Здравствуйте", а бот отвечает "Привет"
        val result = bot.handleCommand("привет", "User")
        assertTrue(result!!.contains("Здравствуйте"))
    }

    @Test
    fun `handleCommand возвращает список команд`() {
        val result = bot.handleCommand("помощь", "User")
        assertNotNull(result)
        assertTrue(result!!.contains("Доступные команды"))
    }

    @Test
    fun `handleCommand возвращает список команд - провальный`() {
        // Ошибка: проверяем несуществующий пункт меню
        val result = bot.handleCommand("помощь", "User")
        assertTrue(result!!.contains("погода"))
    }

    @Test
    fun `handleCommand возвращает время в правильном формате`() {
        val result = bot.handleCommand("время", "User")
        assertNotNull(result)
        assertTrue(result!!.contains("Сейчас:"))
    }

    @Test
    fun `handleCommand возвращает время - провальный`() {
        // Ошибка: ожидаем формат без слова "Сейчас"
        val result = bot.handleCommand("время", "User")
        assertTrue(result!!.startsWith("Время: "))
    }

    @Test
    fun `handleCommand возвращает null для неизвестной команды`() {
        val result = bot.handleCommand("какая погода", "User")
        assertNull(result)
    }

    @Test
    fun `handleCommand возвращает null для неизвестной команды - провальный`() {
        // Ошибка: ожидаем ответ, хотя команда не распознана
        val result = bot.handleCommand("какая погода", "User")
        assertNotNull(result)
    }

    // ================== ТЕСТЫ ДЛЯ МАТЕМАТИКИ ==================

    @Test
    fun `calculate складывает два числа через плюс`() {
        val result = bot.calculate("2 + 3")
        assertEquals("Считаем: 2.0 + 3.0 = 5", result)
    }

    @Test
    fun `calculate складывает два числа - провальный`() {
        // Ошибка: ожидаем неверный результат сложения
        val result = bot.calculate("2 + 3")
        assertEquals("Считаем: 2.0 + 3.0 = 6", result)
    }

    @Test
    fun `calculate умножает числа словами`() {
        val result = bot.calculate("умножь 4 на 5")
        assertEquals("Считаем: 4.0 * 5.0 = 20", result)
    }

    @Test
    fun `calculate умножает числа - провальный`() {
        // Ошибка: перепутали операцию (ожидаем сложение вместо умножения)
        val result = bot.calculate("умножь 4 на 5")
        assertEquals("Считаем: 4.0 + 5.0 = 9", result)
    }

    @Test
    fun `calculate делит с округлением до двух знаков`() {
        val result = bot.calculate("10 / 3")
        assertEquals("Считаем: 10.0 / 3.0 = 3,33", result)
    }

    @Test
    fun `calculate делит - провальный`() {
        // Ошибка: ожидаем больше знаков после запятой
        val result = bot.calculate("10 / 3")
        assertEquals("Считаем: 10.0 / 3.0 = 3.333", result)
    }

    @Test
    fun `calculate обрабатывает деление на ноль`() {
        val result = bot.calculate("100 / 0")
        assertEquals("На ноль делить нельзя!", result)
    }

    @Test
    fun `calculate обрабатывает деление на ноль - провальный`() {
        // Ошибка: ожидаем математическое исключение вместо сообщения
        val result = bot.calculate("100 / 0")
        assertEquals("Ошибка: деление на ноль", result)
    }

    @Test
    fun `calculate возвращает null если в тексте меньше двух чисел`() {
        assertNull(bot.calculate("просто текст"))
        assertNull(bot.calculate("одно число 42"))
    }

    @Test
    fun `calculate возвращает null - провальный`() {
        // Ошибка: ожидаем результат, хотя чисел недостаточно
        val result = bot.calculate("просто текст")
        assertNotNull(result)
    }

    // ================== ТЕСТЫ ДЛЯ ПРОСТЫХ ОТВЕТОВ ==================

    @Test
    fun `getSimpleResponse отвечает на стандартные фразы`() {
        assertEquals("У меня всё отлично!", bot.getSimpleResponse("как дела?"))
        assertEquals("Пожалуйста!", bot.getSimpleResponse("спасибо большое"))
        assertEquals("До свидания!", bot.getSimpleResponse("пока"))
    }

    @Test
    fun `getSimpleResponse отвечает на фразы - провальный`() {
        // Ошибка: ожидаем другой текст ответа
        assertEquals("Всё хорошо!", bot.getSimpleResponse("как дела?"))
    }

    @Test
    fun `getSimpleResponse возвращает null для неизвестной фразы`() {
        assertNull(bot.getSimpleResponse("расскажи анекдот"))
    }

    @Test
    fun `getSimpleResponse возвращает null - провальный`() {
        // Ошибка: ожидаем ответ на фразу, которая не обработана
        val result = bot.getSimpleResponse("расскажи анекдот")
        assertNotNull(result)
    }

    // ================== ТЕСТЫ ДЛЯ ИСТОРИИ ==================

    @Test
    fun `addToHistory добавляет сообщение в список`() {
        bot.setUser("TestUser")
        bot.addToHistory("Привет", "Здравствуй")

        val history = bot.getHistoryForSave()
        assertFalse(history.contains("История пуста"))
        assertTrue(history.contains("Привет"))
        assertTrue(history.contains("Здравствуй"))
    }

    @Test
    fun `addToHistory добавляет сообщение - провальный`() {
        // Ошибка: ожидаем, что история останется пустой после добавления
        bot.setUser("TestUser")
        bot.addToHistory("Привет", "Здравствуй")

        val history = bot.getHistoryForSave()
        assertTrue(history.contains("История пуста"))
    }

    @Test
    fun `clearHistory полностью очищает историю`() {
        bot.setUser("TestUser")
        bot.addToHistory("msg1", "resp1")
        bot.addToHistory("msg2", "resp2")

        bot.clearHistory()

        val history = bot.getHistoryForSave()
        assertEquals("История пуста", history)
    }

    @Test
    fun `clearHistory очищает историю - провальный`() {
        // Ошибка: ожидаем, что после очистки история сохранит данные
        bot.setUser("TestUser")
        bot.addToHistory("msg1", "resp1")
        bot.clearHistory()

        val history = bot.getHistoryForSave()
        assertTrue(history.contains("msg1"))
    }

    @Test
    fun `setUser очищает историю при смене пользователя`() {
        bot.setUser("User1")
        bot.addToHistory("old", "old")

        bot.setUser("User2")

        val history = bot.getHistoryForSave()
        assertEquals("История пуста", history)
    }

    @Test
    fun `setUser очищает историю - провальный`() {
        // Ошибка: ожидаем, что история сохранится при смене пользователя
        bot.setUser("User1")
        bot.addToHistory("old", "old")

        bot.setUser("User2")

        val history = bot.getHistoryForSave()
        assertTrue(history.contains("old"))
    }
}