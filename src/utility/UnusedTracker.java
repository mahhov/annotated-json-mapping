package utility;

public class UnusedTracker {
    private int firstUnused = 0;
    private SortedLinkedList<Integer> useds;

    public UnusedTracker() {
        useds = new SortedLinkedList<>();
    }

    public int nextUnused() {
        int r = firstUnused;
        moveFirstUnused();
        return r;
    }

    public void setUsed(int index) {
        if (index == firstUnused)
            moveFirstUnused();
        else
            useds.sortedInsert(index);
    }

    private void moveFirstUnused() {
        firstUnused++;
        Integer first = useds.getFirst();
        while (first != null && firstUnused == first) {
            firstUnused++;
            useds.removeFirst();
            first = useds.getFirst();
        }
    }

    public boolean isUsed(int index) {
        if (index < firstUnused)
            return true;

        for (Integer used : useds)
            if (used == index)
                return true;
            else if (used > index)
                break;

        return false;
    }
}