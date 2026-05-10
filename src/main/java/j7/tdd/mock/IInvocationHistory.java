package j7.tdd.mock;

import java.lang.reflect.Method;
import java.util.List;

public interface IInvocationHistory {

    void recordInvocation(MethodInvocation invocation);
    void redactLastInvocation(Object object);
    void redactAllObjectInvocations(Object object);

    List<MethodInvocation> getInvocationsOfMethodAfter(MethodInvocation prior, Object object, Method method);
    List<MethodInvocation> getInvocationsOfMethod(Object object, Method method);
}
