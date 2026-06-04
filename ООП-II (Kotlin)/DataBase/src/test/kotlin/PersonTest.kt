package com.zxcoop.database.model

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.LocalDate

/**
 * Простые тесты класса Person.
 * Проверяют только базовую логику: создание и валидацию.
 */
class PersonTest {

    /**
     * Тест: валидный человек проходит проверку
     */
    @Test
    fun `valid person should pass validation`() {
        // Создаём объект с корректными данными
        val person = Person(
            fullName = "Иванов Иван",
            age = 25,
            dateOfBirth = LocalDate.of(1999, 1, 1),
            phone = "+79991234567"
        )

        // Проверяем, что валидация прошла успешно
        assertTrue(person.isValid())
    }

    /**
     * Тест: короткое ФИО не проходит валидацию
     */
    @Test
    fun `short name should fail validation`() {
        val person = Person(
            fullName = "Аб",  // Меньше 3 символов
            age = 20,
            dateOfBirth = LocalDate.now(),
            phone = ""
        )

        // Ожидаем, что валидация вернёт false
        assertFalse(person.isValid())
    }

    /**
     * Тест: возраст вне диапазона не проходит валидацию
     */
    @Test
    fun `invalid age should fail validation`() {
        val person1 = Person(fullName = "Тест", age = -1, dateOfBirth = LocalDate.now(), phone = "")
        val person2 = Person(fullName = "Тест", age = 200, dateOfBirth = LocalDate.now(), phone = "")

        assertFalse(person1.isValid())  // Отрицательный возраст
        assertFalse(person2.isValid())  // Слишком большой возраст
    }

    /**
     * Тест: пустая дата рождения не проходит валидацию
     */
    @Test
    fun `null date should fail validation`() {
        val person = Person(
            fullName = "Тест Тестович",
            age = 30,
            dateOfBirth = null,  // Дата обязательна
            phone = ""
        )

        assertFalse(person.isValid())
    }
}