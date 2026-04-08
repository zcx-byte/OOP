package com.example.mistralchatbot

// JavaFX — для интерфейса
import javafx.application.Platform
import javafx.fxml.FXML
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.ScrollPane
import javafx.scene.control.TextField
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
import javafx.scene.layout.VBox

// Корутины — чтобы интерфейс не зависал во время запроса к ИИ
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// Работа с файлами и временем
import java.io.FileWriter
import java.time.LocalDateTime
import java.io.File

// Контроллер чата — связывает интерфейс (FXML) с логикой бота
// Отвечает за: отображение сообщений, обработку ввода, сохранение истории
class ChatController {

    // @FXML связывает переменные с элементами из hello-view.fxml
    // Это как "мостик" между кодом и визуальной частью
    @FXML private lateinit var userLabel: Label           // Отображает имя пользователя
    @FXML private lateinit var scrollPane: ScrollPane     // Прокручиваемая область чата
    @FXML private lateinit var messagesContainer: VBox    // Контейнер для пузырьков с сообщениями
    @FXML private lateinit var messageField: TextField    // Поле ввода текста
    @FXML private lateinit var sendButton: Button         // Кнопка "Отправить"

    // Данные сессии
    private var userName = ""              // Имя текущего пользователя
    private lateinit var apiKey: String    // API-ключ для доступа к нейросети

    // Создаём бота через интерфейс, можно подменить Bot на другой класс без правок контроллера
    private val bot: IBot = Bot()

    // Добавляет сообщение в чат с разным оформлением для пользователя, бота и системы
    private fun addMessage(who: String, text: String) {

        val label = Label(text)
        label.wrapTextProperty().set(true)  // Включаем перенос длинных строк
        label.maxWidth = 350.0              // Ограничиваем ширину, чтобы текст не растягивал окно
        label.padding = Insets(8.0)         // Отступы внутри "пузырька"

        val box = HBox(label)  // Оборачиваем текст в контейнер для выравнивания

        when (who) {
            "пользователь" -> {

                // Сообщения пользователя: справа, голубой фон
                label.style = "-fx-background-color: #cce5ff; -fx-background-radius: 10;"
                label.alignment = Pos.CENTER_RIGHT
                box.alignment = Pos.CENTER_RIGHT
                HBox.setHgrow(box, Priority.ALWAYS)  // Растягиваем, чтобы прижималось вправо
            }

            "бот" -> {
                // Сообщения бота: слева, светло-серый фон
                label.style = "-fx-background-color: #e2e3e5; -fx-background-radius: 10;"
                label.alignment = Pos.CENTER_LEFT
                box.alignment = Pos.CENTER_LEFT
            }

            else -> {
                // Системные сообщения: по центру, без фона, серым цветом
                label.style = "-fx-background-color: transparent; -fx-text-fill: gray; -fx-font-size: 11px;"
                label.alignment = Pos.CENTER
                box.alignment = Pos.CENTER
            }
        }

        messagesContainer.children.add(box)  // Добавляем сообщение в контейнер
        scrollPane.vvalue = 1.0              // Автопрокрутка вниз к новому сообщению
    }

    // Вызывается из RegistrationController после ввода имени
    // Инициализируем чат: запоминаем пользователя, читаем ключ, очищаем историю
    fun setUserName(name: String) {

        userName = name
        userLabel.text = "Пользователь: $name"
        apiKey = ApiKeyReader.readApiKey()

        // Полностью очищаем файл истории для нового пользователя
        // Пишем пустую строку — файл либо создастся, либо обнулится
        File("history_$userName.txt").writeText("")

        // Приветственное сообщение в чате
        addMessage("система", "Привет, $name! История чата очищена. Напиши 'помощь'.")
    }

    // Удаляет системное сообщение (например, "Печатает..."), если оно ещё висит
    // Нужно, чтобы индикатор не остался висеть, если ответ уже пришёл
    private fun removeSystemMessage(text: String) {
        val last = messagesContainer.children.lastOrNull() as? HBox
        val label = last?.children?.firstOrNull() as? Label
        if (label?.text == text) {
            messagesContainer.children.remove(last)
        }
    }

