package com.zxcoop.database.model
import javafx.beans.property.*
import java.time.LocalDate

/**
 * Класс Person представляет модель данных - одну запись о человеке.
 *
 * ПАТТЕРН "НАБЛЮДАТЕЛЬ" (Observer Pattern):
 * Класс использует JavaFX Properties вместо обычных типов (Int, String).
 * Properties - это специальные обёртки, которые автоматически уведомляют
 * всех подписчиков (например, ячейки таблицы) об изменении значения.
 *
 * Пример: когда мы меняем person.fullName = "Новое имя",
 * TableView автоматически получает уведомление и обновляет соответствующую ячейку.
 */
class Person {

    // Объявление приватного свойства idProperty типа SimpleIntegerProperty
    // SimpleIntegerProperty - это наблюдаемое свойство целого числа из JavaFX
    // Приватный модификатор (private) означает, что свойство доступно только внутри этого класса
    private val idProperty = SimpleIntegerProperty(0)

    // Объявление приватного свойства fullNameProperty типа SimpleStringProperty
    // SimpleStringProperty - наблюдаемое свойство строки
    // Начальное значение: пустая строка ""
    private val fullNameProperty = SimpleStringProperty("")

    // Объявление приватного свойства ageProperty типа SimpleIntegerProperty
    // Хранит возраст человека
    private val ageProperty = SimpleIntegerProperty(0)

    // Объявление приватного свойства dateOfBirthProperty типа SimpleObjectProperty<LocalDate>
    // SimpleObjectProperty<T> - универсальное наблюдаемое свойство для любого типа T
    // В данном случае T = LocalDate (дата рождения)
    // Угловые скобки <> обозначают дженерик (обобщённый тип)
    private val dateOfBirthProperty = SimpleObjectProperty<LocalDate>()

    // Объявление приватного свойства phoneProperty типа SimpleStringProperty
    // Хранит номер телефона
    private val phoneProperty = SimpleStringProperty("")

    // Публичная функция idProperty(), возвращающая ссылку на само свойство
    // Возвращаемый тип: SimpleIntegerProperty (выводится автоматически)
    // Эта функция нужна для PropertyValueFactory в TableView
    // Когда таблица видит PropertyValueFactory("id"), она ищет метод idProperty()
    fun idProperty() = idProperty

    // Публичная функция fullNameProperty(), возвращающая SimpleStringProperty
    fun fullNameProperty() = fullNameProperty

    // Публичная функция ageProperty(), возвращающая SimpleIntegerProperty
    fun ageProperty() = ageProperty

    // Публичная функция dateOfBirthProperty(), возвращающая SimpleObjectProperty<LocalDate>
    fun dateOfBirthProperty() = dateOfBirthProperty

    // Публичная функция phoneProperty(), возвращающая SimpleStringProperty
    fun phoneProperty() = phoneProperty

    // Объявление свойства id типа Int с геттером и сеттером
    // var - изменяемое свойство (в отличие от val - неизменяемое)
    // Тип указан после двоеточия: Int
    var id: Int

        // Геттер: функция получения значения
        // get() вызывается при чтении свойства: val x = person.id
        // Возвращает значение, хранящееся внутри idProperty через метод get()
        get() = idProperty.get()

        // Сеттер: функция установки значения
        // set(value) вызывается при записи: person.id = 5
        // value - автоматически созданное имя для нового значения
        // set() вызывает уведомление всех наблюдателей об изменении
        set(value) = idProperty.set(value)

    // Объявление свойства fullName типа String
    var fullName: String

        // Геттер возвращает строку из fullNameProperty
        get() = fullNameProperty.get()

        // Сеттер обновляет fullNameProperty и уведомляет наблюдателей
        set(value) = fullNameProperty.set(value)

    // Объявление свойства age типа Int
    var age: Int

        // Геттер возвращает возраст
        get() = ageProperty.get()

        // Сеттер устанавливает новый возраст
        set(value) = ageProperty.set(value)

    // Объявление свойства dateOfBirth типа LocalDate?
    // Знак вопроса ? означает, что свойство может быть null (пустым)
    // Это называется "nullable type" в Kotlin
    var dateOfBirth: LocalDate?

        // Геттер возвращает дату или null
        get() = dateOfBirthProperty.get()

        // Сеттер устанавливает новую дату
        set(value) = dateOfBirthProperty.set(value)

    // Объявление свойства phone типа String
    var phone: String

        // Геттер возвращает телефон
        get() = phoneProperty.get()

        // Сеттер устанавливает новый телефон
        set(value) = phoneProperty.set(value)

    // Вторичный конструктор с параметрами для создания заполненного объекта
    // Параметры: id (Int, по умолчанию 0), fullName (String), age (Int),
    //            dateOfBirth (LocalDate?), phone (String)
    // Значение по умолчанию (= 0) означает, что параметр можно не указывать при вызове
    constructor(id: Int = 0, fullName: String, age: Int, dateOfBirth: LocalDate?, phone: String) {

        // this.id обращается к свойству id через сеттер
        // this ссылается на текущий экземпляр объекта
        this.id = id

        // Присваиваем ФИО через сеттер (чтобы сработало уведомление)
        this.fullName = fullName

        // Присваиваем возраст
        this.age = age

        // Присваиваем дату рождения
        this.dateOfBirth = dateOfBirth

        // Присваиваем телефон
        this.phone = phone
    }

    // Публичная функция isValid(), возвращающая Boolean
    // Проверяет корректность данных человека
    fun isValid(): Boolean {

        // dateOfBirth != null - проверка, что дата не пустая
        return fullName.trim().length >= 3 && age in 0..150 && dateOfBirth != null
    }
}