package j7.tdd.mock;

public abstract class Anonymous<T>{
    protected abstract T execute(Object[] ...args) throws Throwable;
}
