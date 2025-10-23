// Автор: Прасков Даниил. ИВТ-24-2.
// Данная программа является калькулятором для решения квадратных уравнений.    
    
#include <iostream>
#include <windows.h>
#include "include/formulas.h"
#include <cmath>
#include <cassert>
#include <stdexcept>    

using namespace std;

// объявляем тесты (реализация в quadratic_tests.cpp)
// так нужно, чтобы main знала, что есть такое пространство имён и в нём лежит функция
namespace QuadraticTest {

    void run_all_tests();

}
    
    // ! компиляция g++ main.cpp quadratic_tests.cpp formulas.cpp -o program
    // !            ./program
    int main() {

        SetConsoleOutputCP(CP_UTF8);

        // Запуск тестов
        // QuadraticTest::run_all_tests();
        // cout << "\n";

        double x_1, x_2, ax, bx, c;
    
        // запрашиваем коэфф. у пользователя
        cout << "введите коэффициент аx, bx, c соответственно\n";
        cin >> ax >> bx >> c;

        try {

            // проверка 1 на наличие коэфф. b и c
            if ((bx == 0) && (c == 0)) {

                cout << "Один корень: x = 0";

            } else if (bx == 0) {
                
                // функция типа bool, поэтому её можно использовать в if
                if (Quadratic::solve_equation_outB(ax, c, x_1, x_2)) {

                    cout << "ваши корни: \n";

                    cout << "x1 = " << x_1 << "\n";

                    cout << "x2 = " << x_2 << "\n";
                
            } else {
                
                cout << "корней нет";

            }

        } else if (c == 0) {
            
            // функция типа void И записывает результат через ссылки, поэтому можем так вызвать
            Quadratic::solve_equation_outC(ax, bx, x_1, x_2);

            cout << "ваши корни: \n";

            cout << "x1 = " << x_1 << " \n";

            cout << "x2 = " << x_2;

        } else {

            double dic = Quadratic::discriminant(ax, bx, c);

            if (dic == 0) {

                x_1 = -bx / 2*ax;

                  cout << "дискриминант равен 0, ваш корень: x = " << x_1 << "\n"; 

            } else if (dic < 0) {

                  cout << "дискриминант меньше 0, корней нет";

            } else {

                x_1 = (-bx + sqrt(dic)) / 2*ax;

                x_2 = (-bx - sqrt(dic)) / 2*ax;

                  cout << "ваши корни: \n";

                  cout << "x1 = " << x_1 << "\n";
                
                  cout << "x2 = " << x_2 << "\n";
            }

        } 
    }

    // блоки обработки исключений (catch), которые идут после try. 
    // они нужны, чтобы перехватить и корректно обработать ошибки, которые могут возникнуть в программе.
    // err - просто переменная для ошибки
    catch (const invalid_argument &err) {

        // err.what() — метод, который возвращает текст ошибки, который указали при throw
        cerr << "Ошибка: " << err.what() << endl;

        return 1;  // Код ошибки
    }

    // обобщённый обработчик — ловит все стандартные исключения, которые не ожидаешь
    // например: out_of_range, bad_alloc(не получилось выделить память), и т.п.
    catch (const exception &err) {

        cerr << "Неизвестная ошибка: " << err.what() << endl;

        return 1;
    }
            
        return 0;
    }