/*

Автор: Прасков Даниил ИВТ - 24 - 2.

Задача: Даны натуральное число n, действительные числа a₁,..., aₙ. Вычислить:
н) (√|a₁| - a₁)² + ... + (√|aₙ| - aₙ)²;

*/

#include <iostream>
#include <windows.h>
#include "formulas.h"
#include <cmath>
#include <cassert>
#include <stdexcept>   
#include <cstdlib>      // для rand и srand
#include <ctime>        // для time

// для работы с файлами
#include <fstream>      
#include <iostream> 

// для работы с типом данных vector
#include <vector>

using namespace std;

int main(){

    int choose;

    SetConsoleOutputCP(CP_UTF8);

    // вызываем функцию для заполнения массива или вектора случайными числами
    srand(time(nullptr)); 

    const string filename = "mass.txt";

    vector<double> vec;

    cout << "Как желаете заполнить вектор и массив?" << endl;
    cout << "1. Вручную" << endl;
    cout << "2. Из файла" << endl;
    cout << "3. Случайно" << endl;
    cout << "Введите (1, 2 или 3) \n";

    cin >> choose;

    if (choose == 1){

        size_t size_v;

        cout << "Введите кол-во чисел в векторе"<< endl;
        cin >> size_v;
        
        try{

            vector_mass_work::fillVector(vec, size_v);

        } catch (const invalid_argument& e){

            cerr << "Ошибка при заполнении вектора: " << e.what() << endl;

            return 1;

        }

        cout << "числа, использованные в векторе:" << endl;
        vector_mass_work::printVector(vec);

        // ! массив
        size_t size_arr;

        cout << "Введите кол-во чисел в массиве"<< endl;
        cin >> size_arr;

        // выделяем память для массива
        double *arr = new double[size_arr];
        
        try{

            array_work::fillMass(arr, size_arr);

        } catch (const invalid_argument& e){

            cerr << "Ошибка при заполнении массива: " << e.what() << endl;

            return 1;

        }

        cout << "числа, использованные в векторе:" << endl;
        vector_mass_work::printVector(vec);

        cout << "числа, использованные в массиве:" << endl;
        array_work::printArray(arr, size_arr);

        double sum_v = result::res_vector(vec);

        double sum_arr = result::res_mass(arr, size_arr);

        cout << "Результат вектора = " << sum_v << endl;
        cout << "Результат массива = " << sum_arr << endl;

        save_res::saveResultsToFile("result.txt", vec, sum_v, arr, size_arr, sum_arr);

        delete[] arr;

        return 0;
    }

    if (choose == 2){

        // читаем вектор из файла
        // данная функция открывает, читает и закрывает файл
        // простыми словами вектор - это массив, в который можно добавлять элементы и он сам увеличивается
        // не нужно вручную выделять и освобождать память, к элементам можно обращаться так же по индексу
        // сам "растянется", если заранее не известен размер
        try{

            vec = vector_mass_work::readVectorFromFile(filename);

        } catch (const runtime_error& e) {

            cerr << "Ошибка при заполнении вектора: " << e.what() << endl;

            return 1;
        }

        if (vec.empty()){

            cout << "Файл пуст или не найден. Используем заполнение случайными числами" << endl;

            int min, max;
            size_t size;

            cout << "Введите размер вектора: \n";
            cin >> size;

            cout << "введите диапазон заполнения массива: \n";
            cout << "от: \n";
            cin >> min;

            cout << "до: \n";
            cin >> max;
            
            // vector имеет динамический размер, может увеличиваться и уменьшаться
            // данная операция изменяет размер вектора
            vec.resize(size);
            
            try {
                // используем нашу функцию по заполнению вектора рандомными числами
                vector_mass_work::fillVectorWithRandom(vec, min, max);
            }

            catch (const invalid_argument& e) {

                cerr << "Ошибка при заполнении вектора: " << e.what() << endl;

                return 1;
            }
        }

        cout << "числа, использованные в векторе:" << endl;
        vector_mass_work::printVector(vec);

        // ! решаем с помощью вектора
        double sum_v = result::res_vector(vec);

        // выводим сумму
        cout << "сумма в векторе равна = " << sum_v << endl;

        // ! Массив
        cout << "=ТЕПЕРЬ С ИСПОЛЬЗУЕМ МАССИВ=" << endl;

        double *arr = nullptr;  // по факту пока просто создаём массив
        size_t size = 0;   // соответственно и размер у него пока 0

        // снова его открываем для чтения
        ifstream input_file(filename);

        if (!input_file.is_open()) {

            cerr << "Ошибка: не удалось открыть файл " << filename << endl;

            return 1;
        }

        double value;
        int count = 0;

        while (input_file >> value){
            count++;
        }

        input_file.close();

        if (count == 0) {
            cout << "Файл пуст, используем заполнение рандомными числами" << endl;

            int min, max;

            cout << "Введите размер массива: \n";
            cin >> size;

            cout << "введите диапазон заполнения массива: \n";
            cout << "от: \n";
            cin >> min;

            cout << "до: \n";
            cin >> max;

            try {

                // и заполняем
                arr = array_work::createAndFillArray(size, min, max);

            }
            catch (const invalid_argument& e) {

                cerr << "Ошибка при создании массива: " << e.what() << endl;

                return 1;
            }

        } else {
            cout << "Файл содержит " << count << " чисел. Загружаем в массив." << endl;

            size = count;

            arr = new double[size];

            // снова открываем файл и читаем числа в массив
            ifstream file3(filename);

            for (int i = 0; i < size; i++) {

                file3 >> arr[i];

            }
            file3.close();
        }

        cout << "Числа, использованные в массиве" << endl;

        try{

            array_work::printArray(arr, size);

        } catch (const invalid_argument& e) {
            
            cerr << "Ошибка при создании массива: " << e.what() << endl;

            return 1;
        }

        // ! решаем с помощью массива
        double sum_arr;

        // проверяем на исключение
        try {

            sum_arr = result::res_mass(arr, size);

        } catch (const invalid_argument& e) {
            
            cerr << "Ошибка: " << e.what() << endl;

            return 1;
        }

        cout << "сумма в массиве равна = " << sum_arr << endl;

        save_res::saveResultsToFile("result.txt", vec, sum_v, arr, size, sum_arr);

        // очищаем массив
        delete[] arr;

        return 0;
    }

    if (choose == 3) {
        int min, max;
        size_t size_v;
        std::cout << "Введите размер вектора: ";
        std::cin >> size_v;
        std::cout << "Введите диапазон случайных чисел (от): ";
        std::cin >> min;
        std::cout << "до: ";
        std::cin >> max;
    
        try {

            vec.resize(size_v);
            vector_mass_work::fillVectorWithRandom(vec, min, max);
    
            std::cout << "Числа в векторе: ";
            vector_mass_work::printVector(vec);
    
            size_t size_arr;
            std::cout << "Введите размер массива: ";
            std::cin >> size_arr;
            std::cout << "Введите диапазон случайных чисел (от): ";
            std::cin >> min;
            std::cout << "до: ";
            std::cin >> max;
    
            double* arr = array_work::createAndFillArray(size_arr, min, max);
    
            std::cout << "Числа в массиве: ";
            array_work::printArray(arr, size_arr);
    
            double sum_v = result::res_vector(vec);
            double sum_arr = result::res_mass(arr, size_arr);
    
            std::cout << "Сумма вектора = " << sum_v << std::endl;
            std::cout << "Сумма массива = " << sum_arr << std::endl;
    
            save_res::saveResultsToFile("result.txt", vec, sum_v, arr, size_arr, sum_arr);
    
            delete[] arr;
        }

        catch (const std::invalid_argument& e) {

            std::cerr << "Ошибка при генерации или заполнении: " << e.what() << std::endl;

        }
        catch (const std::runtime_error& e) {

            std::cerr << "Ошибка выполнения: " << e.what() << std::endl;

        }
    }
}