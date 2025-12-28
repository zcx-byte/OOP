#ifndef FORMULAS_H
#define FORMULAS_H

#include <vector>
#include <fstream>

using namespace std;

namespace random_utils {
    /* 
    функция для генерации случайного числа в диапазоне [min, max]
    возвращает случайное вещественное (double) число в диапазоне [min, max)
    */
    double getRandomValue(double min, double max);
}

namespace array_work {
    /*
    функция для создания и заполнения массива случайными числами
    принимает размер массива типа size_t, и диапазон заполнения от min до max
    возвращает указатель на созданный массив типа double
    */
    double *createAndFillArray(size_t size, double min, double max);

    /*
    функция для вывода массива
    принимает указатель на массив и его размер
    */
    void printArray(const double *arr, size_t size);

    /*
    функция для заполнения массива вручную
    принимает указатель на массив и его размер
    */
    void fillArr(double *arr, size_t size);

}

namespace vector_mass_work {

    /*
    функция для вывода вектора
    принимает константную ссылку на вектор
    */
    void printVector(const vector<double>& vec);

    /*
    функция для заполнения вектора случайными числами
    принимает ссылку на вектор и диапазон заполнения
    */
    void fillVectorWithRandom(vector<double>& vec, double min, double max);

    /*
    функция для ручного заполнения вектора
    принимает ссылку на вектор и его размер
    возвращает заполненный вектор
    */
    vector<double> fillVector(vector<double>& vec, size_t size);

}

namespace result {

    /*
    функция для вычисления суммы квадратов разностей (sqrt|x| - x) для вектора в виде шаблона
    принимает константную ссылку на вектор
    возвращает результат типа T
    */
    template<typename T>
    T res_vector(const std::vector<T>& vec);

    /*
    функция для вычисления результата суммы квадратов разностей (sqrt|x| - x) для массива в виде шаблона
    принимает указатель на массив и его размер
    возвращает результат типа T
    */
    template<typename T>
    T res_mass(const T *arr, size_t size);
}

namespace save_res {

    /*
    функция для общего записи результата в файл
    принимает имя файла, вектор, сумму элементов вектора, массив, его размер и сумму элементов массива
    */
    void saveResultsToFile(const string& filename, 
        const vector<double>& vec, double sum_v,
        const double* arr, size_t size, double sum_arr);

}

namespace file_work {

    /*
    функция для чтения вектора из файла
    принимает имя файла
    возвращает вектор типа double
    */
   vector<double> readVectorFromFile(const string& filename);

   /*
    функция для записи вектора в файл
    принимает константную ссылку на вектор, сумму элементов вектора и ссылку на файл
    */
   void writeVectorToFile(const std::vector<double>& vec, double sum_v,  std::ofstream& file);

   /*
    функция для подсчёта количества значений в файле
    принимает ссылку на входной файл
    возвращает количество успешно прочитанных значений
    */
   int count_values(std::ifstream& input_file);

   /*
    функция для записи массива в файл
    принимает указатель на массив, его размер, сумму элементов массива и ссылку на файл
    */
   void writeArrayToFile(const double *arr, size_t size, double sum_arr,  std::ofstream& file);

    /*
    функция для заполнения массива из файла
    принимает имя файла, указатель на массив и его размер
    */
   void fillArrFromFile(const string& filename, double* arr, size_t size);
    
} // namespace file_work


#endif