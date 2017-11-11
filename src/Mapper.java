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
            if (field.getType() == String.class)
                field.set(mappedJson, applyPath(jsonObj, path));
            else 
                field.set(mappedJson, map(field.getType(), path, jsonInput));
        }

        return mappedJson;
    }

    private static Path getPath(Path basePath, Field field) {
        JsonAnnotation annotation = (JsonAnnotation) field.getAnnotation(JsonAnnotation.class);
        if (annotation == null)
            return Path.append(basePath, field.getName());
        else if (Path.isLeaf(annotation.value()))
            return Path.append(basePath, annotation.value());
        else
            return Path.append(basePath, annotation.value() + field.getName());
    }

    private static Object applyPath(JSONObject jsonObj, Path path) {
        try {
            for (int i = 0; i < path.segments.length - 1; i++)
                jsonObj = jsonObj.getJSONObject(path.segments[i]);
            return jsonObj.get(path.segments[path.segments.length - 1]);
        } catch (JSONException e) {
        }
        return null;
    }

    public static void print(Object mappedJson) throws Exception {
        for (Field field : mappedJson.getClass().getDeclaredFields())
            System.out.printf("%-10s: %s\n", field.getName(), field.get(mappedJson));
    }
}
