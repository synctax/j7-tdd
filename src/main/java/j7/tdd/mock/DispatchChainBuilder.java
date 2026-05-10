package j7.tdd.mock;

import java.util.ArrayList;
import java.util.List;

public class DispatchChainBuilder {
    private  IDispatchChainInterceptor interceptor;
    private final List<Class<?>> interfaces = new ArrayList<>();

    public DispatchChainBuilder addInterceptor(IDispatchChainInterceptor interceptor){
        this.interceptor = interceptor;
        return this;
    }

    public <T extends IDispatchHandler> DispatchChainBuilder addHandler(T handler) {
        interceptor.addHandler(handler);
        return this;
    }

    public <K,T extends HiddenInterfaceHandler<K>>
    DispatchChainBuilder addHiddenInterface(T handler) {
        interceptor.addHandler(handler);
        interfaces.add(handler.getInterface());
        return this;
    }

    public DispatchChain build(){
        return new DispatchChain(interceptor, interfaces.toArray(new Class<?>[0]));
    }
}
