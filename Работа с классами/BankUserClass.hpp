// автор: Прасков Даниил ИВТ-24-2

#ifndef BANK_USER_CLASS_HPP
#define BANK_USER_CLASS_HPP

#include <string>
#include <fstream>

/// @brief Класс, представляющий пользователя банка.
/// Хранит имя, идентификатор банка и текущий баланс.
class BankUserClass {
private:
    std::string name;       ///< Имя пользователя.
    std::string bank_ID;    ///< Идентификатор банка.
    double balance;         ///< Текущий баланс счёта.

public:
    /// @brief Конструктор с параметрами.
    /// @param name Имя пользователя.
    /// @param bank_ID Идентификатор банка.
    /// @param initial_balance Начальный баланс (должен быть >= 0).
    /// @throw std::invalid_argument если initial_balance < 0.
    BankUserClass(const std::string& name, const std::string& bank_ID, double initial_balance);

    /// @brief Возвращает текущий баланс пользователя.
    /// @return Текущий баланс (this->balance).
    double get_balance() const;

    /// @brief Возвращает имя пользователя.
    /// @return Имя пользователя (this->name).
    std::string get_Name() const;

    /// @brief Возвращает идентификатор банка пользователя.
    /// @param bank_ID Идентификатор банка для проверки подлинности запроса.
    /// @return Идентификатор банка (this->bank_ID).
    /// @throw std::invalid_argument если bank_ID не совпадает с this->bank_ID.
    std::string get_bankID(const std::string& bank_ID) const;

    /// @brief Пополняет счёт пользователя на указанную сумму.
    /// @param amount Сумма пополнения — должна быть > 0.
    /// @throw std::invalid_argument если amount <= 0.
    /// @note При успешной операции выводится сообщение в std::cout.
    void deposit(double amount);

    /// @brief Снимает сумму со счёта пользователя.
    /// @param amount Сумма снятия — должна быть > 0 и <= баланса.
    /// @throw std::invalid_argument если amount <= 0.
    /// @throw std::runtime_error если недостаточно средств.
    /// @note При успешной операции выводится сообщение в std::cout.
    void withdraw(double amount);

    /// @brief Изменяет имя пользователя.
    /// @param new_name Новое имя.
    /// @note При успешной операции выводится сообщение в std::cout.
    void change_name(const std::string& new_name);

    /// @brief Преобразует данные пользователя в строку.
    /// @return Строка в формате: "Name: ..., Bank ID: ..., Balance: ..."
    std::string to_string() const;

    /// @brief Сохраняет данные пользователя в уже открытый выходной поток.
    /// @param out Поток std::ostream (например, std::ofstream), открытый для записи.
    /// @note Формат строки: "name bank_ID balance\n" (поля разделены пробелами).
    /// @note Перезаписывает текущие поля объекта (name, bank_ID, balance).
    /// @note При успешной операции выводится сообщение в std::cout.
    void save_to_file_append(const std::string& filename) const;

    /// @brief Загружает данные пользователя из уже открытого входного потока.
    /// @param in Поток std::istream (например, std::ifstream), открытый для чтения.
    /// @note Ожидает формат: name bank_ID balance (поля разделены пробелами).
    /// @note Перезаписывает текущие поля объекта (name, bank_ID, balance).
    /// @note При успешной операции выводится сообщение в std::cout.
    void load_from_file(const std::string& filename);
};

#endif // BANK_USER_CLASS_HPP