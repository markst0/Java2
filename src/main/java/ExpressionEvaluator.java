import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Stack;

/**
 * Класс ExpressionEvaluator предназначен для вычисления математических выражений,
 * поддерживающих переменные и базовые арифметические операции.
 *
 * <p>Выражения могут содержать числа, переменные (которые должны быть определены заранее),
 * а также операторы: +, -, *, / и скобки для управления порядком операций.</p>
 */
public class ExpressionEvaluator {

    /**
     * Хранит значения переменных, используемых в выражениях.
     */
    static final Map<String, Double> variables = new HashMap<>();

    /**
     * Вычисляет значение заданного математического выражения.
     *
     * @param expression строка, представляющая математическое выражение
     * @return результат вычисления выражения в виде числа с плавающей запятой
     * @throws Exception если происходит ошибка при разборе или вычислении выражения,
     *                   например, если переменная не определена или происходит деление на ноль
     */
    public static double evaluate(String expression) throws Exception {
        char[] tokens = expression.toCharArray();

        Stack<Double> values = new Stack<>();
        Stack<Character> operators = new Stack<>();

        for (int i = 0; i < tokens.length; i++) {
            if (tokens[i] == ' ') {
                continue;
            }

            if (Character.isDigit(tokens[i])) {
                StringBuilder sb = new StringBuilder();
                while (i < tokens.length && (Character.isDigit(tokens[i]) || tokens[i] == '.')) {
                    sb.append(tokens[i++]);
                }
                values.push(Double.parseDouble(sb.toString()));
                i--;
            } else if (Character.isLetter(tokens[i])) {
                StringBuilder sb = new StringBuilder();
                while (i < tokens.length && Character.isLetter(tokens[i])) {
                    sb.append(tokens[i++]);
                }
                String varName = sb.toString();
                if (variables.containsKey(varName)) {
                    values.push(variables.get(varName));
                } else {
                    throw new Exception("Переменная не определена: " + varName);
                }
                i--;
            } else if (tokens[i] == '(') {
                operators.push(tokens[i]);
            } else if (tokens[i] == ')') {
                while (!operators.isEmpty() && operators.peek() != '(') {
                    values.push(applyOperation(operators.pop(), values.pop(), values.pop()));
                }
                if (!operators.isEmpty()) {
                    operators.pop();
                }
            } else if (isOperator(tokens[i])) {
                while (!operators.isEmpty() && precedence(tokens[i]) <= precedence(operators.peek())) {
                    values.push(applyOperation(operators.pop(), values.pop(), values.pop()));
                }
                operators.push(tokens[i]);
            }
        }

        while (!operators.isEmpty()) {
            values.push(applyOperation(operators.pop(), values.pop(), values.pop()));
        }

        return values.pop();
    }

    /**
     * Проверяет, является ли заданный символ оператором.
     *
     * @param c символ для проверки
     * @return true, если символ является оператором; false в противном случае
     */
    private static boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/';
    }

    /**
     * Определяет приоритет оператора.
     *
     * @param operator оператор для определения приоритета
     * @return целое число, представляющее приоритет оператора
     */
    private static int precedence(char operator) {
        switch (operator) {
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
                return 2;
            default:
                return -1;
        }
    }

    /**
     * Применяет заданный оператор к двум числам.
     *
     * @param operator оператор, который нужно применить
     * @param b        второе число
     * @param a        первое число
     * @return результат применения оператора к числам a и b
     * @throws Exception если происходит деление на ноль или оператор неизвестен
     */
    private static double applyOperation(char operator, double b, double a) throws Exception {
        switch (operator) {
            case '+':
                return a + b;
            case '-':
                return a - b;
            case '*':
                return a * b;
            case '/':
                if (b == 0) throw new Exception("Деление на ноль!");
                return a / b;
            default:
                throw new Exception("Неизвестный оператор: " + operator);
        }
    }


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Введите количество переменных: ");
        int varCount = scanner.nextInt();
        scanner.nextLine();

        for (int i = 0; i < varCount; i++) {
            System.out.print("Введите имя переменной: ");
            String varName = scanner.nextLine();
            System.out.print("Введите значение переменной: ");
            double varValue = scanner.nextDouble();
            variables.put(varName, varValue);
            scanner.nextLine();
        }

        System.out.print("Введите выражение для вычисления: ");
        String expression = scanner.nextLine();

        try {
            double result = evaluate(expression);
            System.out.println("Результат: " + result);
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }

        scanner.close();
    }
}
