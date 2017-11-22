package utility;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UnusedTrackerTest {
    private UnusedTracker unusedTracker;

    @BeforeEach
    void setUp() {
        unusedTracker = new UnusedTracker();
    }

    @Test
    void copyConstructor() {
        unusedTracker.nextUnused();
        unusedTracker.nextUnused();
        unusedTracker.setUsed(2);
        unusedTracker.nextUnused();
        unusedTracker.setUsed(5);
        unusedTracker.setUsed(6);
        unusedTracker.nextUnused();
        unusedTracker.nextUnused();

        UnusedTracker unusedTrackerCopy = new UnusedTracker(unusedTracker);
        assertEquals(unusedTrackerCopy.nextUnused(), 8);
        assertEquals(unusedTrackerCopy.nextUnused(), 9);
    }

    @Test
    void simple() {
        assertEquals(unusedTracker.nextUnused(), 0);
        assertTrue(unusedTracker.isUsed(0));
        assertFalse(unusedTracker.isUsed(1));

        assertEquals(unusedTracker.nextUnused(), 1);
        assertTrue(unusedTracker.isUsed(0));
        assertTrue(unusedTracker.isUsed(1));
        assertFalse(unusedTracker.isUsed(2));

        unusedTracker.setUsed(2);
        assertTrue(unusedTracker.isUsed(2));
        assertFalse(unusedTracker.isUsed(3));

        assertEquals(unusedTracker.nextUnused(), 3);
        unusedTracker.setUsed(5);
        unusedTracker.setUsed(6);

        assertTrue(unusedTracker.isUsed(3));
        assertFalse(unusedTracker.isUsed(4));
        assertTrue(unusedTracker.isUsed(5));
        assertTrue(unusedTracker.isUsed(6));

        assertEquals(unusedTracker.nextUnused(), 4);
        assertTrue(unusedTracker.isUsed(4));
        assertEquals(unusedTracker.nextUnused(), 7);
        assertTrue(unusedTracker.isUsed(7));
    }
}