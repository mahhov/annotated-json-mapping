package utility;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StackTest {
    private Stack<Integer> stack;

    @BeforeEach
    void setUp() {
        stack = new Stack<>(10);
    }

    @Test
    void simple() {
        stack.push(5);
        assertEquals(stack.size(), 1);
        assertEquals((int) stack.pop(), 5);
        assertEquals(stack.size(), 0);
        stack.push(3);
        assertEquals(stack.size(), 1);
        stack.push(1);
        assertEquals(stack.size(), 2);
        assertEquals((int) stack.pop(), 1);
        assertEquals(stack.size(), 1);
        assertEquals((int) stack.pop(), 3);
        assertEquals(stack.size(), 0);
        stack.push(9);
        assertEquals(stack.size(), 1);
    }

    @Test
    void popFront() {
        stack.push(5);
        stack.push(6);
        stack.push(7);
        assertEquals((int) stack.popFront(), 5);
        assertEquals(stack.size(), 2);
        assertEquals((int) stack.popFront(), 6);
        assertEquals(stack.size(), 1);
        assertEquals((int) stack.popFront(), 7);
        assertEquals(stack.size(), 0);
        stack.push(8);
        assertEquals(stack.size(), 1);
        assertEquals((int) stack.popFront(), 8);
        assertEquals(stack.size(), 0);
    }
}