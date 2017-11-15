import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

class Mapper {
    static Object map(Class clazz, String jsonInput) throws Exception {
        return mapObject(clazz, Path.EMPTY_PATH, new JSONObject(jsonInput), new int[0]);
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
                List list = mapList(TypeCatagorizer.getListType(field.getGenericType()), path, jsonObj, indices);
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

    private static List mapList(Type listType, Path path, JSONObject jsonObj, int[] indices) throws Exception {
        if (TypeCatagorizer.isSimple(listType))
            return mapListSimples((Class) listType, path, jsonObj, indices);
        else if (TypeCatagorizer.isList(listType))
            return mapListLists(path, jsonObj, indices);
        else
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

    private static List mapListLists(Path basePath, JSONObject jsonObj, int[] indices) throws Exception {
        List list = new ArrayList();
        int i = 0;
        List listValue;
        do {
            int[] nextIndices = ArrayGrower.append(indices, i++);
            Path path = Path.nestList(basePath);
            listValue = mapList(Integer.class, path, jsonObj, nextIndices);
            if (listValue.size() != 0)
                list.add(listValue);
        } while (listValue.size() != 0);

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
        // todo: refactor
        try {
            if (path.segments.length == 0)
                return null;

            for (int i = 0; i < path.segments.length - 1; i++) {
                Path.Segment segment = path.segments[i];
                if (segment.array != -1) {
                    JSONArray jsonArray = jsonObj.getJSONArray(segment.value);
                    for (int j = 0; j < segment.arrayLayers - 1; j++)
                        jsonArray = jsonArray.getJSONArray(indices[segment.array + j]);
                    jsonObj = jsonArray.getJSONObject(indices[segment.array + segment.arrayLayers - 1]);
                } else
                    jsonObj = jsonObj.getJSONObject(segment.value);
            }

            Path.Segment lastSegment = path.segments[path.segments.length - 1];
            if (lastSegment.array != -1) {
                JSONArray jsonArray = jsonObj.getJSONArray(lastSegment.value);
                for (int j = 0; j < lastSegment.arrayLayers - 1; j++)
                    jsonArray = jsonArray.getJSONArray(indices[lastSegment.array + j]);
                return jsonArray.get(indices[lastSegment.array + lastSegment.arrayLayers - 1]);
            } else
                return jsonObj.get(lastSegment.value);

        } catch (JSONException e) {
            return null;
        }
    }
}