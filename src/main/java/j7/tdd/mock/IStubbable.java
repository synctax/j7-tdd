package j7.tdd.mock;

import java.lang.reflect.Method;

public interface IStubbable extends IDispatchHandler{
    void startStubbing(IStubContext context);
    void addStub(Method method, MethodStub<?> handler);
}
