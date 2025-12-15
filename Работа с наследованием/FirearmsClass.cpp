#include "FirearmsClass.hpp"
#include <iostream>

FirearmsClass::FirearmsClass(int gun_clip, const std::string& calibre_type, float weight)
    : gun_clip(gun_clip), calibre_type(calibre_type), weight(weight) {}

int FirearmsClass::getGunClip() const {
    return gun_clip;
}

// PistolClass
PistolClass::PistolClass(int gun_clip, const std::string& calibre_type, float weight, const std::string& model)
    : FirearmsClass(gun_clip, calibre_type, weight), model(model) {}  // ← model инициализируется!

void PistolClass::shoot() const {
    if (gun_clip > 0) {
        std::cout << "Пистолет " << model << " стреляет! Осталось: " << (gun_clip - 1) << std::endl;
    } else {
        std::cout << "Пистолет " << model << " пуст!" << std::endl;
    }
}

void PistolClass::reload() {
    gun_clip = 15;
    std::cout << "Пистолет " << model << " перезаряжен." << std::endl;
}

std::string PistolClass::showModel() const {
    return model;
}

// RifleClass
RifleClass::RifleClass(int gun_clip, const std::string& calibre_type, float weight, bool hasScope)
    : FirearmsClass(gun_clip, calibre_type, weight), hasScope(hasScope) {}

void RifleClass::shoot() const {
    if (gun_clip > 0) {
        std::cout << "Винтовка (" << (hasScope ? "с прицелом" : "без прицела") << ") выстрелила! осталось: " << (gun_clip - 1)
        << std::endl;
    } else {
        std::cout << "Винтовка пуста!" << std::endl;
    }
}

void RifleClass::reload() {
    gun_clip = 30;
    std::cout << "Винтовка перезаряжена." << std::endl;
}

void RifleClass::toggleScope() {
    hasScope = !hasScope;
    std::cout << "Прицел " << (hasScope ? "включён" : "выключен") << "." << std::endl;
}