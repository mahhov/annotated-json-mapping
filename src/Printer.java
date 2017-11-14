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
                    printField(indent, field.getName(), "null");
                else {
                    printField(indent, field.getName());
                    for (Object element : list)
                        printField(indent + 2, element != null ? element.toString() : null); // todo: printing for non-simple lists
                }
            } else { // todo: do we need null check for unmapped obj ?
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