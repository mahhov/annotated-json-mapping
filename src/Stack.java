public class Stack<T> {
    private int size;
    private T[] values;

    public Stack(int size) {
        this.values = (T[]) new Object[size];
    }

    public void push(T value) {
        values[size++] = value;
    }

    public T pop() {
        return values[--size];
    }
}
