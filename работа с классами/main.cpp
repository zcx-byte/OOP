// автор: Прасков Даниил ИВТ-24-2

#include <iostream>
#include <windows.h>
#include "BankUserClass.hpp"

int main(){

    // ! дополнить с задания на гите

    SetConsoleOutputCP(CP_UTF8);

    // Задаём первого пользователя банка
    BankUserClass User1 ("Алексей", "1234", 500.0);
    
    // Задаём второго пользователя банка
    BankUserClass User2 ("Иван", "2345", 12000.0);

    // Задаём третьего пользователя банка
    BankUserClass User3 ("Матвей", "1444", 0.0);

    // меняем имя у первого пользователя
    User1.change_name("Андрей", "1234");

    std::cout << User1.to_string() << std::endl;

    // выводим все деньги у 2 пользователя
    User2.withdraw(12000, "2345");

    std::cout << User2.to_string() << std::endl;

    // пополняем баланс у третьего пользователя
    User3.deposit(1500, "1444");

    std::cout << User3.to_string() << std::endl;

    // получаем только имя у всех пользователей
    std::cout << User1.get_Name("1234") << std::endl;
    std::cout << User2.get_Name("2345") << std::endl;
    std::cout << User3.get_Name("1444") << std::endl;

    // получаем только баланс у всех пользователей
    std::cout << User1.get_balance("1234") << std::endl;
    std::cout << User2.get_balance("2345") << std::endl;
    std::cout << User3.get_balance("1444") << std::endl;    
}