    // Главный обработчик: срабатывает при нажатии Enter или кнопки "Отправить"
    @FXML
    private fun handleSendButton() {

        val text = messageField.text.trim()
        if (text.isEmpty()) return  // Игнорируем пустые сообщения

        // Показываем сообщение пользователя в чате
        addMessage("пользователь", text)
        messageField.clear()        // Очищаем поле ввода
        blockInput(true)            // Блокируем ввод, пока ждём ответ (чтобы не спамить запросами)

        // --- Цепочка обработки: от простого к сложному ---

        // 1. Сначала проверяем команды (/привет, /время, /курс)
        // Если бот распознал команду — он вернёт ответ, и мы сразу покажем его
        val commandResponse = bot.handleCommand(text, userName)
        if (commandResponse != null) {
            sendAnswer(text, commandResponse)
            return  // Важно: завершаем метод, чтобы не пошло дальше к ИИ
        }

        // 2. Пробуем посчитать математику (2+2, "умножь 5 на 3")
        // Если выражение распознано — показываем результат и выходим
        val mathResult = bot.calculate(text)
        if (mathResult != null) {
            sendAnswer(text, mathResult)
            return
        }

        // 3. Проверяем простые фразы ("как дела", "спасибо")
        // Экономим запросы к платному API, отвечая заранее заготовленными фразами
        val simpleResponse = bot.getSimpleResponse(text)
        if (simpleResponse != null) {
            sendAnswer(text, simpleResponse)
            return
        }


        // 4. Если ничего не подошло — запрашиваем ответ у нейросети
        // Делаем это в фоновом потоке (Dispatchers.IO), чтобы интерфейс не завис
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Показываем индикатор "Печатает..." в главном потоке (UI можно менять только из него)
                Platform.runLater { addMessage("система", "Печатает...") }

                // Сам запрос к API — тяжёлая операция, выполняется в фоне
                val aiAnswer = bot.getAIResponse(text, apiKey)

                // Возвращаемся в главный поток, чтобы обновить интерфейс
                Platform.runLater {
                    removeSystemMessage("Печатает...")  // Убираем индикатор
                    sendAnswer(text, aiAnswer)          // Показываем ответ от ИИ
                }
            } catch (e: Exception) {
                // Если что-то пошло не так (нет интернета, ошибка API) — показываем ошибку, но не крашим приложение
                Platform.runLater {
                    removeSystemMessage("Печатает...")
                    sendAnswer(text, "Ошибка: ${e.message}")
                }
            }
        }
    }

    // Вспомогательный метод: оформляет и показывает ответ бота
    private fun sendAnswer(userText: String, botText: String) {
        addMessage("бот", botText)      // Добавляем сообщение в чат
        saveToFile(userText, botText)   // Сохраняем переписку в файл
        blockInput(false)               // Разблокируем поле ввода для следующего сообщения
    }

    // Блокирует/разблокирует поле ввода и кнопку
    // Нужно, чтобы пользователь не отправил 10 запросов, пока ждёт ответ на первый
    private fun blockInput(block: Boolean) {
        messageField.isDisable = block
        sendButton.isDisable = block
        if (!block) messageField.requestFocus()  // Возвращаем фокус, чтобы удобно было печатать дальше
    }



    // Сохраняет переписку в текстовый файл
    // Формат: дата, кто написал, что написал, разделитель
    // TODO: сделать переменную, которая будет хранить историю чата (добавить в класс бота)
    private fun saveToFile(userMsg: String, botMsg: String) {
        try {
            // appendText — дописывает в конец файла, не стирая старое
            // Используем Kotlin-стиль: File().appendText() вместо громоздкого FileWriter
            File("history_$userName.txt").appendText(
                "${LocalDateTime.now()}\n" +
                        "Вы: $userMsg\n" +
                        "Бот: $botMsg\n" +
                        "${"-".repeat(50)}\n"  // Визуальный разделитель между сообщениями
            )
        } catch (e: Exception) {
            // Если не удалось сохранить (нет прав, диск полон) — логируем, но не ломаем чат
            println("Ошибка сохранения: ${e.message}")
        }
    }
}