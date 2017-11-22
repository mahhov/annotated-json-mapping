package utility;

public class ArrayGrower {
    public static int[] appendI(int[] a, int v) {
        int[] b = new int[a.length + 1];
        copyI(a, b);
        b[a.length] = v;
        return b;
    }

    public static int[] setI(int[] a, int i, int v) {
        int[] b = new int[a.length];
        copyI(a, b);
        b[i] = v;
        return b;
    }

    public static String[] concatS(String[] a, String[] b) {
        String[] c = new String[a.length + b.length];
        System.arraycopy(a, 0, c, 0, a.length);
        System.arraycopy(b, 0, c, a.length, b.length);
        return c;
    }

    public static void copyI(int[] a, int[] b) {
        System.arraycopy(a, 0, b, 0, a.length);
    }

    public static void copyO(Object[] a, Object[] b, int n) {
        System.arraycopy(a, 0, b, 0, n);
    }
}