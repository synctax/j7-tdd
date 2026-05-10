package j7.tdd.mock;

public abstract class DispatchHandler implements IDispatchHandler{
    private IInvocationHistory invocationHistory;

    protected IInvocationHistory getInvocationHistory() {return invocationHistory;}
    @Override
    public void setInvocationHistory(IInvocationHistory history) {this.invocationHistory = history;}

}
