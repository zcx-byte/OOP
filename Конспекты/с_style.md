### **Конспект: Рекомендации по стилю кодирования в C++**

#### **1. Именование (Naming Conventions)**

- **`snake_case`**:
  - Используется для переменных, функций, параметров.
  - Слова разделяются `_`, все буквы строчные.
  ```cpp
  int student_count = 0;
  double calculate_gpa(const std::vector<double>& grades);
  void print_menu();
  ```

- **`PascalCase`**:
  - Используется для классов, структур, объединений, шаблонов типов.
  - Каждое слово начинается с заглавной буквы.
  ```cpp
  class StudentRecord {};
  struct Point3D {};
  template<typename T> class LinkedList {};
  ```

- **`SCREAMING_SNAKE_CASE`**:
  - Используется для глобальных констант, макросов (в макросах осторожно).
  ```cpp
  const double PI = 3.141592653589793;
  constexpr int MAX_BUFFER_SIZE = 1024;
  #define BUFFER_SIZE 256
  ```

---

#### **2. Отступы и форматирование (Indentation & Formatting)**

- **Отступы**: Используйте **2 или 4 пробела** (не табуляцию).
- **Фигурные скобки**:
  - **Stroustrup style (Стиль Страуструпа)** (часто используется в C++):
    ```cpp
    if (condition) {
        do_something();
    } else {
        do_other_thing();
    }

    for (int i = 0; i < 10; ++i) {
        process(i);
    }

    void my_function() {
        // тело функции
    }
    ```
  - **Allman style** (альтернатива):
    ```cpp
    if (condition)
    {
        do_something();
    }
    ```

- **Выравнивание аргументов**:
  ```cpp
  function_name(arg1, arg2,
                arg3, arg4);

  function_name(
      arg1,
      arg2,
      arg3
  );
  ```

---

#### **3. Пробелы и пустые строки (Whitespace)**

- **Операторы**:
  - Добавляйте **пробелы** вокруг бинарных операторов.
    ```cpp
    int result = a + b * c;
    bool is_valid = (x > 0 && y < 10);
    ```
  - **Без пробелов** вокруг унарных операторов.
    ```cpp
    int* ptr = &value;
    --counter;
    ```

- **Круглые скобки**:
  - **Без пробелов** внутри.
    ```cpp
    if (condition) { ... }
    calculate(a, b);
    ```

- **Пустые строки**:
  - Используйте для разделения логических блоков.
  - Одна пустая строка между функциями.
  - Внутри функции — для отделения смысловых частей.
    ```cpp
    int main() {
        int a = 1;
        int b = 2;

        int sum = a + b;

        std::cout << sum << std::endl;
        return 0;
    }
    ```

---

#### **4. Комментарии (Comments)**

- **Однострочные**:
  ```cpp
  // Это комментарий к строке кода
  int counter = 0; // счётчик итераций
  ```

- **Многострочные**:
  ```cpp
  /*
   * Это
   * многострочный
   * комментарий
   */
  ```

- **Документирование** (например, Doxygen):
  ```cpp
  /**
   * @brief Вычисляет среднее значение вектора
   * @param values Вектор чисел
   * @return Среднее значение
   */
  double compute_average(const std::vector<double>& values);
  ```

---

#### **5. Организация кода (Code Organization)**

- **Заголовочные файлы** (`.h`, `.hpp`):
  - Объявления функций, классов, констант.
  - Используйте `#pragma once` для защиты от повторного включения.
    ```cpp
    #pragma once
    #include <vector>

    class MyClass {
    public:
        void do_something();
    };
    ```

- **Файлы реализации** (`.cpp`):
  - Определения функций, тело методов.
    ```cpp
    #include "myclass.h"
    #include <iostream>

    void MyClass::do_something() {
        std::cout << "Hello\n";
    }
    ```

---

#### **6. Современный C++ (Modern C++)**

- **`auto`**:
  - Вывод типа, особенно полезен с итераторами и сложными типами.
    ```cpp
    auto it = my_map.find(key);
    auto lambda = [](int x) { return x * x; };
    ```

- **`const`**:
  - Указывайте `const`, если данные не изменяются.
    ```cpp
    const int value = 42; // value не изменяется
    void print_data(const std::vector<int>& data); // не изменяет data
    int get_value() const; // метод не изменяет объект
    ```

- **`nullptr`**:
  - Всегда используйте `nullptr` вместо `NULL` или `0` для указателей.
    ```cpp
    int* ptr = nullptr;
    
---

#### **7. Полезные ресурсы (Resources)**

- **Google C++ Style Guide**: https://google.github.io/styleguide/cppguide.html
- **CppCoreGuidelines**: https://isocpp.github.io/CppCoreGuidelines/CppCoreGuidelines