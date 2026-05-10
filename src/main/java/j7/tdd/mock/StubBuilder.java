package j7.tdd.mock;

import java.lang.reflect.Method;

public abstract class StubBuilder<T> {
    private Object object;
    private Method method;
    private boolean installed = false;

    protected Object getObject() {return object;}
    protected void setObject(Object object) {this.object = object;}

    protected Method getMethod() {return method;}
    protected void setMethod(Method method) {this.method = method;}

    protected boolean isInstalled() {return installed;}
    protected void setInstalled(boolean installed) {this.installed = installed;}

    protected void installIfNot(MethodStub<T> stub){
        if (isInstalled()) return;
        setInstalled(true);

        IStubbable stubbable = MockFactory.getStubbable(object);
        stubbable.addStub(method, stub);
    }
}
