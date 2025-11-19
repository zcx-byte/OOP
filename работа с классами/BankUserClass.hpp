// автор: Прасков Даниил ИВТ-24-2

#ifndef BANKUSERCLASS_HPP
#define BANKUSERCLASS_HPP

#include <string>

// ! автора
// ! общий комментарий для класса

class BankUserClass {
private:
    std::string name;      ///< Имя пользователя.
    std::string bank_ID;   ///< Идентификатор банка, к которому привязан пользователь.
    double balance;        ///< Текущий баланс пользователя в банке.

    /// @brief Проверяет, совпадает ли переданный bank_ID с внутренним ID пользователя.
    /// @param bank_ID Идентификатор банка для проверки.
    /// @return true, если bank_ID совпадает с тем, что хранится в объекте; иначе false.
    bool right_bank_ID(const std::string& bank_ID) const;

public:
    /// @brief Конструирует объект пользователя банка.
    /// @param name Имя пользователя (не пустое, но валидация не требуется по условию).
    /// @param bank_ID Идентификатор банка (проверяется в методах, формат не валидируется дополнительно).
    /// @param initial_balance Начальный баланс — должен быть >= 0.
    /// @throw std::invalid_argument если initial_balance < 0.
    BankUserClass(const std::string& name, const std::string& bank_ID, double initial_balance);

    /// @brief Деструктор по умолчанию (освобождает ресурсы, удерживаемые std::string).
    ~BankUserClass();

    /// @brief Возвращает текущий баланс пользователя.
    /// @param bank_ID Идентификатор банка — должен совпадать с тем, что был задан при создании.
    /// @return Значение баланса (double).
    /// @throw std::invalid_argument если bank_ID не совпадает с внутренним.
    double get_balance(const std::string& bank_ID) const;

    /// @brief Возвращает имя пользователя.
    /// @param bank_ID Идентификатор банка — должен совпадать с внутренним.
    /// @return Имя пользователя (копия строки).
    /// @throw std::invalid_argument если bank_ID не совпадает с внутренним.
    std::string get_Name(const std::string& bank_ID) const;

    /// @brief Пополняет счёт пользователя на указанную сумму.
    /// @param amount Сумма пополнения — должна быть > 0.
    /// @param bank_ID Идентификатор банка — должен совпадать с внутренним.
    /// @throw std::invalid_argument если bank_ID неверный или amount <= 0.
    /// @note При успешной операции выводится сообщение в std::cout.
    void deposit(double amount, const std::string& bank_ID);

    /// @brief Снимает указанную сумму со счёта пользователя.
    /// @param amount Сумма снятия — должна быть > 0 и <= текущего баланса.
    /// @param bank_ID Идентификатор банка — должен совпадать с внутренним.
    /// @throw std::invalid_argument если bank_ID неверный или amount <= 0.
    /// @throw std::runtime_error если средств на счёте недостаточно.
    /// @note При успешной операции выводится сообщение в std::cout.
    void withdraw(double amount, const std::string& bank_ID);

    /// @brief Изменяет имя пользователя.
    /// @param new_name Новое имя (может быть пустым — ограничений нет).
    /// @param bank_ID Идентификатор банка — должен совпадать с внутренним.
    /// @throw std::invalid_argument если bank_ID не совпадает с внутренним.
    /// @note При успешной операции выводится сообщение в std::cout.
    void change_name(const std::string& new_name, const std::string& bank_ID);

    /// @brief Возвращает текстовое представление объекта.
    /// @return Строка вида: "Name: ..., Bank ID: ..., Balance: ...".
    /// @note Не гарантируется формат числа (зависит от std::to_string).
    std::string to_string() const;
};

#endif // BANKUSERCLASS_HPP