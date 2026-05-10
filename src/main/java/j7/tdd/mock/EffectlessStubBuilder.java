package j7.tdd.mock;

import java.util.ArrayList;
import java.util.List;

public class EffectlessStubBuilder<T> extends StubBuilder<T> implements IStubContext{
    private final List<Anonymous<T>> actions = new ArrayList<>();

    private EffectlessStubBuilder(Anonymous<T> action) {
        actions.add(action);
    }

    public static <T> EffectlessStubBuilder<T> doReturn(T value) {
        Anonymous<T> action = StubActionFactory.ReturnValue(value);
        return new EffectlessStubBuilder<>(action);
    }

    public static EffectlessStubBuilder<?> doThrow(Throwable error) {
        Anonymous<?> action = StubActionFactory.ThrowError(error);
        return new EffectlessStubBuilder<>(action);
    }

    public static <T> EffectlessStubBuilder<T> doExecute(Anonymous<T> fn) {
        Anonymous<T> action = StubActionFactory.ExecuteAnonymous(fn);
        return new EffectlessStubBuilder<>(action);
    }

    public <K> K when(K object) {
        IStubbable stubbable = MockFactory.getStubbable(object);
        stubbable.startStubbing(this);
        return object;
    }

    public EffectlessStubBuilder<T> thenReturn(final T value) {
        Anonymous<T> action = StubActionFactory.ReturnValue(value);
        actions.add(action);
        return this;
    }

    public EffectlessStubBuilder<T> thenThrow(Throwable error) {
        Anonymous<T> action = StubActionFactory.ThrowError(error);
        actions.add(action);
        return this;
    }
    public EffectlessStubBuilder<T> thenExecute(Anonymous<T> fn) {
        Anonymous<T> action = StubActionFactory.ExecuteAnonymous(fn);
        actions.add(action);
        return this;
    }

    @Override
    public void registerInvocation(MethodInvocation invocation) {
        setObject(invocation.object);
        setMethod(invocation.method);

        MethodStub<T> stub = new MethodStub<>(invocation.matchers);
        stub.addAllActions(actions);
        installIfNot(stub);
    }
}
