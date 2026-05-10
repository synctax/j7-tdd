package mockito.clone.runner;

import mockito.clone.agent.MockAgent;
import mockito.clone.test.Test;

import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;

import java.util.List;

public class MockRunner extends BlockJUnit4ClassRunner {

    public MockRunner(Class<?> testClass) throws InitializationError {
        super(testClass);
        MockAgent.ensureAttached();
    }

    @Override
    protected List<FrameworkMethod> computeTestMethods() {
        return getTestClass().getAnnotatedMethods(Test.class);
    }

    @Override
    protected void validateTestMethods(List<Throwable> errors) {
        for (FrameworkMethod m : computeTestMethods()) {
            m.validatePublicVoidNoArg(false, errors);
        }
    }
}
