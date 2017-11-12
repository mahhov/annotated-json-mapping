public class Path {
    private String value;
    String[] segments;

    Path(String value) {
        String[] shortenValue = value.split("~");
        this.value = shortenValue[shortenValue.length - 1];
        this.segments = value.split("/");
    }

    static Path append(Path basePath, String value) {
        if (isLeaf(basePath.value))
            return new Path(basePath.value + "/" + value);
        else
            return new Path(basePath.value + value);
    }

    static boolean isLeaf(String value) {
        return !value.isEmpty() && value.charAt(value.length() - 1) != '/';
    }

    public String toString() {
        return value;
    }
}