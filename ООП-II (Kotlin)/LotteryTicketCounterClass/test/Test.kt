import java.io.File

fun main() {
    // Запуск всех тестов для проверки корректности работы класса LotteryTicketCounterClass
    testConstructor()
    testAddTickets()
    testSellTickets()
    testChangeLotteryName()
    testToString()
    testSaveToFile()
    testLoadTicketsFromFile()
    testSaveAllToFile()

    println("Все тесты пройдены успешно!")
}

// --- Тесты ---

fun testConstructor() {

    // Проверяем корректную работу конструктора с валидными данными
    val obj = LotteryTicketCounterClass("A", "X", 10)
    check(obj.getavailableTickets() == 10) { "Конструктор: неверное значение количества билетов" }

    // Проверяем, что конструктор выбрасывает исключение при отрицательном количестве билетов
    var exceptionThrown = false
    try {
        LotteryTicketCounterClass("A", "X", -1)
    } catch (e: IllegalArgumentException) {
        exceptionThrown = true
    }
    check(exceptionThrown) { "Должно быть исключение для отрицательного количества билетов" }
}

fun testAddTickets() {
    // Проверяем добавление билетов: начальное количество 10 + 5 = 15
    val obj = LotteryTicketCounterClass("A", "X", 10)
    obj.addTickets(5)
    check(obj.getavailableTickets() == 15) { "addTickets: неверное увеличение количества" }

    // Проверяем, что метод addTickets выбрасывает исключение при отрицательном amount
    var exceptionThrown = false
    try {
        obj.addTickets(-1)
    } catch (e: IllegalArgumentException) {
        exceptionThrown = true
    }
    check(exceptionThrown) { "Должно быть исключение для отрицательного amount" }
}

fun testSellTickets() {
    // Проверяем продажу билетов: начальное количество 10 - 3 = 7
    val obj = LotteryTicketCounterClass("A", "X", 10)
    obj.sellTickets(3)
    check(obj.getavailableTickets() == 7) { "sellTickets: неверное уменьшение количества" }

    // Проверяем, что нельзя продать больше билетов, чем доступно
    var exceptionThrown = false
    try {
        obj.sellTickets(11) // Пытаемся продать 11 при наличии 10
    } catch (e: IllegalArgumentException) {
        exceptionThrown = true
    }
    check(exceptionThrown) { "Должно быть исключение при превышении доступного количества" }
}

fun testChangeLotteryName() {
    // Проверяем изменение имени лотереи
    val obj = LotteryTicketCounterClass("Old", "X", 10)
    obj.changeLotteryName("New")
    check(obj.getLotteryName() == "New") { "changeLotteryName: имя не изменилось" }

    // Проверяем, что нельзя установить пустое имя лотереи
    var exceptionThrown = false
    try {
        obj.changeLotteryName(" ")
    } catch (e: IllegalArgumentException) {
        exceptionThrown = true
    }
    check(exceptionThrown) { "Должно быть исключение для пустого имени" }
}

fun testToString() {
    // Проверяем форматирование строкового представления объекта
    val obj = LotteryTicketCounterClass("T", "ID", 5)
    val expected = "Лотерея: T, Тираж: ID, Доступно билетов: 5"
    check(obj.toString() == expected) { "toString: неверный формат строки" }
}

fun testSaveToFile() {
    // Проверяем сохранение данных одного объекта в файл
    val filename = "saveTest.txt"
    File(filename).delete() // Очищаем файл перед тестом

    val obj = LotteryTicketCounterClass("S", "SAVE", 99)
    obj.saveToFile(filename)

    val content = File(filename).readText().trim()
    val expected = "S SAVE 99"
    check(content == expected) { "saveToFile: ожидалось '$expected', получено '$content'" }
}

fun testLoadTicketsFromFile() {
    // Создаем тестовый файл с данными для загрузки
    val filename = "TestTickets.txt"
    File(filename).writeText(
        """Быстроденежки id50demons 150
Гослото DRAW-001 150
МегаЛото MEGA-2023 200
СуперЛото SUPER-99 75""".trimIndent()
    )

    // Загружаем список объектов из файла
    val list = LotteryTicketCounterClass.loadTicketsFromFile(filename)
    check(list.size == 4) { "Ожидалось 4 объекта, загружено: ${list.size}" }

    // Проверяем первый объект из файла
    check(list[0].getLotteryName() == "Быстроденежки" &&
            list[0].getticketcirculation() == "id50demons" &&
            list[0].getavailableTickets() == 150) { "Первый объект неверен" }

    // Проверяем последний объект из файла
    check(list[3].getLotteryName() == "СуперЛото" &&
            list[3].getavailableTickets() == 75) { "Последний объект неверен" }
}

fun testSaveAllToFile() {
    // Проверяем сохранение списка объектов в файл
    val filename = "saveTest.txt"
    File(filename).delete() // Очищаем файл перед тестом

    val list = listOf(
        LotteryTicketCounterClass("A", "1", 1),
        LotteryTicketCounterClass("B", "2", 2)
    )
    LotteryTicketCounterClass.saveAllToFile(list, filename)

    val content = File(filename).readText().trim()
    val expected = "A 1 1\nB 2 2"
    check(content == expected) { "saveAllToFile: ожидалось '$expected', получено '$content'" }
}
