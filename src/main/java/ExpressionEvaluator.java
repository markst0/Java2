import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Stack;

/**
 * Класс для разбора и вычисления математических выражений.
 */
public class ExpressionEvaluator {

    private Map<String, Double> variables = new HashMap<>();

    /**
     * Метод для вычисления значения выражения.
     *
     * @param expression математическое выражение в виде строки
     * @return результат вычисления выражения
     * @throws IllegalArgumentException если выражение некорректно
     */
    public double evaluate(String expression) {
        expression = expression.replaceAll("\s+", ""); // Удаление пробелов
        if (!isValidExpression(expression)) {
            throw new IllegalArgumentException("Некорректное выражение: " + expression);
        }

        return evaluateExpression(expression);
    }

    /**
     * Метод для добавления переменной и её значения.
     *
     * @param name имя переменной
     * @param value значение переменной
     */
    public void setVariable(String name, double value) {
        variables.put(name, value);
    }

    private boolean isValidExpression(String expression) {
        // Простая проверка на корректность (можно улучшить)
        return expression.matches("[0-9+\-*/()a-zA-Z]+");
    }

    private double evaluateExpression(String expression) {
        Stack<Double> values = new Stack<>();
        Stack<Character> operators = new Stack<>();

        for (int i = 0; i < expression.length(); i++) {
            char ch = expression.charAt(i);

            // Если текущий символ - число или переменная
            if (Character.isDigit(ch) || Character.isLetter(ch)) {
                StringBuilder sb = new StringBuilder();
                while (i < expression.length() && (Character.isDigit(expression.charAt(i)) || Character.isLetter(expression.charAt(i)))) {
                    sb.append(expression.charAt(i++));
                }
                i--; // Для корректного увеличения i в основном цикле
                String token = sb.toString();
                if (Character.isDigit(token.charAt(0))) {
                    values.push(Double.parseDouble(token));
                } else {
                    values.push(variables.getOrDefault(token, 0.0)); // Получаем значение переменной
                }
            } else if (ch == '(') {
                operators.push(ch);
            } else if (ch == ')') {
                while (operators.peek() != '(') {
                    values.push(applyOperation(operators.pop(), values.pop(), values.pop()));
                }
                operators.pop(); // Удаляем '('
            } else if (isOperator(ch)) {
                while (!operators.isEmpty() && precedence(ch) <= precedence(operators.peek())) {
                    values.push(applyOperation(operators.pop(), values.pop(), values.pop()));
                }
                operators.push(ch);
            }
        }

        while (!operators.isEmpty()) {
            values.push(applyOperation(operators.pop(), values.pop(), values.pop()));
        }

        return values.pop();
    }

    private boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/';
    }

    private int precedence(char operator) {
        switch (operator) {
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
                return 2;
            default:
                return 0;
        }
    }

    private double applyOperation(char operator, double b, double a) {
        switch (operator) {
            case '+':
                return a + b;
            case '-':
                return a - b;
            case '*':
                return a * b;
            case '/':
