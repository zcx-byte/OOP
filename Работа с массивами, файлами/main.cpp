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

    SetConsoleOutputCP(CP_UTF8);

    // вызываем функцию для заполнения массива или вектора случайными числами
    srand(time(nullptr)); 

    const string filename = "mass.txt";

    // ! Вектор
    cout << "=СНАЧАЛА С ПОМОЩЬЮ ВЕКТОРА=" << endl;

    // читаем вектор из файла
    // данная функция открывает, читает и закрывает файл
    // простыми словами вектор - это массив, в который можно добавлять элементы и он сам увеличивается
    // не нужно вручную выделять и освобождать память, к элементам можно обращаться так же по индексу
    // сам "растянется", если заранее не известен размер
    vector<double> vec;

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
    cout << "=ТЕПЕРЬ С ПОМОЩЬЮ МАССИВА=" << endl;

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

    ofstream output_file("result.txt");

    if (output_file.is_open()) {

        // записываем результат вектора в файл
        write_toFile::writeVectorToFile(vec, sum_v, output_file);       // передаём file

        try { 

            // записываем результат массива в файл
            write_toFile::writeArrayToFile(arr, size, sum_arr, output_file);

        } catch (const invalid_argument& e) {
        
            cerr << "Ошибка при записи массива: " << e.what() << endl;
    
            return 1;
        }

        output_file.close();

    } else {

        cout << "Не удалось открыть файл для записи.\n";

    }

    // очищаем массив
    delete[] arr;

    return 0;

}