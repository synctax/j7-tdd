package j7.tdd.processor;

import j7.tdd.test.ConstructedMock;
import j7.tdd.test.ConstructedSpy;
import j7.tdd.test.InlineTarget;
import j7.tdd.test.StaticMock;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.annotation.Annotation;
import java.util.*;

@SupportedAnnotationTypes("*")
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class InlineMockProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        processingEnv.getMessager().printMessage(
                Diagnostic.Kind.NOTE,
                "[InlineMockProcessor] process() round, annotations=" + annotations.size()
        );
        try {
            Map<ExecutableElement, InlineParamData> targets = collectAnnotationData(InlineTarget.class, roundEnv);

            Map<ExecutableElement, InlineParamData> statics = collectAnnotationData(StaticMock.class, roundEnv);
            Map<ExecutableElement, InlineParamData> constructed = collectAnnotationData(ConstructedMock.class, roundEnv);
            Map<ExecutableElement, InlineParamData> spies = collectAnnotationData(ConstructedSpy.class, roundEnv);

            Set<ExecutableElement> targetMethods = targets.keySet();
            warnUnused(statics, targetMethods);
            warnUnused(constructed, targetMethods);
            warnUnused(spies, targetMethods);


            return false;
        } catch (Exception e) {
            // This forces the compiler to stay alive long enough to show you the stack trace
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "Processor Crash: " + sw.toString());
            return false;
        }
    }

    private void warnUnused(Map<ExecutableElement, InlineParamData> annotations, Set<ExecutableElement> targetMethods) {
        Set<ExecutableElement> annotatedMethods = new HashSet<>(annotations.keySet());
        annotatedMethods.removeAll(targetMethods);

        for (ExecutableElement method : annotatedMethods) {
            InlineParamData data = annotations.get(method);
            processingEnv.getMessager().printMessage(
                    Diagnostic.Kind.WARNING,
                    "The @" + data.annotationMirror.getAnnotationType().asElement().getSimpleName() +
                            " annotation does nothing without specifying @InlineTarget",
                    method,
                    data.annotationMirror
            );
        }
    }

    public <T extends Annotation> Map<ExecutableElement, InlineParamData>
    collectAnnotationData(Class<T> annotationType, RoundEnvironment env){
        Map<ExecutableElement, InlineParamData> data = new HashMap<>();
        for (Element element : env.getElementsAnnotatedWith(annotationType)) {
            if (element.getKind() != ElementKind.METHOD)
                continue;

            ExecutableElement method = (ExecutableElement) element;

            for (AnnotationMirror mirror : element.getAnnotationMirrors()) {
                if (annotationType.getName().equals(mirror.getAnnotationType().toString())) {
                    InlineParamData annotationData = new InlineParamData();
                    List<TypeMirror> typeMirrors = new ArrayList<>();

                    ClassArrayTypeCollector collector = new ClassArrayTypeCollector();

                    for (AnnotationValue value : mirror.getElementValues().values()) {
                        value.accept(collector, typeMirrors);
                    }

                    annotationData.annotationMirror = mirror;
                    annotationData.types = typeMirrors;
                    data.put(method, annotationData);
                    break;
                }
            }
        }
        return data;
    }


}
