// подключаем библиотеку JUnit 5 для тестирования
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*

// подключаем библиотеки для работы с файлами
import java.nio.file.Files
import java.nio.file.Path

/**
 * Класс для тестирования функциональности LotteryTicketCounterClass.
 *
 * Этот класс содержит набор тестов, которые проверяют:
 * - Корректность создания объектов
 * - Работу методов добавления и продажи билетов
 * - Работу с файлами (сохранение и загрузка)
 * - Обработку ошибочных данных
 */
class LotteryTicketTest {

    // переменная для хранения пути к временному файлу
    // используется во всех тестах, связанных с файлами
    //
    // lateinit var — модификатор, который означает "инициализировано позже".
    // - Мы обещаем компилятору, что переменная будет заполнена до первого использования.
    // - Тип Path означает, что переменная НЕ может быть null.
    // - Это избавляет от необходимости проверять null и использовать операторы !! или ?.
    private lateinit var tempFile: Path

    /**
     * Метод выполняется перед каждым тестом.
     *
     * @BeforeEach — аннотация, которая говорит JUnit: "Запусти этот метод
     * перед каждым тестом, независимо от того, какой именно тест будет выполняться".
     *
     * - Создать чистый временный файл для теста
     * - Гарантировать, что каждый тест начинает работу с новыми данными
     * - Избежать конфликтов между тестами
     */
    @BeforeEach
    fun setUp() {

        // Files.createTempFile() создаёт уникальный временный файл в системной папке
        // "lottery_test_" — префикс имени файла
        // ".txt" — расширение файла
        // Результат сразу записывается в переменную tempFile (благодаря lateinit)
        tempFile = Files.createTempFile("lottery_test_", ".txt")
    }

    /**
     * Метод выполняется после каждого теста.
     *
     * @AfterEach — аннотация, которая говорит JUnit: "Запусти этот метод
     * после каждого завершённого теста".
     *
     * - Удалить временный файл после теста
     * - Очистить память и диск от мусора
     * - Гарантировать, что файлы не накопятся на компьютере после запуска тестов
     */
    @AfterEach
    fun tearDown() {

        // Files.deleteIfExists() — удаляет файл, если он существует
        // Так как tempFile имеет тип Path (не null), мы можем передать её напрямую
        // без проверок на null и без операторов безопасности
        Files.deleteIfExists(tempFile)
    }

    /**
     * Тест проверяет создание объекта с корректными данными.
     *
     * @Test — аннотация, которая определяет метод как тест.
     * JUnit автоматически найдёт и запустит все методы с этой аннотацией.
     *
     * Что проверяем:
     * - Объект создаётся без ошибок
     * - Все поля инициализируются правильными значениями
     */
    @Test
    fun `test constructor with valid data`() {

        // создаём объект лотерейного билета с валидными данными
        val ticket = LotteryTicketCounterClass("Sportloto", "12345", 100)

        // assertEquals(ожидаемое, фактическое) — проверяет равенство значений
        // если значения не равны — тест падает с ошибкой
        assertEquals("Sportloto", ticket.getLotteryName())
        assertEquals("12345", ticket.getticketcirculation())
        assertEquals(100, ticket.getavailableTickets())
    }

    /**
     * Тест проверяет, что конструктор выбрасывает исключение при отрицательном количестве билетов.
     *
     * assertThrows<ТипИсключения> { код } — проверяет, что внутри блока
     * обязательно возникнет исключение указанного типа.
     *
     * Что проверяем:
     * - При передаче -10 билетов объект НЕ создаётся
     * - Выбрасывается IllegalArgumentException
     * - Сообщение об ошибке соответствует ожидаемому
     */
    @Test
    fun `test constructor with negative tickets throws exception`() {

        // assertThrows ловит исключение и возвращает его в переменную exception
        val exception = assertThrows<IllegalArgumentException> {

            // этот код должен вызвать ошибку
            LotteryTicketCounterClass("Sportloto", "12345", -10)
        }

        // проверяем, что сообщение об ошибке именно такое, как мы указали в require()
        assertEquals("Кол-во билетов не может быть отрицательным", exception.message)
    }


