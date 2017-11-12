import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

public class Mapper {
    public static Object map(Class clazz, String jsonInput) throws Exception {
        return map(clazz, new Path(""), jsonInput);
    }

    private static Object map(Class clazz, Path basePath, String jsonInput) throws Exception {
        Field[] fields = clazz.getDeclaredFields();
        JSONObject jsonObj = new JSONObject(jsonInput);

        Constructor<?> ctor = clazz.getDeclaredConstructor();
        Object mappedJson = ctor.newInstance();

        for (Field field : fields) {
            Path path = getPath(basePath, field);
            if (isString(field)) {
                Object value = applyPath(jsonObj, path);
                if (value != null)
                    field.set(mappedJson, String.valueOf(value));
            } else if (isNumeric(field)) {
                Object value = applyPath(jsonObj, path);
                if (value != null)
                    field.set(mappedJson, Integer.valueOf(value.toString()));
            } else if (isBoolean(field)) {
                Object value = applyPath(jsonObj, path);
                if (value != null)
                    field.set(mappedJson, Boolean.valueOf(value.toString()));
            } else
                field.set(mappedJson, map(field.getType(), path, jsonInput));
        }

        return mappedJson;
    }

    private static Path getPath(Path basePath, Field field) {
        JsonAnnotation annotation = (JsonAnnotation) field.getAnnotation(JsonAnnotation.class);

        Path path;
        if (annotation == null)
            path = Path.append(basePath, field.getName());
        else {
            if (annotation.value().isEmpty())
                path = basePath;
            else if (Path.isLeaf(annotation.value()))
                path = Path.append(basePath, annotation.value());
            else
                path = Path.append(basePath, annotation.value() + field.getName());

            if (annotation.debug())
                System.out.println("DEBUG " + field.getName() + " - " + path);
        }

        return path;
    }

    private static Object applyPath(JSONObject jsonObj, Path path) {
        try {
            for (int i = 0; i < path.segments.length - 1; i++)
                jsonObj = jsonObj.getJSONObject(path.segments[i]);
            return jsonObj.get(path.segments[path.segments.length - 1]);
        } catch (JSONException e) {
            return null;
        }
    }

    public static void printObject(Object mappedJson) throws Exception {
        printObject(0, mappedJson);
    }

    private static void printObject(int indent, Object mappedJson) throws Exception {
        for (Field field : mappedJson.getClass().getDeclaredFields())
            if (isString(field) || isNumeric(field) || isBoolean(field))
                printField(indent, field.getName(), field.get(mappedJson));
            else {
                printField(indent, field.getName());
                printObject(indent + 2, field.get(mappedJson));
            }
    }

    private static void printField(int indent, String name) {
        System.out.printf("%" + (indent + 1) + "s %-10s\n", "", name);
    }

    private static void printField(int indent, String name, Object value) {
        System.out.printf("%" + (indent + 1) + "s %-10s: %s\n", "", name, value);
    }

    private static boolean isString(Field field) {
        return field.getType() == String.class;
    }

    private static boolean isNumeric(Field field) {
        return field.getType() == int.class || field.getType() == Integer.class;
        //                field.getType() == short.class || field.getType() == Short.class ||
        //                field.getType() == byte.class || field.getType() == Byte.class ||
        //                field.getType() == long.class || field.getType() == Long.class ||
        //                field.getType() == double.class || field.getType() == Double.class ||
        //                field.getType() == float.class || field.getType() == Float.class ||
        //                field.getType() == char.class || field.getType() == Character.class;
    }

    private static boolean isBoolean(Field field) {
        return field.getType() == boolean.class || field.getType() == Boolean.class;
    }
}
