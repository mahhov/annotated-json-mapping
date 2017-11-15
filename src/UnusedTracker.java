class UnusedTracker<T> {
    private int firstUnused = 0;
    private SortedLinkedList<Integer> useds;

    UnusedTracker() {
        useds = new SortedLinkedList<>();
    }

    int getUnused() {
        int r = firstUnused;
        moveFirstUsed();
        return r;
    }

    void setUsed(int index) {
        if (index == firstUnused)
            moveFirstUsed();
        else
            useds.sortedInsert(index);
    }

    private void moveFirstUsed() {
        Integer first = useds.getFirst();
        while (first != null && ++firstUnused == first) {
            useds.removeFirst();
            first = useds.getFirst();
        }
    }

    boolean isUsed(int index) {
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
