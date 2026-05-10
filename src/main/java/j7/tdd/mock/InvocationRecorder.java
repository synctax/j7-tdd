package j7.tdd.mock;

import java.lang.reflect.Method;
import java.util.*;

public class InvocationRecorder implements IInvocationHistory{
    private static final ThreadLocal<LinkedList<MethodInvocation>> invocations =
            new ThreadLocal<LinkedList<MethodInvocation>>() {
                @Override
                protected LinkedList<MethodInvocation> initialValue() {
                    return new LinkedList<>();
                }
            };

    @Override
    public void recordInvocation(MethodInvocation invocation)
    {invocations.get().add(invocation);}

    @Override
    public void redactLastInvocation(Object object) {
        Iterator<MethodInvocation> it = invocations.get().descendingIterator();
        while (it.hasNext()) {
            if (it.next().object == object) {
                it.remove();
                return;
            }
        }
    }

    @Override
    public void redactAllObjectInvocations(Object object) {
        Iterator<MethodInvocation> it = invocations.get().iterator();
        while (it.hasNext()) {
            if (it.next().object == object) {
                it.remove();
            }
        }
    }

    @Override
    public List<MethodInvocation> getInvocationsOfMethodAfter(MethodInvocation prior, Object object, Method method) {
        List<MethodInvocation> out = new ArrayList<>();
        Iterator<MethodInvocation> it = invocations.get().descendingIterator();
        while (it.hasNext()) {
            MethodInvocation inv = it.next();
            if (inv.equals(prior)) return out;
            if (inv.object == object && inv.method.equals(method))
                out.add(inv);
        }
        return out;
    }

    @Override
    public List<MethodInvocation> getInvocationsOfMethod(Object object, Method method) {
        List<MethodInvocation> out = new ArrayList<>();
        for (MethodInvocation invocation : invocations.get()) {
            if (invocation.object == object && invocation.method.equals(method))
                out.add(invocation);
        }
        return out;
    }
}
