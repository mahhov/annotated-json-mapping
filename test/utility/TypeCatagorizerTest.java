package utility;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TypeCatagorizerTest {
    @Test
    void isSimple() {
        assertTrue(TypeCatagorizer.isSimple(int.class));
        assertTrue(TypeCatagorizer.isSimple(short.class));
        assertTrue(TypeCatagorizer.isSimple(byte.class));
        assertTrue(TypeCatagorizer.isSimple(long.class));
        assertTrue(TypeCatagorizer.isSimple(double.class));
        assertTrue(TypeCatagorizer.isSimple(float.class));
        assertTrue(TypeCatagorizer.isSimple(char.class));

        assertTrue(TypeCatagorizer.isSimple(String.class));
        assertTrue(TypeCatagorizer.isSimple(Integer.class));
        assertTrue(TypeCatagorizer.isSimple(Short.class));
        assertTrue(TypeCatagorizer.isSimple(Byte.class));
        assertTrue(TypeCatagorizer.isSimple(Long.class));
        assertTrue(TypeCatagorizer.isSimple(Double.class));
        assertTrue(TypeCatagorizer.isSimple(Float.class));
        assertTrue(TypeCatagorizer.isSimple(Character.class));

        assertFalse(TypeCatagorizer.isSimple(Object.class));

        assertFalse(TypeCatagorizer.isSimple(List.class));
        assertFalse(TypeCatagorizer.isSimple(ArrayList.class));
        assertFalse(TypeCatagorizer.isSimple(Array.class));
    }

    @Test
    void isList() {
        assertFalse(TypeCatagorizer.isList(int.class));
        assertFalse(TypeCatagorizer.isList(short.class));
        assertFalse(TypeCatagorizer.isList(byte.class));
        assertFalse(TypeCatagorizer.isList(long.class));
        assertFalse(TypeCatagorizer.isList(double.class));
        assertFalse(TypeCatagorizer.isList(float.class));
        assertFalse(TypeCatagorizer.isList(char.class));

        assertFalse(TypeCatagorizer.isList(String.class));
        assertFalse(TypeCatagorizer.isList(Integer.class));
        assertFalse(TypeCatagorizer.isList(Short.class));
        assertFalse(TypeCatagorizer.isList(Byte.class));
        assertFalse(TypeCatagorizer.isList(Long.class));
        assertFalse(TypeCatagorizer.isList(Double.class));
        assertFalse(TypeCatagorizer.isList(Float.class));
        assertFalse(TypeCatagorizer.isList(Character.class));

        assertFalse(TypeCatagorizer.isList(Object.class));

        assertTrue(TypeCatagorizer.isList(List.class));
        assertTrue(TypeCatagorizer.isList(ArrayList.class));
        assertFalse(TypeCatagorizer.isList(Array.class));
    }

    @Test
    void getListType() {
    }

    @Test
    void convertSimpleValue() throws Exception {
        assertNull(TypeCatagorizer.convertSimpleValue(Integer.class, null));

        assertEquals(TypeCatagorizer.convertSimpleValue(Integer.class, new Integer(5)), 5);
        assertEquals(TypeCatagorizer.convertSimpleValue(Integer.class, "5"), 5);

        assertEquals(TypeCatagorizer.convertSimpleValue(Short.class, new Short((short) 5)), (short) 5);
        assertEquals(TypeCatagorizer.convertSimpleValue(Short.class, new Integer(5)), (short) 5);
        assertEquals(TypeCatagorizer.convertSimpleValue(Short.class, "5"), (short) 5);

        assertEquals(TypeCatagorizer.convertSimpleValue(String.class, new Integer(5)), "5");
        assertEquals(TypeCatagorizer.convertSimpleValue(String.class, "5"), "5");

        assertEquals(TypeCatagorizer.convertSimpleValue(Boolean.class, false), false);
        assertEquals(TypeCatagorizer.convertSimpleValue(Boolean.class, "false"), false);
    }

}