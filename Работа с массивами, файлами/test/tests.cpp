#include <windows.h>
#include "../include/formulas.h" // Подключаем наш заголовок
#include <iostream>
#include <vector>
#include <cassert> // Подключаем assert
#include <cmath>   // Подключаем cmath для abs и sqrt

int main() {

    SetConsoleOutputCP(CP_UTF8);

    std::cout << "Запуск тестов...\n";

    // --- Тестируем функцию res_vector ---
    std::cout << "Тестируем res_vector...\n";

    // Создадим вектор с простыми числами
    std::vector<double> test_vec = {0.0, 1.0, 4.0};

    // Рассчитаем вручную:
    // (sqrt(|0|) - 0)^2 + (sqrt(|1|) - 1)^2 + (sqrt(|4|) - 4)^2
    // (sqrt(0) - 0)^2 + (sqrt(1) - 1)^2 + (sqrt(4) - 4)^2
    // (0 - 0)^2 + (1 - 1)^2 + (2 - 4)^2
    // 0^2 + 0^2 + (-2)^2
    // 0 + 0 + 4 = 4
    double expected_result = 4.0;

    // Вызываем нашу функцию
    double actual_result = result::res_vector(test_vec);

    // Проверяем, равен ли результат ожидаемому
    assert(actual_result == expected_result);
    std::cout << "  res_vector({0, 1, 4}) = " << actual_result << " - OK\n";

    // --- Тестируем функцию res_mass ---
    std::cout << "Тестируем res_mass...\n";

    // Создадим простой массив
    double test_arr[] = {0.0, 1.0, 4.0};
    size_t size = 3;

    // Ожидаемый результат тот же
    double expected_result_arr = 4.0;

    // Вызываем функцию для массива
    double actual_result_arr = result::res_mass(test_arr, size);

    // Проверяем результат
    assert(actual_result_arr == expected_result_arr);
    std::cout << "  res_mass({0, 1, 4}) = " << actual_result_arr << " - OK\n";

    // --- Тестируем функцию getRandomValue ---
    std::cout << "Тестируем getRandomValue...\n";

    // Проверим, что значение в нужном диапазоне
    double min = 5.0;
    double max = 10.0;
    double random_val = random_utils::getRandomValue(min, max);

    // Проверяем, что значение не меньше минимума
    assert(random_val >= min);
    // Проверяем, что значение не больше максимума
    assert(random_val <= max);
    std::cout << "  getRandomValue(5, 10) = " << random_val << " - OK (в диапазоне)\n";

    // --- Тестируем функцию fillVectorWithRandom ---
    std::cout << "Тестируем fillVectorWithRandom...\n";

    std::vector<double> vec_to_fill(3); // Создаем вектор размером 3
    double fill_min = 1.0;
    double fill_max = 2.0;

    // Заполняем вектор случайными числами
    vector_mass_work::fillVectorWithRandom(vec_to_fill, fill_min, fill_max);

    // Проверим, что все элементы в нужном диапазоне
    for (double val : vec_to_fill) {
        assert(val >= fill_min);
        assert(val <= fill_max);
    }
    std::cout << "  fillVectorWithRandom заполнил 3 элемента в диапазоне [1, 2] - OK\n";

    std::cout << "Все тесты пройдены!\n";
    return 0;
}