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

    // создаём экземпляры через обычные указатели
    // обычные (сырые) указатели требуют ручного управления памятью
    PistolClass* raw_pistol = new PistolClass(10, "7.62mm", 1.0f, "TT", false);
    RifleClass* raw_rifle = new RifleClass(20, "5.56mm", 3.2f, "M16", true);

    // вызов реализации метода
    pistol->shoot();   // Пистолет Glock-19 стреляет!
    rifle->toggleScope(); // Прицел выключен.

    rifle->shoot();
    rifle->reload();

    pistol->setAutomaticMode();
    pistol -> checkAutomaticMode();

    // Пример работы с dynamic_cast (если нужно доступ к методам производного класса через базовый указатель)
    std::unique_ptr<FirearmsClass> ptr = std::make_unique<PistolClass>(10, "7.62mm", 1.0f, "TT", false);
    ptr->shoot();

    //ptr -> setAutomaticMode();
    // не сработает, потому что ptr типа FirearmsClass, в таком классе нет реализации setAutomaticMode()
    // shoot() сработал, потому что он определён в базовом классе, и тем более в дочернем и в нём он переопределяется

    // ptr.get() - проверяем, возвращает ли ptr какой-нибудь сырой указатель на управляемый объект
    // управляемый объект — это объект в динамической памяти (heap), владение которым взял на себя умный указатель
    // dynamic_cast пытается привести объект, на который указывает ptr, к типу PistolClass
    // Если объект действительно является PistolClass (или его наследником), p получит адрес объекта
    // в противном случае dynamic_cast вернёт пустой указатель (nullptr)
    PistolClass *p = dynamic_cast<PistolClass*>(ptr.get());

    // как раз проверка на пустой указатель
    if (p != nullptr) {
        p->setAutomaticMode();
    }

    // работа с обычными указателями
    raw_pistol->shoot();    // Вызов метода через обычный указатель
    raw_rifle->toggleScope(); // Вызов метода через обычный указатель

    // освобождение памяти, выделенной через new
    // обязательно нужно вызвать delete для обычных указателей, чтобы избежать утечек памяти
    delete raw_pistol;
    delete raw_rifle;

    return 0;
}
