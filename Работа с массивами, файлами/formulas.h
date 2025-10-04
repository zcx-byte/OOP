#ifndef FORMULAS_H
#define FORMULAS_H

#include <vector>
#include <string>

using namespace std;

namespace vector_mass_work{

    // функция для чтения данных из файла с помощью вектора
    vector<double> readVectorFromFile(const string& filename);

    // функция для вывода вектора
    void printVector(const vector<double>& vec);

    // функция для заполнения вектора случайными числами
    void fillVectorWithRandom(vector<double>& vec, double min, double max);
}

namespace array_work {
    
    // функция для создания и заполнения массива случайными числами
    double* createAndFillArray(int size, double min, double max);
    
    // функция для вывода массива
    void printArray(const double* arr, int size);
    
    // функция для удаления массива
    void deleteArray(double* arr);
}

namespace random_utils {

    // функция для генерации случайного числа в диапазоне [min, max]
    double getRandomValue(double min, double max);

}

#endif