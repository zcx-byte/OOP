package com.zxcoop.database.controller
import com.zxcoop.database.model.Person
import com.zxcoop.database.model.PersonDatabase
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.fxml.FXMLLoader
import javafx.scene.control.*
import javafx.scene.control.cell.PropertyValueFactory
import javafx.scene.layout.GridPane
import java.net.URL
import java.time.LocalDate

import java.util.*

/**
 * Класс MainWindowController - контроллер главного окна приложения.
 *
 * Реализует интерфейс Initializable для получения уведомления о завершении загрузки FXML.
 */
class MainWindowController : Initializable {

    // Поле таблицы для отображения данных
    // @FXML - аннотация, указывающая что это поле связано с элементом в FXML
    // private - поле доступно только внутри этого класса
    // lateinit var - отложенная инициализация переменной
    //   lateinit означает, что переменная будет инициализирована позже (JavaFX при загрузке FXML)
    //   var - изменяемая переменная
    // TableView<Person> - таблица, отображающая объекты типа Person
    //   <Person> - дженерик параметр, указывающий тип данных в таблице
    @FXML private lateinit var tableView: TableView<Person>

    // Колонка таблицы для отображения ФИО
    // TableColumn<Person, String> - колонка для объектов Person, отображающая String
    //   Первый параметр: тип объекта в таблице (Person)
    //   Второй параметр: тип данных в колонке (String)
    @FXML private lateinit var colFullName: TableColumn<Person, String>

    // Колонка для возраста
    // TableColumn<Person, Int> - колонка отображает целые числа (Int)
    @FXML private lateinit var colAge: TableColumn<Person, Int>

    // Колонка для даты рождения
    // TableColumn<Person, LocalDate> - колонка отображает даты
    @FXML private lateinit var colDateOfBirth: TableColumn<Person, LocalDate>

    // Колонка для телефона
    @FXML private lateinit var colPhone: TableColumn<Person, String>

    // Поле ввода для поискового запроса
    // TextField - однострочное текстовое поле
    @FXML private lateinit var searchField: TextField

    // Метка (label) для отображения статуса приложения
    // Label - элемент для отображения текста (только чтение)
    @FXML private lateinit var statusLabel: Label

    // Приватное свойство для хранения экземпляра базы данных
    private val db = PersonDatabase()

    // Переопределение метода initialize() из интерфейса Initializable
    // Этот метод вызывается JavaFX автоматически ПОСЛЕ загрузки FXML файла
    // Параметры:
    //   location: URL? - путь к FXML файлу (nullable, может быть null)
    //   resources: ResourceBundle? - ресурсы локализации (не используются)
    // override - ключевое слово, указывающее что мы переопределяем метод родителя
    override fun initialize(location: URL?, resources: ResourceBundle?) {

        // Вызов приватного метода настройки таблицы
        setupTable()

        // Вызов метода загрузки данных из файла
        // db.load() читает people.txt и заполняет peopleList
        db.load()

        // Вызов метода обновления статуса
        // {db.getPeopleList().size} - строковая интерполяция
        //   getPeopleList() возвращает ObservableList<Person>
        //   .size - свойство списка, возвращающее количество элементов (Int)
        updateStatus("Загружено записей: ${db.getPeopleList().size}")
    }

    // Приватный метод настройки колонок таблицы
    private fun setupTable() {

        // Привязка колонки к свойству объекта Person
        // colFullName.setCellValueFactory(...) - устанавливает фабрику значений
        // PropertyValueFactory("fullName") - создаёт фабрику для свойства "fullName"
        //
        // КАК ЭТО РАБОТАЕТ:
        // 1. TableView ищет метод fullNameProperty() в классе Person
        // 2. Подписывается на изменения этого свойства
        // 3. При изменении person.fullName таблица автоматически обновляется
        colFullName.setCellValueFactory(PropertyValueFactory("fullName"))

        // Привязка колонки возраста к свойству "age"
        // PropertyValueFactory найдёт метод ageProperty() в Person
        colAge.setCellValueFactory(PropertyValueFactory("age"))

        // Привязка колонки даты к свойству "dateOfBirth"
        colDateOfBirth.setCellValueFactory(PropertyValueFactory("dateOfBirth"))

        // Привязка колонки телефона к свойству "phone"
        colPhone.setCellValueFactory(PropertyValueFactory("phone"))

        // Добавление всех колонок в таблицу
        // tableView.columns - свойство таблицы, возвращающее ObservableList<TableColumn>
        // .addAll(...) - метод добавления нескольких элементов в список
        // Колонки добавляются в порядке слева направо
        tableView.columns.addAll(colFullName, colAge, colDateOfBirth, colPhone)

        // Привязка таблицы к наблюдаемому списку данных
        // tableView.items - свойство для установки списка данных
        // db.getPeopleList() возвращает ObservableList<Person>
        tableView.items = db.getPeopleList()
    }

