package utility;

public class Stack<T> {
    private int back;
    private int front;
    private T[] values;

    public Stack(int size) {
        this.values = (T[]) new Object[size];
    }

    public void push(T value) {
        values[back++] = value;
    }

    public T pop() {
        return values[--back];
    }

    public T popFront() {
        return values[front++];
    }

    public int size() {
        return back - front;
    }
}
