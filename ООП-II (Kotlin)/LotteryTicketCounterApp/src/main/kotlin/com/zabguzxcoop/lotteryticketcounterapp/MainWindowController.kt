package com.zabguzxcoop.lotteryticketcounterapp

import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TextArea
import javafx.scene.control.TextField
import kotlin.random.Random

class MainWindowController {

    // Поля интерфейса, связанные с элементами из FXML через атрибут fx:id
    // Ключевое слово lateinit позволяет объявить не nullable-поле без немедленной инициализации
    // Поле будет инициализировано фреймворком до первого использования, поэтому проверка на null не требуется
    @FXML private lateinit var lottaryName: TextField
    @FXML private lateinit var ticketcirculation: TextField
    @FXML private lateinit var availableTicket: TextField
    @FXML private lateinit var statusArea: TextArea
    @FXML private lateinit var resultArea: TextArea

    // Единственная ссылка (класс - ссылочный тип) лотереи, с которым работает приложение
    // currentLottery - сслыка на объект типа LotteryTicketCounterClass
    // Тип LotteryTicketCounterClass? означает, что переменная может хранить ссылку на объект или null
    // Изначально присваиваем null, так как лотерея ещё не создана пользователем
    // Используем var, потому что ссылка будет изменяться: сначала null, затем созданный объект, возможно — новый при перезагрузке
    private var currentLottery: LotteryTicketCounterClass? = null

    /**
     * Метод вызывается при нажатии кнопки "Добавить билеты".
     * Связь с кнопкой в FXML осуществляется через атрибут onAction="#onaddTicketsButtonClick".
     * Аннотация @FXML обязательна для методов, вызываемых из FXML.
     *
     * Логика метода изменена: теперь он либо создаёт единственный объект лотереи,
     * либо обновляет параметры уже существующего объекта.
     */
    @FXML
    private fun onaddTicketsButtonClick() {

        // Метод trim() удаляет пробельные символы с начала и конца строки
        // Это защита от случайных пробелов, введённых пользователем
        val name = lottaryName.text.trim()
        val circulation = ticketcirculation.text.trim()
        val countText = availableTicket.text.trim()

        // Проверка на пустые поля с использованием логического ИЛИ (||)
        // Если хотя бы одно поле пустое, выводим сообщение об ошибке и прерываем выполнение через return
        if (name.isEmpty() || circulation.isEmpty() || countText.isEmpty()) {
            statusArea.text = "Ошибка: заполните все поля"
            return
        }

        // Преобразование строки в число с безопасной обработкой ошибок
        // Метод toIntOrNull() возвращает null вместо исключения при невозможности преобразования
        // Это предпочтительнее использования toInt(), который выбрасывает NumberFormatException
        val count = countText.toIntOrNull()
        if (count == null || count <= 0) {
            statusArea.text = "Ошибка: количество должно быть числом больше 0"
            return
        }

        // Логика работы с единственным объектом:
        // Если объект ещё не создан — создаём его, иначе — обновляем существующий
        if (currentLottery == null) {
            // Создание объекта класса с использованием конструктора
            // Параметры передаются в том же порядке, как объявлены в конструкторе класса LotteryTicketCounterClass
            currentLottery = LotteryTicketCounterClass(name, circulation, count)
            statusArea.text = "Создана новая лотерея: $name (тираж $circulation), билетов: $count"
        } else {
            // Обновление параметров существующего объекта через методы-сеттеры
            // Для корректной работы в классе LotteryTicketCounterClass должны быть реализованы соответствующие методы
            currentLottery?.setLotteryName(name)
            currentLottery?.setTicketСirculation(circulation)
            currentLottery?.setTickets(count) // Метод для установки нового количества билетов

            statusArea.text = "Летерея создана: $name (тираж $circulation), билетов: $count"
        }

        // Формирование строки с использованием строкового шаблона (string template)
        // Синтаксис $имяПеременной или ${выражение} позволяет встраивать значения прямо в строку
        // Это альтернатива конкатенации через оператор + и более читаема
        // Используем безопасный вызов ?.toString(), так как currentLottery может быть null
        resultArea.appendText("${currentLottery?.toString()}\n")

        // Очистка полей ввода через метод clear()
        lottaryName.clear()
        ticketcirculation.clear()
        availableTicket.clear()

        // Установка фокуса ввода на первое поле для удобства пользователя
        lottaryName.requestFocus()
    }

