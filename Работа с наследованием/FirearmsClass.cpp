#include "FirearmsClass.hpp"
#include <stdexcept>
#include <string>
#include <iostream>

class FirearmsClass {
private:
    int gun_clip;
    std::string calibre_type;
    float weight;

public:

// ? нужно ли указывать конструктор в cpp и В hpp файлах класса
FirearmsClass(int gun_clip, const std::string calibre_type, float weight)
    : gun_clip(gun_clip), calibre_type(calibre_type), weight(weight) {}

virtual ~FirearmsClass() {};

virtual void shoot() const = 0;

virtual void reload() {
    int new_gun_clip = gun_clip;

};

virtual int getGunClip() {
    return gun_clip;
};

};

class PistolClass : public FirearmsClass {
private:
    std::string model;

public:
    PistolClass(int gun_clip, const std::string calibre_type, float weight)
    : FirearmsClass(gun_clip, calibre_type, weight), model(model) {}

    std::string showModel(){
        return model;

    };
};

class RifleClass : public FirearmsClass {
private:
    bool hasScope;

public:
    RifleClass(int gun_clip, const std::string calibre_type, float weight)
    : FirearmsClass(gun_clip, calibre_type, weight), hasScope(hasScope){}

    void toggleScope() {

        hasScope = !hasScope;

        std::cout << "Прицел " << (hasScope ? "включён" : "выключен") << "." << std::endl;

    }


};