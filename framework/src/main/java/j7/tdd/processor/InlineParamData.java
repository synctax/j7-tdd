package j7.tdd.processor;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.type.TypeMirror;
import java.util.List;

public class InlineParamData {
    AnnotationMirror annotationMirror;
    List<TypeMirror> types;
}
