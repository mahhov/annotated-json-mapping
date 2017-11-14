import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

class Mapper {
    static Object map(Class clazz, String jsonInput) throws Exception {
        return mapObject(clazz, new Path(""), new JSONObject(jsonInput));
    }

    private static Object mapObject(Class clazz, Path basePath, JSONObject jsonObj) throws Exception {
        Field[] fields = clazz.getDeclaredFields();

        Object mappedJson = clazz.getDeclaredConstructor().newInstance();

        for (Field field : fields) {
            if (TypeCatagorizer.isSimple(field.getType())) {
                Path path = Path.createPath(basePath, field, false);
                Object simpleValue = applyPath(jsonObj, path);
                field.set(mappedJson, TypeCatagorizer.getSimpleValue(field.getType(), simpleValue));
            } else if (TypeCatagorizer.isList(field.getType())) {
                Path path = Path.createPath(basePath, field, true);
                List list = mapList(field, path, jsonObj);
                field.set(mappedJson, list);
            } else {
                Path path = Path.createPath(basePath, field, false);
                Object objectValue = mapObject(field.getType(), path, jsonObj);
                field.set(mappedJson, objectValue);
            }
        }

        return mappedJson;
    }

    private static List mapList(Field field, Path path, JSONObject jsonObj) throws Exception {
        Type genericType = field.getGenericType();
        if (genericType instanceof ParameterizedType) {
            Type type = ((ParameterizedType) genericType).getActualTypeArguments()[0];
            if (TypeCatagorizer.isSimple(type))
                return mapListSimples((Class) type, path, jsonObj);
            else
                return mapListObjects((Class) type, path, jsonObj);
        } else
            return mapListSimples(String.class, path, jsonObj);
    }

    private static List mapListSimples(Class clazz, Path basePath, JSONObject jsonObj) throws Exception {
        JSONArray values = (JSONArray) applyPath(jsonObj, basePath);
        if (values == null)
            return null;

        List list = new ArrayList();
        for (int i = 0; i < values.length(); i++)
            list.add(TypeCatagorizer.getSimpleValue(clazz, values.get(i)));
        return list;
    }

    private static List mapListObjects(Class clazz, Path basePath, JSONObject jsonObj) throws Exception {
        List list = new ArrayList();
        boolean done = false;
        while (!done) { // todo: fix infinite loop
            Object obj = mapObject(clazz, basePath, jsonObj);
            list.add(TypeCatagorizer.getSimpleValue(clazz, obj));
            done = true;
        }

        return list;
    }

    private static Object applyPath(JSONObject jsonObj, Path path) {
        try {
            if (path.segments.length == 0)
                return null;
            for (int i = 0; i < path.segments.length - 1; i++)
                jsonObj = jsonObj.getJSONObject(path.segments[i].value);
            return jsonObj.get(path.segments[path.segments.length - 1].value);
        } catch (JSONException e) {
            return null;
        }
    }
}
