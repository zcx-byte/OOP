// автор: Прасков Даниил ИВТ-24-2

#include <iostream>
#include <windows.h>
#include "BankUserClass.hpp"
#include <fstream>

// Создаём статический объект, до main
// Статические объекты — это объекты, которые создаются один раз при запуске программы и живут до её завершения
// Существуют независимо от вызовов функций и областей видимости.
BankUserClass static_user("Стасик", "static_user", 500.0);

int main(){

    SetConsoleOutputCP(CP_UTF8);

    const std::string input_filename = "users.txt";
    const std::string save_filename = "UsersSave.txt";

    std::cout << "Статический экземпляр создан" << std::endl;
    std::cout << static_user.to_string() << std::endl;

    std::cout << "-----------------------------------------------" << std::endl;

    // Задаём первого пользователя банка
    BankUserClass User1 ("Алексей", "User1", 500.0);
    std::cout << User1.to_string() << std::endl;

    // меняем имя у первого пользователя
    User1.change_name("Андрей", "User1");
    std::cout << User1.to_string() << std::endl;
    
    std::cout << "-----------------------------------------------" << std::endl;

    // Задаём второго пользователя банка
    BankUserClass User2 ("Иван", "User2", 12000.0);
    std::cout << User2.to_string() << std::endl;

    // выводим все деньги у 2 пользователя
    User2.withdraw(12000, "User2");
    std::cout << User2.to_string() << std::endl;

    std::cout << "-----------------------------------------------" << std::endl;

    // Задаём третьего пользователя банка
    BankUserClass User3 ("Матвей", "User3", 0.0);
    std::cout << User3.to_string() << std::endl;

    // пополняем баланс у третьего пользователя
    User3.deposit(1500, "User3");
    std::cout << User3.to_string() << std::endl;

    std::cout << "-----------------------------------------------" << std::endl;

    // получаем только имя у всех пользователей
    std::cout << User1.get_Name("User1") << std::endl;
    std::cout << User2.get_Name("User2") << std::endl;
    std::cout << User3.get_Name("User3") << std::endl;

    std::cout << "-----------------------------------------------" << std::endl;

    // получаем только баланс у всех пользователей
    std::cout << User1.get_balance("User1") << std::endl;
    std::cout << User2.get_balance("User2") << std::endl;
    std::cout << User3.get_balance("User3") << std::endl;

    std::cout << "-----------------------------------------------" << std::endl;

    // Динамически создадим объект
    BankUserClass* ptr = new BankUserClass ("Димасик", "ptr", 100.0);
    
    // -> - поскольку ptr - это не сам объект, а указатель на него (хранит адрес в памяти)
    // по факту является операцией разыменования
    std::cout << ptr -> to_string() << std::endl;
    
    // Не забываем очистить
    delete ptr;

    std::cout << "-----------------------------------------------" << std::endl;

    // Массив из объектов
    // для создания создан специальный метод, как бы для копирования объекта класса
    BankUserClass arr [2] = {
        BankUserClass("Первый массив", "1_0", 0.0),
        BankUserClass("Второй массив", "2_0", 0.0)
    };

    // ВЫвод каждого объекта
    for (int i = 0; i < 2; i++) {
        std::cout << "arr[" << i << "]: " << arr[i].to_string() << std::endl;
    }

    std::cout << "-----------------------------------------------" << std::endl;

    // Создаём динамический массив из объектов
    BankUserClass* dynam_arr = new BankUserClass[2]{
        BankUserClass("Первый динамический массив", "1_2_0", 0.0),
        BankUserClass("Второй динамический массив", "2_2_0", 0.0)
    };

    // выводим
    for (int i = 0; i < 2; i++) {
        std::cout << "dynam_arr[" << i << "]: " << dynam_arr[i].to_string() << std::endl;
    }
    
    // Очищаем
    delete[] dynam_arr;

    std::cout << "-----------------------------------------------" << std::endl;

    // работаем с сохранением пользователей в файл
    BankUserClass file_user_1("Файловый", "F_s_1", 999.9);
    file_user_1.save_to_file_append(save_filename);

    BankUserClass file_user_2("Файловый2", "F_s_2", 0.9);
    file_user_2.save_to_file_append(save_filename);

    BankUserClass file_user_3("Файловый3", "F_s_3", 1999.9);
    file_user_3.save_to_file_append(save_filename);

    std::cout << "-----------------------------------------------" << std::endl;

    // работаем с загрузкой пользователей из файла
    int count = 0;

    // считаем кол-во пользователей в файле
    {
        std::ifstream input_filename__(input_filename);
        std::string line;
        while (std::getline(input_filename__, line)) {
            if (!line.empty()) {    // пропускаем пустые строки
                count++;
            }
        }
    }   // поскольку этот блок кода заключён в {} - file.close() прописывать не нужно
    
    // ! спросить на счёт этого
    //  BankUserClass* users_from_file = new BankUserClass[count] <- ошибка
    BankUserClass** users_from_file = new BankUserClass*[count];    // массив из указателей, объект не создан

    // открываем файл
    std::ifstream input_filename_ (input_filename);

    for (int i = 0; i < count; i++) {
        users_from_file[i] = new BankUserClass("temp", "temp", 0.0);    // создаём объект
        users_from_file[i]->load_from_file(input_filename_);      // перезаписываем
    }

    input_filename_.close();

    for (int i = 0; i < count; i++){
        std::cout << users_from_file[i]->to_string() << std::endl;
    }

    for (int i = 0; i < count; i++) {
        delete users_from_file[i];
    }

    delete[] users_from_file;
    
}