package j7.tdd.mock;

public class DummyDataProvider {
    @SuppressWarnings("unchecked")
    protected static <K> K getDummyValue(Class<K> clazz) {
        if (clazz == byte.class
                || clazz == short.class
                || clazz == int.class
                || clazz == float.class
                || clazz == double.class
                || clazz == long.class)
            return (K)(Object)0;
        if (clazz == boolean.class) return (K)(Object)false;
        if (clazz == char.class) return (K)(Object)'a';

        return null;
    }
}
