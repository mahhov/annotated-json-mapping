import java.lang.reflect.Field;
import java.util.List;

class Printer {
    static void printObject(Object mappedJson) throws Exception {
        printObject(0, mappedJson);
    }

    private static void printObject(int indent, Object mappedJson) throws Exception {
        for (Field field : mappedJson.getClass().getDeclaredFields())
            if (TypeCatagorizer.isSimple(field.getType()))
                printField(indent, field.getName(), field.get(mappedJson));
            else if (TypeCatagorizer.isList(field.getType())) {
                List list = (List) field.get(mappedJson);
                if (list == null)
                    printField(indent, field.getName(), "null"); // todo: make sure lists are never null
                else {
                    printField(indent, field.getName());
                    if (TypeCatagorizer.isSimple(TypeCatagorizer.getListType(field.getGenericType())))
                        for (Object element : list)
                            printField(indent + 2, element != null ? element.toString() : null);
                    else
                        for (int i = 0; i < list.size(); i++) {
                            printField(indent + 2, i + "");
                            printObject(indent + 4, list.get(i));
                        }
                }
            } else {
                Object nestedObject = field.get(mappedJson);
                if (nestedObject == null)
                    printField(indent, field.getName(), "null");
                else {
                    printField(indent, field.getName());
                    printObject(indent + 2, nestedObject);
                }
            }
    }

    private static void printField(int indent, String name) {
        System.out.printf("%" + (indent + 1) + "s %-10s\n", "", name);
    }

    private static void printField(int indent, String name, Object value) {
        System.out.printf("%" + (indent + 1) + "s %-10s: %s\n", "", name, value);
    }
}