package utility;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class SortedLinkedListTest {
    SortedLinkedList<Integer> sortedLinkedList;

    @BeforeEach
    void setUp() {
        sortedLinkedList = new SortedLinkedList<>();
    }

    @Test
    void getFirst() {
        assertNull(sortedLinkedList.getFirst());
        sortedLinkedList.sortedInsert(4);
        assertEquals((int) sortedLinkedList.getFirst(), 4);
        sortedLinkedList.sortedInsert(5);
        assertEquals((int) sortedLinkedList.getFirst(), 4);
        sortedLinkedList.sortedInsert(1);
        assertEquals((int) sortedLinkedList.getFirst(), 1);
    }

    @Test
    void removeFirst() {
        assertNull(sortedLinkedList.getFirst());
        sortedLinkedList.sortedInsert(4);
        sortedLinkedList.removeFirst();
        assertNull(sortedLinkedList.getFirst());
        sortedLinkedList.sortedInsert(1);
        sortedLinkedList.sortedInsert(2);
        assertEquals((int) sortedLinkedList.getFirst(), 1);
        sortedLinkedList.removeFirst();
        assertEquals((int) sortedLinkedList.getFirst(), 2);
        sortedLinkedList.removeFirst();
        assertNull(sortedLinkedList.getFirst());
    }

    @Test
    void sortedInsert() {
        sortedLinkedList.sortedInsert(5);
        assertEquals((int) sortedLinkedList.getFirst(), 5);
        sortedLinkedList.sortedInsert(4);
        assertEquals((int) sortedLinkedList.getFirst(), 4);
        sortedLinkedList.sortedInsert(7);
        assertEquals((int) sortedLinkedList.getFirst(), 4);
        sortedLinkedList.sortedInsert(2);
        assertEquals((int) sortedLinkedList.getFirst(), 2);
        sortedLinkedList.sortedInsert(8);
        sortedLinkedList.sortedInsert(6);
        sortedLinkedList.sortedInsert(3);
        assertEquals((int) sortedLinkedList.getFirst(), 2);
        sortedLinkedList.sortedInsert(1);
        assertEquals((int) sortedLinkedList.getFirst(), 1);
    }

    @Test
    void iterator() {
        sortedLinkedList.sortedInsert(5);
        sortedLinkedList.sortedInsert(4);
        sortedLinkedList.sortedInsert(7);
        sortedLinkedList.sortedInsert(2);
        sortedLinkedList.sortedInsert(8);
        sortedLinkedList.sortedInsert(6);
        sortedLinkedList.sortedInsert(3);
        sortedLinkedList.sortedInsert(1);

        Iterator iterator = sortedLinkedList.iterator();
        assertEquals(iterator.next(), 1);
        assertEquals(iterator.next(), 2);
        assertEquals(iterator.next(), 3);
        assertEquals(iterator.next(), 4);
        assertEquals(iterator.next(), 5);
        assertEquals(iterator.next(), 6);
        assertEquals(iterator.next(), 7);
        assertEquals(iterator.next(), 8);
    }

}