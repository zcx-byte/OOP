import java.io.File

fun main() {
    try {
        testConstructor()
        testAddTickets()
        testSellTickets()
        testChangeLotteryName()
        testToString()
        testSaveToFile()
        testLoadTicketsFromFile()
        testSaveAllToFile()

        println("Все тесты пройдены")
    } finally {
        // Очистка (опционально — можно закомментировать, если хотите видеть результат)
//        File("testresources/saveTest.txt").delete()
    }
}

// --- Тесты ---

fun testConstructor() {
    val obj = LotteryTicketCounterClass("A", "X", 10)
    if (obj.getavailableTicket() != 10) error("Конструктор: неверное значение")

    try {
        LotteryTicketCounterClass("A", "X", -1)
        error("Должно быть исключение для отрицательного количества")
    } catch (e: IllegalArgumentException) {}
}

fun testAddTickets() {
    val obj = LotteryTicketCounterClass("A", "X", 10)
    obj.addTickets(5)
    if (obj.getavailableTicket() != 15) error("addTickets: неверное увеличение")

    try {
        obj.addTickets(-1)
        error("Должно быть исключение для отрицательного amount")
    } catch (e: IllegalArgumentException) {}
}

fun testSellTickets() {
    val obj = LotteryTicketCounterClass("A", "X", 10)
    obj.sellTickets(3)
    if (obj.getavailableTicket() != 7) error("sellTickets: неверное уменьшение")

    try {
        obj.sellTickets(11)
        error("Должно быть исключение при превышении доступного")
    } catch (e: IllegalArgumentException) {}
}

fun testChangeLotteryName() {
    val obj = LotteryTicketCounterClass("Old", "X", 10)
    obj.changeLotteryName("New")
    if (obj.getLotteryName() != "New") error("changeLotteryName: имя не изменилось")

    try {
        obj.changeLotteryName(" ")
        error("Должно быть исключение для пустого имени")
    } catch (e: IllegalArgumentException) {}
}

fun testToString() {
    val obj = LotteryTicketCounterClass("T", "ID", 5)
    val expected = "Лотерея: T, Тираж: ID, Доступно билетов: 5"
    if (obj.toString() != expected) error("toString: неверный формат")
}

fun testSaveToFile() {
    val filename = "saveTest.txt"
    File(filename).delete()

    val obj = LotteryTicketCounterClass("S", "SAVE", 99)
    obj.saveToFile(filename)

    val content = File(filename).readText().trim()
    val expected = "S SAVE 99"
    if (content != expected) error("saveToFile: ожидалось '$expected', получено '$content'")
}

fun testLoadTicketsFromFile() {
    // Гарантируем, что файл существует с правильным содержимым
    val filename = "TestTickets.txt"
    File(filename).writeText(
        """Быстроденежки id50demons 150
Гослото DRAW-001 150
МегаЛото MEGA-2023 200
СуперЛото SUPER-99 75""".trimIndent()
    )

    val list = LotteryTicketCounterClass.loadTicketsFromFile(filename)
    if (list.size != 4) error("Ожидалось 4 объекта")

    if (list[0].getLotteryName() != "Быстроденежки" ||
        list[0].getticketcirculation() != "id50demons" ||
        list[0].getavailableTicket() != 150) {
        error("Первый объект неверен")
    }

    if (list[3].getLotteryName() != "СуперЛото" ||
        list[3].getavailableTicket() != 75) {
        error("Последний объект неверен")
    }
}

fun testSaveAllToFile() {
    val filename = "saveTest.txt"
    File(filename).delete()

    val list = listOf(
        LotteryTicketCounterClass("A", "1", 1),
        LotteryTicketCounterClass("B", "2", 2)
    )
    LotteryTicketCounterClass.saveAllToFile(list, filename)

    val content = File(filename).readText().trim()
    val expected = "A 1 1\nB 2 2"
    if (content != expected) error("saveAllToFile: ожидалось '$expected', получено '$content'")
}