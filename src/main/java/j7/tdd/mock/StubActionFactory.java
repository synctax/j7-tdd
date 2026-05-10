package j7.tdd.mock;

public class StubActionFactory {
    public static <T> Anonymous<T> ReturnValue(final T value) {
        return new Anonymous<T>() {
            @Override
            public T execute(Object[] ...args) {
                return value;
            }
        };
    }

    public static <T> Anonymous<T> ThrowError(final Throwable error) {
        return new Anonymous<T>() {
            @Override
            public T execute(Object[] ...args) throws Throwable{
                throw error;
            }
        };
    }

    public static <T> Anonymous<T> ExecuteAnonymous(final Anonymous<T> fn) {
        return new Anonymous<T>() {
            @Override
            public T execute(Object[] ...args) throws Throwable {
                return fn.execute(args);
            }
        };
    }
}