    /**
     * Метод вызывается при нажатии кнопки "Выдать случайный билет".
     * Работает с единственным объектом лотереи: генерирует случайный номер билета
     * и уменьшает количество доступных билетов через метод sellTickets().
     */
    @FXML
    private fun onTakeRandTicketButtonClick() {

        // Проверка, создан ли объект лотереи
        // Используем safe call (?) для безопасного обращения к nullable-переменной
        val lottery = currentLottery

        if (lottery == null) {
            statusArea.text = "Ошибка: лотерея не создана"
            return
        }

        // Проверка наличия доступных билетов через геттер
        // Если билетов нет — выводим сообщение и прерываем выполнение
        if (lottery.getAvailableTicket() <= 0) {
            statusArea.text = "Ошибка: все билеты уже выданы"
            return
        }

        // Генерация случайного целого числа в диапазоне [от, до)
        // Конструктор Random.nextInt(от, до) возвращает число от "от" включительно до "до" исключительно
        // Поэтому для включения верхней границы добавляем 1 к количеству билетов
        // Например, если билетов 10, то диапазон будет [1, 11), что даёт числа от 1 до 10 включительно
        val ticketNumber = Random.nextInt(1, lottery.getAvailableTicket() + 1)

        // Вызов метода класса для уменьшения счётчика доступных билетов
        // Метод sellTickets() реализован в классе и содержит внутреннюю валидацию
        // После вызова количество доступных билетов уменьшится на 1
        // Важно: мы изменяем состояние того же самого объекта, ссылка на который хранится в currentLottery
        lottery.sellTickets(1)

        // Формирование статусного сообщения с использованием строковых шаблонов
        statusArea.text = "Выдан билет №$ticketNumber из лотереи '${lottery.getLotteryName()}' " +
                "(тираж ${lottery.getTicketcirculation()})"

        // Добавление записи в историю операций с указанием всех параметров билета
        resultArea.appendText("Выдан билет: Лотерея='${lottery.getLotteryName()}', " +
                "Тираж=${lottery.getTicketcirculation()}, Билет №$ticketNumber\n")
    }

    /**
     * Метод вызывается при нажатии кнопки "Показать 10 выйгрышных билетов".
     * Генерирует 10 случайных номеров билетов из единственной лотереи.
     * Каждый номер генерируется независимо, дубликаты возможны.
     * Количество билетов не уменьшается — метод только отображает информацию.
     */
    @FXML
    private fun onwiewRandTicketsButtonClick() {

        // Проверка, создан ли объект лотереи
        val lottery = currentLottery

        if (lottery == null) {
            statusArea.text = "Ошибка: лотерея не создана"
            return
        }

        // Проверка наличия доступных билетов для генерации
        if (lottery.getAvailableTicket() <= 0) {
            statusArea.text = "Ошибка: все билеты уже выданы"
            return
        }

        // Добавление разделителя в историю операций перед выводом списка
        // Символ \n создаёт новую строку
        resultArea.appendText("\nТОП-10 ВЫИГРЫШНЫХ БИЛЕТОВ:\n")

        // Цикл repeat выполняет блок кода указанное количество раз
        // Индекс index начинается с 0 и увеличивается на каждой итерации
        // Это более читаемая альтернатива циклу for (i in 0 until 10)
        repeat(10) { index ->
            // Генерация случайного номера билета в пределах доступного количества
            // Поскольку лотерея одна, не требуется выбор случайного элемента из списка
            val ticketNumber = Random.nextInt(1, lottery.getAvailableTicket() + 1)

            // Формирование строки с информацией о билете
            // Используем index + 1, потому что индексация в repeat начинается с 0
            // Формат: "1. Лотерея='...', Тираж=..., Билет №..."
            val ticketInfo = "${index + 1}. Лотерея='${lottery.getLotteryName()}', Тираж=${lottery.getTicketcirculation()}, Билет №$ticketNumber"

            // Добавление информации о билете в историю операций
            resultArea.appendText("$ticketInfo\n")
        }

        // Обновление статусного поля
        statusArea.text = "Сгенерировано 10 выигрышных билетов"
    }

