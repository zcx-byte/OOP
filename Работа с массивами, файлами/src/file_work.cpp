#include "../include/formulas.h"
#include <iostream>
#include <fstream>
#include <stdexcept>

namespace file_work {

    /*
    функция для подсчёта количества значений в файле
    принимает ссылку на входной файл
    возвращает количество успешно прочитанных значений
    */
    int count_values(std::ifstream& input_file) {
        int count = 0;
        double value;
        while (input_file >> value) {
            count++;
        }
        return count;
    }
    
    /*
    функция для чтения вектора из файла
    принимает имя файла
    возвращает вектор типа double
    */
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

    /*
    функция для записи вектора в файл
    принимает константную ссылку на вектор, сумму элементов вектора и ссылку на файл
    */
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

    /*
    функция для заполнения массива из файла
    принимает имя файла, указатель на массив и его размер
    */
    // ! память под массив должна быть выделена заранее
    void fillArrFromFile(const std::string& filename, double* arr, size_t size) {

        if (arr == nullptr) {
            throw std::invalid_argument("Переданный массив (arr) равен nullptr в fillArrFromFile.");
        }

        if (size == 0) {
             throw std::invalid_argument("Размер массива (size) равен 0 в fillArrFromFile.");
        }

        std::ifstream file(filename);
        if (!file.is_open()) {

            // Бросаем исключение, так как это ошибка, которую нужно обработать в вызывающем коде
            throw std::runtime_error("Ошибка: не удалось открыть файл " + filename + " в fillArrFromFile.");
        }

        // Предполагаем, что файл содержит ровно 'size' чисел
        // Иначе нужно будет проверять результат операции извлечения
        for (size_t i = 0; i < size; ++i) {
            if (!(file >> arr[i])) { // Проверяем, удалось ли прочитать
                 file.close();
                 throw std::runtime_error("Ошибка: не удалось прочитать " + std::to_string(i + 1) + "-е число из файла " + filename + ".");
            }
        }

        file.close();
        std::cout << "Файл " << filename << " успешно загружен в массив размером " << size << "." << std::endl;
    }

    /*
    функция для записи массива в файл
    принимает указатель на массив, его размер, сумму элементов массива и ссылку на файл
    */
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

} // namespace file_work
