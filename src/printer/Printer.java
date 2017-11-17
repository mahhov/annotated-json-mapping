package printer;

import utility.TypeCatagorizer;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.List;

public class Printer {
    private static StringBuilder stringBuilder = new StringBuilder();

    // todo : print class name at first line like Repeater does
    public static String printObject(Object mappedJson) throws Exception {
        stringBuilder.setLength(0);
        printObject(0, mappedJson);
        return stringBuilder.toString();
    }

    private static void printObject(int indent, Object mappedObject) throws Exception {
        for (Field field : mappedObject.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            if (TypeCatagorizer.isSimple(field.getType()))
                printField(indent, field.getName(), field.get(mappedObject));
            else if (TypeCatagorizer.isList(field.getType())) {
                List list = (List) field.get(mappedObject);
                if (list == null)
                    printField(indent, field.getName(), "null");
                else {
                    printField(indent, field.getName(), "(size " + list.size() + ")");
                    printList(indent + 2, TypeCatagorizer.getListType(field.getGenericType()), (List) field.get(mappedObject));
                }
            } else {
                Object nestedObject = field.get(mappedObject);
                if (nestedObject == null)
                    printField(indent, field.getName(), "null");
                else {
                    printField(indent, field.getName());
                    printObject(indent + 2, nestedObject);
                }
            }
        }
    }

    private static void printList(int indent, Type listType, List list) throws Exception {
        if (TypeCatagorizer.isSimple(listType))
            for (Object element : list)
                printField(indent, element != null ? element.toString() : null);
        else if (TypeCatagorizer.isList(listType)) {
            for (int i = 0; i < list.size(); i++) {
                List innerList = (List) list.get(i);
                printField(indent, i + "", "(size " + innerList.size() + ")");
                printList(indent + 2, TypeCatagorizer.getListType(listType), innerList);
            }
        } else
            for (int i = 0; i < list.size(); i++) {
                printField(indent, i + "");
                printObject(indent + 2, list.get(i));
            }
    }

    private static void printField(int indent, String name) {
        stringBuilder.append(String.format("%" + (indent + 1) + "s %-10s\n", "", name));
    }

    private static void printField(int indent, String name, Object value) {
        stringBuilder.append(String.format("%" + (indent + 1) + "s %-10s: %s\n", "", name, value));
    }
}