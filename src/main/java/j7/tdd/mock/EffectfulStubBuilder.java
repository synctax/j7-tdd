package j7.tdd.mock;

public class EffectfulStubBuilder<T> extends StubBuilder<T>{
    private final MethodStub<T> stub;

    private static final ThreadLocal<MethodInvocation> lastInvocation = new ThreadLocal<>();
    public static void notifyInvocation(MethodInvocation invocation) {
        lastInvocation.set(invocation);
    }

    private EffectfulStubBuilder(MethodInvocation invocation) {
        this.stub = new MethodStub<>(invocation.matchers);
        setMethod(invocation.method);
        setObject(invocation.object);
    }

    public static <T> EffectfulStubBuilder<T> when(T underscore) {
        MethodInvocation invocation = lastInvocation.get();
        MockFactory.redactLastInvocation(invocation.object);
        return new EffectfulStubBuilder<T>(invocation);
    }

    public EffectfulStubBuilder<T> thenReturn(final T value){
       stub.addAction(StubActionFactory.ReturnValue(value));
       installIfNot(stub);
       return this;
    }

    public EffectfulStubBuilder<T> thenThrow(final Throwable error) {
       stub.addAction(StubActionFactory.<T>ThrowError(error));
       installIfNot(stub);
       return this;
    }

    public EffectfulStubBuilder<T> thenDo(final Anonymous<T> fn) {
       stub.addAction(StubActionFactory.ExecuteAnonymous(fn));
       installIfNot(stub);
       return this;
    }
}
