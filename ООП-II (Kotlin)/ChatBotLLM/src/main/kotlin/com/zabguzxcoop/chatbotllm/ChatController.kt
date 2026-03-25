package com.zabguzxcoop.chatbotllm

import javafx.application.Platform
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.control.*
import javafx.scene.input.KeyCode
import javafx.scene.layout.VBox
import javafx.stage.FileChooser
import javafx.stage.Stage
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

    /**
 * Контроллер для управления чат-интерфейсом.
 * Связывает UI (FXML) с бизнес-логикой (ChatBotLogic).
 *
 * В JavaFX контроллер — это обычный класс, методы и свойства которого
 * привязываются к элементам интерфейса через аннотации @FXML.
 */
class ChatController {

    // === Инъекция зависимостей из FXML ===

    // '@FXML' — аннотация, указывающая загрузчику FXML, что это поле должно быть
    // автоматически заполнено ссылкой на элемент из XML-разметки по совпадению 'fx:id'.
    // Без этой аннотации поле осталось бы null, и доступ к нему вызвал бы NullPointerException.

    // 'private' — модификатор видимости: поле доступно только внутри этого класса.
    // 'lateinit var' — специальная конструкция Kotlin для отложенной инициализации.
    // 'lateinit' (late initialization) означает: "мы обещаем инициализировать это свойство
    // позже, до первого обращения к нему". Это нужно, потому что JavaFX создаёт объект
    // контроллера через рефлексию, а поля заполняет уже после вызова конструктора.
    // Нельзя использовать 'lateinit' с примитивными типами (Int, Boolean) — только с ссылочными.
    @FXML private lateinit var chatArea: VBox           // Вертикальный контейнер для сообщений

    @FXML private lateinit var messageField: TextField  // Однострочное поле ввода текста

    @FXML private lateinit var chatScrollPane: ScrollPane // Область прокрутки для чата

    // === Свойства для хранения состояния ===

    // 'lateinit var' — инициализируются программно в методах setUsername/setPrimaryStage,
    // а не через FXML-инъекцию.
    private lateinit var botLogic: ChatBotLogic   // Экземпляр логики бота
    private lateinit var currentUser: String      // Имя текущего пользователя
    private lateinit var primaryStage: Stage      // Главное окно приложения (Stage)

    /**
     * Метод инициализации, вызываемый автоматически после загрузки FXML-файла.
     * '@FXML' — аннотация не обязательна здесь, но указывает, что метод может быть
     * привязан к событию 'onInitialize' в разметке.
     * 'fun' — ключевое слово для объявления функции в Kotlin.
     * Возвращаемый тип не указан явно — компилятор выводит 'Unit' (аналог void).
     */
    @FXML
    fun initialize() {

        // 'setOnKeyPressed { ... }' — установка обработчика события нажатия клавиши.
        // '{ event -> ... }' — лямбда-выражение (анонимная функция) с параметром 'event'.
        // В Kotlin, если лямбда — последний параметр функции, её можно вынести за скобки.
        messageField.setOnKeyPressed { event ->

            // 'event.code' — получение кода нажатой клавиши (перечисление KeyCode).
            // 'KeyCode.ENTER' — константа, представляющая клавишу Enter.
            // '==' — оператор сравнения, возвращает Boolean.
            if (event.code == KeyCode.ENTER)

            // Если условие истинно, вызываем функцию отправки сообщения.
            // В Kotlin можно опустить фигурные скобки для однострочных тел if/функций.
                sendMessage()
        }
    }

    /**
     * Публичный метод, вызываемый из LoginController после успешной аутентификации.
     * '@param name' — строка с именем пользователя, переданная извне.
     */
    fun setUsername(name: String) {
        // Присваивание значения в свойство класса.
        // Так как свойство объявлено как 'lateinit', это его первая инициализация.
        currentUser = name

        // 'ChatBotLogic()' — создание нового экземпляра класса логики через конструктор.
        // В реальном проекте здесь могла бы быть внедрённая зависимость (DI).
        botLogic = ChatBotLogic()

        // Вызов метода инициализации пользователя в логике бота.
        // Точка '.' — оператор вызова метода или доступа к свойству.
        botLogic.setCurrentUser(name)

        // Вызов приватного метода для добавления системного сообщения в интерфейс.
        addSystemMessage("Добро пожаловать, $name!")
        // '$name' — строковая интерполяция: значение переменной подставляется в строку.
    }

