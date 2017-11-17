package printer;

import mapper.JsonAnnotation;
import utility.TypeCatagorizer;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Type;

public class Repeater {
    private static final int MAX_INDENT = 10;
    private static StringBuilder stringBuilder = new StringBuilder();

    public static String repeatClass(Class clazz) throws Exception {
        stringBuilder.setLength(0);
        repeatField(clazz.getSimpleName());
        repeatField("");
        repeatClass(0, clazz);
        return stringBuilder.toString();
    }

    private static void repeatClass(int indent, Class clazz) throws Exception {
        if (indent < MAX_INDENT)
            for (Field field : clazz.getDeclaredFields()) {
                field.setAccessible(true);

                Annotation[] annotations = field.getDeclaredAnnotations();
                repeatAnnotations(indent, annotations);

                if (TypeCatagorizer.isSimple(field.getType()))
                    repeatField(indent, field.getType().getSimpleName(), field.getName());
                else if (TypeCatagorizer.isList(field.getType())) {
                    Type listType = TypeCatagorizer.getListType(field.getGenericType());
                    repeatField(indent, field.getType().getSimpleName(), field.getName());
                    repeatList(indent + 2, listType);
                } else {
                    repeatField(indent, field.getType().getSimpleName(), field.getName());
                    repeatClass(indent + 2, field.getType());
                }
                repeatField("");
            }
    }

    private static void repeatList(int indent, Type listType) throws Exception {
        if (TypeCatagorizer.isSimple(listType))
            repeatField(indent, ((Class) listType).getSimpleName());
        else if (TypeCatagorizer.isList(listType)) {
            Type listTypeInner = TypeCatagorizer.getListType(listType);
            repeatList(indent + 2, listTypeInner);
        } else {
            repeatField(indent, ((Class) listType).getSimpleName());
            repeatClass(indent + 2, (Class) listType);
        }
    }

    private static void repeatAnnotations(int indent, Annotation[] annotations) {
        for (Annotation annotation : annotations)
            if (annotation instanceof JsonAnnotation)
                repeatAnnotation(indent, ((JsonAnnotation) annotation).value());
    }

    private static void repeatAnnotation(int indent, String value) {
        stringBuilder.append(String.format("%" + (indent + 1) + "s @JsonAnnotation (%s)\n", "", value));
    }

    private static void repeatField(String name) {
        stringBuilder.append(String.format("%s\n", name));
    }

    private static void repeatField(int indent, String name) {
        stringBuilder.append(String.format("%" + (indent + 1) + "s %-30s\n", "", name));
    }

    private static void repeatField(int indent, String name, Object value) {
        stringBuilder.append(String.format("%" + (indent + 1) + "s %-30s: %s\n", "", name, value));
    }
}