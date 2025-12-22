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
    auto pistol = std::make_unique<PistolClass>(15, "9mm", 0.8f, "Glock 17", false);
    auto rifle = std::make_unique<RifleClass>(30, ".223 Rem", 3.5f, "AWM", true);
    
    pistol -> shoot();   // Пистолет Glock-19 стреляет!
    rifle -> toggleScope(); // Прицел выключен.

    rifle -> shoot();
    rifle -> reload();

    pistol -> setAutomaticMode();

    // Пример работы с dynamic_cast (если нужно доступ к методам производного класса через базовый указатель)
    std::unique_ptr<FirearmsClass> ptr = std::make_unique<PistolClass>(10, "7.62mm", 1.0f, "TT", false);
    ptr->shoot();

    // Проверяем, можно ли сделать downcast
    if (PistolClass* p = dynamic_cast<PistolClass*>(ptr.get())) {
        p->setAutomaticMode();  
    }

}       