package mockito.clone.runner;

import mockito.clone.test.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class TestRunner {

    public static final class Result {
        public int passed;
        public int failed;

        public int total() {
            return passed + failed;
        }
    }

    public Result run(Class<?> testClass) {
        Result result = new Result();

        for (Method method : testClass.getDeclaredMethods()) {
            if (!method.isAnnotationPresent(Test.class)) continue;
            if (Modifier.isStatic(method.getModifiers())) {
                System.err.println("[SKIP] " + label(testClass, method) + " — @Test methods must not be static");
                continue;
            }
            if (method.getParameterTypes().length != 0) {
                System.err.println("[SKIP] " + label(testClass, method) + " — @Test methods must take no arguments");
                continue;
            }

            runOne(testClass, method, result);
        }

        return result;
    }

    private void runOne(Class<?> testClass, Method method, Result result) {
        String label = label(testClass, method);
        try {
            Object instance = testClass.getDeclaredConstructor().newInstance();
            method.setAccessible(true);
            method.invoke(instance);
            System.out.println("[PASS] " + label);
            result.passed++;
        } catch (Throwable t) {
            Throwable cause = unwrap(t);
            System.out.println("[FAIL] " + label + " — " + cause.getClass().getSimpleName()
                    + (cause.getMessage() != null ? ": " + cause.getMessage() : ""));
            cause.printStackTrace(System.out);
            result.failed++;
        }
    }

    private static Throwable unwrap(Throwable t) {
        return (t instanceof java.lang.reflect.InvocationTargetException && t.getCause() != null)
                ? t.getCause() : t;
    }

    private static String label(Class<?> testClass, Method method) {
        return testClass.getName() + "#" + method.getName();
    }
}
