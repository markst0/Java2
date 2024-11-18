import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ExpressionEvaluatorTest {

    private Map<String, Double> testVariables;

    @BeforeEach
    void setUp() {
        testVariables = new HashMap<>();
        testVariables.put("x", 10.0);
        testVariables.put("y", 5.0);
    }

    @Test
    void testEvaluateSimpleExpression() throws Exception {
        assertEquals(15.0, ExpressionEvaluator.evaluate("10 + 5"));
    }

    @Test
    void testEvaluateWithVariables() throws Exception {
        ExpressionEvaluator.variables.putAll(testVariables);
        assertEquals(15.0, ExpressionEvaluator.evaluate("x + y"));
    }

    @Test
    void testEvaluateWithParentheses() throws Exception {
        assertEquals(30.0, ExpressionEvaluator.evaluate("(10 + 5) * 2"));
    }

    @Test
    void testEvaluateWithDivision() throws Exception {
        assertEquals(2.0, ExpressionEvaluator.evaluate("10 / 5"));
    }

    @Test
    void testDivisionByZero() {
        Exception exception = assertThrows(Exception.class, () -> {
            ExpressionEvaluator.evaluate("10 / 0");
        });
        assertEquals("Деление на ноль!", exception.getMessage());
    }

    @Test
    void testUndefinedVariable() {
        Exception exception = assertThrows(Exception.class, () -> {
            ExpressionEvaluator.evaluate("z + 5");
        });
        assertEquals("Переменная не определена: z", exception.getMessage());
    }
}
