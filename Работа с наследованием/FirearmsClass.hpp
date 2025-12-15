#ifndef FIREARMS_CLASS_HPP

#include <string>
#include <stdexcept>

class FirearmsClass{
private:
    int gun_clip;
    std::string calibre_type;
    float weight;

public:
    FirearmsClass(int gun_clip, const std::string calibre_type, float weight)
    : gun_clip(gun_clip), calibre_type(calibre_type), weight(weight) {}

    virtual ~FirearmsClass();

    virtual void shoot() const = 0;

    virtual void reload();

    virtual int getGunClip() const;
};

class PistolClass : public FirearmsClass {
private:
    std::string model;

public:
    PistolClass(int gun_clip, const std::string calibre_type, float weight)
    : FirearmsClass(gun_clip, calibre_type, weight), model(model) {}

    std::string showModel();

};

class RifleClass : public FirearmsClass {
private:
    bool hasScope;

public:
    RifleClass(int gun_clip, const std::string calibre_type, float weight)
    : FirearmsClass(gun_clip, calibre_type, weight), hasScope(hasScope){}

    void toggleScope();
};
#endif