### ADT BankUserClass

// ! посмотреть шаблон написания

**Назначение**:  
Пользователь банковской системы, привязанный к одному банку по идентификатору `bank_ID`. Все операции требуют указания корректного `bank_ID`.

---

#### Данные:
- `private:`
  - `std::string name` — имя пользователя  
  - `std::string bank_ID` — идентификатор банка  
  - `double balance` — текущий баланс (≥ 0)

---

#### Операции:

1. **Конструктор**  
   `BankUserClass(const std::string& name, const std::string& bank_ID, double initial_balance)`  
   Инициализирует поля. Если `initial_balance < 0` — исключение `std::invalid_argument`.

2. **Деструктор**  
   `~BankUserClass()` — освобождает ресурсы строк.

3. **Получить баланс**  
   `double get_balance(const std::string& bank_ID) const`  
   Возвращает `balance`, если `bank_ID` совпадает с внутренним; иначе — `std::invalid_argument`.

4. **Получить имя**  
   `std::string get_Name(const std::string& bank_ID) const`  
   Возвращает `name`, если `bank_ID` совпадает; иначе — `std::invalid_argument`.

5. **Пополнить счёт**  
   `void deposit(double amount, const std::string& bank_ID)`  
   Увеличивает `balance` на `amount`, если:  
   - `bank_ID` совпадает,  
   - `amount > 0`.  
   Иначе — исключение. Выводит сообщение об успехе.

6. **Снять средства**  
   `void withdraw(double amount, const std::string& bank_ID)`  
   Уменьшает `balance` на `amount`, если:  
   - `bank_ID` совпадает,  
   - `amount > 0`,  
   - `balance ≥ amount`.  
   Иначе — исключение. Выводит сообщение об успехе.

7. **Изменить имя**  
   `void change_name(const std::string& new_name, const std::string& bank_ID)`  
   Присваивает `name = new_name`, если `bank_ID` совпадает; иначе — исключение. Выводит сообщение об успехе.

8. **Строковое представление**  
   `std::string to_string()`  
   Возвращает строку вида: `"Name: ..., Bank ID: ..., Balance: ..."`.

---

#### Инварианты:
- `balance` всегда ≥ 0.  
- `bank_ID` неизменяем после создания.  
- Все публичные операции (кроме конструктора и деструктора) проверяют `bank_ID`.