class Stack<T> {
    private int back;
    private int front;
    private T[] values;

    Stack(int size) {
        this.values = (T[]) new Object[size];
    }

    void push(T value) {
        values[back++] = value;
    }

    T pop() {
        return values[--back];
    }

    T popFront() {
        return values[front++];
    }

    int size() {
        return back - front;
    }
}
