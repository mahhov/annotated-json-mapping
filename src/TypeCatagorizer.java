import java.lang.reflect.Type;
import java.util.List;

class TypeCatagorizer {
    static boolean isString(Type type) {
        return type == String.class;
    }

    static boolean isInteger(Type type) {
        return type == int.class || type == Integer.class;
    }

    static boolean isShort(Type type) {
        return type == short.class || type == Short.class;
    }

    static boolean isByte(Type type) {
        return type == byte.class || type == Byte.class;
    }

    static boolean isLong(Type type) {
        return type == long.class || type == Long.class;
    }

    static boolean isDouble(Type type) {
        return type == double.class || type == Double.class;
    }

    static boolean isFloat(Type type) {
        return type == float.class || type == Float.class;
    }

    static boolean isChar(Type type) {
        return type == char.class || type == Character.class;
    }

    static boolean isBoolean(Type type) {
        return type == boolean.class || type == Boolean.class;
    }

    static boolean isSimple(Type type) {
        return isString(type) || isInteger(type) || isShort(type) || isByte(type) || isLong(type) || isDouble(type) || isFloat(type) || isChar(type) || isBoolean(type);
    }

    static boolean isList(Type type) {
        return List.class.isAssignableFrom((Class) type); // || type.isArray();
    }

    static Object convertSimpleValue(Class clazz, Object value) throws Exception {
        if (value == null)
            return null;

        if (isString(clazz))
            return String.valueOf(value);
        else if (isInteger(clazz))
            return Integer.valueOf(value.toString());
        else if (isShort(clazz))
            return Short.valueOf(value.toString());
        else if (isByte(clazz))
            return Byte.valueOf(value.toString());
        else if (isLong(clazz))
            return Long.valueOf(value.toString());
        else if (isDouble(clazz))
            return Double.valueOf(value.toString());
        else if (isFloat(clazz))
            return Float.valueOf(value.toString());
        else if (isChar(clazz))
            return value.toString().charAt(0);
        else if (isBoolean(clazz))
            return Boolean.valueOf(value.toString());

        return null;
    }
}
