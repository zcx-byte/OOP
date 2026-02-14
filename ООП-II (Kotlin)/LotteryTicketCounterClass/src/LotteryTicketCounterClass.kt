import java.io.File     // подключаем библиотеку для работы с файлам
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
) {
    // блок init выполняется при создании объекта и используется для проверки данных
    // Этот блок выполняется сразу при создании объекта — здесь гарантируется, что объект создастся с корректными данными
    init {
        // require проверяет условие; если оно ложно — выбрасывает IllegalArgumentException с указанным сообщением
        require(availableTicket >= 0) { "Кол-во билетов не может быть отрицательным" }
    }

    private var available: Int = availableTicket

    fun getLotteryName(): String = lotteryName
    fun getticketcirculation(): String = ticketcirculation
    fun getavailableTicket(): Int = available

    /**
     * Пополняет количество доступных билетов.
     * @param amount Количество добавляемых билетов.
     */
    fun addTickets(amount: Int) {
        require(amount > 0) { "Кол-во билетов для добавления должно быть положительным" }
        available += amount
        println("успешно добавлено $amount билетов")
    }

    /**
     * Продаёт указанное количество билетов.
     * @param amount Количество продаваемых билетов.
     */
    fun sellTickets(amount: Int) {
        require(available >= amount) { "Кол-во билетов на продажу не должно превышать общее кол-во билетов в кассе" }
        available -= amount
        println("Продано $amount билетов. Осталось: $available")
    }

    /**
     * Изменяет название лотереи.
     */
    fun changeLotteryName(newName: String) {
        require(newName != " ") { "Нельзя передать пустое значение" }
        lotteryName = newName
        println("Название лотереи изменено на: $newName")
    }

    // override — заменяем стандартный метод родительского класса (Any) своей реализацией
    override fun toString(): String {
        return "Лотерея: $lotteryName, Тираж: $ticketcirculation, Доступно билетов: $available"
    }

    /**
     * Функция загружает данные из текстового файла и создаёт список объектов LotteryTicketCounterClass.
     *
     * @param filename Имя файла, который нужно прочитать (должен находиться в той же папке, что и программа).
     * @return Список объектов типа LotteryTicketCounterClass — по одному на каждую строку файла.
     *
     * Каждая строка превращается в один объект.
     */
    companion object {
        /**
         * Загружает лотереи из файла и создает список объектов.
         *
         * companion object — это статическая часть класса.
         * Позволяет вызывать функции без создания объекта:
         *
         * Возвращает: List<LotteryTicketCounterClass>
         */
        fun loadTicketsFromFile(filename: String): List<LotteryTicketCounterClass> {

            val file = File(filename)
            check(file.exists()) { "Файл '$filename' не найден!" }

            // .readLines() - открывает и читает все строки целиком и возвращает их в виде списка List<String>
            // после закрывает файл
            val lines = file.readLines()

            // map — это функция преобразования списка.
            // берёт каждый элемент списка, применяет к нему функцию и собирает новый список.
            // "Возьми каждый элемент из lines, назови его line, сделай с ним что-то, и собери всё в новый список".
            return lines.map { line ->

                // trim() - Убирает лишние пробелы в начале и в конце строки.
                // split() - Разбивает строку на части по символу пробела и возвращает список этих частей.
                val words = line.trim().split(" ")
                require(words.size == 3) { "Нужны 3 слова: '$line'" }

                val name = words[0]
                val draw = words[1]
                val count = words[2].toInt()

                LotteryTicketCounterClass(name, draw, count)
            }
        }

        /**
         * Сохраняет список объектов в файл.
         * Каждый объект записывается в отдельной строке.
         */
        fun saveAllToFile(tickets: List<LotteryTicketCounterClass>, filename: String) {
            val lines = tickets.map { ticket ->
                "${ticket.getLotteryName()} ${ticket.getticketcirculation()} ${ticket.getavailableTicket()}"
            }
            // appendText() - Открывает файл в режиме дозаписи, добавляет переданную строку в конец файла,
            // затем закрывает файл.
            // lines.joinToString("\n") — объединяет все строки из списка в одну, разделяя их символом новой строки
            // + "\n" — добавляет завершающий перенос строки, чтобы следующая запись начиналась с новой строки
            File(filename).appendText(lines.joinToString("\n") + "\n")
        }

    }

    /**
     * Сохраняет текущий объект в файл, дозаписывая его содержимое.
     * Внимание: каждый вызов перезаписывает файл. Для сохранения списка объектов используйте статический метод.
     */
    fun saveToFile(filename: String) {
        val line = "${getLotteryName()} ${getticketcirculation()} ${getavailableTicket()}"
        File(filename).appendText(line + "\n")
    }
}