// автор: Прасков Даниил ИВТ-24-2

#ifndef BANKUSERCLASS_HPP
#define BANKUSERCLASS_HPP

#include <string>
#include <fstream>

/*
Данный класс представляет собой небольшую банковскую систему.
Позволяет создать экземпляры, у которых есть имя, индивидуальный ID и начальный баланс.
Данный класс позволяет пополнять, снимать средства с балансов, а также изменять имя пользователя.
Все операции требуют передачи корректного Bank ID для подтверждения прав доступа.
*/

class BankUserClass {
private:
    std::string name;      ///< Имя пользователя.
    std::string bank_ID;   ///< Идентификатор банка, к которому привязан пользователь.
    double balance;        ///< Текущий баланс пользователя в банке.

public:
    /// @brief Конструирует объект пользователя банка.
    /// @param name Имя пользователя (не пустое, но валидация не требуется по условию).
    /// @param bank_ID Идентификатор банка — сохраняется как this->bank_ID.
    /// @param initial_balance Начальный баланс — должен быть >= 0.
    /// @throw std::invalid_argument если initial_balance < 0.
    BankUserClass(const std::string& name, const std::string& bank_ID, double initial_balance);

    /// @brief Возвращает текущий баланс пользователя.
    /// @param bank_ID Идентификатор банка для проверки подлинности запроса.
    /// @return Значение баланса (this->balance).
    /// @throw std::invalid_argument если bank_ID не совпадает с this->bank_ID.
    /// @note Без корректного bank_ID доступ к балансу запрещён.
    double get_balance(const std::string& bank_ID) const;

    /// @brief Возвращает имя пользователя.
    /// @param bank_ID Идентификатор банка для проверки подлинности запроса.
    /// @return Имя пользователя (this->name).
    /// @throw std::invalid_argument если bank_ID не совпадает с this->bank_ID.
    /// @note Без корректного bank_ID доступ к имени запрещён.
    std::string get_Name(const std::string& bank_ID) const;

    /// @brief Возвращает идентификатор банка пользователя.
    /// @param bank_ID Идентификатор банка для проверки подлинности запроса.
    /// @return Идентификатор банка (this->bank_ID).
    /// @throw std::invalid_argument если bank_ID не совпадает с this->bank_ID.
    /// @note Возвращает реальный ID только при успешной аутентификации.
    std::string get_bankID(const std::string& bank_ID) const;

    /// @brief Пополняет счёт пользователя на указанную сумму.
    /// @param amount Сумма пополнения — должна быть > 0.
    /// @param bank_ID Идентификатор банка — проверяется на соответствие this->bank_ID.
    /// @throw std::invalid_argument если amount <= 0 или bank_ID неверный.
    /// @note При успешной операции выводится сообщение в std::cout.
    void deposit(double amount, const std::string& bank_ID);

    /// @brief Снимает указанную сумму со счёта пользователя.
    /// @param amount Сумма снятия — должна быть > 0 и <= текущего баланса.
    /// @param bank_ID Идентификатор банка — проверяется на соответствие this->bank_ID.
    /// @throw std::invalid_argument если amount <= 0 или bank_ID неверный.
    /// @throw std::runtime_error если средств на счёте недостаточно.
    /// @note При успешной операции выводится сообщение в std::cout.
    void withdraw(double amount, const std::string& bank_ID);

    /// @brief Изменяет имя пользователя.
    /// @param new_name Новое имя (может быть пустым — ограничений нет).
    /// @param bank_ID Идентификатор банка — проверяется на соответствие this->bank_ID.
    /// @throw std::invalid_argument если bank_ID неверный.
    /// @note При успешной операции выводится сообщение в std::cout.
    void change_name(const std::string& new_name, const std::string& bank_ID);

    /// @brief Сохраняет текущее состояние объекта (имя, ID, баланс) в текстовый файл в режиме дозаписи.
    /// @param filename Имя файла для записи (например, "users.txt").
    /// @throw std::ios_base::failure если файл не удалось открыть для записи.
    /// @note Формат записи: одна строка — имя, bank_ID и balance, разделённые пробелами.
    /// При успешной записи выводится сообщение в std::cout.
    void save_to_file_append(const std::string& filename) const;

    /// @brief Загружает состояние объекта (имя, ID, баланс) из уже открытого входного потока.
    /// @param file Ссылка на открытый std::ifstream (должен быть готов к чтению).
    /// @note Чтение выполняется через оператор >>: сначала имя, затем ID, затем баланс (в этом порядке).
    /// Перезаписывает текущие значения полей объекта. При успешной загрузке выводится сообщение в std::cout.
    void load_from_file(std::ifstream& file);

    /// @brief Возвращает текстовое представление объекта.
    /// @return Строка вида: "Name: ..., Bank ID: ..., Balance: ...".
    /// @note Для баланса используется std::to_string, что может привести к экспоненциальному формату
    /// при очень больших или малых значениях.
    std::string to_string() const;
};

#endif // BANKUSERCLASS_HPP