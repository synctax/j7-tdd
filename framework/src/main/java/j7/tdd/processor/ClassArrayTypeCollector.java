package j7.tdd.processor;

import javax.lang.model.element.AnnotationValue;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.SimpleAnnotationValueVisitor7;
import java.util.List;

public class ClassArrayTypeCollector extends SimpleAnnotationValueVisitor7<Void, List<TypeMirror>> {

    @Override
    public Void visitArray(List<? extends AnnotationValue> values, List<TypeMirror> collectedTypes) {
        for (AnnotationValue value : values) {
            value.accept(this, collectedTypes);
        }
        return null;
    }

    @Override
    public Void visitType(TypeMirror type, List<TypeMirror> collectedTypes) {
        collectedTypes.add(type);
        return null;
    }
}
