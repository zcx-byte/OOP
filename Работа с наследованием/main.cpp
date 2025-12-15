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
    // 
    std::unique_ptr<FirearmsClass> pistol = std::make_unique<PistolClass>(15, "9mm", 0.8f, "Glock 17");
    std::unique_ptr<FirearmsClass> rifle = std::make_unique<RifleClass>(30, ".223 Rem", 3.5f, true);;

    


}