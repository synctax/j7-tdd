package j7.tdd.agent;

import java.lang.instrument.Instrumentation;

public class MockAgent {

    private static final String INST_KEY = "j7.tdd.instrumentation";
    private static volatile Instrumentation instrumentation;

    public static void premain(String agentArgs, Instrumentation inst) {
        instrumentation = inst;
        System.getProperties().put(INST_KEY, inst);
    }

    public static void agentmain(String agentArgs, Instrumentation inst) {
        instrumentation = inst;
    }

    public static Instrumentation getInstrumentation() {
        if (instrumentation != null) {
            return instrumentation;
        }
        return (Instrumentation) System.getProperties().get(INST_KEY);
    }

    public static boolean isAttached() {return getInstrumentation() != null;}
}