    /**
     * Сохраняет ссылку на главное окно приложения.
     * Нужно для отображения диалоговых окон (FileChooser, Dialog) с привязкой к окну.
     */
    fun setPrimaryStage(stage: Stage) {
        primaryStage = stage
    }

    /**
     * Публичный обработчик кнопки "Отправить" из FXML-разметки.
     * '= sendMessage()' — выражение-тело функции (expression body).
     * В Kotlin, если функция состоит из одного выражения, можно не писать фигурные скобки
     * и ключевое слово 'return', а сразу вернуть результат выражения после '='.
     */
    @FXML
    private fun handleSendButton() = sendMessage()

    /**
     * Приватная функция отправки сообщения.
     * Не принимает параметров и не возвращает значения (возвращает Unit).
     */
    private fun sendMessage() {

        // 'messageField.text' — обращение к свойству 'text' объекта TextField.
        // '.trim()' — вызов функции-расширения Kotlin для String, удаляющей
        // пробельные символы в начале и конце строки.
        val text = messageField.text.trim()

        // Ранний выход из функции, если строка пустая.
        // 'if (condition) return' — идиома "guard clause", уменьшающая вложенность кода.
        if (text.isEmpty()) return

        // 1. Немедленное отображение сообщения пользователя в интерфейсе
        addUserMessage(text)

        // Очистка поля ввода для следующего сообщения.
        messageField.clear()

        // 2. Выполнение "тяжёлой" операции в отдельном потоке.
        // 'Thread { ... }' — создание нового потока выполнения через лямбду.
        // В Kotlin конструктор Thread может принимать лямбду как реализацию run().
        // Это нужно, чтобы сетевой запрос к API не блокировал главный поток JavaFX,
        // иначе интерфейс "зависнет" до получения ответа.
        Thread {

            // Вызов блокирующего метода обработки сообщения (сеть, JSON, файл).
            val response = botLogic.processMessage(text)

            // 3. Возврат в главный поток JavaFX для обновления UI.
            // В JavaFX все изменения интерфейса ДОЛЖНЫ выполняться в JavaFX Application Thread.
            // 'Platform.runLater { ... }' — помещает лямбду в очередь событий главного потока.
            // Код внутри будет выполнен асинхронно, когда главный поток освободится.
            Platform.runLater {

                // Добавление ответа бота в интерфейс.
                addBotMessage(response)
            }
        }.start() // Метод 'start()' запускает новый поток (выполняет код в лямбде).
    }

    // === Методы для отображения сообщений в UI ===

    /**
     * Добавляет сообщение пользователя в чат.
     * '@param text' — текст сообщения.
     */
    private fun addUserMessage(text: String) {

        // 'chatArea.children' — свойство, возвращающее наблюдаемый список дочерних узлов.
        // '.add(...)' — добавление нового элемента в конец списка.
        // 'createMessageBubble(text, isUser = true)' — вызов функции с именованным аргументом.
        // 'isUser = true' — явное указание имени параметра улучшает читаемость,
        // особенно когда у функции несколько параметров одного типа.
        chatArea.children.add(createMessageBubble(text, isUser = true))

        // Прокрутка скролла вниз, чтобы показать новое сообщение.
        scrollToBottom()
    }

    /**
     * Добавляет сообщение бота в чат (аналогично, но isUser = false).
     */
    private fun addBotMessage(text: String) {
        chatArea.children.add(createMessageBubble(text, isUser = false))
        scrollToBottom()
    }

