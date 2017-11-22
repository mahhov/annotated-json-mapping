package utility;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class ArrayGrowerTest {
    @Test
    void append() {
        int[] orig = new int[] {6, 1, 3, 4};
        int[] copy = ArrayGrower.appendI(orig, 10);
        assertArrayEquals(orig, new int[] {6, 1, 3, 4});
        assertArrayEquals(copy, new int[] {6, 1, 3, 4, 10});
    }

    @Test
    void set() {
        int[] orig = new int[] {6, 1, 3, 4};
        int[] copy = ArrayGrower.setI(orig, 1, 10);
        assertArrayEquals(orig, new int[] {6, 1, 3, 4});
        assertArrayEquals(copy, new int[] {6, 10, 3, 4});
    }
}