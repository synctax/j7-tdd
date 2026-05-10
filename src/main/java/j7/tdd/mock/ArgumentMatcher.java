package j7.tdd.mock;

import java.util.Objects;

public abstract class ArgumentMatcher {
    protected abstract boolean matches(Object value);

    public static ArgumentMatcher[] generateFromConcrete(Object[] args) {
        ArgumentMatcher[] out = new ArgumentMatcher[args.length];
        for (int i = 0; i < args.length; i++) {
            out[i] = equivalentMatcher(args[i]);
        }
        return out;
    }

    public static boolean allMatch(ArgumentMatcher[] matchers, Object[] args) {
        if (matchers.length != args.length) return false;
        for (int i = 0; i < matchers.length; ++i) {
            if (!matchers[i].matches(args[i])) return false;
        }
        return true;
    }

    public static <T> T eq(final T value) {
        ArgumentMatcherCollector.pushArgumentMatcher(equivalentMatcher(value));
        return value;
    }

    public static <T> T nullOf(Class<T> clazz) {
        ArgumentMatcherCollector.pushArgumentMatcher(equivalentMatcher(null));
        return (T) null;
    }

    @SuppressWarnings("unchecked")
    public static <T> T anyNumber() {
        ArgumentMatcherCollector.pushArgumentMatcher(new ArgumentMatcher() {
            @Override
            protected boolean matches(Object value) {
                return value instanceof Number;
            }
        });
        return (T)(Object)0;
    }

    public static int anyInt() {return anyNumber();}
    public static byte anyByte() {return anyNumber();}
    public static long anyLong() {return anyNumber();}
    public static short anyShort() {return anyNumber();}
    public static float anyFloat() {return anyNumber();}
    public static double anyDouble() {return anyNumber();}

    public static char anyChar() {return 'a';}
    public static boolean anyBool() {return false;}

    private static <T> ArgumentMatcher equivalentMatcher(final T value) {
        return new ArgumentMatcher() {
            @Override
            public boolean matches(Object other) {
                return Objects.equals(other, value);
            }
        };
    }

}
