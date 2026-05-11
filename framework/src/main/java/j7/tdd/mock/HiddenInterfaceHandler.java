package j7.tdd.mock;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static j7.tdd.mock.DispatchHandlerResult.Pass;
import static j7.tdd.mock.DispatchHandlerResult.Value;

public class HiddenInterfaceHandler<T> extends DispatchHandler{
    private final T concrete;
    private final Class<T> clazz;
    private final Set<Method> interfaceMethods = new HashSet<>();

    public HiddenInterfaceHandler(Class<T> clazz, T concrete){
        this.concrete = concrete;
        this.clazz = clazz;
        this.interfaceMethods.addAll(Arrays.asList(clazz.getDeclaredMethods()));
    }

    public Class<T> getInterface() {return clazz;}

    @Override
    public DispatchHandlerResult handle(MethodInvocation invocation) throws Throwable {
        if (!interfaceMethods.contains(invocation.method)) return Pass();
        return Value(invocation.method.invoke(concrete));
    }

    @Override
    public void reset() {}
}
