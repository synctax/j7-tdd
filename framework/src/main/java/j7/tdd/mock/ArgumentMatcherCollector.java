package j7.tdd.mock;

import java.util.ArrayList;
import java.util.List;

public class ArgumentMatcherCollector {
    private static final ThreadLocal<List<ArgumentMatcher>> lastArgumentMatchers =
            new ThreadLocal<List<ArgumentMatcher>>() {
                @Override
                protected List<ArgumentMatcher> initialValue() {return new ArrayList<>();}
            };
    public static void pushArgumentMatcher(ArgumentMatcher matcher) {
        lastArgumentMatchers.get().add(matcher);
    }

    protected static ArgumentMatcher[] popArgumentMatchers() {
        ArgumentMatcher[] matchers = lastArgumentMatchers.get().toArray(new ArgumentMatcher[0]);
        lastArgumentMatchers.remove();
        return matchers;
    }
}
