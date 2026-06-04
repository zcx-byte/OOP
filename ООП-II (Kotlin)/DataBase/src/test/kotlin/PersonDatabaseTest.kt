package com.zxcoop.database.model

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDate

/**
 * Простые тесты класса PersonDatabase.
 * Проверяют добавление, удаление, поиск и сортировку.
 * Не тестируют работу с файлом (это сложно и не обязательно для лабы).
 */
class PersonDatabaseTest {

    // Переменная для базы данных
    // lateinit означает: "будет инициализирована позже, до первого использования"
    private lateinit var db: PersonDatabase

    /**
     * Этот метод запускается ПЕРЕД КАЖДЫМ тестом (@BeforeEach)
     * Гарантирует, что каждый тест начинает с чистого состояния
     */
    @BeforeEach
    fun setUp() {
        db = PersonDatabase()
    }

    /**
     * Тест: добавление человека увеличивает размер списка
     */
    @Test
    fun `add person increases list size`() {
        // Создаём валидного человека
        val person = Person(
            fullName = "Тестов Тест",
            age = 25,
            dateOfBirth = LocalDate.now(),
            phone = ""
        )

        // Добавляем в базу
        val result = db.addPerson(person)

        // Проверяем результаты
        assertTrue(result)  // Метод должен вернуть true
        assertEquals(1, db.getPeopleList().size)  // В списке 1 запись
        assertTrue(person.id > 0)  // ID должен быть сгенерирован
    }

    /**
     * Тест: невалидный человек не добавляется
     */
    @Test
    fun `invalid person is not added`() {
        val person = Person(fullName = "А", age = 20, dateOfBirth = LocalDate.now(), phone = "")

        val result = db.addPerson(person)

        assertFalse(result)  // Метод должен вернуть false
        assertEquals(0, db.getPeopleList().size)  // Список остался пустым
    }

    /**
     * Тест: удаление человека уменьшает размер списка
     */
    @Test
    fun `delete person removes from list`() {
        // Сначала добавляем человека
        val person = Person(fullName = "Удаляемый", age = 30, dateOfBirth = LocalDate.now(), phone = "")
        db.addPerson(person)

        // Запоминаем сгенерированный ID
        val id = person.id

        // Удаляем по ID
        val result = db.deletePerson(id)

        // Проверяем результат
        assertTrue(result)  // Удаление прошло успешно
        assertEquals(0, db.getPeopleList().size)  // Список пуст
    }

    /**
     * Тест: поиск находит запись по части ФИО
     */
    @Test
    fun `search finds person by name part`() {
        // Добавляем несколько записей
        db.addPerson(Person(fullName = "Иванов Иван", age = 20, dateOfBirth = LocalDate.now(), phone = ""))
        db.addPerson(Person(fullName = "Петров Петр", age = 25, dateOfBirth = LocalDate.now(), phone = ""))

        // Ищем по части имени (без учёта регистра)
        val results = db.searchByName("иван")

        // Проверяем, что нашли только Иванова
        assertEquals(1, results.size)
        assertEquals("Иванов Иван", results[0].fullName)
    }

    /**
     * Тест: сортировка по имени работает корректно
     */
    @Test
    fun `sort by name works correctly`() {
        // Добавляем в случайном порядке
        db.addPerson(Person(fullName = "Сидоров", age = 20, dateOfBirth = LocalDate.now(), phone = ""))
        db.addPerson(Person(fullName = "Андреев", age = 22, dateOfBirth = LocalDate.now(), phone = ""))
        db.addPerson(Person(fullName = "Борисов", age = 21, dateOfBirth = LocalDate.now(), phone = ""))

        // Сортируем по возрастанию
        db.sortBy(PersonDatabase.SortField.NAME)

        // Проверяем порядок
        val list = db.getPeopleList()
        assertEquals("Андреев", list[0].fullName)
        assertEquals("Борисов", list[1].fullName)
        assertEquals("Сидоров", list[2].fullName)
    }
}