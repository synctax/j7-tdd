package j7.tdd.mock;


public class DispatchHandlerResult {

    public enum Type {
        PASS,
        VALUE,
        DUMMY
    }

    private final Type type;
    public Type getType() {return type;}

    private final Object value;
    public Object getValue() {return value;}

    public DispatchHandlerResult(Type type, Object value) {
        this.type = type;
        this.value = value;
    }

    static DispatchHandlerResult Pass(){
        return new DispatchHandlerResult(Type.PASS, null);
    }

    static DispatchHandlerResult Value(Object value) {
        return new DispatchHandlerResult(Type.VALUE, value);
    }
    static DispatchHandlerResult Dummy(){
        return new DispatchHandlerResult(Type.DUMMY, null);
    }
}
