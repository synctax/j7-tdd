package j7.tdd.runner;

import j7.tdd.agent.MockAgent;
import j7.tdd.test.Test;

import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;

import java.util.List;

public class MockRunner extends BlockJUnit4ClassRunner {

    public MockRunner(Class<?> testClass) throws InitializationError {
        super(testClass);
        if (!MockAgent.isAttached()) {
            throw new InitializationError(
                    "\n[j7-tdd] Java Agent not detected! \n" +
                            "This framework requires the agent to be loaded at startup.\n" +
                            "Please add the following to your JVM arguments / IDE Run Configuration:\n" +
                            "-javaagent:target/j7-tdd-1.0-SNAPSHOT.jar"
            );
        }
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
