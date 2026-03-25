package com.zabguzxcoop.chatbotllm
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileWriter
import java.net.HttpURLConnection
import java.net.URL
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.regex.Pattern

// === Объявление класса ===
// 'class' — ключевое слово Kotlin для создания класса.
// 'ChatBotLogic' — имя класса, по соглашению пишется с заглавной буквы (PascalCase).
// Класс инкапсулирует всю бизнес-логику бота.
class ChatBotLogic {

    // === Свойства класса (поля) ===

    // 'private' — модификатор видимости: поле доступно только внутри этого класса.
    // 'val' — неизменяемая ссылка (read-only property), аналог 'final' в Java.
    // 'mutableListOf<Message>()' — функция стандартной библиотеки Kotlin, создающая изменяемый список.
    // '<Message>' — дженерик-параметр: список хранит только объекты типа Message.
    // Свойство инициализируется сразу при объявлении (не требует конструктора).
    private val messages = mutableListOf<Message>()

    // ': String' — явное указание типа данных (строка).
    // '=' — оператор присваивания.
    // "" — пустая строка, начальное значение.
    private var currentUser: String = ""

    // Создаётся объект файла, но сам файл на диске пока не создаётся (ленивая инициализация).
    // Путь "chat_history.txt" — относительный, отсчитывается от рабочей директории JVM.
    private val historyFile = File("chat_history.txt")

    // Аналогично: файл для сохранения статистики пользователя.
    private val statsFile = File("user_stats.txt")

    // Строковая константа с API-ключом.
    // В реальном проекте ключи хранят в переменных окружения или конфиг-файлах, а не в коде.

    private fun loadApiKeyFromResources(): String {

        // 'javaClass.getResourceAsStream(...)' — загрузка файла из classpath (папки resources).
        // Путь с '/' в начале — абсолютный путь внутри jar/classpath.
        val inputStream = javaClass.getResourceAsStream("/APIKey.txt")

            ?: throw IllegalStateException("Ресурс /APIKey.txt не найден в classpath")

        // 'bufferedReader().readText()' — чтение потока в строку.
        // 'use { ... }' — автозакрытие ресурса после чтения (защита от утечек).
        return inputStream.bufferedReader().use { it.readText().trim() }
    }

    private val apiKey = loadApiKeyFromResources()

    // === Регулярные выражения (шаблоны для распознавания команд) ===

    // 'Pattern' — класс из Java (java.util.regex).
    // 'compile()' — статический метод, компилирует строку-регулярку в объект Pattern для быстрого повторного использования.
    // "\\s+" — экранированный регулярок: один или более пробельных символов.
    // "(\\d+)" — захватывающая группа: одна или более цифр; скобки позволяют извлечь число позже.
    // 'Pattern.CASE_INSENSITIVE' — флаг: игнорировать регистр букв при сравнении.
    private val multiplyPattern = Pattern.compile("умножь\\s+(\\d+)\\s+на\\s+(\\d+)",
        Pattern.CASE_INSENSITIVE)

    // Аналогичный шаблон для команды сложения.
    private val addPattern = Pattern.compile("сложи\\s+(\\d+)\\s+и\\s+(\\d+)",
        Pattern.CASE_INSENSITIVE)

    // Шаблон с оператором '|' (ИЛИ): сработает, если найдено любое из перечисленных словосочетаний.
    // Здесь нет групп захвата (), так как нам не нужно извлекать данные, только факт совпадения.
    private val timePattern = Pattern.compile("который\\s+час|время|сколько\\s+времени",
        Pattern.CASE_INSENSITIVE)

    // Шаблон для запроса статистики.
    private val statsPattern = Pattern.compile("статистика|сколько\\s+сообщений",
        Pattern.CASE_INSENSITIVE)

    // Шаблон для курса валют: группа (доллара|евро|валюты) захватывает конкретное слово.
    private val currencyPattern = Pattern.compile("курс\\s+(доллара|евро|валюты)",
        Pattern.CASE_INSENSITIVE)

    // Шаблон для вызова справки.
    private val helpPattern = Pattern.compile("помощь|help|команды",
        Pattern.CASE_INSENSITIVE)

