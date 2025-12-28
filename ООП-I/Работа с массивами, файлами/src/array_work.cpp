#include "../include/formulas.h"
#include <iostream>
#include <fstream>
#include <stdexcept>
#include <limits>       // для numeric_limits

namespace array_work {

    /*
    функция для создания и заполнения массива случайными числами
    принимает размер массива типа size_t, и диапазон заполнения от min до max
    возвращает указатель на созданный массив типа double
    */
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

    /*
    функция для вывода массива
    принимает указатель на массив и его размер
    */
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

    /*
    функция для заполнения массива вручную
    принимает указатель на массив и его размер
    */
    void fillArr(double *arr, size_t size){

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
}