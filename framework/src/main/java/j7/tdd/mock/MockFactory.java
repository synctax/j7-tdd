package j7.tdd.mock;

import net.sf.cglib.proxy.Enhancer;

public class MockFactory {
    static final IInvocationHistory invocationRecorder = new InvocationRecorder();

    public static <T> T mock(Class<T> clazz){
        Enhancer enhancer = getDefaultChain()
                .build()
                .asEnhancer();
        enhancer.setSuperclass(clazz);
        return clazz.cast(enhancer.create());
    }

    @SuppressWarnings("unchecked")
    public static <T> T spy(T instance) {
        Class<?> clazz = instance.getClass();
        Enhancer enhancer = getDefaultChain()
                .addHandler(new PassthroughHandler<>(instance))
                .build()
                .asEnhancer();
        enhancer.setSuperclass(clazz);
        return (T) clazz.cast(enhancer.create());
    }

    public static void reset(Object object) {
        IDispatchChainInterceptor manager = getManager(object);
        manager.reset();
    }

    public static IStubbable getStubbable(Object object){
        IDispatchChainInterceptor manager = getManager(object);
        return manager.getHandler(IStubbable.class);
    }

    public static IVerifiable getVerifiable(Object object){
        IDispatchChainInterceptor manager = getManager(object);
        return manager.getHandler(IVerifiable.class);
    }

    public static void redactLastInvocation(Object object) {
        invocationRecorder.redactLastInvocation(object);
    }

    private static DispatchChainBuilder getDefaultChain() {
        DispatchChainInterceptor interceptor = new DispatchChainInterceptor(invocationRecorder);
        return new DispatchChainBuilder()
                .addInterceptor(interceptor)
                .addHiddenInterface(
                        new HiddenInterfaceHandler<>(
                                IDispatchedManagedMock.class,
                                new DispatchManagedMock(interceptor))
                )
                .addHandler(new VerificationHandler())
                .addHandler(new StubHandler());
    }

    private static IDispatchChainInterceptor getManager(Object object) {
        IDispatchedManagedMock managed = (IDispatchedManagedMock) object;
        return managed.getManager();
    }
}
