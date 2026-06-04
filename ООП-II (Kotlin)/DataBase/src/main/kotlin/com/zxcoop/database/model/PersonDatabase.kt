package com.zxcoop.database.model
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import java.io.File
import java.time.LocalDate
import java.util.Timer
import java.util.TimerTask

/**
 * Класс PersonDatabase отвечает за работу с данными:
 * - Хранение списка людей в ObservableList
 * - Загрузка и сохранение в файл
 * - CRUD-операции (Create, Read, Update, Delete)
 * - Поиск и сортировка
 *
 * ПАТТЕРН "НАБЛЮДАТЕЛЬ":
 * ObservableList автоматически уведомляет TableView при изменении данных.
 * Это вторая часть паттерна (первая - Properties в классе Person).
 */
class PersonDatabase {

    // Приватное свойство peopleList типа ObservableList<Person>
    // FXCollections.observableArrayList() - фабричный метод, создающий наблюдаемый список
    // <Person> - дженерик параметр, указывающий тип элементов в списке
    // Инициализация происходит сразу при создании объекта PersonDatabase
    private val peopleList: ObservableList<Person> = FXCollections.observableArrayList()

    // Приватное свойство filePath типа String
    // Хранит путь к файлу с данными (относительный путь от рабочей директории)
    private val filePath = "people.txt"

    // Публичная функция getPeopleList(), возвращающая ObservableList<Person>
    // Возвращает ссылку на наблюдаемый список для привязки к TableView
    // Контроллер вызывает эту функцию: tableView.items = db.getPeopleList()
    fun getPeopleList(): ObservableList<Person> = peopleList
    // Тип возвращаемого значения указан после двоеточия: ObservableList<Person>

    // Публичная функция load() для загрузки данных из файла
    // Возвращаемый тип: Unit (аналог void в Java)
    // Unit можно не указывать - Kotlin выводит тип автоматически
    fun load() {

        // clear() - метод ObservableList, удаляющий все элементы
        peopleList.clear()

        // Создаём объект File для работы с файлом
        val file = File(filePath)

        // проверка существования файла
        // return - выход из функции
        if (!file.exists()) return

        // Чтение файла построчно
        // file.forEachLine - функция расширения (extension function) для File
        // Принимает лямбда-выражение { line -> ... }
        // line - параметр лямбды, содержащий текущую строку файла
        // Тип line выводится автоматически как String
        file.forEachLine { line ->

            // Разбиение строки на части по разделителю "|"
            // line.split("|") - метод String, возвращающий List<String>
            // Разделитель "|" указан в кавычках
            // Пример: "1|Иванов|25|1999-01-01|+79991234567" ->
            //         ["1", "Иванов", "25", "1999-01-01", "+79991234567"]
            val parts = line.split("|")

            // Проверка количества полей
            // parts.size - свойство списка, возвращающее количество элементов (Int)
            // if (parts.size == 6) - проверяем, что строка содержит все 6 полей
            if (parts.size == 6) {

                // Создание объекта Person из данных строки
                // parts[3].takeIf { it.isNotEmpty() } - фильтрация:
                //   takeIf возвращает значение, если условие true, иначе null
                //   it - неявное имя параметра лямбды (строка)
                //   isNotEmpty() - метод String, проверяющий что строка не пустая
                // ?.let { LocalDate.parse(it) } - безопасный вызов:
                //   ?. выполняет код только если значение не null
                //   let { ... } - передаёт значение в лямбду
                //   LocalDate.parse(it) - парсит строку в объект LocalDate
                val person = Person(
                    id = parts[0].toInt(),
                    fullName = parts[1],
                    age = parts[2].toInt(),
                    dateOfBirth = parts[3].takeIf { it.isNotEmpty() }?.let { LocalDate.parse(it) },
                    phone = parts[4],
                )

                // Добавление человека в наблюдаемый список
                peopleList.add(person)
            }
        }
    }

