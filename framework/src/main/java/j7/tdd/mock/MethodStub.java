package j7.tdd.mock;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MethodStub<T> {
    private final ArgumentMatcher[] matchers;
    private final List<Anonymous<T>> actions = new ArrayList<>();
    private int actionIndex = 0;

    public MethodStub(ArgumentMatcher[] matchers) {
        this.matchers = matchers;
    }

    public void addAction(Anonymous<T> action) {
        actions.add(action);
    }

    public void addAllActions(Collection<Anonymous<T>> actionCollection) {
        actions.addAll(actionCollection);
    }

    protected T handle(MethodInvocation inv) throws Throwable {
        T value = actions.get(actionIndex++).execute(inv.args);
        if (actionIndex == actions.size()) --actionIndex;

        return value;
    }

    protected boolean shouldHandle(MethodInvocation inv) {
        return ArgumentMatcher.allMatch(matchers, inv.args);
    }
}