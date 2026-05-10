package j7.tdd.mock;

import static j7.tdd.mock.DispatchHandlerResult.Value;

public class PassthroughHandler<T> extends DispatchHandler{
    private final T concrete;

    public PassthroughHandler(T concrete) {
        this.concrete = concrete;
    }

    @Override
    public DispatchHandlerResult handle(MethodInvocation invocation) throws Throwable {
        return Value(invocation.method.invoke(concrete, invocation.args));
    }

    @Override
    public void reset() {}
}
