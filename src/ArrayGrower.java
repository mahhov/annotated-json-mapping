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

    // todo : compare performance with System.arraycopy()
    private static int[] copy(int[] a, int[] b) {
        for (int i = 0; i < a.length; i++)
            b[i] = a[i];
        return b;
    }
}