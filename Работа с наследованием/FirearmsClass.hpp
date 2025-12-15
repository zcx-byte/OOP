#ifndef FIREARMS_CLASS_HPP
#define FIREARMS_CLASS_HPP  

#include <string>

class FirearmsClass {
protected:
    int gun_clip;
    std::string calibre_type;
    float weight;

public:
    FirearmsClass(int gun_clip, const std::string& calibre_type, float weight);
    virtual ~FirearmsClass() = default;

    virtual void shoot() const = 0;      
    virtual void reload() = 0;

    int getGunClip() const;             
    std::string getCalibreType() const { return calibre_type; }
    float getWeight() const { return weight; }
};

class PistolClass : public FirearmsClass {
private:
    std::string model;

public:
    
    PistolClass(int gun_clip, const std::string& calibre_type, float weight, const std::string& model);

    void shoot() const override;
    void reload() override;

    // const, т.к. не меняет объект
    std::string showModel() const;
};

class RifleClass : public FirearmsClass {
private:
    bool hasScope;

public:
    
    RifleClass(int gun_clip, const std::string& calibre_type, float weight, bool hasScope = false);

    void shoot() const override;
    void reload() override;
    void toggleScope();
};

#endif // FIREARMS_CLASS_HPP