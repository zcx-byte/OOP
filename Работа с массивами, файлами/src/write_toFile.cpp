#include "../include/formulas.h"
#include <iostream>
#include <fstream>

namespace write_toFile{

    // функция для записи вектора в файл
    void writeVectorToFile(const std::vector<double>& vec, double sum_v,  std::ofstream& file) {

        if (file.is_open()){
            file << "результат вычислений c помощью вектора: \n";
            file << sum_v;
            file << std::endl;
            std::cout << "Данные вектора записаны в файл.\n";
        } else {
            std::cout << "Не удалось открыть файл для записи вектора.\n";
        }
    }

    // функция для записи массива и его результата в файл
    void writeArrayToFile(const double *arr, size_t size, double sum_arr,  std::ofstream& file) {

        // проверка, если передали пустой массив
        if (arr == nullptr){
            throw std::invalid_argument("в массив ничего не записано или он не создан");
        }

        if (file.is_open()) {
            file << "результат вычислений с помощью массива: \n";
            file << sum_arr;
            file << std::endl;
            std::cout << "Данные массива записаны в файл.\n";
        } else {
            std::cout << "Не удалось открыть файл для записи массива.\n";
        }
    }

}