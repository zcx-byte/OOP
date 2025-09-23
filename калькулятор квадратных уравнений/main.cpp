    // калькулятор для решения квадратных уравнений всех видов
    #include <iostream>
    #include <windows.h>
    #include "formulas.h"
    #include <cmath>
    #include <cassert>

// функция для сравнения double с погрешностью
bool eq(double a, double b) {

    return std::abs(a - b) < 1e-9;

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


        // std::cout << "все тесты пройдены успешно\n";

    
        
        std::cout << "введите коэффициент аx (он должен быть отличен от 0)\n";
        std::cout << "ax = ";
        std:: cin >> ax;

        std::cout << "введите коэффициент bx^2\n";
        std::cout << "bx^2 = ";
        std:: cin >> bx;

        std::cout << "введите коэффициент c\n";
        std::cout << "c = ";
        std:: cin >> c;


        if ((bx == 0) && (c == 0)) {

            std:: cout << "Один корень: x = 0";

        } else if (bx == 0) {
            
            // функция типа bool, поэтому её можно использовать в if
            if (solve_equation_outB(ax, c, x_1, x_2)) {

                std:: cout << "ваши корни: \n";

                std:: cout << "x1 = " << x_1 << "\n";

                std:: cout << "x2 = " << x_2 << "\n";
                
            } else {

                std:: cout << "корней нет";

            }

        } else if (c == 0) {

            // функция типа void И записывает результат через ссылки, поэтому можем так вызвать
            solve_equation_outC(ax, bx, x_1, x_2);

            std:: cout << "ваши корни: \n";

            std:: cout << "x1 = " << x_1 << " \n";

            std:: cout << "x2 = " << x_2;

        } else {

            double dic = discriminant(ax, bx, c);

            if (dic == 0) {

                x_1 = -bx / 2*ax;

                std:: cout << "дискриминант равен 0, ваш корень: x = " << x_1 << "\n"; 

            } else if (dic < 0) {

                std:: cout << "дискриминант меньше 0, корней нет";

            } else {

                x_1 = (-bx + sqrt(dic)) / 2*ax;

                x_2 = (-bx - sqrt(dic)) / 2*ax;

                std:: cout << "ваши корни: \n";

                std:: cout << "x1 = " << x_1 << "\n";
                
                std:: cout << "x2 = " << x_2 << "\n";
            }

        } 
            
        return 0;
    }