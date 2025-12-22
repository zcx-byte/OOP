#ifndef FIREARMS_CLASS_HPP
#define FIREARMS_CLASS_HPP

#include <string>

/**
 * @brief Абстрактный базовый класс огнестрельного оружия.
 */
class FirearmsClass {
protected:
    std::string model;
    int gun_clip;
    std::string calibre_type;
    float weight;

public:
    /**
     * @brief Конструктор.
     * @param gun_clip Количество патронов в магазине.
     * @param calibre_type Калибр патрона.
     * @param weight Вес оружия (кг).
     * @param model Модель оружия.
     */
    FirearmsClass(int gun_clip, const std::string& calibre_type, float weight, const std::string& model);

    virtual ~FirearmsClass() = default;

    /// @brief Произвести выстрел.
    virtual void shoot() = 0;

    /// @brief Перезарядить оружие.
    virtual void reload() = 0;

    /// @brief Получить текущее количество патронов.
    /// @return Количество патронов.
    int getGunClip() const;

    /// @brief Получить калибр.
    /// @return Строка с калибром.
    std::string getCalibreType() const { return calibre_type; }

    /// @brief Получить вес.
    /// @return Вес в килограммах.
    float getWeight() const { return weight; }

    /// @brief Получить модель.
    /// @return Название модели.
    std::string getModel() const { return model; }
};

/**
 * @brief Класс пистолета.
 */
class PistolClass : public FirearmsClass {
private:
    bool automatic;

public:
    /**
     * @brief Конструктор.
     * @param gun_clip Количество патронов.
     * @param calibre_type Калибр.
     * @param weight Вес (кг).
     * @param model Модель.
     * @param automatic Режим стрельбы (по умолчанию — false).
     */
    PistolClass(int gun_clip, const std::string& calibre_type, float weight, const std::string& model, bool automatic = false);

    void shoot() override;
    void reload() override;

    /// @brief Переключить режим стрельбы.
    void setAutomaticMode();
};

/**
 * @brief Класс винтовки.
 */
class RifleClass : public FirearmsClass {
private:
    bool hasScope;

public:
    /**
     * @brief Конструктор.
     * @param gun_clip Количество патронов.
     * @param calibre_type Калибр.
     * @param weight Вес (кг).
     * @param model Модель.
     * @param hasScope Наличие прицела (по умолчанию — false).
     */
    RifleClass(int gun_clip, const std::string& calibre_type, float weight, const std::string& model, bool hasScope = false);

    void shoot() override;
    void reload() override;

    /// @brief Переключить прицел.
    void toggleScope();
};

#endif // FIREARMS_CLASS_HPP