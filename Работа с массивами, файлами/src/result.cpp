#include "../include/formulas.h"
#include <cmath>        // для sqrt, abs
#include <stdexcept>    // для исключений (если нужно)

namespace result {

    
    /*
    функция для вычисления суммы квадратов разностей (sqrt|x| - x) для вектора в виде шаблона
    принимает константную ссылку на вектор
    возвращает результат типа T
    */
    template<typename T>
    T res_vector(const std::vector<T>& vec){

        T sum_v = T{}; // инициализация нулём нужного типа

        // вычисляем для каждого элемента вектора
        for (size_t i = 0; i < vec.size(); i++){
            T res = std::sqrt(std::abs(vec[i])) - vec[i];
            sum_v += res * res;  // суммируем квадраты разностей
        }

        return sum_v;
    }

    /*
    функция для вычисления результата суммы квадратов разностей (sqrt|x| - x) для массива в виде шаблона
    принимает указатель на массив и его размер
    возвращает результат типа T
    */
    template<typename T>
    T res_mass (const T *arr, size_t size){

        // проверка, если передали пустой массив
        if (size == 0){
            throw std::invalid_argument("в массив ничего не записано или он не создан");
        }

        T sum_arr = T{}; // инициализация нулём нужного типа

        // вычисляем для каждого элемента массива
        for (size_t i = 0; i < size; i++) {
            T res = std::sqrt(std::abs(arr[i])) - arr[i];
            sum_arr += res * res;  // суммируем квадраты разностей
        }

        return sum_arr;
    }

    // явное инстанцирование для double, чтобы использовать в других .cpp файлах
    template double res_vector<double>(const std::vector<double>& vec);

    template double res_mass<double>(const double *arr, size_t size);

}