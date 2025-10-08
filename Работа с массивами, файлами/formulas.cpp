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
            throw invalid_argument("min или max — не числа (NaN)!");
        }

        if (min > max) {
            throw invalid_argument("минимальное значение больше максимального!");
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
    double *createAndFillArray(size_t size, double min, double max) {
  
        // выделяем память для массива
        double *arr = new double[size];

        
        if (size <= 0){

            throw invalid_argument("размер не может быть отрицательным или равным 0");

        } else {

            // заполняем массив случайными числами
            for (size_t i = 0; i < size; i++) {
    
                arr[i] = random_utils::getRandomValue(min, max);
    
            }

        }

        return arr;
    }
    
    // функция для вывода массива
    void printArray(const double *arr, size_t size) {

        // проверка, если передали пустой массив
        if (arr == nullptr){

            throw invalid_argument("в массив ничего не записано или он не создан");

        }

        // проверка, если размер массива = или < 0
        if (size <= 0){

            throw invalid_argument("размер не может быть отрицательным или равным 0");

        }


        for (int i = 0; i < size; i++) {

            cout << "arr[" << i << "] = " << arr[i] << endl;

        }

    }

}

namespace vector_mass_work{
    
    // функция для чтения вектора из файла
    vector<double> readVectorFromFile(const string& filename){

        // открываем файл
        ifstream file(filename);

        // проверяем на открытие файла
        if (!file.is_open()) {

            // данная ошибка происходит во время выполнения программы
            throw runtime_error("Ошибка: не удалось открыть файл " + filename);

        }

        vector<double> numbers;

        double value;

        while (file >> value) {

            // добавляем значение в конец вектора
            numbers.push_back(value);
        }

        // закрываем файл
        file.close();

        return numbers;
    }

    // функция для вывода вектора
    void printVector(const vector<double>& vec){
        
        // auto - тут компилятор автоматически определит тип переменной (в данном случае double)
        for (const auto &num : vec){
            cout << num << " ";
        }
        cout << endl;             
    }

    // функция для заполнения вектора рандомными числами
    void fillVectorWithRandom(vector<double>& vec, double min, double max) {
                
        for (auto& element : vec) {

            // используем функцию из random_utils для генерации значений
            element = random_utils::getRandomValue(min, max);
        }
    }
}

namespace write_toFile{
    // функция для записи вектора в файл
    void writeVectorToFile(const vector<double>& vec, double sum_v,  ofstream& file) {
        
        if (file.is_open()){
            file << "результат вычислений c помощью вектора: \n";
            file << sum_v;
            file << endl;
            cout << "Данные вектора записаны в файл.\n";
        } else {

            cout << "Не удалось открыть файл для записи вектора.\n";
        }
    }

    // Функция для записи массива и его результата в файл
    void writeArrayToFile(const double *arr, size_t size, double sum_arr,  ofstream& file) {

        // проверка, если передали пустой массив
        if (arr == nullptr){

            throw invalid_argument("в массив ничего не записано или он не создан");

        }

        if (file.is_open()) {

            file << "результат вычислений с помощью массива: \n";
            file << sum_arr;
            file << endl;
            cout << "Данные массива записаны в файл.\n";
        } else {
            cout << "Не удалось открыть файл для записи массива.\n";
        }
    }

}

namespace result {

    // функция для решения задачи с помощью вектора
    double res_vector(const vector<double>& vec){
        
        double sum_v = 0.0;

        for (int i = 0; i < vec.size(); i++){

            double res = sqrt(abs(vec[i])) - vec[i];
    
            sum_v += res * res;
        }

        return sum_v;
    }
    
    // функция для решения задачи с помощью массива
    double res_mass (const double *arr, size_t size){

        // проверка, если передали пустой массив
        if (arr == nullptr){

            throw invalid_argument("в массив ничего не записано или он не создан");

        }

        double sum_arr = 0.0;

        for (int i = 0; i < size; i++) {

            double res = sqrt(abs(arr[i])) - arr[i];
    
            sum_arr += res * res;
        }

        return sum_arr;
    }

}