    // Публичная функция addPerson() для добавления новой записи
    // Принимает параметр person типа Person
    // Возвращает Boolean (true если успешно добавлен)
    fun addPerson(person: Person): Boolean {

        // Проверка валидности данных
        // person.isValid() - вызов метода валидации
        if (!person.isValid()) return false

        // Генерация нового ID
        // if (peopleList.isEmpty()) 1 else peopleList.maxOf { it.id } + 1
        // isEmpty() - метод проверки на пустоту списка
        // maxOf { it.id } - функция, находящая максимальное значение свойства id
        // it - неявный параметр лямбды (каждый Person в списке)
        // .id - обращение к свойству id
        // + 1 - увеличиваем максимальный ID на 1
        person.id = if (peopleList.isEmpty()) 1 else peopleList.maxOf { it.id } + 1

        // Добавление в наблюдаемый список
        // После этого TableView автоматически обновится
        peopleList.add(person)

        // Возврат true при успешном добавлении
        return true
    }

    // Публичная функция updatePerson() для обновления существующей записи
    // Принимает updated типа Person (обновлённые данные)
    // Возвращает Boolean (true если успешно обновлён)
    fun updatePerson(updated: Person): Boolean {

        // Проверка валидности и наличия ID
        // updated.id == 0 - проверка что ID установлен
        if (!updated.isValid() || updated.id == 0) return false

        // Поиск индекса записи с таким же ID
        // indexOfFirst { it.id == updated.id } - метод поиска первого элемента
        // Возвращает индекс (Int) или -1 если не найдено
        // { it.id == updated.id } - лямбда-условие поиска
        val index = peopleList.indexOfFirst { it.id == updated.id }

        // Если запись не найдена (индекс -1), возвращаем false
        if (index == -1) return false

        // Обновление свойств существующего объекта
        // peopleList[index] - доступ к элементу списка по индексу
        // .fullName = updated.fullName - присваивание нового значения
        // При присваивании срабатывает сеттер, который уведомляет TableView
        peopleList[index].fullName = updated.fullName
        peopleList[index].age = updated.age
        peopleList[index].dateOfBirth = updated.dateOfBirth
        peopleList[index].phone = updated.phone

        // Возврат true при успешном обновлении
        return true
    }

    // Публичная функция deletePerson() для удаления записи по ID
    // Принимает id типа Int
    // Возвращает Boolean (true если удалено)
    fun deletePerson(id: Int): Boolean {

        // Удаление из списка по условию
        // removeIf { it.id == id } - метод удаления элементов, соответствующих условию
        // Возвращает Boolean (true если хотя бы один элемент удалён)
        // { it.id == id } - лямбда-условие: сравниваем id каждого элемента с заданным
        val deleted = peopleList.removeIf { it.id == id }

        // Возврат результата удаления
        return deleted
    }

    // Публичная функция searchByName() для поиска по ФИО
    // Принимает query типа String (поисковый запрос)
    // Возвращает List<Person> (список найденных)
    fun searchByName(query: String): List<Person> {

        // Приведение строки к нижнему регистру
        // query.lowercase() - метод String, возвращающий строку в нижнем регистре
        // Нужно для поиска без учёта регистра (Иванов = иванов = ИВАНОВ)
        val lower = query.lowercase()

        // Фильтрация списка
        // peopleList.filter { ... } - метод фильтрации, возвращающий новый List
        // { it.fullName.lowercase().contains(lower) } - условие фильтрации:
        //   it.fullName - ФИО текущего человека
        //   .lowercase() - приводим к нижнему регистру
        //   .contains(lower) - проверяет содержит ли строка подстроку query
        return peopleList.filter { it.fullName.lowercase().contains(lower) }
    }