    /**
     * Тест проверяет корректное добавление билетов.
     *
     * Что проверяем:
     * - Метод addTickets() увеличивает счётчик
     * - Новое значение = старое + добавленное
     */
    @Test
    fun `test addTickets increases count`() {

        // создаём объект с 50 билетами
        val ticket = LotteryTicketCounterClass("Loto", "001", 50)

        // добавляем ещё 10 билетов
        ticket.addTickets(10)

        // проверяем, что стало 60 билетов (50 + 10)
        assertEquals(60, ticket.getavailableTickets())
    }

    /**
     * Тест проверяет, что добавление отрицательного количества билетов запрещено.
     *
     * Что проверяем:
     * - Метод addTickets() не принимает отрицательные значения
     * - Выбрасывается исключение с правильным сообщением
     */
    @Test
    fun `test addTickets with invalid amount throws exception`() {
        val ticket = LotteryTicketCounterClass("Loto", "001", 50)

        val exception = assertThrows<IllegalArgumentException> {

            // пытаемся добавить -5 билетов (некорректно)
            ticket.addTickets(-5)
        }

        assertEquals("Кол-во билетов для добавления должно быть положительным", exception.message)
    }

    /**
     * Тест проверяет корректную продажу билетов.
     *
     * Что проверяем:
     * - Метод sellTickets() уменьшает счётчик
     * - Новое значение = старое - проданное
     */
    @Test
    fun `test sellTickets decreases count`() {
        val ticket = LotteryTicketCounterClass("Loto", "001", 50)

        // продаём 20 билетов
        ticket.sellTickets(20)

        // проверяем, что осталось 30 билетов (50 - 20)
        assertEquals(30, ticket.getavailableTickets())
    }

    /**
     * Тест проверяет, что нельзя продать больше билетов, чем есть в наличии.
     *
     * Что проверяем:
     * - При попытке продать больше, чем есть, выбрасывается исключение
     * - Количество билетов не меняется после неудачной попытки
     */
    @Test
    fun `test sellTickets more than available throws exception`() {
        val ticket = LotteryTicketCounterClass("Loto", "001", 10)

        val exception = assertThrows<IllegalArgumentException> {

            // пытаемся продать 15 билетов, а есть только 10
            ticket.sellTickets(15)
        }

        assertEquals("Кол-во билетов на продажу не должно превышать общее кол-во билетов в кассе", exception.message)
    }

    /**
     * Тест проверяет изменение названия лотереи.
     *
     * Что проверяем:
     * - Метод changeLotteryName() обновляет название
     * - Новое название сохраняется в объекте
     */
    @Test
    fun `test changeLotteryName`() {
        val ticket = LotteryTicketCounterClass("OldName", "001", 10)

        // меняем название
        ticket.changeLotteryName("NewName")

        // проверяем, что название изменилось
        assertEquals("NewName", ticket.getLotteryName())
    }

    /**
     * Тест проверяет, что нельзя установить название из одного пробела.
     *
     * Что проверяем:
     * - Метод changeLotteryName() отклоняет некорректные значения
     * - Выбрасывается исключение с правильным сообщением
     */
    @Test
    fun `test changeLotteryName with space throws exception`() {
        val ticket = LotteryTicketCounterClass("OldName", "001", 10)

        val exception = assertThrows<IllegalArgumentException> {

            // пытаемся установить название из пробела (некорректно)
            ticket.changeLotteryName(" ")
        }

        assertEquals("Нельзя передать пустое значение", exception.message)
    }

    /**
     * Тест проверяет работу переопределённого метода toString().
     *
     * Что проверяем:
     * - Метод toString() возвращает строку в ожидаемом формате
     * - Все поля объекта отображаются в строке
     */
    @Test
    fun `test toString override`() {
        val ticket = LotteryTicketCounterClass("SuperLoto", "999", 42)

        // ожидаемая строка в том формате, который мы указали в toString()
        val expected = "Лотерея: SuperLoto, Тираж: 999, Доступно билетов: 42"

        assertEquals(expected, ticket.toString())
    }