    // Обработчик кнопки "Добавить"
    @FXML private fun onAdd() {

        // Блок try-catch для обработки исключений
        // try - код, который может вызвать ошибку
        try {

            // Получение пути к FXML файлу диалога
            // javaClass.classLoader.getResource(...) загружает ресурс из classpath
            // "com/zxcoop/database/person_dialog.fxml" - путь относительно src/main/resources
            // Если слева null, выполняется код справа
            val fxmlLocation = javaClass.classLoader.getResource("com/zxcoop/database/person_dialog.fxml")
                ?: run {

                    // Если файл не найден, показываем ошибку
                    showError("Не найден файл person_dialog.fxml")

                    // return - выход из функции
                    return
                }

            // Создание загрузчика FXML
            // FXMLLoader(fxmlLocation) - конструктор с путём к файлу
            val loader = FXMLLoader(fxmlLocation)

            // Загрузка содержимого FXML
            // loader.load<Any>() - парсит XML и создаёт объекты JavaFX
            // <Any> - любой тип (в данном случае GridPane)
            val dialogContent = loader.load<Any>()

            // Получение контроллера диалога
            // loader.getController() возвращает экземпляр PersonDialogController
            // который указан в FXML как fx:controller
            // : PersonDialogController - явное указание типа
            val dialogController: PersonDialogController = loader.getController()

            // Установка режима добавления в контроллере диалога
            // dialogController.setModeAdd() очищает поля и устанавливает значения по умолчанию
            dialogController.setModeAdd()

            // Создание диалогового окна
            // Dialog<Person> - диалог, возвращающий значение типа Person
            val dialog = Dialog<Person>()

            // Установка заголовка окна
            // dialog.title - свойство заголовка окна (отображается в заголовке окна)
            dialog.title = "Добавление новой записи"

            // Установка текста заголовка внутри диалога
            // dialog.headerText - свойство текста заголовка (под заголовком окна)
            dialog.headerText = "Введите данные нового человека"

            // Добавление кнопок в диалог
            // dialog.dialogPane - панель диалога (содержит все элементы)
            // .buttonTypes - свойство списка типов кнопок
            // .addAll(...) - добавляет кнопки OK и Cancel
            dialog.dialogPane.buttonTypes.addAll(ButtonType.OK, ButtonType.CANCEL)

            // Установка содержимого диалога
            // dialog.dialogPane.content - свойство для установки центрального содержимого
            // dialogContent as? GridPane - безопасное приведение типа
            //   as? пытается привести к GridPane, возвращает null если не получилось
            dialog.dialogPane.content = dialogContent as? GridPane

            // Установка конвертера результата
            // dialog.setResultConverter { ... } - лямбда для преобразования кнопки в результат
            // button - нажатая кнопка (ButtonType)
            dialog.setResultConverter { button ->

                // Проверка: если нажата кнопка OK
                if (button == ButtonType.OK) {

                    // Получаем результат из контроллера диалога
                    // dialogController.getResult() возвращает Person? (может быть null)
                    dialogController.getResult()
                } else {

                    // Если нажата другая кнопка (Cancel), возвращаем null
                    null
                }
            }

            // Показ диалога и ожидание результата
            // dialog.showAndWait() показывает модальное окно и блокирует выполнение
            // Возвращает Optional<Person> (контейнер, который может содержать значение)
            val result = dialog.showAndWait()

            // Обработка результата
            // result.ifPresent { person -> ... } - выполняет код если Optional содержит значение
            // person - параметр лямбды, объект Person из диалога
            result.ifPresent { person ->

                // Добавление человека в базу данных
                // db.addPerson(person) добавляет в ObservableList
                db.addPerson(person)

                // Выделение новой записи в таблице
                // tableView.selectionModel - модель выбора (управляет выделением)
                // .select(person) - выделяет строку с указанным объектом
                tableView.selectionModel.select(person)

                // Прокрутка таблицы к новой записи
                // tableView.scrollTo(person) - прокручивает так, чтобы запись была видна
                tableView.scrollTo(person)

                // Обновление статуса
                updateStatus("Запись добавлена")
            }
        } catch (e: Exception) {

            showError("Ошибка открытия окна: ${e.message}")

            // e.printStackTrace() - печатает полную информацию об ошибке
            e.printStackTrace()
        }
    }

