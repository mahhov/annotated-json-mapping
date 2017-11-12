import java.lang.reflect.Field;

public class FieldType {
    public static boolean isString(Field field) {
        return field.getType() == String.class;
    }

    public static boolean isInteger(Field field) {
        return field.getType() == int.class || field.getType() == Integer.class;
    }

    public static boolean isShort(Field field) {
        return field.getType() == short.class || field.getType() == Short.class;
    }

    public static boolean isByte(Field field) {
        return field.getType() == byte.class || field.getType() == Byte.class;
    }

    public static boolean isLong(Field field) {
        return field.getType() == long.class || field.getType() == Long.class;
    }

    public static boolean isDouble(Field field) {
        return field.getType() == double.class || field.getType() == Double.class;
    }

    public static boolean isFloat(Field field) {
        return field.getType() == float.class || field.getType() == Float.class;
    }

    public static boolean isChar(Field field) {
        return field.getType() == char.class || field.getType() == Character.class;
    }

    public static boolean isBoolean(Field field) {
        return field.getType() == boolean.class || field.getType() == Boolean.class;
    }

    public static boolean isSimple(Field field) {
        return isString(field) || isInteger(field) || isShort(field) || isByte(field) || isLong(field) || isDouble(field) || isFloat(field) || isChar(field) || isBoolean(field);
    }

    public static void setSimple(Object obj, Field field, Object value) throws Exception {
        if (value == null)
            return;

        if (isString(field))
            field.set(obj, String.valueOf(value));
        else if (isInteger(field))
            field.set(obj, Integer.valueOf(value.toString()));
        else if (isShort(field))
            field.set(obj, Short.valueOf(value.toString()));
        else if (isByte(field))
            field.set(obj, Byte.valueOf(value.toString()));
        else if (isLong(field))
            field.set(obj, Long.valueOf(value.toString()));
        else if (isDouble(field))
            field.set(obj, Double.valueOf(value.toString()));
        else if (isFloat(field))
            field.set(obj, Float.valueOf(value.toString()));
        else if (isChar(field))
            field.set(obj, value.toString().charAt(0));
        else if (isBoolean(field))
            field.set(obj, Boolean.valueOf(value.toString()));
    }
}
