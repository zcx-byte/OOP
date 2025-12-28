#include "../include/formulas.h"
#include <iostream>
#include <fstream>
#include <stdexcept>

namespace vector_mass_work{

    /*
    функция для вывода вектора
    принимает константную ссылку на вектор
    */
    void printVector(const std::vector<double>& vec){

        // auto - тут компилятор автоматически определит тип переменной (в данном случае double)
        for (const auto &num : vec){
            std::cout << num << " ";
        }
        std::cout << std::endl;
    }

    /*
    функция для заполнения вектора случайными числами
    принимает ссылку на вектор и диапазон заполнения
    */
    void fillVectorWithRandom(std::vector<double>& vec, double min, double max) {

        for (auto& element : vec) {
            // используем функцию из random_utils для генерации значений
            element = random_utils::getRandomValue(min, max);
        }
    }

    /*
    функция для ручного заполнения вектора
    принимает ссылку на вектор и его размер
    возвращает заполненный вектор
    */
    std::vector<double> fillVector(std::vector<double>& vec, size_t size) {

        for (size_t i = 0; i < size; ++i) {
            double value;
            std::cout << "Элемент " << (i + 1) << ": ";
            std::cin >> value;
            vec.push_back(value);
        }

        return vec;
    }

}