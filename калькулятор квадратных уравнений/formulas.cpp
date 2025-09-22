#include "formulas.h"
#include <cmath>

// функция для вычисления дискриминанта
double discriminant(double a, double b, double c) {

    double res = pow(b, 2) - 4 * a * c;

    return res;
}

// функция для решения уравнения ax^2 + bx = 0
void solve_equation_outC(double a, double b, double& x1, double& x2) {

    x1 = 0;
    x2 = -b / a;

}

// функция для решения уравнения ax^2 + c = 0
bool solve_equation_outB(double a, double c, double& x1, double& x2) {

    double x = -c / a;

    if (x > 0) {

        x1 = sqrt(x) * (-1);

        x2 = sqrt(x);

        return true;

    } else {

        return false;

    }
}