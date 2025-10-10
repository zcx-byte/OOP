#ifndef FORMULAS_H
#define FORMULAS_H

#include <vector>
#include <fstream>

using namespace std;

namespace random_utils {
    // функция для генерации случайного числа в диапазоне [min, max]
    double getRandomValue(double min, double max);
}

namespace array_work {
    // функция для создания и заполнения массива случайными числами
    double *createAndFillArray(size_t size, double min, double max);

    // функция для вывода массива
    void printArray(const double *arr, size_t size);

    void fillMass(double *arr, size_t size);
}

namespace vector_mass_work {
    // функция для чтения вектора из файла
    vector<double> readVectorFromFile(const string& filename);

    // функция для вывода вектора
    void printVector(const vector<double>& vec);

    // функция для заполнения вектора рандомными числами
    void fillVectorWithRandom(vector<double>& vec, double min, double max);

    // функция по ручному заполнению вектора
    vector<double> fillVector(vector<double>& vec, size_t size);
}

namespace write_toFile {
    // функция для записи вектора в файл
    void writeVectorToFile(const vector<double>& vec, double sum_v, ofstream& file);

    // функция для записи массива и его результата в файл
    void writeArrayToFile(const double* arr, size_t size, double sum_arr, ofstream& file);
}

namespace result {
    // функция для вычисления результата с помощью вектора
    double res_vector(const vector<double>& vec);

    // функция для вычисления результата с помощью массива
    double res_mass(const double *arr, size_t size);
}

namespace save_res {

    // функция для общего записи результата в файл
    void saveResultsToFile(const string& filename, 
        const vector<double>& vec, double sum_v,
        const double* arr, size_t size, double sum_arr);

}

#endif