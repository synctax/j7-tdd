package j7.tdd.mock;

import java.util.ArrayList;
import java.util.List;

public class ArgumentCaptor<T> {
    private final List<T> values = new ArrayList<>();
    private final Class<T> clazz;

    public ArgumentCaptor(Class<T> clazz) {this.clazz = clazz;}

    public T get(int call) {return values.get(call);}
    protected void set(T value) {this.values.add(value);}

    public T capture() {
        ArgumentMatcherCollector.pushArgumentMatcher(new CapturedMatcher<>(this));
        return DummyDataProvider.getDummyValue(clazz);
    }

    private static final class CapturedMatcher<T> extends ArgumentMatcher{
        ArgumentCaptor<T> captor;

        public CapturedMatcher(ArgumentCaptor<T> captor) {
            this.captor = captor;
        }

        @SuppressWarnings("unchecked")
        @Override
        public boolean matches(Object value) {
            captor.set((T)value);
            return true;
        }
    }
}
