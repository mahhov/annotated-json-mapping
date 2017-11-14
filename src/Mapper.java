import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

class Mapper {
    static Object map(Class clazz, String jsonInput) throws Exception {
        return mapObject(clazz, new Path(""), new JSONObject(jsonInput), new int[0]);
    }

    private static Object mapObject(Class clazz, Path basePath, JSONObject jsonObj, int[] indices) throws Exception {
        Field[] fields = clazz.getDeclaredFields();

        Object mappedJson = clazz.getDeclaredConstructor().newInstance();

        for (Field field : fields) {
            if (TypeCatagorizer.isSimple(field.getType())) {
                Path path = Path.createPath(basePath, field, false);
                Object simpleValue = applyPath(jsonObj, path, indices);
                field.set(mappedJson, TypeCatagorizer.convertSimpleValue(field.getType(), simpleValue));
            } else if (TypeCatagorizer.isList(field.getType())) {
                Path path = Path.createPath(basePath, field, true);
                List list = mapList(field, path, jsonObj, indices);
                field.set(mappedJson, list);
            } else {
                Path path = Path.createPath(basePath, field, false);
                Object objectValue = mapObject(field.getType(), path, jsonObj, indices);
                field.set(mappedJson, objectValue);
            }
        }

        return mappedJson;
    }

    private static List mapList(Field field, Path path, JSONObject jsonObj, int[] indices) throws Exception {
        Type listType = TypeCatagorizer.getListType(field.getGenericType());
        if (TypeCatagorizer.isSimple(listType))
            return mapListSimples((Class) listType, path, jsonObj, indices);
        return mapListObjects((Class) listType, path, jsonObj, indices);
    }

    private static List mapListSimples(Class clazz, Path path, JSONObject jsonObj, int[] indices) throws Exception {
        List list = new ArrayList();
        boolean done = false;
        while (!done) { // todo: loop correct ammount
            int[] nextIndices = ArrayGrower.append(indices, 0);
            Object simpleValue = applyPath(jsonObj, path, nextIndices);
            list.add(TypeCatagorizer.convertSimpleValue(clazz, simpleValue));
            done = true;
        }

        return list;
    }

    private static List mapListObjects(Class clazz, Path basePath, JSONObject jsonObj, int[] indices) throws Exception {
        List list = new ArrayList();
        boolean done = false;
        while (!done) { // todo: loop correct ammount
            int[] nextIndices = ArrayGrower.append(indices, 0);
            Object obj = mapObject(clazz, basePath, jsonObj, nextIndices);
            list.add(obj);
            done = true;
        }

        return list;
    }

    private static Object applyPath(JSONObject jsonObj, Path path, int[] indices) {
        // todo refactor
        try {
            if (path.segments.length == 0)
                return null;

            for (int i = 0; i < path.segments.length - 1; i++) {
                if (path.segments[i].array != -1)
                    jsonObj = jsonObj.getJSONArray(path.segments[i].value).getJSONObject(indices[path.segments[i].array]);
                else
                    jsonObj = jsonObj.getJSONObject(path.segments[i].value);
            }
            Path.Segment lastSegment = path.segments[path.segments.length - 1];
            if (lastSegment.array != -1)
                return jsonObj.getJSONArray(lastSegment.value).get(indices[lastSegment.array]);
            else
                return jsonObj.get(lastSegment.value);
        } catch (JSONException e) {
            return null;
        }
    }
}