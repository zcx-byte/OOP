    // калькулятор для решения квадратных уравнений всех видов
    #include <iostream>
    #include <windows.h>
    #include "formulas.h"
    #include <cmath>
    #include <cassert>
    #include <stdexcept>    

    using namespace std;


// функция для сравнения double с погрешностью
bool eq(double a, double b) {

    return abs(a - b) < 1e-9;

}
    
    // ! компиляция g++ main.cpp formulas.cpp -o program
    // !            ./program
    int main() {

        

        SetConsoleOutputCP(CP_UTF8);

        double x_1, x_2, ax, bx, c;

        // // --- Тесты для discriminant(a, b, c) ---
        // assert(eq(discriminant(1, 5, 6), 1));      // x^2 + 5x + 6 → D = 25 - 24 = 1
        // assert(eq(discriminant(1, -2, 1), 0));     // x^2 - 2x + 1 → D = 4 - 4 = 0


        // // Тесты для solve_equation_outC(a, b, x1, x2) → ax² + bx = 0
        // // Корни: x1 = 0, x2 = -b/a

        // solve_equation_outC(3, 6, x_1, x_2);          // 3x^2 + 6x = 0 → x = 0, -2
        // assert(eq(x_1, 0) && eq(x_2, -2));

        // solve_equation_outC(1, -4, x_1, x_2);         // x^2 - 4x = 0 → x = 0, 4
        // assert(eq(x_1, 0) && eq(x_2, 4));

        // solve_equation_outC(2, -4, x_1, x_2);         // x^2 - 4x = 0 → x = 0, 4
        // assert(eq(x_1, 0) && eq(x_2, 2));


        // // Тесты для solve_equation_outB(a, c, x1, x2) → ax² + c = 0
        // // Корни: x = ±√(-c/a), если -c/a >= 0

        // bool has_roots = solve_equation_outB(1, -4, x_1, x_2);   // x^2 - 4 = 0 -> ±2
        // assert(has_roots && eq(x_1, -2) && eq(x_2, 2));         // если всё true - то всё в норме

        // has_roots = solve_equation_outB(1, 1, x_1, x_2);         // x^2 + 1 = 0 → нет корней
        // assert(!has_roots);

        // bool has_roots = solve_equation_outB(1, -9, x_1, x_2);   // x^2 - 4 = 0 -> ±3
        // assert(has_roots && eq(x_1, -3) && eq(x_2, 3));         // если всё true - то всё в норме        


        //  cout << "все тесты пройдены успешно\n";

    
        
        cout << "введите коэффициент аx, bx, c соответственно\n";
        cin >> ax >> bx >> c;

        try {

            if (ax == 0) {

                throw invalid_argument("Коэффициент a не может быть равен нулю для квадратного уравнения.");

            }

        if ((bx == 0) && (c == 0)) {

              cout << "Один корень: x = 0";

        } else if (bx == 0) {
            
            // функция типа bool, поэтому её можно использовать в if
            if (solve_equation_outB(ax, c, x_1, x_2)) {

                  cout << "ваши корни: \n";

                  cout << "x1 = " << x_1 << "\n";

                  cout << "x2 = " << x_2 << "\n";
                
            } else {

                  cout << "корней нет";

            }

        } else if (c == 0) {

            // функция типа void И записывает результат через ссылки, поэтому можем так вызвать
            solve_equation_outC(ax, bx, x_1, x_2);

              cout << "ваши корни: \n";

              cout << "x1 = " << x_1 << " \n";

              cout << "x2 = " << x_2;

        } else {

            double dic = discriminant(ax, bx, c);

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