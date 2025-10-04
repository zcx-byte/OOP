#include <iostream>
#include <fstream>
#include "formulas.h"
#include <cmath>
#include <cassert>
#include <stdexcept>   
#include <cstdlib>      // для rand и srand
#include <ctime>        // для time
#include <limits>       // для numeric_limits

using namespace std;

namespace random_utils {

    // функция для генерации случайного числа в диапазоне [min, max]
    double getRandomValue(double min, double max) {

        // проверяем, не являются ли min или max NaN
        // || - или
        if (isnan(min) || isnan(max)) {
            throw std::invalid_argument("Ошибка: min или max — не числа (NaN)!");
        }

        if (min > max) {
            throw std::invalid_argument("Ошибка: минимальное значение больше максимального!");
        }

        // static_cast<double> - явное приведение к определённому типу данных (в данном случае к double)
        // целое число, которое вернул rand() (например, 3000), становится вещественным (3000.0)
        // чтобы не было проблем в целочисленном делении
        // RAND_MAX - максимальное число, которое может вернуть rand(), обычно 32767 или больше
        // static_cast<double>(rand()) / RAND_MAX - нормализуем отрезки [0; 1)
        return min + static_cast<double>(rand()) * (max - min) / RAND_MAX;
    }
    

}

namespace array_work {
    
    // функция для создания и заполнения массива случайными числами
    double *createAndFillArray(int size, double min, double max) {
        
        // выделяем память для массива
        double *arr = new double[size];
        
        // заполняем массив случайными числами
        for (int i = 0; i < size; i++) {

            arr[i] = random_utils::getRandomValue(min, max);

        }
        
        return arr;
    }
    
    // функция для вывода массива
    void printArray(const double* arr, int size) {

        for (int i = 0; i < size; i++) {

            cout << "arr[" << i << "] = " << arr[i] << endl;

        }

    }
    
    // функция для удаления массива
    void deleteArray(double* arr) {

        delete[] arr;

    }
}

namespace vector_mass_work{
    
    vector<double> readVectorFromFile(const string& filename){

        ifstream file(filename);

        if (!file.is_open()) {
            cerr << "Ошибка: не удалось открыть файл " << filename << endl;
            return {}; // возвращаем пустой вектор
        }

        vector<double> numbers;
        double value;

        while (file >> value) {
            // добавляем значение в конец вектора
            numbers.push_back(value);
        }

        file.close();
        return numbers;
    }

    // функция для вывода вектора
    void printVector(const vector<double>& vec){
        
        // auto - тут компилятор автоматически определит тип переменной (в данном случае double)
        for (const auto& num : vec){
            cout << num << " ";
        }
        cout << endl;             
    }

    // функция для заполнения вектора рандомными числами
    void fillVectorWithRandom(vector<double>& vec, double min, double max) {
        static bool first_call = true;

        if (first_call) {

            srand(time(nullptr));

            first_call = false;
        }
        
        for (auto& element : vec) {

            // используем функцию из random_utils для генерации значений
            element = random_utils::getRandomValue(min, max);
        }
    }
}