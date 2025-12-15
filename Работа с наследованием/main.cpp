#include <iostream>
#include "FirearmsClass.hpp"
#include <string>
#include <stdexcept>
#include <windows.h>
#include <memory> // для std::unique_ptr

int main(){

    SetConsoleOutputCP(CP_UTF8);
    
    // поскольку FirearmsClass имеет чисто виртуальные методы,
    // создать его экземпляр напрямую невозможно

    // создаём экземпляры через unique_ptr
    // "умные" указатели, которым не нужен delete
    // unique_ptr применим к типу данных, make_unique - к функциям
    auto pistol = std::make_unique<PistolClass>(15, "9mm", 0.8f, "Glock 17");
    auto rifle = std::make_unique<RifleClass>(30, ".223 Rem", 3.5f, true);
    
    pistol -> shoot();   // Пистолет Glock-19 стреляет!
    rifle -> toggleScope(); // Прицел выключен.

    rifle -> shoot();
    rifle -> reload();

    std::cout << "Модель: " << pistol->showModel() << std::endl;
}