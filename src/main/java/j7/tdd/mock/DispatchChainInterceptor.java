package j7.tdd.mock;

import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.*;

public class DispatchChainInterceptor implements IDispatchChainInterceptor{
    private final List<IDispatchHandler> handlers = new ArrayList<>();
    private final IInvocationHistory invocationRecorder;

    public DispatchChainInterceptor(IInvocationHistory invocationRecorder) {
        this.invocationRecorder = invocationRecorder;
    }

    @Override
    public <T extends IDispatchHandler> T getHandler(Class<T> type) {
        for (IDispatchHandler handler : handlers) {
            if (type.isInstance(handler)) return type.cast(handler);
        }
        return null;
    }

    @Override
    public <T extends IDispatchHandler> void addHandler(T concrete) {
        concrete.setInvocationHistory(invocationRecorder);
        handlers.add(concrete);
    }

    @Override
    public void reset() {
        invocationRecorder.redactAllObjectInvocations(this);
        for (IDispatchHandler handler : handlers) {
            handler.reset();
        }
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        ArgumentMatcher[] argumentMatchers = ArgumentMatcherCollector.popArgumentMatchers();
        if (argumentMatchers.length == 0)
            argumentMatchers = ArgumentMatcher.generateFromConcrete(args);
        else if (argumentMatchers.length < args.length)
            throw new RuntimeException("CANNOT MIX MATCHERS AND LITERALS");

        MethodInvocation invocation = new MethodInvocation.Builder()
            .args(args)
            .method(method)
            .object(obj)
            .proxy(proxy)
            .matchers(argumentMatchers)
            .build();

        invocationRecorder.recordInvocation(invocation);

        Object dummyReturn = DummyDataProvider.getDummyValue(method.getReturnType());
        for (IDispatchHandler handler : handlers) {
            DispatchHandlerResult result = handler.handle(invocation);
            switch (result.getType()){
                case VALUE:
                    return result.getValue();
                case DUMMY:
                    return dummyReturn;
                case PASS:
                default:
            }
        }

        return dummyReturn;
    }
}
