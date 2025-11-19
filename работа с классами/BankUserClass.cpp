// автор: Прасков Даниил ИВТ-24-2

#include "BankUserClass.hpp"
#include <stdexcept>
#include <iostream>

BankUserClass::BankUserClass(const std::string& name, const std::string& bank_ID, double initial_balance)
    : name(name), bank_ID(bank_ID), balance(initial_balance) {
    if (initial_balance < 0) {
        throw std::invalid_argument("Начальный баланс не может быть отрицательным");
    }
}

// default - команда компилятору, чтобы он сам сгенерировал деструктор
// можно было и без деструктора
BankUserClass::~BankUserClass() = default;

double BankUserClass::get_balance(const std::string& bank_ID) const { return balance; }

std::string BankUserClass::get_Name(const std::string& bank_ID) const { return name; }

void BankUserClass::deposit(double amount, const std::string& bank_ID) {
    if (amount <= 0) {
        throw std::invalid_argument("Сумма пополнения должна быть положительной");
    }
    std::cout << "операция пополнения прошла успешно" << std::endl;
    balance += amount;
}

void BankUserClass::withdraw(double amount, const std::string& bank_ID) {
    if (amount <= 0) {
        throw std::invalid_argument("Сумма снятия должна быть положительной");
    }
    if (balance < amount) {
        throw std::runtime_error("Недостаточно средств");
    }
    std::cout << "операция вывода средств прошла успешно" << std::endl;
    balance -= amount;
}

void BankUserClass::change_name(const std::string& new_name, const std::string& bank_ID) {

    std::cout << "операция прошла успешно" << std::endl;
    
    name = new_name;
    
}

std::string BankUserClass::to_string() const {
    return "Name: " + name + ", Bank ID: " + bank_ID + ", Balance: " + std::to_string(balance);
}