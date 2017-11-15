import java.util.Iterator;

class SortedLinkedList<T extends Comparable<T>> implements Iterable<T> {
    private Node head;

    T getFirst() {
        if (head == null)
            return null;
        return head.value;
    }

    void removeFirst() {
        head = head.next;
    }

    void sortedInsert(T value) {
        if (head == null)
            head = new Node(value);

        else {
            Node cur = head;
            while (cur.next != null && cur.next.value.compareTo(value) < 0)
                cur = cur.next;
            cur.next = new Node(value, cur.next);
        }
    }

    private class Node {
        private T value;
        private Node next;

        private Node(T value) {
            this.value = value;
        }

        private Node(T value, Node next) {
            this.value = value;
            this.next = next;
        }
    }

    public Iterator<T> iterator() {
        return new LinkedListIterator();
    }

    private class LinkedListIterator implements Iterator<T> {
        private Node cur = head;

        public boolean hasNext() {
            return cur != null;
        }

        public T next() {
            T r = cur.value;
            cur = cur.next;
            return r;
        }
    }
}
