#include "../include/formulas.h"
#include <cstdlib>      // для rand и srand
#include <ctime>        // для time
#include <cmath>        // для isnan
#include <stdexcept>    // для исключений

namespace random_utils {

    /* 
    функция для генерации случайного числа в диапазоне [min, max]
    возвращает случайное вещественное (double) число в диапазоне [min, max)
    */    
    double getRandomValue(double min, double max) {

        // проверяем, не являются ли min или max NaN
        // || - или
        if (std::isnan(min) || std::isnan(max)) {
            throw std::invalid_argument("min или max — не числа (NaN)!");
        }

        // проверка корректности диапазона
        if (min > max) {
            throw std::invalid_argument("минимальное значение больше максимального!");
        }

        // static_cast<double> - явное приведение к определённому типу данных (в данном случае к double)
        // целое число, которое вернул rand() (например, 3000), становится вещественным (3000.0)
        // чтобы не было проблем в целочисленном делении
        // RAND_MAX - максимальное число, которое может вернуть rand(), обычно 32767 или больше
        // static_cast<double>(rand()) / RAND_MAX - нормализуем отрезки [0; 1)
        return min + static_cast<double>(std::rand()) * (max - min) / RAND_MAX;
    }

}