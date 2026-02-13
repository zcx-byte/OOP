import java.io.File     // подключаем библиотеку для работы с файлами

/**
 * Класс для учёта лотерейных билетов.
 *
 * @property lotteryName Название лотереи
 * @property ticketcirculation Уникальный идентификатор тиража.
 * @property availableTicket Количество доступных для продажи билетов (должно быть >= 0).
 */
class LotteryTicketCounterClass(
    private var lotteryName: String = " ",
    private var ticketcirculation: String = " ",
    private var availableTicket: Int = 0,
){
    // блок init выполняется при создании объекта и используется для проверки данных
    // Этот блок выполняется сразу при создании объекта — здесь проверяют данные и готовят объект к работе
    init {
        // require проверяет условие; если оно ложно — выбрасывает IllegalArgumentException с указанным сообщением
        require(availableTicket >= 0) {"Кол-во билетов не может быть отрицательным"}
    }

    private var available: Int = availableTicket

    fun getLotteryName(): String = lotteryName
    fun getticketcirculation(): String = ticketcirculation
    fun getavailableTicket(): Int = available

    /**
     * Пополняет количество доступных билетов.
     * @param amount Количество добавляемых билетов.
     */
    fun addTickets(amount: Int){
        require(amount > 0) {"Кол-во билетов для добавления должно быть положительным"}
        available += amount
    }

    /**
     * Продаёт указанное количество билетов.
     * @param amount Количество продаваемых билетов.
     */
    fun sellTickets(amount: Int){
        require(available >= amount) {"Кол-во билетов на продажу не должно превышать общее кол-во билетов в кассе"}
        available -= amount
        println("Продано $amount билетов. Осталось: $available")
    }

    /**
     * Изменяет название лотереи.
     */
    fun changeLotteryName(newName: String) {
        lotteryName = newName
        println("Название лотереи изменено на: $newName")
    }

    // override — заменяем стандартный метод родительского класса (Any) своей реализацией
   override fun toString(): String {
      return "Лотерея: $lotteryName, Тираж: $ticketcirculation, Доступно билетов: $available"
   }

    fun loadTicketsFromFile(filename: String){
        // создаём переменную для хранения файла
        // говорим, что у этой переменной тип данных File
        // можно записать и по другому:
        // val file: File = File(filename)
        val file = File(filename)

        // println("Ищу файл по пути: ${file.absolutePath}")

        // check проверяет состояние системы (например, наличие файла);
        // если файл не существует, выбрасывается IllegalStateException
        check(file.exists()) { "Файл не найден: $filename" }

        // readText() — читает весь текст из файла как одну строку
        // - Открывает файл, читает всё содержимое, закрывает файл
        // - Возвращает String (строку)
        // trim() - убирает все пробелы, переносы строк в начале и конце строки.
        val line = file.readText().trim()

        // split(" ") — разбивает строку на части по пробелам
        // - Возвращает список строк: List<String>
        // Пример: "A B C" → ["A", "B", "C"]
        val parts = line.split(" ")

        require(parts.size == 3) {"Некорректный формат файла. Ожидалось 3 поля."}

        // toInt() — преобразует строку в целое число (Int)
        // - Если строка не является числом (например, "abc"), выбросит NumberFormatException
        lotteryName = parts[0]
        ticketcirculation = parts[1]
        available = parts[2].toInt()

        println("Данные загружены из '$filename'")

    }
}