    /**
     * Метод вызывается при нажатии кнопки "Загрузить билеты из файла".
     * Использует статический метод loadTicketsFromFile() вашего класса.
     * Статические методы в Kotlin реализуются через companion object.
     *
     * Логика изменена: из файла загружается один объект, который заменяет currentLottery.
     */
    @FXML
    private fun onLoadFromFileButtonClick() {
        try {

            // Вызов статического метода через имя класса
            // Метод возвращает список объектов LotteryTicketCounterClass
            // Файл "Tickets.txt" должен находиться в рабочей директории проекта
            val loaded = LotteryTicketCounterClass.loadTicketsFromFile("Tickets.txt")

            // Проверка, что файл не пустой и содержит хотя бы одну запись
            if (loaded.isEmpty()) {
                statusArea.text = "Предупреждение: файл пуст или не содержит корректных данных"
                return
            }

            // Присваиваем первый элемент из загруженного списка как текущую лотерею
            // Остальные элементы (если они есть) игнорируются, так как приложение работает с одним объектом
            // firstOrNull() возвращает первый элемент или null, если список пуст
            currentLottery = loaded.firstOrNull()

            // Формирование статусного сообщения с использованием строкового шаблона
            statusArea.text = "Загружена лотерея из файла 'Tickets.txt': ${currentLottery?.getLotteryName()}"

            // Добавление заголовка в историю операций
            resultArea.appendText("\nЗагружена лотерея из файла:\n")

            // Вызов метода toString() вашего класса для получения строкового представления объекта
            // Используем безопасный вызов ?.toString() на случай, если объект не загрузился
            resultArea.appendText("   ${currentLottery?.toString()}\n")

        } catch (e: Exception) {
            // Оператор элвис (?:) возвращает левый операнд, если он не null, иначе правый
            // Используется для предоставления значения по умолчанию при отсутствии сообщения об ошибке
            // Если e.message равно null, будет использована строка "файл не найден"
            statusArea.text = "Ошибка загрузки: ${e.message ?: "файл не найден"}"

            // Добавление информации об ошибке в историю операций
            resultArea.appendText("\nОшибка загрузки: ${e.message}\n")
        }
    }

    /**
     * Метод вызывается при нажатии кнопки "Сохранить билеты в файл".
     * Использует статический метод saveAllToFile() вашего класса.
     *
     * Логика изменена: в файл сохраняется единственный объект currentLottery.
     */
    @FXML
    private fun onLoadToFileButtonClick() {
        try {
            // Проверка наличия данных для сохранения
            // Если объект не создан — нечего сохранять
            val lottery = currentLottery
            if (lottery == null) {
                statusArea.text = "Ошибка: нет данных для сохранения"
                return
            }

            // Вызов статического метода сохранения
            // Поскольку метод ожидает список, передаём список из одного элемента
            // Альтернатива: создать перегрузку метода saveAllToFile, принимающую один объект
            LotteryTicketCounterClass.saveAllToFile(listOf(lottery), "save.txt")

            // Формирование статусного сообщения
            statusArea.text = "Сохранена лотерея '${lottery.getLotteryName()}' в файл 'save.txt'"

            // Добавление записи в историю операций
            resultArea.appendText("\nСохранена лотерея в файл 'save.txt': ${lottery.toString()}\n")

        } catch (e: Exception) {
            // Обработка ошибок сохранения с использованием оператора элвис
            statusArea.text = "Ошибка сохранения: ${e.message ?: "неизвестная ошибка"}"
            resultArea.appendText("\nОшибка сохранения: ${e.message}\n")
        }
    }
}