    /**
     * Добавляет системное уведомление (серый курсивный текст).
     */
    private fun addSystemMessage(text: String) {

        // 'Label(text)' — создание объекта метки с текстом.
        // '.apply { ... }' — scope-функция Kotlin: выполняет блок кода в контексте объекта
        // (внутри блока 'this' ссылается на Label) и возвращает сам объект.
        // Это позволяет настроить объект в момент создания без создания отдельной переменной.
        val label = Label(text).apply {

            // 'style' — свойство для установки инлайн CSS-стилей JavaFX.
            // Строка в кавычках содержит пары "свойство: значение", разделённые точкой с запятой.
            style = "-fx-text-fill: gray; -fx-font-style: italic;"
        }

        // Добавление настроенной метки в контейнер чата.
        chatArea.children.add(label)
    }

    /**
     * Фабричный метод создания стилизованного контейнера сообщения ("пузыря").
     * '@param text' — содержимое сообщения.
     * '@param isUser' — флаг, определяющий, кто отправил сообщение (для стилей).
     * '@return VBox' — контейнер с текстом и временем, готовый к добавлению в чат.
     */
    private fun createMessageBubble(text: String, isUser: Boolean): VBox {

        // 'VBox(10.0)' — создание вертикального контейнера с отступом 10 пикселей между детьми.
        // '.apply { ... }' — настройка контейнера и возврат его как результата функции.
        return VBox(10.0).apply {

            // Формирование строки стилей.
            // Базовые стили (радиус, отступы) одинаковы для всех сообщений.
            // Оператор '+' — конкатенация (склеивание) строк.
            style = "-fx-background-radius: 10; -fx-padding: 10; -fx-max-width: 400;" +
                    // Условное выражение if/else в Kotlin возвращает значение.
                    // Здесь мы выбираем один из двух фрагментов CSS в зависимости от флага.
                    // Это позволяет избежать дублирования кода и писать компактные условия.
                    if (isUser) " -fx-background-color: #0078d7; -fx-alignment: center-right;"
                    else " -fx-background-color: #e0e0e0; -fx-alignment: center-left;"

            // 'children.addAll(...)' — добавление нескольких элементов в список детей.
            // Метод принимает переменное число аргументов (vararg).
            children.addAll(

                // Первый ребёнок: метка с текстом сообщения.
                Label(text).apply {

                    // 'isWrapText' — свойство Label: разрешает перенос длинного текста на новую строку.
                    // В JavaBean-стиле это setWrapText(true), в Kotlin — прямое присваивание свойству.
                    isWrapText = true

                    // Динамический выбор цвета текста в зависимости от отправителя.
                    style = if (isUser) "-fx-text-fill: white;" else "-fx-text-fill: black;"
                },

                // Второй ребёнок: метка со временем отправки.
                Label(

                    // Вложенное выражение: получаем текущее время и форматируем его.
                    // 'LocalDateTime.now()' — статический метод получения текущего момента.
                    // '.format(...)' — применение форматера для преобразования в строку.
                    // 'DateTimeFormatter.ofPattern("HH:mm")' — создание форматера по шаблону
                    // (часы:минуты, 24-часовой формат).
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm"))
                ).apply {

                    // Стиль для времени: мелкий шрифт, цвет зависит от автора.
                    // '${...}' — интерполяция выражения: вычисляем цвет и подставляем в строку.
                    style = "-fx-font-size: 10px; -fx-text-fill: ${if (isUser) "lightblue" else "gray"};"
                }
            )
        }
        // Явный 'return' не нужен: результат 'apply' (сам VBox) автоматически возвращается.
    }

    /**
     * Прокручивает область просмотра чата в самый низ.
     * Обёрнута в Platform.runLater для гарантии выполнения в правильном потоке.
     */
    private fun scrollToBottom() {

        // 'Platform.runLater { ... }' — асинхронное выполнение в UI-потоке.
        // Даже если метод вызван из UI-потока, runLater гарантирует безопасность.
        Platform.runLater {

            // 'chatScrollPane.vvalue' — свойство вертикальной прокрутки (от 0.0 до 1.0).
            // 1.0 означает "прокручено до самого конца".
            chatScrollPane.vvalue = 1.0
        }
    }

    // === Обработчики для функции перевода файлов ===

