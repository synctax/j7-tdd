package j7.tdd.test;

public class Assert {
    public static <T> void equal(T a, T b) throws AssertionError{
        if (a.equals(b)) return;
        throw new AssertionError("Expected " + b + " but got Actual "+a);
    }
}