    // Обработчик кнопки "Изменить"
    @FXML private fun onEdit() {

        // Получение выбранной записи
        // tableView.selectionModel.selectedItem - возвращает выбранный элемент или null
        // ?: run { ... } - если null, показываем ошибку и выходим
        val selected = tableView.selectionModel.selectedItem ?: run {
            showError("Выберите запись для редактирования")
            return
        }

        try {
            // Загрузка FXML диалога (аналогично onAdd)
            val fxmlLocation = javaClass.classLoader.getResource("com/zxcoop/database/person_dialog.fxml")
                ?: run { showError("Не найден файл person_dialog.fxml"); return }

            val loader = FXMLLoader(fxmlLocation)
            val dialogContent = loader.load<Any>()
            val dialogController: PersonDialogController = loader.getController()

            // ПЕРЕДАЧА ДАННЫХ В ДИАЛОГ
            // dialogController.setPersonForEdit(selected) передаёт выбранного человека
            // Контроллер диалога заполнит поля текущими значениями
            dialogController.setPersonForEdit(selected)

            // Создание и настройка диалога
            val dialog = Dialog<Person>()
            dialog.title = "Редактирование записи"

            // ${selected.fullName} - вставка ФИО выбранного человека
            dialog.headerText = "Изменение данных: ${selected.fullName}"
            dialog.dialogPane.buttonTypes.addAll(ButtonType.OK, ButtonType.CANCEL)
            dialog.dialogPane.content = dialogContent as? GridPane

            // Установка конвертера результата
            dialog.setResultConverter { button ->
                if (button == ButtonType.OK) {
                    dialogController.getResult()
                } else null
            }

            // Показ диалога
            val result = dialog.showAndWait()

            // Обработка результата
            result.ifPresent { person ->

                // Обновление записи в базе
                // db.updatePerson(person) обновляет существующий объект
                db.updatePerson(person)

                // Обновление отображения таблицы
                // tableView.refresh() - принудительно перерисовывает таблицу
                tableView.refresh()

                // Обновление статуса
                updateStatus("Изменения сохранены")
            }
        } catch (e: Exception) {
            showError("Ошибка открытия окна: ${e.message}")
            e.printStackTrace()
        }
    }

    // Обработчик кнопки "Удалить"
    @FXML private fun onDelete() {
        // Получение выбранной записи или показ ошибки
        val selected = tableView.selectionModel.selectedItem ?: run {
            showError("Выберите запись для удаления")
            return
        }

        // Создание диалога подтверждения
        // Alert(Alert.AlertType.CONFIRMATION, "...") - диалог с вопросом
        // AlertType.CONFIRMATION - тип диалога (с кнопками OK/Cancel)
        val alert = Alert(Alert.AlertType.CONFIRMATION, "Удалить ${selected.fullName}?")

        // Установка заголовка диалога
        alert.title = "Подтверждение"

        // Показ диалога и проверка результата
        // alert.showAndWait() - показывает и ждёт ответа
        // .orElse(ButtonType.CANCEL) - если null, возвращаем Cancel
        // == ButtonType.OK - сравниваем с нажатой кнопкой
        if (alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {

            // Удаление записи из базы
            // db.deletePerson(selected.id) удаляет по ID
            db.deletePerson(selected.id)

            // Обновление статуса
            updateStatus("Удалено")
        }
        // Если нажат Cancel, ничего не происходит
    }

    // Обработчик кнопки "Поиск"
    @FXML private fun onSearch() {

        // Получение текста из поля поиска
        // searchField.text - свойство TextField, возвращает String
        // .trim() - удаляет пробелы в начале и конце
        val query = searchField.text.trim()

        // Проверка: если запрос не пустой
        if (query.isNotEmpty()) {

            // Поиск записей
            // db.searchByName(query) возвращает List<Person> с найденными
            val results = db.searchByName(query)

            // Показ только найденных записей
            // tableView.items = ... - установка нового списка
            // javafx.collections.FXCollections.observableArrayList(results)
            //   создаёт наблюдаемый список из результатов
            tableView.items = javafx.collections.FXCollections.observableArrayList(results)

            // Обновление статуса с количеством
            // ${results.size} - количество найденных записей
            updateStatus("Найдено: ${results.size}")
        } else {

            // Если запрос пустой, показываем все записи
            tableView.items = db.getPeopleList()
            updateStatus("Показаны все")
        }
    }

    // Обработчик кнопки "Сохранить"
    @FXML private fun onSave() {

        // Сохранение всех данных в файл
        // db.saveAll() перезаписывает people.txt
        db.saveAll()
        updateStatus("Сохранено в файл")
    }

    // Обработчик кнопки "Выход"
    @FXML private fun onExit() {

        // Сохранение данных перед выходом
        db.saveAll()

        // Завершение работы JavaFX приложения
        // javafx.application.Platform.exit() - корректно закрывает приложение
        javafx.application.Platform.exit()
    }

    // Приватный вспомогательный метод обновления статуса
    // Принимает msg типа String - новое сообщение
    private fun updateStatus(msg: String) {

        // Установка текста в метку статуса
        // statusLabel.text - свойство Label для установки текста
        statusLabel.text = msg
    }

    // Приватный вспомогательный метод показа ошибки
    // Принимает msg типа String - текст ошибки
    private fun showError(msg: String) {

        // Создание диалога ошибки
        // Alert(Alert.AlertType.ERROR, msg) - диалог с сообщением об ошибке
        // AlertType.ERROR - тип диалога (с иконкой ошибки)
        val alert = Alert(Alert.AlertType.ERROR, msg)

        // Установка заголовка
        alert.title = "Ошибка"

        // Показ и ожидание закрытия
        alert.showAndWait()
    }
}