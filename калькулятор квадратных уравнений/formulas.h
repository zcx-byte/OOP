#ifndef FORMULAS_H
#define FORMULAS_H

// объявляем свой пространство имён
// оно нужно, чтобы избежать конфликтов имён
// позволяет использовать одинаковое название переменных или функций
namespace Quadratic {

    // функция для вычисления дискриминанта
    double discriminant(double a, double b, double c);

    // функция для решения уравнения ax^2 + bx = 0
    bool solve_equation_outB(double a, double c, double& x1, double& x2);

    // функция для решения уравнения ax^2 + c = 0
    void solve_equation_outC(double a, double b, double& x1, double& x2);

}  // namespace Quadratic

#endif 