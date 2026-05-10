package j7.tdd.agent;

import com.sun.tools.attach.VirtualMachine;

import java.io.File;
import java.lang.instrument.Instrumentation;
import java.lang.management.ManagementFactory;
import java.net.URL;

public class MockAgent {

    private static volatile Instrumentation instrumentation;

    public static void premain(String agentArgs, Instrumentation inst) {
        instrumentation = inst;
    }

    public static void agentmain(String agentArgs, Instrumentation inst) {
        instrumentation = inst;
    }

    public static Instrumentation getInstrumentation() {
        return instrumentation;
    }

    public static synchronized Instrumentation ensureAttached() {
        if (instrumentation != null) return instrumentation;

        File agentJar = locateAgentJar();
        if (agentJar == null) {
            System.err.println("[mockito.clone] Self-attach skipped: framework is not loaded from a jar "
                    + "(running from classes directory). Tests that need bytecode redefinition will fail. "
                    + "Either package the framework as a jar or pass -javaagent:<framework.jar>.");
            return null;
        }

        try {
            VirtualMachine vm = VirtualMachine.attach(currentPid());
            try {
                vm.loadAgent(agentJar.getAbsolutePath());
            } finally {
                vm.detach();
            }
        } catch (Throwable t) {
            throw new IllegalStateException("Failed to self-attach mockito.clone agent from " + agentJar, t);
        }

        if (instrumentation == null) {
            throw new IllegalStateException(
                    "Agent jar loaded but agentmain did not set the Instrumentation handle. "
                            + "Check the Agent-Class manifest entry in " + agentJar);
        }
        return instrumentation;
    }

    private static String currentPid() {
        String name = ManagementFactory.getRuntimeMXBean().getName();
        int at = name.indexOf('@');
        if (at <= 0) throw new IllegalStateException("Cannot extract PID from runtime name: " + name);
        return name.substring(0, at);
    }

    private static File locateAgentJar() {
        try {
            URL location = MockAgent.class.getProtectionDomain().getCodeSource().getLocation();
            File file = new File(location.toURI());

            if (file.isFile()) {
                return file;
            }

            //dirty dirty
            if (file.isDirectory() && file.getName().equals("classes")) {
                File targetDir = file.getParentFile();
                if (targetDir != null && targetDir.isDirectory()) {
                    File[] files = targetDir.listFiles();
                    if (files != null) {
                        for (File f : files) {
                            if (f.getName().endsWith(".jar") &&
                                    f.getName().contains("mockito_clone") &&
                                    !f.getName().startsWith("original-")) {
                                return f;
                            }
                        }
                    }
                }
            }
            return null;
        } catch (Throwable t) {
            return null;
        }
    }
}
