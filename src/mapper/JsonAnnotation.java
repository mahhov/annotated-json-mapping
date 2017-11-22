package mapper;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Repeatable(JsonAnnotations.class)
public @interface JsonAnnotation {
    String value() default "";

    boolean debug() default false;
}