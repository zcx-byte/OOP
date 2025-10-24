#include "../include/formulas.h"
#include <iostream>
#include <fstream>
#include <stdexcept>

namespace save_res {

    // функция для общей записи результата в файл
    void saveResultsToFile(const std::string& filename,
        const std::vector<double>& vec, double sum_v,
        const double* arr, size_t size, double sum_arr){

            std::ofstream output_file(filename);

            if (output_file.is_open()) {

                // записываем результат вектора в файл
                vector_mass_work::writeVectorToFile(vec, sum_v, output_file);

                try {
                    // записываем результат массива в файл
                    array_work::writeArrayToFile(arr, size, sum_arr, output_file);

                } catch (const std::invalid_argument& e) {
                    std::cerr << "Ошибка при записи массива: " << e.what() << std::endl;
                    output_file.close();
                    throw;  // пробрасываем исключение дальше
                }

                output_file.close();

            } else {
                std::cout << "Не удалось открыть файл для записи.\n";
                throw std::runtime_error("Не удалось открыть файл для записи");
            }
    }
}