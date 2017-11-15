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
        boolean empty = true;

        for (Field field : fields) {
            if (TypeCatagorizer.isSimple(field.getType())) {
                Path path = Path.createPath(basePath, field, false);
                Object simpleValue = applyPath(jsonObj, path, indices);
                if (simpleValue != null) {
                    empty = false;
                    field.set(mappedJson, TypeCatagorizer.convertSimpleValue(field.getType(), simpleValue));
                }
            } else if (TypeCatagorizer.isList(field.getType())) {
                Path path = Path.createPath(basePath, field, true);
                List list = mapList(field, path, jsonObj, indices);
                if (list.size() != 0)
                    empty = false;
                field.set(mappedJson, list);
            } else {
                Path path = Path.createPath(basePath, field, false);
                Object objectValue = mapObject(field.getType(), path, jsonObj, indices);
                if (objectValue != null)
                    empty = false;
                field.set(mappedJson, objectValue);
            }
        }

        if (empty)
            return null; // todo: return mappedJson + null, and ony use null for list truncating
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
        int i = 0;
        Object simpleValue;
        do {
            int[] nextIndices = ArrayGrower.append(indices, i++);
            simpleValue = applyPath(jsonObj, path, nextIndices);
            if (simpleValue != null)
                list.add(TypeCatagorizer.convertSimpleValue(clazz, simpleValue));
        } while (simpleValue != null);

        return list;
    }

    private static List mapListObjects(Class clazz, Path basePath, JSONObject jsonObj, int[] indices) throws Exception {
        List list = new ArrayList();
        int i = 0;
        Object value;
        do {
            int[] nextIndices = ArrayGrower.append(indices, i++);
            value = mapObject(clazz, basePath, jsonObj, nextIndices);
            if (value != null)
                list.add(value);
        } while (value != null);

        return list;
    }

    private static Object applyPath(JSONObject jsonObj, Path path, int[] indices) {
        try {
            if (path.segments.length == 0)
                return null;

            for (int i = 0; i < path.segments.length - 1; i++) {
                Path.Segment segment = path.segments[i];
                if (segment.array != -1)
                    jsonObj = jsonObj.getJSONArray(segment.value).getJSONObject(indices[segment.array]);
                else
                    jsonObj = jsonObj.getJSONObject(segment.value);
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