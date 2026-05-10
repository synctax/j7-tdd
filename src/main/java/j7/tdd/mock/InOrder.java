package j7.tdd.mock;

public class InOrder implements IVerificationClient {
    private MethodInvocation lastInvocation;

    public <T> T verify(T mock, CallCount count) {
        VerificationContext context = new VerificationContext(count, lastInvocation);
        context.setClient(this);
        IVerifiable verifiable = MockFactory.getVerifiable(mock);
        verifiable.startVerifying(context);
        return mock;
    }

    @Override
    public void notifyInvocation(MethodInvocation invocation) {
       lastInvocation = invocation;
    }
}
