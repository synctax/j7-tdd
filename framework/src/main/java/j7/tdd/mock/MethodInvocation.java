package j7.tdd.mock;

import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public final class MethodInvocation {
    public final Object[] args;
    public final Object object;
    public final Method method;
    public final MethodProxy proxy;
    public final ArgumentMatcher[] matchers;

    private MethodInvocation(Builder builder) {
        args = builder.args;
        object = builder.object;
        method = builder.method;
        proxy = builder.proxy;
        matchers = builder.matchers;
    }

    public static final class Builder {
        private Object[] args;
        private Object object;
        private Method method;
        private MethodProxy proxy;
        private ArgumentMatcher[] matchers;

        public Builder() {
        }

        public Builder args(Object[] val) {
            args = val;
            return this;
        }

        public Builder object(Object val) {
            object = val;
            return this;
        }

        public Builder method(Method val) {
            method = val;
            return this;
        }

        public Builder proxy(MethodProxy val) {
            proxy = val;
            return this;
        }

        public Builder matchers(ArgumentMatcher[] val){
            matchers = val;
            return this;
        }

        public MethodInvocation build() {
            return new MethodInvocation(this);
        }
    }
}
