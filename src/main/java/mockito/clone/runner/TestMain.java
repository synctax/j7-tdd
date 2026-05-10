package mockito.clone.runner;

import mockito.clone.test.Test;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class TestMain {

    public static void main(String[] args) throws Exception {
        File testClassesDir = resolveTestClassesDir(args);
        if (!testClassesDir.isDirectory()) {
            System.err.println("Test classes directory not found: " + testClassesDir.getAbsolutePath());
            System.exit(2);
            return;
        }

        System.out.println("Discovering tests under " + testClassesDir.getAbsolutePath());

        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        if (loader == null) loader = TestMain.class.getClassLoader();

        List<Class<?>> testClasses = discover(testClassesDir, loader);

        TestRunner runner = new TestRunner();
        TestRunner.Result agg = new TestRunner.Result();

        for (Class<?> testClass : testClasses) {
            System.out.println("Running " + testClass.getName());
            TestRunner.Result r = runner.run(testClass);
            agg.passed += r.passed;
            agg.failed += r.failed;
        }

        System.out.println();
        System.out.println("Tests run: " + agg.total() + ", Passed: " + agg.passed + ", Failed: " + agg.failed);

        if (agg.failed > 0) {
            System.exit(1);
        }
    }

    private static File resolveTestClassesDir(String[] args) {
        if (args != null && args.length > 0) return new File(args[0]);
        return new File("target/test-classes");
    }

    private static List<Class<?>> discover(File root, ClassLoader loader) {
        List<Class<?>> hits = new ArrayList<Class<?>>();
        walk(root, root, loader, hits);
        return hits;
    }

    private static void walk(File root, File current, ClassLoader loader, List<Class<?>> hits) {
        File[] entries = current.listFiles();
        if (entries == null) return;

        for (File entry : entries) {
            if (entry.isDirectory()) {
                walk(root, entry, loader, hits);
                continue;
            }
            if (!entry.getName().endsWith(".class")) continue;
            if (entry.getName().contains("$")) continue;

            String className = toClassName(root, entry);
            Class<?> cls;
            try {
                cls = Class.forName(className, false, loader);
            } catch (Throwable t) {
                System.err.println("Skipping " + className + " — failed to load: " + t);
                continue;
            }
            if (hasTestMethod(cls)) hits.add(cls);
        }
    }

    private static boolean hasTestMethod(Class<?> cls) {
        try {
            for (Method m : cls.getDeclaredMethods()) {
                if (m.isAnnotationPresent(Test.class)) return true;
            }
        } catch (Throwable ignored) {
        }
        return false;
    }

    private static String toClassName(File root, File classFile) {
        String rootPath = root.getAbsolutePath();
        String filePath = classFile.getAbsolutePath();
        String rel = filePath.substring(rootPath.length() + 1);
        rel = rel.substring(0, rel.length() - ".class".length());
        return rel.replace(File.separatorChar, '.');
    }
}