    /**
     * Тест проверяет сохранение и загрузку списка билетов из файла.
     *
     * Что проверяем:
     * - saveAllToFile() корректно записывает данные в файл
     * - loadTicketsFromFile() корректно читает данные из файла
     * - Данные после загрузки совпадают с исходными
     *
     * Это интеграционный тест — проверяет взаимодействие двух методов вместе.
     */
    @Test
    fun `test saveAllToFile and loadTicketsFromFile`() {

        // получаем путь к временному файлу
        // благодаря аннотации @BeforeEach и модификатору lateinit
        val fileName = tempFile.toString()

        // 1. Создаём исходный список билетов
        val originalList = listOf(
            LotteryTicketCounterClass("Loto1", "T1", 10),
            LotteryTicketCounterClass("Loto2", "T2", 20)
        )

        // 2. Сохраняем список в файл
        // вызываем статический метод через имя класса
        LotteryTicketCounterClass.saveAllToFile(originalList, fileName)

        // 3. Загружаем список из файла обратно
        val loadedList = LotteryTicketCounterClass.loadTicketsFromFile(fileName)

        // 4. Проверяем, что количество объектов совпадает
        assertEquals(originalList.size, loadedList.size)

        // 5. Проверяем данные первого объекта
        assertEquals(originalList[0].getLotteryName(), loadedList[0].getLotteryName())
        assertEquals(originalList[0].getavailableTickets(), loadedList[0].getavailableTickets())

        // 6. Проверяем данные второго объекта
        assertEquals(originalList[1].getLotteryName(), loadedList[1].getLotteryName())
        assertEquals(originalList[1].getavailableTickets(), loadedList[1].getavailableTickets())
    }

    /**
     * Тест проверяет обработку несуществующего файла при загрузке.
     *
     * Что проверяем:
     * - При попытке загрузить несуществующий файл выбрасывается исключение
     * - Тип исключения: IllegalStateException (потому что используется check())
     * - Сообщение об ошибке содержит информацию о файле
     */
    @Test
    fun `test loadTicketsFromFile with non-existent file throws exception`() {
        val exception = assertThrows<IllegalStateException> {

            // пытаемся загрузить файл, которого нет
            LotteryTicketCounterClass.loadTicketsFromFile("non_existent_file.txt")
        }

        // проверяем, что сообщение об ошибке содержит слово "не найден"
        // contains() возвращает true, если подстрока есть в строке
        assertTrue(exception.message!!.contains("не найден"))
    }

    /**
     * Тест проверяет обработку файла с неверным форматом данных.
     *
     * Что проверяем:
     * - Если в файле не 3 слова на строку — выбрасывается исключение
     * - Тип исключения: IllegalArgumentException (потому что используется require())
     *
     * Почему это важно:
     * - Файл может быть повреждён
     * - Пользователь мог вручную изменить файл
     * - Программа должна корректно обработать ошибку, а не упасть
     */
    @Test
    fun `test loadTicketsFromFile with invalid format throws exception`() {

        // получаем путь к файлу (безопасно, так как файл создан в setUp)
        val fileName = tempFile.toString()

        // записываем в файл неверный формат (всего 2 слова вместо 3)
        // формат должен быть: "Название Тираж Количество"
        java.io.File(fileName).writeText("LotoName OnlyTwoWords")

        val exception = assertThrows<IllegalArgumentException> {

            // пытаемся загрузить некорректные данные
            LotteryTicketCounterClass.loadTicketsFromFile(fileName)
        }

        // проверяем, что сообщение об ошибке содержит информацию о формате
        assertTrue(exception.message!!.contains("Нужны 3 слова"))
    }

    /**
     * Тест проверяет сохранение одного объекта в файл через метод экземпляра.
     *
     * В чём разница от saveAllToFile():
     * - saveToFile() — метод объекта, сохраняет один билет
     * - saveAllToFile() — статический метод, сохраняет список билетов
     *
     * Что проверяем:
     * - Файл создаётся после вызова метода
     * - Файл не пустой
     * - Содержимое файла соответствует ожидаемому формату
     */
    @Test
    fun `test instance saveToFile`() {
        
        // получаем путь к файлу
        val fileName = tempFile.toString()

        // создаём объект билета
        val ticket = LotteryTicketCounterClass("InstanceLoto", "I1", 5)

        // вызываем метод сохранения объекта (не статический!)
        ticket.saveToFile(fileName)

        // получаем объект файла для проверки
        val file = java.io.File(fileName)

        // проверяем, что файл физически существует на диске
        assertTrue(file.exists())

        // проверяем, что файл не пустой
        assertTrue(file.readText().isNotEmpty())

        // читаем содержимое файла и убираем лишние пробелы по краям
        val content = file.readText().trim()

        // проверяем, что данные записаны в правильном формате
        assertEquals("InstanceLoto I1 5", content)
    }
}