// автор: Прасков Даниил ИВТ-24-2

#include "../BankUserClass.hpp"
#include <cassert>
#include <iostream>
#include <windows.h>

int main() {

    SetConsoleOutputCP(CP_UTF8);

    std::cout << "Запуск простых тестов...\n";

    // Тест 1: Создание пользователя и получение данных
    {
        BankUserClass user("Анна", "BNK1", 1000.0);
        assert(user.get_Name("BNK1") == "Анна");
        assert(user.get_balance("BNK1") == 1000.0);
    }

    // Тест 2: Пополнение счёта
    {
        BankUserClass user("Борис", "BNK2", 500.0);
        user.deposit(200.0, "BNK2");
        assert(user.get_balance("BNK2") == 700.0);
    }

    // Тест 3: Снятие средств
    {
        BankUserClass user("Виктор", "BNK3", 1000.0);
        user.withdraw(300.0, "BNK3");
        assert(user.get_balance("BNK3") == 700.0);
    }

    // Тест 4: Смена имени
    {
        BankUserClass user("Старое", "BNK4", 0.0);
        user.change_name("Новое", "BNK4");
        assert(user.get_Name("BNK4") == "Новое");
    }

    // Тест 5: Несколько пользователей — без конфликтов
    {
        BankUserClass u1("Петя", "BNK1", 100.0);
        BankUserClass u2("Маша", "BNK2", 200.0);

        u1.deposit(50.0, "BNK1");
        u2.withdraw(50.0, "BNK2");

        assert(u1.get_balance("BNK1") == 150.0);
        assert(u2.get_balance("BNK2") == 150.0);
    }

    // Тест 6: проверка метода to_string
    {

        BankUserClass u1("Петя", "BNK1", 100.0);

        assert(u1.to_string() == "Name: Петя, Bank ID: BNK1, Balance: 100.000000");

    }

    std::cout << "Все тесты пройдены!\n";
    return 0;
}