    /**
     * Обработчик кнопки/пункта меню "Перевести файл".
     * '@FXML' — метод привязан к элементу интерфейса в FXML-разметке.
     */
    @FXML
    private fun handleTranslateFile() {

        // 'FileChooser()' — создание диалога выбора файла.
        // '.apply { ... }' — настройка диалога перед показом.
        val file = FileChooser().apply {

            // Установка заголовка окна диалога.
            title = "Выберите файл"

            // 'extensionFilters' — список фильтров по расширениям файлов.
            // '.add(...)' — добавление нового фильтра.
            // 'FileChooser.ExtensionFilter(...)' — конструктор фильтра: название и паттерн.
            // "*.txt" — маска: показать только файлы с расширением .txt.
            extensionFilters.add(FileChooser.ExtensionFilter("Text Files", "*.txt"))

            // 'showOpenDialog(primaryStage)' — модальный показ диалога.
            // Метод возвращает выбранный 'File' или 'null', если пользователь нажал "Отмена".
            // '?: return' — оператор Элвиса: если слева null, выполняется правая часть (выход из функции).
            // Это компактная замена конструкции 'if (file == null) return'.
        }.showOpenDialog(primaryStage) ?: return

        // Если файл выбран (код ниже выполнится только если file != null),
        // открываем диалог настройки перевода.
        showTranslateDialog(file)
    }

    /**
     * Показывает диалоговое окно с настройками перевода.
     * '@param file' — файл, выбранный пользователем для перевода.
     */
        /**
         * Показывает диалоговое окно с настройками перевода.
         * После нажатия ОК или Отмена окно закрывается автоматически.
         */
        private fun showTranslateDialog(file: File) {
            try {
                val fxmlPath = "/com/zabguzxcoop/chatbotllm/translate-dialog.fxml"
                val loader = FXMLLoader(javaClass.getResource(fxmlPath))
                    ?: throw IllegalStateException("Не найдено: $fxmlPath")

                val root: Parent = loader.load()
                val dialogController = loader.getController<TranslateDialogController>()
                dialogController.setFile(file)

                // Создаём и настраиваем диалог
                Dialog<ButtonType>().apply {
                    title = "Перевод файла"
                    headerText = "Настройки"
                    dialogPane.content = root
                    dialogPane.buttonTypes.addAll(ButtonType.OK, ButtonType.CANCEL)

                    // showAndWait() — блокирующий вызов:
                    // 1. Показывает окно
                    // 2. Ждёт нажатия кнопки
                    // 3. Автоматически закрывает окно
                    // 4. Возвращает тип нажатой кнопки
                    val result = showAndWait()

                    // Выполняем перевод ТОЛЬКО если нажали ОК
                    if (result.isPresent && result.get() == ButtonType.OK) {
                        translateFile(
                            file,
                            dialogController.getSourceLanguage(),
                            dialogController.getTargetLanguage()
                        )
                    }
                    // Если нажали Cancel или закрыли крестиком — просто выходим,
                    // окно уже закрыто, никаких действий не требуется
                }

            } catch (e: Exception) {
                addSystemMessage("Ошибка инициализации: ${e.message}")
            }
        }

    /**
     * Запускает процесс перевода файла в фоновом потоке.
     * '@param file' — исходный файл.
     * '@param src' — код исходного языка.
     * '@param tgt' — код целевого языка.
     */
    private fun translateFile(file: File, src: String, tgt: String) {

        // Информирование пользователя о начале операции.
        addSystemMessage("Перевод: ${file.name} ($src → $tgt)...")

        // Запуск фонового потока для выполнения блокирующей операции (API-запрос).
        Thread {
            try {
                val result = botLogic.translateFile(file.absolutePath, src, tgt)

                // Возврат в UI-поток
                Platform.runLater {
                    addBotMessage(result)
                    addSystemMessage("✓ Перевод завершён")
                }
            } catch (e: Exception) {
                // Обработка ошибки в фоновом потоке + возврат в UI для показа
                Platform.runLater {
                    addSystemMessage("✗ Ошибка перевода: ${e.message}")
                }
                // Опционально: логирование для отладки
                e.printStackTrace()
            }
        }.apply {
            isDaemon = true // Поток не помешает закрытию приложения
            start()
        }
    }
}