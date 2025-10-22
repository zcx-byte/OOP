#include "../include/formulas.h"
#include <iostream>
#include <fstream>
#include <stdexcept>
#include <limits>       // для numeric_limits

namespace array_work {

    // функция для создания и заполнения массива случайными числами
    double *createAndFillArray(size_t size, double min, double max) {

        // выделяем память для массива
        double *arr = new double[size];

        // проверка корректности размера массива
        if (size <= 0){
            throw std::invalid_argument("размер не может быть отрицательным или равным 0");
        } else {
            // заполняем массив случайными числами
            for (size_t i = 0; i < size; i++) {

                arr[i] = random_utils::getRandomValue(min, max);

            }
        }

        return arr;
    }

    // функция для вывода массива в консоль
    void printArray(const double *arr, size_t size) {

        // проверка, если передали пустой массив
        if (arr == nullptr){
            throw std::invalid_argument("в массив ничего не записано или он не создан");
        }

        // проверка корректности размера массива
        if (size <= 0){
            throw std::invalid_argument("размер не может быть отрицательным или равным 0");
        }

        // поэлементный вывод массива
        for (int i = 0; i < size; i++) {
            std::cout << "arr[" << i << "] = " << arr[i] << std::endl;
        }
    }

    // функция для ручного заполнения массива с клавиатуры
    void fillMass(double *arr, size_t size){

        // проверка корректности размера массива
        if (size <= 0){
            throw std::invalid_argument("размер не может быть отрицательным или равным 0");
        }

        // запрос ввода для каждого элемента массива
        for (int i = 0; i < size; ++i) {
            std::cout << "Элемент " << (i + 1) << ": ";
            std::cin >> arr[i];
        }
    }

    // функция для записи данных из файла в массив
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
}