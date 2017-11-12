import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.List;

public class Mapper {
    public static Object map(Class clazz, String jsonInput) throws Exception {
        return map(clazz, new Path(""), jsonInput);
    }

    private static Object map(Class clazz, Path basePath, String jsonInput) throws Exception {
        Field[] fields = clazz.getDeclaredFields();
        JSONObject jsonObj = new JSONObject(jsonInput);

        Object mappedJson = clazz.getDeclaredConstructor().newInstance();

        for (Field field : fields) {
            Path path = getPath(basePath, field);

            if (ClassCatagorizer.isSimple(field)) {
                Object value = applyPath(jsonObj, path);
                ClassCatagorizer.setSimple(mappedJson, field, value);
            } else if (ClassCatagorizer.isList(field)) {
                List list = (List) field.getType().getDeclaredConstructor().newInstance();
                field.set(mappedJson, list);
                JSONArray value = (JSONArray) applyPath(jsonObj, path);
                for (int i = 0; i < value.length(); i++)
                    list.add(String.valueOf(value.get(i)));
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
                if (!path.segments[i].isEmpty())
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
            if (ClassCatagorizer.isSimple(field))
                printField(indent, field.getName(), field.get(mappedJson));
            else if (ClassCatagorizer.isList(field)) {
                printField(indent, field.getName());
                List list = (List) field.get(mappedJson);
                for (Object element : list)
                    printField(indent + 2, element.toString());
            } else {
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
}
