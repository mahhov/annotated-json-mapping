package utility;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class ArrayGrowerTest {
    @Test
    void appendI() {
        int[] orig = new int[] {6, 1, 3, 4};
        int[] copy = ArrayGrower.appendI(orig, 10);
        assertArrayEquals(orig, new int[] {6, 1, 3, 4});
        assertArrayEquals(copy, new int[] {6, 1, 3, 4, 10});
    }

    @Test
    void setI() {
        int[] orig = new int[] {6, 1, 3, 4};
        int[] copy = ArrayGrower.setI(orig, 1, 10);
        assertArrayEquals(orig, new int[] {6, 1, 3, 4});
        assertArrayEquals(copy, new int[] {6, 10, 3, 4});
    }

    @Test
    void concatS() {
        String[] strings1 = new String[] {"a", "b"};
        String[] strings2 = new String[] {"c", "d"};
        String[] concat = ArrayGrower.concatS(strings1, strings2);
        assertArrayEquals(strings1, new String[] {"a", "b"});
        assertArrayEquals(strings2, new String[] {"c", "d"});
        assertArrayEquals(concat, new String[] {"a", "b", "c", "d"});
    }

    @Test
    void copyI() {
        int[] orig = new int[] {6, 1, 3, 4};
        int[] copy = new int[5];
        ArrayGrower.copyI(orig, copy);
        assertArrayEquals(orig, new int[] {6, 1, 3, 4});
        assertArrayEquals(copy, new int[] {6, 1, 3, 4, 0});
    }

    @Test
    void copyS() {
        String[] orig = new String[] {"a", "b", "c"};
        String[] copy = new String[5];
        ArrayGrower.copyO(orig, copy, 2);
        assertArrayEquals(orig, new String[] {"a", "b", "c"});
        assertArrayEquals(copy, new String[] {"a", "b", null, null, null});
    }
}