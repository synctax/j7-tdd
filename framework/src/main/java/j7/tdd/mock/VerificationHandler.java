package j7.tdd.mock;

import java.util.ArrayList;
import java.util.List;

import static j7.tdd.mock.DispatchHandlerResult.Dummy;
import static j7.tdd.mock.DispatchHandlerResult.Pass;

public class VerificationHandler extends DispatchHandler implements IVerifiable{
    private boolean verifying = false;
    private VerificationContext verificationContext;

    @Override
    public void startVerifying(VerificationContext context) {
        verifying = true;
        verificationContext = context;
    }
    @Override
    public void reset() {
        verifying = false;
        verificationContext = null;
    }

    @Override
    public DispatchHandlerResult handle(MethodInvocation invocation) {
        if (!verifying) return Pass();
        verifying = false;
        getInvocationHistory().redactLastInvocation(invocation.object);

        List<MethodInvocation> invocations = getInvocationHistory()
                .getInvocationsOfMethodAfter(verificationContext.lastInvocation, invocation.object, invocation.method);


        List<MethodInvocation> matchingInvocations = new ArrayList<>();
        for (MethodInvocation inv : invocations)
            if (ArgumentMatcher.allMatch(invocation.matchers, inv.args))
                matchingInvocations.add(inv);

        int matchCount = matchingInvocations.size();
        MethodInvocation lastInvocation = matchCount < 1 ? null : matchingInvocations.get(matchCount-1);
        verificationContext.verify(lastInvocation, matchCount);
        return Dummy();
    }

}
