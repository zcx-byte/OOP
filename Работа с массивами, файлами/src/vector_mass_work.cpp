#include "../include/formulas.h"
#include <iostream>
#include <fstream>
#include <stdexcept>

namespace vector_mass_work{

    // функция для чтения вектора из файла
    std::vector<double> readVectorFromFile(const std::string& filename){

        // открываем файл
        std::ifstream file(filename);

        // проверяем на открытие файла
        if (!file.is_open()) {
            // данная ошибка происходит во время выполнения программы
            throw std::runtime_error("Ошибка: не удалось открыть файл " + filename);
        }

        std::vector<double> numbers;
        double value;

        // читаем числа из файла пока есть данные
        while (file >> value) {
            // добавляем значение в конец вектора
            numbers.push_back(value);
        }

        // закрываем файл
        file.close();

        return numbers;
    }

    // функция для вывода вектора в консоль
    void printVector(const std::vector<double>& vec){

        // auto - тут компилятор автоматически определит тип переменной (в данном случае double)
        for (const auto &num : vec){
            std::cout << num << " ";
        }
        std::cout << std::endl;
    }

    // функция для заполнения вектора рандомными числами
    void fillVectorWithRandom(std::vector<double>& vec, double min, double max) {

        for (auto& element : vec) {
            // используем функцию из random_utils для генерации значений
            element = random_utils::getRandomValue(min, max);
        }
    }

    // функция для ручного заполнения вектора
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