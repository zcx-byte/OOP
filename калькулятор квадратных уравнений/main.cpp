    // калькулятор для решения квадратных уравнений всех видов

    #include <iostream>
    #include <windows.h>
    #include "formulas.h"
    #include <cmath>

    // ! компиляция g++ main.cpp formulas.cpp -o program
    // !            ./program
    int main() {

        SetConsoleOutputCP(CP_UTF8);

        double x_1, x_2, ax, bx, c;
        
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