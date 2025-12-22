#include "FirearmsClass.hpp"
#include <iostream>

// --- FirearmsClass ---

/**
 * @brief Конструктор базового класса FirearmsClass.
 */
FirearmsClass::FirearmsClass(int gun_clip, const std::string& calibre_type, float weight, const std::string& model)
    : gun_clip(gun_clip), calibre_type(calibre_type), weight(weight), model(model) {}

/**
 * @brief Возвращает текущее количество патронов в магазине.
 */
int FirearmsClass::getGunClip() const {
    return gun_clip;
}

// --- PistolClass ---

/**
 * @brief Конструктор класса PistolClass.
 */
PistolClass::PistolClass(int gun_clip, const std::string& calibre_type, float weight, const std::string& model, bool automatic)
    : FirearmsClass(gun_clip, calibre_type, weight, model), automatic(automatic) {}

/**
 * @brief Реализация выстрела для пистолета.
 */
void PistolClass::shoot() {
    if (gun_clip > 0) {
        std::cout << "Пистолет " << model << " стреляет! Осталось: " << (gun_clip - 1) << std::endl;
        --gun_clip;
    } else {
        std::cout << "Пистолет " << model << " пуст!" << std::endl;
    }
}

/**
 * @brief Перезарядка пистолета (до 15 патронов).
 */
void PistolClass::reload() {
    gun_clip = 15;
    std::cout << "Пистолет " << model << " перезаряжен." << std::endl;
}

/**
 * @brief Переключение режима стрельбы (полуавтомат ↔ автомат).
 */
void PistolClass::setAutomaticMode() {
    automatic = !automatic;
    std::cout << "Пистолет " << model 
              << (automatic ? " переведён на автоматический огонь." : " переведён на полуавтоматический огонь.")
              << std::endl;
}

// --- RifleClass ---

/**
 * @brief Конструктор класса RifleClass.
 */
RifleClass::RifleClass(int gun_clip, const std::string& calibre_type, float weight, const std::string& model, bool hasScope)
    : FirearmsClass(gun_clip, calibre_type, weight, model), hasScope(hasScope) {}

/**
 * @brief Реализация выстрела для винтовки (с учётом прицела).
 */
void RifleClass::shoot() {
    if (gun_clip > 0) {
        std::cout << "Винтовка (" << model << (hasScope ? " с прицелом" : " без прицела")
                  << ") выстрелила! Осталось: " << (gun_clip - 1) << std::endl;
        --gun_clip;
    } else {
        std::cout << "Винтовка " << model << " пуста!" << std::endl;
    }
}

/**
 * @brief Перезарядка винтовки (до 30 патронов).
 */
void RifleClass::reload() {
    gun_clip = 30;
    std::cout << "Винтовка " << model << " перезаряжена." << std::endl;
}

/**
 * @brief Переключение состояния оптического прицела.
 */
void RifleClass::toggleScope() {
    hasScope = !hasScope;
    std::cout << "Прицел " << (hasScope ? "включён" : "выключен") << "." << std::endl;
}