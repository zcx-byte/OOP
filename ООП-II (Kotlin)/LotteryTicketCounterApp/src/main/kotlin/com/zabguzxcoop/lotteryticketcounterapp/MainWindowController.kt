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

    // Список всех лотерей, созданных пользователем
    // Тип MutableList<LotteryTicketCounterClass> означает изменяемый список объектов класса
    // Инициализация через функцию mutableListOf() создаёт пустой список
    // Используем var вместо val, потому что ссылка на список может быть заменена (например, при очистке)
    private var lotteries: MutableList<LotteryTicketCounterClass> = mutableListOf()

    /**
     * Метод вызывается при нажатии кнопки "Добавить билеты".
     * Связь с кнопкой в FXML осуществляется через атрибут onAction="#onaddTicketsButtonClick".
     * Аннотация @FXML обязательна для методов, вызываемых из FXML.
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

        // Создание объекта класса с использованием конструктора
        // Параметры передаются в том же порядке, как объявлены в конструкторе класса LotteryTicketCounterClass
        val lottery = LotteryTicketCounterClass(name, circulation, count)
        lotteries.add(lottery)

        // Формирование строки с использованием строкового шаблона (string template)
        // Синтаксис $имяПеременной или ${выражение} позволяет встраивать значения прямо в строку
        // Это альтернатива конкатенации через оператор + и более читаема
        statusArea.text = "Добавлена лотерея: $name (тираж $circulation), билетов: $count"

        // Метод appendText() добавляет текст в конец существующего содержимого поля
        // В отличие от присваивания свойству text, не очищает предыдущее содержимое
        resultArea.appendText("Добавлена лотерея: ${lottery.toString()}\n")

        // Очистка полей ввода через метод clear()
        lottaryName.clear()
        ticketcirculation.clear()
        availableTicket.clear()

        // Установка фокуса ввода на первое поле для удобства пользователя
        lottaryName.requestFocus()
    }

    /**
     * Метод вызывается при нажатии кнопки "Выдать случайный билет".
     * Выбирает случайную лотерею с доступными билетами и генерирует случайный номер билета.
     * После выдачи билета количество доступных билетов уменьшается на 1 через метод sellTickets().
     */
    @FXML
    private fun onTakeRandTicketButtonClick() {

        // Проверка наличия лотерей в списке через свойство isEmpty()
        // Альтернатива: if (lotteries.size == 0)
        if (lotteries.isEmpty()) {
            statusArea.text = "Ошибка: нет добавленных лотерей"
            return
        }

        // Фильтрация списка через функцию высшего порядка filter()
        // Лямбда-выражение { условие } применяется к каждому элементу списка
        // Результат — новый список, содержащий только элементы, удовлетворяющие условию
        // В данном случае отбираем только лотереи с доступными билетами (больше 0)
        // it — автоматически означает текущий объект на каждой итерации в filter
        val availableLotteries = lotteries.filter { it.getavailableTicket() > 0 }

        if (availableLotteries.isEmpty()) {
            statusArea.text = "Ошибка: все билеты уже выданы"
            return
        }

        // Выбор случайного элемента из списка через расширение random()
        // Это встроенный метод Kotlin для коллекций, не требует ручной генерации индекса
        // Аналог ручного кода: availableLotteries[Random.nextInt(availableLotteries.size)]
        val selectedLottery = availableLotteries.random()

        // Генерация случайного целого числа в диапазоне [от, до)
        // Конструктор Random.nextInt(от, до) возвращает число от "от" включительно до "до" исключительно
        // Поэтому для включения верхней границы добавляем 1 к количеству билетов
        // Например, если билетов 10, то диапазон будет [1, 11), что даёт числа от 1 до 10 включительно
        val ticketNumber = Random.nextInt(1, selectedLottery.getavailableTicket() + 1)

        // Вызов метода класса для уменьшения счётчика доступных билетов
        // Метод sellTickets() реализован в классе и содержит внутреннюю валидацию
        // После вызова количество доступных билетов уменьшится на 1
        selectedLottery.sellTickets(1)

        // Формирование статусного сообщения с использованием строковых шаблонов
        statusArea.text = "Выдан билет №$ticketNumber из лотереи '${selectedLottery.getLotteryName()}' " +
                "(тираж ${selectedLottery.getticketcirculation()})"

        // Добавление записи в историю операций с указанием всех параметров билета
        resultArea.appendText("Выдан билет: Лотерея='${selectedLottery.getLotteryName()}', " +
                "Тираж=${selectedLottery.getticketcirculation()}, Билет №$ticketNumber\n")
    }

    /**
     * Метод вызывается при нажатии кнопки "Показать 10 выйгрышных билетов".
     * Генерирует 10 случайных билетов из всех доступных лотерей.
     * Каждый билет генерируется независимо, дубликаты возможны.
     */
    @FXML
    private fun onwiewRandTicketsButtonClick() {

        // Проверка наличия лотерей в системе
        if (lotteries.isEmpty()) {
            statusArea.text = "Ошибка: нет добавленных лотерей"
            return
        }

        // Фильтрация списка — оставляем только лотереи с доступными билетами
        val availableLotteries = lotteries.filter { it.getavailableTicket() > 0 }

        // Проверка, есть ли лотереи с билетами для генерации
        if (availableLotteries.isEmpty()) {
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
            // Случайный выбор лотереи из доступных
            val lottery = availableLotteries.random()

            // Генерация случайного номера билета в пределах доступного количества
            val ticketNumber = Random.nextInt(1, lottery.getavailableTicket() + 1)

            // Формирование строки с информацией о билете
            // Используем index + 1, потому что индексация в repeat начинается с 0
            // Формат: "1. Лотерея='...', Тираж=..., Билет №..."
            val ticketInfo = "${index + 1}. Лотерея='${lottery.getLotteryName()}', Тираж=${lottery.getticketcirculation()}, Билет №$ticketNumber"

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
     */
    @FXML
    private fun onLoadFromFileButtonClick() {
        try {
            // Вызов статического метода через имя класса
            // Метод возвращает список объектов LotteryTicketCounterClass
            // Файл "данные.txt" должен находиться в рабочей директории проекта
            val loaded = LotteryTicketCounterClass.loadTicketsFromFile("Tickets.txt")

            // Очистка текущего списка лотерей через метод clear()
            // Это удаляет все элементы из списка, но сохраняет сам объект списка
            lotteries.clear()

            // Добавление всех элементов из одного списка в другой через метод addAll()
            // Альтернатива: for (item in loaded) lotteries.add(item)
            lotteries.addAll(loaded)

            // Формирование статусного сообщения с использованием строкового шаблона
            // Свойство size возвращает количество элементов в списке
            statusArea.text = "Загружено ${loaded.size} лотерей из файла 'Tickets.txt'"

            // Добавление заголовка в историю операций
            resultArea.appendText("\nЗагружены лотереи из файла:\n")

            // Итерация по списку через цикл for
            // Синтаксис for (элемент in коллекция) — идиоматичный способ перебора в Kotlin
            // Альтернатива с функцией высшего порядка: loaded.forEach { lottery -> ... }
            for (lottery in loaded) {
                // Вызов метода toString() вашего класса для получения строкового представления объекта
                resultArea.appendText("   ${lottery.toString()}\n")
            }
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
     */
    @FXML
    private fun onLoadToFileButtonClick() {
        try {
            // Проверка наличия данных для сохранения
            if (lotteries.isEmpty()) {
                statusArea.text = "Ошибка: нет данных для сохранения"
                return
            }

            // Вызов статического метода сохранения
            // Передаём ссылку на список и имя файла
            // Метод сохраняет все объекты из списка в текстовый файл
            LotteryTicketCounterClass.saveAllToFile(lotteries, "save.txt")

            // Формирование статусного сообщения
            statusArea.text = "Сохранено ${lotteries.size} лотерей в файл 'save.txt'"

            // Добавление записи в историю операций
            resultArea.appendText("\nСохранено ${lotteries.size} лотерей в файл 'save.txt'\n")
        } catch (e: Exception) {
            // Обработка ошибок сохранения с использованием оператора элвис
            statusArea.text = "Ошибка сохранения: ${e.message ?: "неизвестная ошибка"}"
            resultArea.appendText("\nОшибка сохранения: ${e.message}\n")
        }
    }
}