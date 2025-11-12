#include <windows.h>
#include "../include/formulas.h" // подключаем наш заголовок
#include <iostream>
#include <vector>
#include <cassert> // подключаем assert
#include <cmath>   // подключаем cmath для abs и sqrt

using namespace std;

int main() {

    SetConsoleOutputCP(CP_UTF8);

    cout << "запуск тестов...\n";

    // --- тестируем функцию res_vector ---
    cout << "тестируем res_vector...\n";

    // создадим вектор с простыми числами
    vector<double> test_vec_1 = {0.0, 1.0, 4.0};

    // рассчитаем вручную:
    // (sqrt(|0|) - 0)^2 + (sqrt(|1|) - 1)^2 + (sqrt(|4|) - 4)^2
    // (sqrt(0) - 0)^2 + (sqrt(1) - 1)^2 + (sqrt(4) - 4)^2
    // (0 - 0)^2 + (1 - 1)^2 + (2 - 4)^2
    // 0^2 + 0^2 + (-2)^2
    // 0 + 0 + 4 = 4
    double expected_result_1 = 4.0;

    vector<double> test_vec_2 = {-1.0, -4.0, 9.0};
    double expected_result_2 = 76.0;

    vector<double> test_vec_3 = {0.25, 2.25, 6.25};
    double expected_result_3 = 14.6875;

    // вызываем нашу функцию
    // todo описание функции
    double actual_result_1 = result::res_vector(test_vec_1);
    double actual_result_2 = result::res_vector(test_vec_2);
    double actual_result_3 = result::res_vector(test_vec_3);

    // проверяем, равен ли результат ожидаемому
    assert(actual_result_1 == expected_result_1);
    cout << "  res_vector_1({0, 1, 4}) = " << actual_result_1 << " - ok\n";

    assert(actual_result_2 == expected_result_2);
    cout << "  res_vector_2({-1.0, -4.0, 9.0}) = " << actual_result_2 << " - ok\n";

    assert(actual_result_3 == expected_result_3);
    cout << "  res_vector({0.25, 2.25, 6.25}) = " << actual_result_3 << " - ok\n";

    // --- тестируем функцию res_mass ---
    cout << "тестируем res_mass...\n";

    // создадим простой массив
    double test_arr_1[] = {0.0, 1.0, 4.0};
    size_t size_1 = 3;

    // ожидаемый результат тот же
    double expected_result_arr_1 = 4.0;

    double test_arr_2[] = {-1.0, -4.0, 9.0};
    size_t size_2 = 3;

    double expected_result_arr_2 = 76.0;

    double test_arr_3[] = {0.25, 2.25, 6.25};
    size_t size_3 = 3;

    double expected_result_arr_3 = 14.6875;

    // вызываем функцию для массива
    double actual_result_arr_1 = result::res_mass(test_arr_1, size_1);
    double actual_result_arr_2 = result::res_mass(test_arr_2, size_2);
    double actual_result_arr_3 = result::res_mass(test_arr_3, size_3);

    // проверяем результат
    assert(actual_result_arr_1 == expected_result_arr_1);
    cout << "  res_mass_1({0, 1, 4}) = " << actual_result_arr_1 << " - ok\n";

    assert(actual_result_arr_2 == expected_result_arr_2);
    cout << "  res_mass_2({0-1.0, -4.0, 9.0}) = " << actual_result_arr_2 << " - ok\n";

    assert(actual_result_arr_3 == expected_result_arr_3);
    cout << "  res_mass_3({0.25, 2.25, 6.25}) = " << actual_result_arr_3 << " - ok\n";

    // --- тестируем функцию getRandomValue ---
    cout << "тестируем getRandomValue...\n";

    // проверим, что значение в нужном диапазоне
    double min = 5.0;
    double max = 10.0;
    double random_val = random_utils::getRandomValue(min, max);

    // проверяем, что значение не меньше минимума
    assert(random_val >= min);
    // проверяем, что значение не больше максимума
    assert(random_val <= max);
    cout << "  getRandomValue(5, 10) = " << random_val << " - ok (в диапазоне)\n";

    // --- тестируем функцию fillVectorWithRandom ---
    cout << "тестируем fillVectorWithRandom...\n";

    vector<double> vec_to_fill(3); // создаем вектор размером 3
    double fill_min = 1.0;
    double fill_max = 2.0;

    // заполняем вектор случайными числами
    vector_mass_work::fillVectorWithRandom(vec_to_fill, fill_min, fill_max);

    // проверим, что все элементы в нужном диапазоне
    for (double val : vec_to_fill) {
        assert(val >= fill_min);
        assert(val <= fill_max);
    }
    cout << "  fillVectorWithRandom заполнил 3 элемента в диапазоне [1, 2] - ok\n";

    cout << "все тесты пройдены!\n";
    return 0;
}