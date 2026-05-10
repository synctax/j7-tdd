package j7.tdd.mock;

public class VerificationContext {
    final CallCount callCount;
    final MethodInvocation lastInvocation;
    IVerificationClient client;

    public VerificationContext(CallCount count, MethodInvocation lastInvocation){
        this.callCount = count;
        this.lastInvocation = lastInvocation;
    }

    public void setClient(IVerificationClient client) {
        this.client = client;
    }

    public void verify(MethodInvocation invocation, int count) throws RuntimeException {
        if (!callCount.accepts(count))
            throw new RuntimeException("incorrect number of calls");
        if (client != null)
            client.notifyInvocation(invocation);
    }
}
