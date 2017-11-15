class ArrayGrower {
    static int[] append(int[] a, int v) {
        int[] b = new int[a.length + 1];
        copy(a, b);
        b[a.length] = v;
        return b;
    }

    static int[] set(int[] a, int i, int v) {
        int[] b = new int[a.length];
        copy(a, b);
        b[i] = v;
        return b;
    }

    private static void copy(int[] a, int[] b) {
        System.arraycopy(a, 0, b, 0, a.length);
    }
}