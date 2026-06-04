package com.zxcoop.database.controller
import com.zxcoop.database.model.Person
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.*
import java.net.URL
import java.time.LocalDate
import java.util.*

/**
 * Класс PersonDialogController - контроллер диалогового окна.
 *
 * ЖИЗНЕННЫЙ ЦИКЛ:
 * 1. MainWindowController загружает FXML через FXMLLoader
 * 2. JavaFX создаёт экземпляр PersonDialogController
 * 3. Вызывается initialize()
 * 4. MainWindowController вызывает setModeAdd() или setPersonForEdit()
 * 5. Пользователь вводит данные и нажимает OK/Cancel
 * 6. MainWindowController вызывает getResult() для получения результата
 */
class PersonDialogController : Initializable {

    // Поле ввода ФИО
    // lateinit var - будет инициализировано JavaFX при загрузке FXML
    @FXML private lateinit var nameField: TextField

    // Поле ввода возраста (спиннер)
    // Spinner<Int> - поле выбора числа с кнопками +/-
    // <Int> - тип значения (целое число)
    @FXML private lateinit var ageField: Spinner<Int>

    // Поле выбора даты
    // DatePicker - календарь для выбора даты
    @FXML private lateinit var dateField: DatePicker

    // Поле ввода телефона
    @FXML private lateinit var phoneField: TextField

    // Приватное поле режима редактирования
    // Boolean: true = редактирование, false = добавление
    // Инициализация false по умолчанию
    private var isEditMode = false

    // Приватное поле для хранения редактируемого человека
    // Person? - nullable тип (может быть null при добавлении)
    // Инициализация null по умолчанию
    private var editingPerson: Person? = null

    // Метод инициализации, вызываемый после загрузки FXML
    override fun initialize(location: URL?, resources: ResourceBundle?) {

        // Настройка фабрики значений для спиннера
        // ageField.valueFactory - свойство для установки фабрики
        // SpinnerValueFactory.IntegerSpinnerValueFactory(min, max, initialValue)
        //   Создаёт фабрику для целых чисел
        //   0 - минимальное значение
        //   150 - максимальное значение
        //   18 - начальное значение
        ageField.valueFactory = SpinnerValueFactory.IntegerSpinnerValueFactory(0, 150, 18)
    }

    // Публичный метод установки режима добавления
    // Вызывается из MainWindowController при нажатии "Добавить"
    fun setModeAdd() {

        // Установка флага в режим добавления
        isEditMode = false

        // Очистка поля ФИО
        // nameField.clear() - метод TextField, удаляет весь текст
        nameField.clear()

        // Установка возраста по умолчанию
        // ageField.valueFactory?.value - безопасный доступ к значению
        // ? - если valueFactory null, не выполняем присваивание
        ageField.valueFactory?.value = 18

        // Установка даты по умолчанию (18 лет назад)
        // LocalDate.now() - текущая дата
        // .minusYears(18) - вычитает 18 лет
        dateField.value = LocalDate.now().minusYears(18)

        // Очистка поля телефона
        phoneField.clear()
    }

    // Публичный метод установки данных для редактирования
    // Принимает person типа Person - человека для редактирования
    fun setPersonForEdit(person: Person) {

        // Установка флага в режим редактирования
        isEditMode = true

        // Сохранение ссылки на редактируемого человека
        // editingPerson = person - сохраняем для последующего обновления
        editingPerson = person

        // Заполнение поля ФИО текущим значением
        // nameField.text = ... - установка текста в TextField
        nameField.text = person.fullName

        // Заполнение спиннера возрастом
        // ageField.valueFactory?.value - установка значения спиннера
        ageField.valueFactory?.value = person.age

        // Заполнение поля даты
        // dateField.value - свойство DatePicker для установки даты
        dateField.value = person.dateOfBirth

        // Заполнение поля телефона
        phoneField.text = person.phone
    }

    // Публичный метод получения результата
    // Возвращает Person? (Person или null)
    // null возвращается если:
    //   - Нажата кнопка Cancel
    //   - Данные не прошли валидацию
    fun getResult(): Person? {

        // Проверка режима работы
        // if (editingPerson != null) - если не null, значит режим редактирования
        if (editingPerson != null) {
            // РЕЖИМ РЕДАКТИРОВАНИЯ

            // Обновление данных редактируемого человека из полей ввода
            // editingPerson!! - безопасное извлечение (проверили что не null)
            // !! выбрасывает ошибку если null
            editingPerson!!.fullName = nameField.text
            editingPerson!!.age = ageField.value ?: 0
            // ageField.value ?: 0 - если value null, используем 0

            editingPerson!!.dateOfBirth = dateField.value
            editingPerson!!.phone = phoneField.text

            // Возврат объекта если валиден, иначе null
            // if (editingPerson!!.isValid()) - проверка валидности
            // editingPerson - возвращаем объект если true
            // else null - возвращаем null если false
            return if (editingPerson!!.isValid()) editingPerson else null
        } else {
            // РЕЖИМ ДОБАВЛЕНИЯ

            // Создание нового объекта Person из полей ввода
            // Person(...) - вызов конструктора
            val newPerson = Person(
                fullName = nameField.text,
                age = ageField.value ?: 0,
                dateOfBirth = dateField.value,
                phone = phoneField.text
            )

            // Возврат нового объекта если валиден, иначе null
            return if (newPerson.isValid()) newPerson else null
        }
    }
}