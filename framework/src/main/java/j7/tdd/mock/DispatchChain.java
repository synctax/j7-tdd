package j7.tdd.mock;

import net.sf.cglib.proxy.Enhancer;

public class DispatchChain {

    private final IDispatchChainInterceptor interceptor;
    public IDispatchChainInterceptor getCallback() {return interceptor;}

    private final Class<?>[] interfaces;
    public Class<?>[] getInterfaces() {return interfaces;}

    public DispatchChain(IDispatchChainInterceptor interceptor, Class<?>[] interfaces) {
        this.interceptor = interceptor;
        this.interfaces = interfaces;
    }

    public Enhancer asEnhancer() {
        Enhancer enhancer = new Enhancer();
        enhancer.setCallback(interceptor);
        enhancer.setInterfaces(interfaces);
        return enhancer;
    }
}
