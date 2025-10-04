#include "formulas.h"
#include <iostream>
#include <cassert>
#include <cmath>

using namespace std;

// пространство имён для проверки функций из файла formulas.cpp
namespace QuadraticTest {

    bool eq(double a, double b, double eps = 1e-9) {
        return abs(a - b) < eps;
    }

    void run_all_tests() {
        double x1, x2;

        // Тесты discriminant
        assert(eq(Quadratic::discriminant(1, 5, 6), 1));      // D = 25 - 24 = 1
        assert(eq(Quadratic::discriminant(1, -2, 1), 0));     // D = 4 - 4 = 0

        // Тесты solve_equation_outC: ax² + bx = 0 → x = 0, -b/a
        Quadratic::solve_equation_outC(3, 6, x1, x2);
        assert(eq(x1, 0) && eq(x2, -2));

        Quadratic::solve_equation_outC(1, -4, x1, x2);
        assert(eq(x1, 0) && eq(x2, 4));

        Quadratic::solve_equation_outC(2, -4, x1, x2);
        assert(eq(x1, 0) && eq(x2, 2));

        // Тесты solve_equation_outB: ax² + c = 0 → x = ±√(-c/a)
        bool has_roots = Quadratic::solve_equation_outB(1, -4, x1, x2); // x² - 4 = 0
        assert(has_roots && eq(x1, -2) && eq(x2, 2));

        has_roots = Quadratic::solve_equation_outB(1, 1, x1, x2); // x² + 1 = 0
        assert(!has_roots);

        has_roots = Quadratic::solve_equation_outB(1, -9, x1, x2); // x² - 9 = 0
        assert(has_roots && eq(x1, -3) && eq(x2, 3));

        cout << "Все тесты пройдены успешно!\n";
    }

} // namespace QuadraticTest