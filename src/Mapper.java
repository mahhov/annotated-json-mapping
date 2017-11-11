import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

public class Mapper {
    Class clazz;
    Field[] fields;

    public Mapper(Class clazz) {
        this.clazz = clazz;
        fields = clazz.getDeclaredFields();
    }

    public Object map(String jsonInput) throws Exception {
        return map(new Path(""), jsonInput);
    }

    public Object map(Path basePath, String jsonInput) throws Exception {
        JSONObject jsonObj = new JSONObject(jsonInput);

        Constructor<?> ctor = clazz.getDeclaredConstructor();
        Object mappedJson = ctor.newInstance();

        for (Field field : fields)
            if (field.getType() == String.class) {
                Path path = getPath(basePath, field);
                field.set(mappedJson, applyPath(jsonObj, path));
            } else {
                // todo: support nested fields
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

    public void print(Object mappedJson) throws Exception {
        for (Field field : fields)
            System.out.printf("%-10s: %s\n", field.getName(), field.get(mappedJson));
    }
}
