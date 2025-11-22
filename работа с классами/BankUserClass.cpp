// автор: Прасков Даниил ИВТ-24-2

#include "BankUserClass.hpp"
#include <stdexcept>
#include <iostream>
#include <fstream>

BankUserClass::BankUserClass(const std::string& name, const std::string& bank_ID, double initial_balance)
    : name(name), bank_ID(bank_ID), balance(initial_balance) {
    if (initial_balance < 0) {
        throw std::invalid_argument("Начальный баланс не может быть отрицательным");
    }
}

/// @brief Возвращает текущий баланс пользователя.
/// @param bank_ID Идентификатор банка для проверки подлинности запроса.
/// @return Текущий баланс (this->balance).
/// @throw std::invalid_argument если bank_ID не совпадает с this->bank_ID.
/// @note Без проверки bank_ID возможна утечка данных.
double BankUserClass::get_balance(const std::string& bank_ID) const {
    if (bank_ID != this->bank_ID) {
        throw std::invalid_argument("Неверный Bank ID: доступ к балансу запрещён");
    }
    return this->balance;
}

/// @brief Возвращает имя пользователя.
/// @param bank_ID Идентификатор банка для проверки подлинности запроса.
/// @return Имя пользователя (this->name).
/// @throw std::invalid_argument если bank_ID не совпадает с this->bank_ID.
/// @note Без проверки bank_ID возможна утечка данных.
std::string BankUserClass::get_Name(const std::string& bank_ID) const {
    if (bank_ID != this->bank_ID) {
        throw std::invalid_argument("Неверный Bank ID: доступ к имени запрещён");
    }
    return this->name;
}

/// @brief Возвращает идентификатор банка пользователя.
/// @param bank_ID Идентификатор банка для проверки подлинности запроса.
/// @return Идентификатор банка (this->bank_ID).
/// @throw std::invalid_argument если bank_ID не совпадает с this->bank_ID.
/// @note Возвращает реальный ID только при корректной аутентификации.
std::string BankUserClass::get_bankID(const std::string& bank_ID) const {
    if (bank_ID != this->bank_ID) {
        throw std::invalid_argument("Неверный Bank ID: доступ к идентификатору запрещён");
    }
    return this->bank_ID;
}

BankUserClass::BankUserClass(const BankUserClass& other)
    : name(other.name), bank_ID(other.bank_ID), balance(other.balance) {
        std::cout << "Создана копия пользователя: " << name << std::endl;
}

/// @brief Пополняет счёт пользователя на указанную сумму.
/// @param amount Сумма пополнения — должна быть > 0.
/// @param bank_ID Идентификатор банка — проверяется на соответствие this->bank_ID.
/// @throw std::invalid_argument если amount <= 0 или bank_ID неверный.
/// @note При успешной операции выводится сообщение в std::cout.
void BankUserClass::deposit(double amount, const std::string& bank_ID) {
    if (bank_ID != this->bank_ID) {
        throw std::invalid_argument("Неверный Bank ID: операция пополнения запрещена");
    }
    if (amount <= 0) {
        throw std::invalid_argument("Сумма пополнения должна быть положительной");
    }
    std::cout << "операция пополнения прошла успешно" << std::endl;
    balance += amount;
}

/// @brief Снимает сумму со счёта пользователя.
/// @param amount Сумма снятия — должна быть > 0 и <= баланса.
/// @param bank_ID Идентификатор банка — проверяется на соответствие this->bank_ID.
/// @throw std::invalid_argument если amount <= 0 или bank_ID неверный.
/// @throw std::runtime_error если недостаточно средств.
/// @note При успешной операции выводится сообщение в std::cout.
void BankUserClass::withdraw(double amount, const std::string& bank_ID) {
    if (bank_ID != this->bank_ID) {
        throw std::invalid_argument("Неверный Bank ID: операция снятия запрещена");
    }
    if (amount <= 0) {
        throw std::invalid_argument("Сумма снятия должна быть положительной");
    }
    if (balance < amount) {
        throw std::runtime_error("Недостаточно средств");
    }
    std::cout << "операция вывода средств прошла успешно" << std::endl;
    balance -= amount;
}

/// @brief Изменяет имя пользователя.
/// @param new_name Новое имя.
/// @param bank_ID Идентификатор банка — проверяется на соответствие this->bank_ID.
/// @throw std::invalid_argument если bank_ID неверный.
/// @note При успешной операции выводится сообщение в std::cout.
void BankUserClass::change_name(const std::string& new_name, const std::string& bank_ID) {
    if (bank_ID != this->bank_ID) {
        throw std::invalid_argument("Неверный Bank ID: изменение имени запрещено");
    }
    std::cout << "операция изменения имени прошла успешно" << std::endl;
    name = new_name;
}

/// @brief Преобразует данные пользователя в строку для вывода.
/// @return Строка в формате: "Name: ..., Bank ID: ..., Balance: ..."
/// @note Не требует аутентификации (публичная информация только при вызове напрямую).
std::string BankUserClass::to_string() const {
    return "Name: " + name + ", Bank ID: " + bank_ID + ", Balance: " + std::to_string(balance);
}

/// @brief Сохраняет данные пользователя в файл (режим дозаписи).
/// @param filename Имя файла для сохранения.
/// @note Формат: "name bank_ID balance\n"
/// @note При успешной операции выводится сообщение в std::cout.
void BankUserClass::save_to_file_append(const std::string& filename) const {
    std::ofstream file(filename, std::ios::app);        // режим дозаписи в конец файла
    file << name << " " << bank_ID << " " << balance << "\n";
    file.close();
    std::cout << "Данные пользователя " << name << " сохранены в файл " << filename << std::endl;
}

/// @brief Загружает данные пользователя из открытого файла.
/// @param file Поток ifstream, открытый для чтения.
/// @note Читает данные в формате: name bank_ID balance (разделённые пробелами)
/// @note Перезаписывает текущие поля объекта (name, bank_ID, balance).
/// @note При успешной операции выводится сообщение в std::cout.
void BankUserClass::load_from_file(std::ifstream& file) {
    file >> name >> bank_ID >> balance;
    std::cout << "Данные пользователя " << name << " загружены." << std::endl;
}