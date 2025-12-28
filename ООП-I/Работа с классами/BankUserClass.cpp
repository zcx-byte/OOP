// автор: Прасков Даниил ИВТ-24-2

#include "BankUserClass.hpp"
#include <stdexcept>
#include <iostream>
#include <fstream>

// конструктор с параметрами
/// @brief Конструктор с параметрами.
/// @param name Имя пользователя.
/// @param bank_ID Идентификатор банка.
/// @param initial_balance Начальный баланс (должен быть >= 0).
/// @throw std::invalid_argument если initial_balance < 0.
BankUserClass::BankUserClass(const std::string& name, const std::string& bank_ID, double initial_balance)
    : name(name), bank_ID(bank_ID), balance(initial_balance) {
    if (initial_balance < 0) {
        throw std::invalid_argument("Начальный баланс не может быть отрицательным");
    }
}

/// @brief Возвращает текущий баланс пользователя.
/// @return Текущий баланс (this->balance).
double BankUserClass::get_balance() const {
    return this->balance;
}

/// @brief Возвращает имя пользователя.
/// @return Имя пользователя (this->name).
std::string BankUserClass::get_Name() const {
    return this->name;
}

/// @brief Возвращает идентификатор банка пользователя.
/// @param bank_ID Идентификатор банка для проверки подлинности запроса.
/// @return Идентификатор банка (this->bank_ID).
/// @throw std::invalid_argument если bank_ID не совпадает с this->bank_ID.
std::string BankUserClass::get_bankID(const std::string& bank_ID) const {
    if (bank_ID != this->bank_ID) {
        throw std::invalid_argument("Неверный Bank ID: доступ к идентификатору запрещён");
    }
    return this->bank_ID;
}

/// @brief Пополняет счёт пользователя на указанную сумму.
/// @param amount Сумма пополнения — должна быть > 0.
/// @throw std::invalid_argument если amount <= 0.
/// @note При успешной операции выводится сообщение в std::cout.
void BankUserClass::deposit(double amount) {
    if (amount <= 0) {
        throw std::invalid_argument("Сумма пополнения должна быть положительной");
    }
    std::cout << "операция пополнения прошла успешно" << std::endl;
    balance += amount;
}

/// @brief Снимает сумму со счёта пользователя.
/// @param amount Сумма снятия — должна быть > 0 и <= баланса.
/// @throw std::invalid_argument если amount <= 0.
/// @throw std::runtime_error если недостаточно средств.
/// @note При успешной операции выводится сообщение в std::cout.
void BankUserClass::withdraw(double amount) {
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
/// @note При успешной операции выводится сообщение в std::cout.
void BankUserClass::change_name(const std::string& new_name) {
    std::cout << "операция изменения имени прошла успешно" << std::endl;
    name = new_name;
}

/// @brief Преобразует данные пользователя в строку.
/// @return Строка в формате: "Name: ..., Bank ID: ..., Balance: ..."
std::string BankUserClass::to_string() const {
    return "Name: " + name + ", Bank ID: " + bank_ID + ", Balance: " + std::to_string(balance);
}

/// @brief Сохраняет данные пользователя в файл в режиме дозаписи.
/// @param filename Имя файла для сохранения.
/// @note Формат строки: "name bank_ID balance\n".
/// @note Если файл не существует — он создаётся.
/// @note При успешной операции выводится сообщение в std::cout.
void BankUserClass::save_to_file_append(const std::string& filename) const {
    std::ofstream file(filename, std::ios::app);
    file << name << " " << bank_ID << " " << balance << "\n";
    file.close();
    std::cout << "Данные пользователя " << name << " сохранены в файл " << filename << std::endl;
}

/// @brief Загружает данные пользователя из файла.
/// @param filename Имя файла для загрузки.
/// @note Ожидает формат: "name bank_ID balance" (поля разделены пробелами).
/// @note Перезаписывает текущие поля объекта (name, bank_ID, balance).
/// @note При успешной операции выводится сообщение в std::cout.
void BankUserClass::load_from_file(const std::string& filename) {
    std::ifstream file(filename);
    file >> name >> bank_ID >> balance;
    file.close();
    std::cout << "Данные пользователя " << name << " загружены из файла " << filename << std::endl;
}