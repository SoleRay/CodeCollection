package spring.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

public class AnnotatedElementUtil {

    public static boolean hasAnnotation(AnnotatedElement element, Class<? extends Annotation> annotationType) {

        // Shortcut: directly present on the element, with no processing needed?
        if (element.isAnnotationPresent(annotationType)) {
            return true;
        }
        return false;
    }
}
