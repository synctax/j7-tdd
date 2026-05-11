package j7.tdd.mock;

import net.sf.cglib.proxy.MethodInterceptor;

public interface IDispatchChainInterceptor extends MethodInterceptor {
    <T extends IDispatchHandler> T getHandler(Class<T> type);
    <T extends IDispatchHandler> void addHandler(T handler);
    void reset();
}
