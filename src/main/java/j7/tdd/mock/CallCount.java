package j7.tdd.mock;

public abstract class CallCount {
    public abstract boolean accepts(int count);

    public static CallCount atLeastOnce() {
        return new CallCount() {
            @Override
            public boolean accepts(int count) {
                return count > 0;
            }
        };
    }
    public static CallCount times(final int expected ) {
        return new CallCount() {
            @Override
            public boolean accepts(int count) {
                return count ==expected;
            }
        };
    }
    public static CallCount exactlyOnce() {
        return new CallCount() {
            @Override
            public boolean accepts(int count) {
                return count ==1;
            }
        };
    }
}