    /**
     * Публичный метод установки текущего пользователя.
     * 'fun' — ключевое слово Kotlin для объявления функции (от 'function').
     * ': Unit' — возвращаемый тип (аналог 'void' в Java);
     * '(name: String)' — параметр функции: имя 'name', тип 'String'.
     */
    fun setCurrentUser(name: String) {

        // 'currentUser' — свойство класса, '=' — присваивание значения из параметра.
        currentUser = name

        // Вызов приватного метода класса для загрузки истории из файла.
        loadHistory()
    }

    /**
     * Главная публичная функция обработки сообщения.
     * Возвращает String — ответ бота, который будет показан пользователю.
     */
    fun processMessage(text: String): String {

        // 'messages.add(...)' — вызов метода Java-коллекции (ArrayList) для добавления элемента.
        // 'Message(...)' — создание нового объекта данных (предполагается, что класс Message определён).
        messages.add(Message(currentUser, text))

        // 'when' — мощное выражение Kotlin (аналог switch-case, но гибче).
        // Возвращает значение первой ветки, условие которой истинно.
        val response = when {

            // Ветвь 1: Проверка приветствия.
            // 'text.matches(...)' — метод String, проверяет полное совпадение строки с регулярок.
            // 'Regex(...)' — нативный класс регулярных выражений Kotlin.
            // 'RegexOption.IGNORE_CASE' — флаг игнорирования регистра для Regex.
            // '.*' в конце регулярки означает: после приветствия может идти любой текст.
            text.matches(Regex("(привет|здравствуй).*", RegexOption.IGNORE_CASE)) ->

                // Строковый шаблон Kotlin с интерполяцией: $currentUser подставит значение переменной.
                "Привет, $currentUser! Чем могу помочь?"

            // Ветвь 2: Запрос времени.
            // 'timePattern.matcher(text).find()': создаём Matcher, ищем вхождение.
            // '.find()' возвращает Boolean, если шаблон найден в любом месте строки.
            timePattern.matcher(text).find() ->

                // 'LocalDateTime.now()' — получение текущей даты/времени (java.time API).
                // '.format(...)' — форматирование в строку "ЧЧ:мм".
                // 'DateTimeFormatter.ofPattern(...)' — создание форматера по шаблону.
                "Сейчас ${LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm"))}"

            // Ветвь 3: Статистика.
            statsPattern.matcher(text).find() -> {

                // Вызов приватной функции, результат сохраняется в локальную переменную 'stats'.
                // Тип выводится компилятором (type inference), явно указывать не обязательно.
                val stats = loadStats()

                // Интерполяция в строке: ${stats.messagesSent} вычислит и вставит значение.
                "Всего сообщений: ${messages.size}. Вы: ${stats.messagesSent}, Бот: ${stats.messagesReceived}"
            }

            // Ветвь 4: Умножение.
            multiplyPattern.matcher(text).find() -> {

                // Создаём Matcher один раз, чтобы переиспользовать для find() и group().
                val m = multiplyPattern.matcher(text)
                // 'if' — условное выражение в Kotlin: оно возвращает значение (как тернарный оператор ?: в Java).
                if (m.find())

                // 'm.group(1)' — возвращает строку, соответствующую первой группе (\\d+) в регулярке.
                // '.toInt()' — метод-расширение Kotlin для String, преобразует строку в Int.
                // Конкатенация строк через '+'.
                    "${m.group(1).toInt()} * ${m.group(2).toInt()} = " +
                            "${m.group(1).toInt() * m.group(2).toInt()}"
                else

                // Возврат строки-заглушки, если числа не распознаны.
                    "Не распознал числа"
            }

            // Ветвь 5: Сложение (логика идентична умножению).
            addPattern.matcher(text).find() -> {
                val m = addPattern.matcher(text)
                if (m.find()) "${m.group(1).toInt()} + ${m.group(2).toInt()} = " +
                        "${m.group(1).toInt() + m.group(2).toInt()}"
                else "Не распознал числа"
            }

            // Ветвь 6: Курс валют — вызов функции, возвращающей String.
            currencyPattern.matcher(text).find() -> getCurrencyRate()

            // Ветвь 7: Справка — вызов функции, возвращающей многострочный текст.
            helpPattern.matcher(text).find() -> getHelpText()

            // Ветвь 'else': срабатывает, если ни одно условие выше не выполнилось.
            // Вызов LLM API для обработки свободного запроса.
            else -> callLLM(text, "mistral-tiny", "Ты полезный ассистент. Отвечай кратко.")
        }

        // После формирования ответа: добавляем сообщение бота в историю.
        messages.add(Message("Bot", response))

        // Вызов функции сохранения всех данных на диск.
        saveData()

        // 'return' — возврат значения из функции.
        // В Kotlin последняя строка функции может быть возвращаемым значением без 'return',
        // но явное указание улучшает читаемость.
        return response
    }

    /**
     * Приватная функция получения курса валют.
     * ': String' — возвращаемый тип.
     */
    private fun getCurrencyRate(): String {

        // 'try-catch' — обработка исключений (аналог Java).
        // Если в блоке 'try' возникнет ошибка, управление перейдёт в 'catch'.
        return try {

            // 'URL(...).readText()' — цепочка вызовов:
            // 1. Создаётся java.net.URL
            // 2. openConnection() открывает соединение
            // 3. .readText() — функция-расширение Kotlin для InputStream, читает весь поток в String.
            val json = JSONObject(URL("https://www.cbr-xml-daily.ru/daily_json.js").readText())

            // Работа с org.json.JSONObject: парсинг вложенных объектов.
            val valute = json.getJSONObject("Valute")

            // Формирование строки ответа с интерполяцией значений.
            // '.getDouble("Value")' — извлечение числа из JSON.
            "USD: ${valute.getJSONObject("USD").getDouble("Value")} RUB\nEUR: " +
                    "${valute.getJSONObject("EUR").getDouble("Value")} RUB"

        } catch (e: Exception) {

            // 'e.message' — получение текста ошибки из объекта исключения.
            // '$' в строке подставит значение, '${}' позволяет вычислять выражения.
            "Не удалось получить курс: ${e.message}"
        }
    }

    /**
     * Функция вызова LLM через HTTP API.
     * @param message текст сообщения от пользователя
     * @param model название модели для генерации ответа
     * @param systemPrompt системная инструкция для настройки поведения модели
     * @return строка с ответом модели или текстом ошибки
     */
    private fun callLLM(message: String, model: String, systemPrompt: String): String {
        return try {

            // === Формирование массива сообщений для API ===

            // Создаём JSON-массив для хранения последовательности сообщений
            // apply выполняет настройку в контексте объекта и возвращает сам объект
            val messagesArray = JSONArray().apply {

                // Добавляем системное сообщение с инструкцией для модели
                // Роль "system" задаёт контекст и правила поведения ассистента
                put(
                    JSONObject()
                        .put("role", "system")
                        .put("content", systemPrompt)
                )

                // Добавляем сообщение пользователя с его запросом
                // Роль "user" обозначает входные данные от клиента
                put(
                    JSONObject()
                        .put("role", "user")
                        .put("content", message)
                )
            }

            // === Формирование корневого объекта запроса ===

            // Создаём главный JSON-объект с параметрами запроса к API
            val requestJson = JSONObject().apply {
                put("model", model)
                put("messages", messagesArray)
            }

            // === Настройка HTTP-соединения ===

            // Указываем конечную точку API, удаляем лишние пробелы для безопасности
            val apiUrl = "https://api.mistral.ai/v1/chat/completions".trim()

            // Открываем соединение и приводим к типу HttpURLConnection для доступа к HTTP-методам
            val connection = URL(apiUrl).openConnection() as HttpURLConnection

            // === Конфигурация параметров соединения ===

            connection.apply {

                // Устанавливаем метод запроса POST для отправки данных в теле

                requestMethod = "POST"

                // Разрешаем запись данных в поток вывода
                doOutput = true

                // Указываем формат данных запроса как JSON
                setRequestProperty("Content-Type", "application/json")

                // Передаём токен авторизации в заголовке для доступа к API
                setRequestProperty("Authorization", "Bearer $apiKey")

                // Таймаут подключения: максимальное время на установку соединения (10 секунд)
                connectTimeout = 10_000

                // Таймаут чтения: максимальное время ожидания ответа от сервера (30 секунд)
                readTimeout = 30_000

                // Записываем сериализованный JSON в поток отправки
                // use гарантирует закрытие ресурса после завершения операции
                outputStream.use { stream ->
                    val jsonBytes = requestJson.toString().toByteArray(Charsets.UTF_8)
                    stream.write(jsonBytes)
                }
            }

            // === Обработка ответа от сервера ===

            // Проверяем код состояния HTTP: 200 означает успешное выполнение
            if (connection.responseCode == 200) {

                // Читаем тело ответа в строку с автоматическим закрытием потока
                val responseBody = connection.inputStream.bufferedReader().use { reader ->
                    reader.readText()
                }

                // Парсим строку ответа в JSON-объект для извлечения данных
                val responseJson = JSONObject(responseBody)

                // Извлекаем текст ответа из вложенной структуры:
                // choices[0] -> message -> content
                responseJson
                    .getJSONArray("choices")
                    .getJSONObject(0)
                    .getJSONObject("message")
                    .getString("content")

            } else {

                // Возвращаем сообщение об ошибке с кодом состояния для диагностики
                "Ошибка API: код ответа ${connection.responseCode}"
            }

        } catch (e: java.net.SocketTimeoutException) {

            // Обрабатываем таймаут соединения или чтения отдельно для понятного сообщения
            "Таймаут: сервер не ответил вовремя. Проверьте подключение к интернету."

        } catch (e: java.net.UnknownHostException) {

            // Обрабатываем ошибку разрешения имени хоста
            "Не удалось найти сервер. Проверьте адрес API и настройки сети."

        } catch (e: java.net.ConnectException) {

            // Обрабатываем отказ в установке соединения
            "Сервер недоступен. Возможно, он перегружен или блокирует запросы."

        } catch (e: Exception) {

            // Обрабатываем любые другие исключения с выводом типа и сообщения ошибки
            "Ошибка: ${e.javaClass.simpleName} - ${e.message}"
        }
    }

    /**
     * Публичная функция перевода файла через LLM.
     * Возвращает сообщение о результате операции.
     */
    fun translateFile(inputPath: String, sourceLang: String, targetLang: String): String {
        return try {

            // Создание объекта файла по пути.
            val file = File(inputPath)

            // 'if (!condition) return' — ранний выход из функции при ошибке.
            // 'exists()' и 'canRead()' — методы Java File для проверки доступа.
            if (!file.exists() || !file.canRead()) return "Ошибка: файл не доступен"

            // 'readText()' — функция-расширение Kotlin, читает файл целиком в String.
            // указываем с ячной кодировкой
            val content = file.readText(Charsets.UTF_8)

            // Проверка ограничения на размер (защита от переполнения контекста LLM).
            // '_000' — разделитель разрядов для читаемости чисел.
            if (content.length > 10_000) return "Файл слишком большой (макс. 10_000 символов)"

            // Формирование промпта с интерполяцией параметров.
            // '\n' — символ новой строки.
            val prompt = "Переведи текст с $sourceLang на $targetLang. Сохрани форматирование. Текст:\n$content"

            // Вызов LLM с другой моделью ("mistral-small") и системным промптом для переводчика.
            val translated = callLLM(prompt, "mistral-small", "Ты профессиональный переводчик.")

            // Создание объекта выходного файла.
            // 'file.parent' — директория исходного файла.
            // 'file.nameWithoutExtension' — имя файла без расширения (свойство-расширение Kotlin).
            // '$targetLang' — подстановка целевого языка в имя.
            val outFile = File(file.parent, "${file.nameWithoutExtension}_translated_$targetLang.txt")

            // 'writeText()' — функция-расширение Kotlin для записи строки в файл.
            outFile.writeText(translated, Charsets.UTF_8)

            // Возврат успешного сообщения.
            "Готово! Файл сохранён: ${outFile.name}"
        } catch (e: Exception) {

            // Возврат сообщения об ошибке.
            "Ошибка перевода: ${e.message}"
        }
    }

    /**
     * Приватная функция возврата текста справки.
     * Использует многострочную строку (triple-quoted string).
     */
    private fun getHelpText(): String =

    // """ ... """ — многострочная строка в Kotlin, сохраняет переносы и отступы.
    // '.trimIndent()' — метод, удаляющий общий минимальный отступ у каждой строки,
        // чтобы текст был выровнен по левому краю при выводе.
        """
        Доступные команды:
        • "который час" — текущее время
        • "умножь X на Y" / "сложи X и Y" — калькулятор
        • "курс доллара/евро" — курс валют ЦБ РФ
        • "статистика" — счётчик сообщений
        • "помощь" — этот список
        • Любой другой текст — ответ от ИИ (Mistral API)
    """.trimIndent()

    // === Приватные вспомогательные функции для работы с файлами ===

    /**
     * Сохраняет историю сообщений и статистику на диск.
     */
    private fun saveData() {

        // 'FileWriter(historyFile).use { writer -> ... }' — использование ресурса с автозакрытием.
        // 'FileWriter' — Java-класс для записи символов в файл.
        FileWriter(historyFile).use { writer ->

            // 'messages.forEach { ... }' — вызов функции высшего порядка для итерации по коллекции.
            // 'msg' — параметр лямбды, каждый элемент списка.
            messages.forEach { msg ->

                // 'writer.write(...)' — запись строки в файл.
                // '|||' — кастомный разделитель полей для последующего парсинга.
                // '${msg.timestamp}' — вызов toString() у объекта LocalDateTime при интерполяции.
                writer.write("${msg.author}|||${msg.content}|||${msg.timestamp}\n")
            }
        }

        // Обновление и сохранение статистики.
        // 'loadStats().apply { ... }' — загрузили объект, модифицировали его свойства в контексте, вернули.
        val stats = loadStats().apply {

            // 'messages.count { ... }' — функция высшего порядка: считает элементы, удовлетворяющие предикату.
            // 'it.author == currentUser' — лямбда с неявным параметром 'it'.
            messagesSent = messages.count { it.author == currentUser }
            messagesReceived = messages.count { it.author == "Bot" }
        }

        // Запись статистики в файл одной строкой.
        // 'FileWriter(statsFile).use { it.write(...) }' — 'it' это экземпляр FileWriter.
        FileWriter(statsFile).use { it.write("${stats.username}, ${stats.messagesSent}, " +
                "${stats.messagesReceived}, ${stats.firstVisit}\n") }
    }

    /**
     * Загружает историю сообщений из файла в память.
     */
    private fun loadHistory() {

        // Ранний выход, если файл не существует (защита от FileNotFoundException).
        if (!historyFile.exists()) return

        // Очистка списка перед загрузкой (чтобы не дублировать сообщения).
        messages.clear()

        // 'historyFile.forEachLine { line: String -> ... }' — функция-расширение Kotlin,
        // построчно читает файл, вызывая лямбду для каждой строки.
        // ': String' — явное указание типа параметра лямбды (можно опустить, компилятор выведет).
        historyFile.forEachLine { line: String ->

            // 'line.split("|||")' — разбиение строки по разделителю на массив строк.
            val parts = line.split("|||")

            // Проверка: если частей достаточно (>=3), создаём объект сообщения.
            if (parts.size >= 3) {

                // 'LocalDateTime.parse(parts[2])' — парсинг строки даты в объект.
                // 'parts[0]', 'parts[1]' — доступ к элементам массива по индексу.
                messages.add(Message(parts[0], parts[1], LocalDateTime.parse(parts[2])))
            }
            // Если частей < 3, строка пропускается (защита от повреждённых данных).
        }
    }

    /**
     * Загружает или создаёт объект статистики пользователя.
     * Возвращает объект типа UserStats (предполагается, что класс определён).
     */
    private fun loadStats(): UserStats {

        // 'if (statsFile.exists()) { ... } else { ... }' — условное выражение, возвращающее значение.
        // В Kotlin if/else — это выражение, результат которого можно присвоить переменной.
        return if (statsFile.exists()) {

            // Чтение первой строки файла и разбиение по ", ".
            // 'readLines()' — читает все строки в List<String>.
            // 'firstOrNull()' — безопасное получение первого элемента (возвращает null, если список пуст).
            // '?:' — оператор Элвиса: если слева null, возвращается значение справа.
            val parts = statsFile.readLines().firstOrNull()?.split(", ") ?:

            // Если файл пуст или строка не распарсилась — возврат нового объекта с именем пользователя.
            return UserStats(currentUser)

            // Проверка количества частей и создание объекта.
            if (parts.size >= 4) UserStats(parts[0], parts[1].toInt(),
                parts[2].toInt(), parts[3]) else UserStats(currentUser)

        } else {

            // Если файла статистики нет — создаём новый объект с нулевыми значениями.
            UserStats(currentUser)
        }
    }

    /**
     * Публичный геттер для истории сообщений.
     * 'messages.toList()' — создание неизменяемой копии списка (защита инкапсуляции).
     * Внешний код не сможет изменить внутренний список 'messages' через этот метод.
     */
    fun getHistory(): List<Message> = messages.toList()
}