    // Публичная функция sortBy() для сортировки списка
    // Принимает field типа SortField (поле для сортировки)
    // Принимает ascending типа Boolean (по умолчанию true)
    // true = по возрастанию, false = по убыванию
    fun sortBy(field: SortField, ascending: Boolean = true) {

        // Сортировка списка
        // peopleList.sortWith(comparator) - метод сортировки с компаратором
        // when (field) - аналог switch-case, выбирает вариант в зависимости от field
        peopleList.sortWith(
            when (field) {
                // Сортировка по имени
                // compareBy { it.fullName.lowercase() } - создаёт компаратор по возрастанию
                // compareByDescending - компаратор по убыванию
                SortField.NAME -> if (ascending)
                    compareBy { it.fullName.lowercase() }
                else
                    compareByDescending { it.fullName.lowercase() }

                // Сортировка по возрасту
                SortField.AGE -> if (ascending)
                    compareBy { it.age }
                else
                    compareByDescending { it.age }

                // Сортировка по дате
                SortField.DATE -> if (ascending)
                    compareBy { it.dateOfBirth }
                else
                    compareByDescending { it.dateOfBirth }
            }
        )
    }

    // Перечисление (enum) полей для сортировки
    // enum class - специальный тип класса с фиксированным набором констант
    enum class SortField { NAME, AGE, DATE }

    // Приватное свойство для хранения таймера автосохранения
    // Timer? - nullable тип (может быть null)
    // Изначально равно null (не инициализирован)
    private var autoSaveTimer: Timer? = null

    // Приватное свойство-флаг состояния автосохранения
    // false = остановлено, true = запущено
    private var isAutoSaveRunning = false

    // Публичная функция startAutoSave() для запуска периодического сохранения
    // Принимает intervalSec типа Long (интервал в секундах)
    fun startAutoSave(intervalSec: Long) {
        // Проверка: если уже запущено, выходим
        if (isAutoSaveRunning) return

        // Установка флага активности
        isAutoSaveRunning = true

        // Создание таймера
        // Timer("AutoSave", true) - конструктор таймера
        // "AutoSave" - имя потока (для отладки)
        // true = daemon поток (не мешает завершению приложения)
        autoSaveTimer = Timer("AutoSave", true)

        // Планирование задачи
        // autoSaveTimer?.schedule(...) - безопасный вызов метода
        // schedule принимает:
        //   1. TimerTask - задача для выполнения
        //   2. initialDelay - задержка перед первым запуском (в миллисекундах)
        //   3. period - интервал между запусками (в миллисекундах)
        autoSaveTimer?.schedule(object : TimerTask() {

            // Переопределение метода run()
            // Этот метод вызывается при каждом срабатывании таймера
            override fun run() {
                // Вызов сохранения
                saveAll()
            }
        }, intervalSec * 1000, intervalSec * 1000)
        // intervalSec * 1000 - перевод секунд в миллисекунды
    }

    // Публичная функция stopAutoSave() для остановки автосохранения
    fun stopAutoSave() {

        // Отмена таймера
        // autoSaveTimer?.cancel() - безопасный вызов метода cancel()
        // cancel() останавливает таймер и отменяет все запланированные задачи
        autoSaveTimer?.cancel()

        // Сброс флага активности
        isAutoSaveRunning = false
    }

    // Публичная функция saveAll() для сохранения всех данных в файл
    fun saveAll() {

        // Запись в файл с буферизацией
        // File(filePath).bufferedWriter() - создаёт буферизованный писатель
        // .use { writer -> ... } - автоматическое закрытие ресурса
        // use() гарантирует вызов close() даже при возникновении исключения
        // writer - параметр лямбды, BufferedWriter для записи
        File(filePath).bufferedWriter().use { writer ->
            // Цикл по всем объектам в списке
            // for (p in peopleList) - цикл for-each
            // p - переменная цикла типа Person
            for (p in peopleList) {
                // Формирование строки в формате CSV
                // "${...}" - строковая интерполяция (template string)
                // Внутри ${} вычисляются выражения и вставляются в строку
                val line = "${p.id}|${p.fullName}|${p.age}|${p.dateOfBirth}|${p.phone}|"

                // Запись строки в файл
                // writer.write(line) - метод записи строки
                writer.write(line)

                // Добавление перевода строки
                // writer.newLine() - метод добавления системного разделителя строк
                // (\n в Linux/Mac, \r\n в Windows)
                writer.newLine()
            }
        }
        // Здесь автоматически вызывается writer.close()
    }
}