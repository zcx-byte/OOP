// автор: Прасков Даниил ИВТ-24-2

#include "../BankUserClass.hpp"
#include <cassert>
#include <iostream>
#include <windows.h>

int main() {

    SetConsoleOutputCP(CP_UTF8);

    std::cout << "Запуск тестов...\n";

    // Тест 1: Создание пользователя и получение данных
    {
        BankUserClass user("Анна", "BNK1", 1000.0);
        assert(user.get_Name() == "Анна");
        assert(user.get_balance() == 1000.0);
    }

    // Тест 2: Пополнение счёта
    {
        BankUserClass user("Борис", "BNK2", 500.0);
        user.deposit(200.0);
        assert(user.get_balance() == 700.0);
    }

    // Тест 3: Снятие средств
    {
        BankUserClass user("Виктор", "BNK3", 1000.0);
        user.withdraw(300.0);
        assert(user.get_balance() == 700.0);
    }

    // Тест 4: Смена имени
    {
        BankUserClass user("Старое", "BNK4", 0.0);
        user.change_name("Новое");
        assert(user.get_Name() == "Новое");
    }

    // Тест 5: Несколько пользователей — без конфликтов
    {
        BankUserClass u1("Петя", "BNK1", 100.0);
        BankUserClass u2("Маша", "BNK2", 200.0);

        u1.deposit(50.0);
        u2.withdraw(50.0);

        assert(u1.get_balance() == 150.0);
        assert(u2.get_balance() == 150.0);
    }

    // Тест 6: проверка метода to_string
    {

        BankUserClass u1("Петя", "BNK1", 100.0);

        assert(u1.to_string() == "Name: Петя, Bank ID: BNK1, Balance: 100.000000");

    }

    {
        const std::string filename = "test/test_save.txt";
    
        BankUserClass user("Анна", "BNK1", 1000.0);
    
        // Открываем поток в режиме дозаписи
        std::ofstream outFile(filename, std::ios::app);
        assert(outFile.is_open() && "Не удалось открыть файл для записи");
        
        // Сохраняем в поток
        user.save_to_file_append(filename);  // ← передаём поток, а не filename
        outFile.close();
    
        // Проверка, что данные записаны корректно
        std::ifstream inFile(filename);
        assert(inFile.is_open() && "Не удалось открыть файл для чтения");
    
        std::string loadedName;
        std::string loadedID;
        double loadedBalance;
        inFile >> loadedName >> loadedID >> loadedBalance;
        inFile.close();
    
        assert(loadedName == user.get_Name());
        assert(loadedBalance == user.get_balance());
        assert(loadedID == user.get_bankID("BNK1"));
    }

    {
        const std::string filename = "test/test_load.txt";
    
        BankUserClass user("Анна", "BNK1", 1000.0);
    
        std::ifstream inputFile(filename);
        assert(inputFile.is_open() && "Не удалось открыть test/test_load.txt");
    
        user.load_from_file(filename);  // ← передаём поток, НЕ filename
        inputFile.close();
    
        // Проверки
        assert(user.get_Name() == "Carol");
        assert(user.get_bankID("BNK1") == "BNK1");
        assert(user.get_balance() == 1234.56);
    }

    std::cout << "Все тесты пройдены!\n";
    return 0;
}